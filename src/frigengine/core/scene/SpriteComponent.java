package frigengine.core.scene;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Transform;
import org.newdawn.slick.util.xml.SlickXMLException;
import org.newdawn.slick.util.xml.XMLElement;

import frigengine.core.component.*;
import frigengine.core.exceptions.data.*;
import frigengine.core.geom.*;
import frigengine.core.idable.*;


public class SpriteComponent extends Component implements AnimationFinishedSubscriber {
	// Required components
	@Override
	public  Collection<Class<? extends Component>> requiredComponents() {
		return new ArrayList<Class<? extends Component>>(Arrays.asList(
				PositionComponent.class
			));
	}
	
	// Attributes
	private String activeAnimation;
	private String continuousAnimation;
	private Rectangle domain;
	private IDableCollection<String, Animation> animations;
	
	// Constructors and initialization
	public SpriteComponent() {
		this.activeAnimation = "";
		this.continuousAnimation = Animation.PLACEHOLDER_ID;
		this.domain = new Rectangle(0,0,0,0);
		this.animations = new IDableCollection<String, Animation>();
		this.animations.add(Animation.getPlaceholder());
	}
	private SpriteComponent(SpriteComponent other) {
		super(other);
		
		this.activeAnimation = other.activeAnimation;
		this.continuousAnimation = other.continuousAnimation;
		this.domain = new Rectangle(other.domain.getX(), other.domain.getY(), other.domain.getWidth(), other.domain.getHeight());
		this.animations = new IDableCollection<String, Animation>();
		for(Animation a : other.animations) {
			this.animations.add(a.clone());
		}
	}
	@Override
	public SpriteComponent clone() {
		return new SpriteComponent(this);
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

		// domain
		try {
			this.domain.setX((float) xmlElement.getDoubleAttribute("offsetx", this.domain.getX()));
		} catch (SlickXMLException e) {
			throw new AttributeFormatException(xmlElement.getName(), "offsetx",
					xmlElement.getAttribute("offsetx"));
		}
		try {
			this.domain.setY((float) xmlElement.getDoubleAttribute("offsety", this.domain.getY()));
		} catch (SlickXMLException e) {
			throw new AttributeFormatException(xmlElement.getName(), "offsety",
					xmlElement.getAttribute("offsety"));
		}
		try {
			((Rectangle)this.domain).setWidth((float) xmlElement.getDoubleAttribute("width", this.domain.getWidth()));
		} catch (SlickXMLException e) {
			throw new AttributeFormatException(xmlElement.getName(), "width",
					xmlElement.getAttribute("width"));
		}
		try {
			((Rectangle)this.domain).setHeight((float) xmlElement.getDoubleAttribute("height", this.domain.getHeight()));
		} catch (SlickXMLException e) {
			throw new AttributeFormatException(xmlElement.getName(), "height",
					xmlElement.getAttribute("height"));
		}

		// animations
		for (int i = 0; i < xmlElement.getChildrenByName(Animation.class.getSimpleName()).size(); i++) {
			XMLElement child = xmlElement.getChildrenByName(Animation.class.getSimpleName()).get(i);

			Animation newAnimation = new Animation();
			newAnimation.init(child);
			newAnimation.addFinishedSubscriber(this);
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
	public Rectangle getLocalDomain() {
		if(this.domain != null && this.domain.getWidth() > 0 && this.domain.getHeight() > 0) {
			return this.domain;
		} else {
			return this.getCurrentAnimation().getDomain();
		}
	}
	public Rectangle getWorldDomain() {
		if(this.domain.getWidth() > 0 && this.domain.getHeight() > 0) {
			Rectangle relative = this.domain.transform(Transform.createTranslateTransform(0,0));
			relative.setCenterX(getComponent(PositionComponent.class).getX() + this.domain.getX());
			relative.setCenterY(getComponent(PositionComponent.class).getY() + this.domain.getY());
			return relative;
		} else {
			Rectangle relative = this.getCurrentAnimation().getDomain().transform(Transform.createTranslateTransform(0,0));
			relative.setCenterX(getComponent(PositionComponent.class).getX());
			relative.setCenterY(getComponent(PositionComponent.class).getY());
			return relative;
		}
	}
	public Animation getActiveAnimation() {
		if(this.hasActiveAnimation()) {
			return this.animations.get(this.activeAnimation);
		} else {
			return null;
		}
	}
	public void playAnimation(String animationId) {
		this.setActiveAnimation(animationId);
	}
	public void playAnimation(String animationId, int duration) {
		Animation animation = this.animations.get(animationId);
		for(int i = 0; i < animation.getFrameCount(); i++) {
			animation.setDuration(i, duration / animation.getFrameCount());
		}
		this.setActiveAnimation(animationId);
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
	public void loopAnimation(String animationId) {
		this.setContinuousAnimation(animationId);
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
	public void subscribeTo(Animation reporter) {
		reporter.addFinishedSubscriber(this);
	}
	@Override
	public void reportedAnimationFinished(Animation source) {
		if(source.getId().equals(this.activeAnimation)) {
			this.killAnimation();
		}
	}
}
