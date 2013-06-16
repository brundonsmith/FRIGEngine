package frigengine.entities.old;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.util.xml.SlickXMLException;
import org.newdawn.slick.util.xml.XMLElement;

import frigengine.Scene;
import frigengine.exceptions.AttributeFormatException;
import frigengine.exceptions.ComponentException;
import frigengine.exceptions.InvalidTagException;
import frigengine.util.*;
import frigengine.util.graphics.AnimationFinishedListener;
import frigengine.util.graphics.FRIGAnimation;

public class DrawableComponent extends EntityComponent implements AnimationFinishedListener {
	// Attributes
	private Rectangle presence;
	private IDableCollection<String, FRIGAnimation> animations;
	private String activeAnimation;
	private String continuousAnimation;
	private List<AnimationFinishedListener> animationFinishedListeners;

	// Constructors and initialization
	public DrawableComponent(Entity entity) {
		super(entity);
	}
	public void init(XMLElement xmlElement) {
		if (!xmlElement.getName().equals(this.getClass().getSimpleName()))
			throw new InvalidTagException(this.getClass().getSimpleName(), xmlElement.getName());

		// presence
		float presenceWidth;
		try {
			presenceWidth = (float) xmlElement.getDoubleAttribute("width", 0);
		} catch (SlickXMLException e) {
			throw new AttributeFormatException(xmlElement.getName(), "width",
					xmlElement.getAttribute("width"));
		}
		float presenceHeight;
		try {
			presenceHeight = (float) xmlElement.getDoubleAttribute("height", 0);
		} catch (SlickXMLException e) {
			throw new AttributeFormatException(xmlElement.getName(), "height",
					xmlElement.getAttribute("height"));
		}
		this.presence = new Rectangle(0, 0, 0, 0);
		this.presence.setX(0);
		this.presence.setY(0);
		this.presence.setWidth(presenceWidth);
		this.presence.setHeight(presenceHeight);
		if ((int) this.presence.getWidth() == 0 || (int) this.presence.getHeight() == 0)
			this.presence = null;

		// animations
		this.animations = new IDableCollection<String, FRIGAnimation>();
		for (int i = 0; i < xmlElement.getChildrenByName(FRIGAnimation.class.getSimpleName()).size(); i++) {
			XMLElement child = xmlElement.getChildrenByName(FRIGAnimation.class.getSimpleName()).get(i);

			FRIGAnimation newAnimation = new FRIGAnimation();
			newAnimation.init(child);
			newAnimation.addFinishedListener(this);
			this.animations.add(newAnimation);
		}
		
		// activeAnimation
		this.activeAnimation = "";
		
		// continuousAnimation
		this.continuousAnimation = "";
	}

	// Main loop methods
	@Override
	public void update(int delta, Input input, Scene scene) {
		if (this.getActiveAnimation() != null && this.getActiveAnimation().isStopped())
			this.activeAnimation = "";
		this.getCurrentAnimation().update(delta);
	}
	public void render(Graphics g, Scene scene) {
		scene.renderObject(g, getCurrentAnimation(), new Rectangle(
				this.getMinX(),
				this.getMinY(),
				this.getCurrentWidth(),
				this.getCurrentHeight()));
	}

	// Getters and setters
	public float getMinX() {
		if (this.presence == null)
			return this.entity.spacial().getX()
					+ this.getCurrentAnimation().getPresence().getX()
					- this.getCurrentAnimation().getPresence().getWidth() / 2;
		else
			return this.entity.spacial().getX()
					+ this.presence.getX() - this.presence.getWidth() / 2;
	}
	public float getMinY() {
		if (this.presence == null)
			return this.entity.spacial().getY()
					+ this.getCurrentAnimation().getPresence().getY()
					- this.getCurrentAnimation().getPresence().getHeight() / 2;
		else
			return this.entity.spacial().getY()
					+ this.presence.getY() - this.presence.getHeight() / 2;
	}
	public float getMaxX() {
		return this.getMinX() + this.getCurrentWidth();
	}
	public float getMaxY() {
		return this.getMinY() + this.getCurrentHeight();
	}
	public float getCurrentWidth() {
		if (this.presence == null)
			return this.getCurrentAnimation().getPresence().getWidth();
		else
			return this.presence.getWidth();
	}
	public float getCurrentHeight() {
		if (this.presence == null)
			return this.getCurrentAnimation().getPresence().getHeight();
		else
			return this.presence.getHeight();
	}
	public Rectangle getWorldPresence() {
		return new Rectangle(this.getMinX(), this.getMinY(), this.getCurrentWidth(), this.getCurrentHeight());
	}
	public FRIGAnimation getCurrentAnimation() {
		return this.activeAnimation.equals("") ? this.getContinuousAnimation() : this.getActiveAnimation();
	}
	public String getCurrentAnimationID() {
		return this.activeAnimation.equals("") ? this.continuousAnimation : this.activeAnimation;
	}
	public void setActiveAnimation(String animation) {
		this.activeAnimation = animation;
		this.getActiveAnimation().setLooping(false);
		this.getActiveAnimation().restart();
	}
	public FRIGAnimation getActiveAnimation() {
		if (this.animations.contains(activeAnimation))
			return this.animations.get(activeAnimation);
		return null;
	}
	public String getActiveAnimationID() {
		return this.activeAnimation;
	}
	public void setContinuousAnimation(String animation) {
		this.activeAnimation = "";
		if (!this.continuousAnimation.equals(animation)) {
			this.continuousAnimation = animation;
			if (this.getContinuousAnimation() == null)
				throw new ComponentException("Drawable component in entity '" + this.entity.getId()
						+ "' doesn't contain animation '" + animation + "'");
			this.getContinuousAnimation().setLooping(true);
			this.getContinuousAnimation().start();
		}
	}
	public FRIGAnimation getContinuousAnimation() {
		if (this.animations.size() == 0)
			return FRIGAnimation.getPlaceholder();
		if (this.continuousAnimation.equals(""))
			for (FRIGAnimation a : this.animations)
				return a;
		if (this.animations.contains(this.continuousAnimation))
			return this.animations.get(this.continuousAnimation);
		return FRIGAnimation.getPlaceholder();
	}
	public String getContinuousAnimationID() {
		return this.continuousAnimation;
	}
	
	// Utilities
	@Override
	public String toString() {
		return this.getId() + ": " + this.getCurrentAnimation().getPresence();
	}
	
	// Events
	public void addFinishedListener(AnimationFinishedListener listener) {
		this.animationFinishedListeners.add(listener);
	}
	@Override
	public void animationFinished(FRIGAnimation source) {
		for(AnimationFinishedListener listener : this.animationFinishedListeners)
			listener.animationFinished(source);
	}

	// EntityComponent
	public static Set<Class<?>> getComponentDependencies() {
		return new HashSet<Class<?>>( Arrays.asList(new Class<?>[] {SpacialComponent.class} ) );
	}
	public static Set<Class<?>> getComponentExclusives() {
		return new HashSet<Class<?>>();
	}
}