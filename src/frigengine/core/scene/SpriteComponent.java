package frigengine.core.scene;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Transform;
import org.newdawn.slick.util.xml.SlickXMLException;
import org.newdawn.slick.util.xml.XMLElement;

import frigengine.core.AnimationFinishedListener;
import frigengine.core.component.*;
import frigengine.core.exceptions.data.*;
import frigengine.core.geom.*;
import frigengine.core.idable.*;


public class SpriteComponent extends Component implements AnimationFinishedListener {
	// Required components
	@Override
	public  Collection<Class<? extends Component>> requiredComponents() {
		return new ArrayList<Class<? extends Component>>(Arrays.asList(
				PositionComponent.class
			));
	}
	
	// Attributes
	private Rectangle presence;
	private String activeAnimation;
	private String continuousAnimation;
	private IDableCollection<String, Animation> animations;
	
	// Constructors and initialization
	public SpriteComponent() {
		this.activeAnimation = "";
		this.continuousAnimation = Animation.PLACEHOLDER_ID;
		this.presence = new Rectangle(0,0,0,0);
		this.animations = new IDableCollection<String, Animation>();
		this.animations.add(Animation.getPlaceholder());
	}
	@Override
	public void init(XMLElement xmlElement) {
		// Check element name
		if (!xmlElement.getName().equals(this.getClass().getSimpleName())) {
			throw new InvalidTagException(this.getClass().getSimpleName(), xmlElement.getName());
		}
		
		// activeAnimation
		
		// continuousAnimation
		this.continuousAnimation =xmlElement.getAttribute("defaultanimation", this.continuousAnimation);

		// presence
		try {
			this.presence.setX((float) xmlElement.getDoubleAttribute("offsetx", this.presence.getX()));
		} catch (SlickXMLException e) {
			throw new AttributeFormatException(xmlElement.getName(), "offsetx",
					xmlElement.getAttribute("offsetx"));
		}
		try {
			this.presence.setY((float) xmlElement.getDoubleAttribute("offsety", this.presence.getY()));
		} catch (SlickXMLException e) {
			throw new AttributeFormatException(xmlElement.getName(), "offsety",
					xmlElement.getAttribute("offsety"));
		}
		try {
			((Rectangle)this.presence).setWidth((float) xmlElement.getDoubleAttribute("width", this.presence.getWidth()));
		} catch (SlickXMLException e) {
			throw new AttributeFormatException(xmlElement.getName(), "width",
					xmlElement.getAttribute("width"));
		}
		try {
			((Rectangle)this.presence).setHeight((float) xmlElement.getDoubleAttribute("height", this.presence.getHeight()));
		} catch (SlickXMLException e) {
			throw new AttributeFormatException(xmlElement.getName(), "height",
					xmlElement.getAttribute("height"));
		}

		// animations
		for (int i = 0; i < xmlElement.getChildrenByName(Animation.class.getSimpleName()).size(); i++) {
			XMLElement child = xmlElement.getChildrenByName(Animation.class.getSimpleName()).get(i);

			Animation newAnimation = new Animation();
			newAnimation.init(child);
			newAnimation.addFinishedListener(this);
			this.animations.add(newAnimation);
		}
	}

	// Main loop methods
	@Override
	public void update(int delta, Input input) {
		if (this.hasActiveAnimation() && this.getActiveAnimation().isStopped()) {
			this.killAnimation();
		}
		this.getCurrentAnimation().update(delta);
	}
	
	// Internal
	private boolean hasActiveAnimation() {
		return this.activeAnimation != null && !this.activeAnimation.equals("");
	}
	private void setActiveAnimation(String animationId) {
		if(this.animations.contains(animationId)) {
			this.activeAnimation = animationId;
		} else {
			this.activeAnimation = Animation.PLACEHOLDER_ID;
		}
		this.animations.get(this.activeAnimation).setLooping(false);
		this.animations.get(this.activeAnimation).restart();
	}
	private void setContinuousAnimation(String animationId) {
		if (!this.continuousAnimation.equals(animationId)) {
			this.killAnimation();
			if(this.animations.contains(animationId)) {
				this.continuousAnimation = animationId;
			} else {
				this.continuousAnimation = Animation.PLACEHOLDER_ID;
			}
			this.animations.get(this.continuousAnimation).setLooping(true);
			this.animations.get(this.continuousAnimation).start();
		}
	}
	private Animation getContinuousAnimation() {
		if(!this.animations.contains(this.continuousAnimation)) {
			this.continuousAnimation = Animation.PLACEHOLDER_ID;
		}
		return this.animations.get(this.continuousAnimation);
	}
	
	// External
	public Rectangle getLocalPresence() {
		if(this.presence != null && this.presence.getWidth() > 0 && this.presence.getHeight() > 0) {
			Shape p = this.presence;
			return new Rectangle(p.getMinX(), p.getMinY(), p.getWidth(), p.getHeight());
		} else {
			Shape p =  this.getCurrentAnimation().getPresence();
			return new Rectangle(p.getMinX(), p.getMinY(), p.getWidth(), p.getHeight());
		}
	}
	public Rectangle getWorldPresence() {
		if(this.presence.getWidth() > 0 && this.presence.getHeight() > 0) {
			Shape relative = this.presence.transform(Transform.createTranslateTransform(0,0));
			relative.setCenterX(getComponent(PositionComponent.class).getX() + this.presence.getX());
			relative.setCenterY(getComponent(PositionComponent.class).getY() + this.presence.getY());
			return new Rectangle(relative.getMinX(), relative.getMinY(), relative.getWidth(), relative.getHeight());
		} else {
			Shape relative = this.getCurrentAnimation().getPresence().transform(Transform.createTranslateTransform(0,0));
			relative.setCenterX(getComponent(PositionComponent.class).getX());
			relative.setCenterY(getComponent(PositionComponent.class).getY());
			return new Rectangle(relative.getMinX(), relative.getMinY(), relative.getWidth(), relative.getHeight());
		}
	}
	public Animation getActiveAnimation() {
		if(this.hasActiveAnimation()) {
			return this.animations.get(this.activeAnimation);
		} else {
			return null;
		}
	}
	public void playAnimation(String animation) {
		this.setActiveAnimation(animation);
	}
	public void playAnimation(String animation, int duration) {
		Animation anim = this.animations.get(animation);
		
		for(int i = 0; i < anim.getFrameCount(); i++) {
			anim.setDuration(i, duration / anim.getFrameCount());
		}
		this.setActiveAnimation(animation);
	}
	public void killAnimation() {
		if(this.hasActiveAnimation()) {
			this.getActiveAnimation().stop();
		}
		this.activeAnimation = "";
	}
	public Animation getLoopingAnimation() {
		return this.getContinuousAnimation();
	}
	public void loopAnimation(String animation) {
		this.setContinuousAnimation(animation);
	}
	public Animation getCurrentAnimation() {
		return this.hasActiveAnimation() ? this.getActiveAnimation() : this.getContinuousAnimation();
	}
	
	// Utilities
	@Override
	public String toString() {
		return this.getClass().getSimpleName() + ": { " + "current animation: " + this.getCurrentAnimation().getId() + "}";
	}
	
	// Events
	@Override
	public void animationFinished(Animation source) {
		if(source.getId().equals(this.activeAnimation)) {
			this.killAnimation();
		}
	}
}
