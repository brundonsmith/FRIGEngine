package frigengine.entities;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.util.xml.SlickXMLException;
import org.newdawn.slick.util.xml.XMLElement;

import frigengine.FRIGAnimation;
import frigengine.exceptions.AttributeFormatException;
import frigengine.exceptions.ComponentException;
import frigengine.exceptions.DataParseException;
import frigengine.scene.*;
import frigengine.util.*;

public class ComponentDrawable extends EntityComponent {
	public static String getComponentID() {
		return "drawable";
	}

	public String getTagName() {
		return getComponentID();
	}

	public static String[] getComponentDependencies() {
		return new String[] { "spacial" };
	}

	public static String[] getComponentExclusives() {
		return new String[] {};
	}

	// Attributes
	private String activeAnimation = "";
	private String continuousAnimation = "";
	private IDableCollection<FRIGAnimation> animations;
	private Rectangle presence;

	// Constructors and initialization
	public ComponentDrawable(Entity entity) {
		super(entity);
		this.id = getComponentID();
	}

	public void init(XMLElement xmlElement) {
		if (!xmlElement.getName().equals(this.getID()))
			throw new DataParseException(
					"Xml node does not match component type '" + this.id + "'");

		// Attributes
		float presenceWidth;
		try {
			presenceWidth = (float) xmlElement.getDoubleAttribute("width", 0);
		} catch (SlickXMLException e) {
			throw new AttributeFormatException(this.getTagName(), "width",
					xmlElement.getAttribute("width"));
		}
		float presenceHeight;
		try {
			presenceHeight = (float) xmlElement.getDoubleAttribute("height", 0);
		} catch (SlickXMLException e) {
			throw new AttributeFormatException(this.getTagName(), "height",
					xmlElement.getAttribute("height"));
		}
		presence = new Rectangle(0,0,0,0);
		presence.setX(0);
		presence.setY(0);
		presence.setWidth(presenceWidth);
		presence.setHeight(presenceHeight);
		
		if ((int) presence.getWidth() == 0 || (int) presence.getHeight() == 0)
			presence = null;

		// Animations
		this.animations = new IDableCollection<FRIGAnimation>();
		for (int i = 0; i < xmlElement.getChildrenByName("animation").size(); i++) {
			XMLElement child = xmlElement.getChildrenByName("animation").get(i);

			FRIGAnimation newAnimation = new FRIGAnimation();
			newAnimation.init(child);
			animations.add(newAnimation);
		}
	}

	// Main loop methods
	@Override
	public void update(GameContainer container, int delta, Scene scene) {
		if (getActiveAnimation() != null && getActiveAnimation().isStopped())
			this.activeAnimation = "";
		getCurrentAnimation().update(delta);
	}

	public void render(GameContainer container, Graphics g, Scene scene) {
		if(presence == null)
			scene.renderObject(container, g, getCurrentAnimation(), new Rectangle(
					((ComponentSpacial) entity.getComponent("spacial")).getX()
							+ getCurrentAnimation().getPresence().getX() - getCurrentAnimation().getPresence().getWidth() / 2,
					((ComponentSpacial) entity.getComponent("spacial")).getY()
							+ getCurrentAnimation().getPresence().getY() - getCurrentAnimation().getPresence().getHeight() / 2,
					getCurrentAnimation().getPresence().getWidth(),
					getCurrentAnimation().getPresence().getHeight()));
		else
			scene.renderObject(container, g, getCurrentAnimation(), new Rectangle(
					((ComponentSpacial) entity.getComponent("spacial")).getX()
							+ presence.getX() - presence.getWidth() / 2,
					((ComponentSpacial) entity.getComponent("spacial")).getY()
							+ presence.getY() - presence.getHeight() / 2,
					presence.getWidth(),
					presence.getHeight()));
	}

	// Getters and setters
	public FRIGAnimation getCurrentAnimation() {
		return activeAnimation.equals("") ? getContinuousAnimation()
				: getActiveAnimation();
	}

	public String getCurrentAnimationID() {
		return activeAnimation.equals("") ? continuousAnimation
				: activeAnimation;
	}

	public void setActiveAnimation(String animation) {
		activeAnimation = animation;
		getActiveAnimation().setLooping(false);
		getActiveAnimation().restart();
	}

	public FRIGAnimation getActiveAnimation() {
		if (animations.contains(activeAnimation))
			return animations.get(activeAnimation);
		return null;
	}

	public String getActiveAnimationID() {
		return activeAnimation;
	}

	public void setContinuousAnimation(String animation) {
		activeAnimation = "";
		if (!continuousAnimation.equals(animation)) {
			continuousAnimation = animation;
			if (getContinuousAnimation() == null)
				throw new ComponentException("Drawable component in entity '" + entity.getID()
						+ "' doesn't contain animation '" + animation + "'");
			getContinuousAnimation().setLooping(true);
			getContinuousAnimation().start();
		}
	}

	public FRIGAnimation getContinuousAnimation() {
		if(animations.size() == 0)
			return FRIGAnimation.getPlaceholder();
		if (continuousAnimation.equals(""))
			for(FRIGAnimation a : animations)
				return a;
		if (animations.contains(continuousAnimation))
			return animations.get(continuousAnimation);
		return FRIGAnimation.getPlaceholder();
	}

	public String getContinuousAnimationID() {
		return continuousAnimation;
	}

	@Override
	public String toString() {
		return this.getID() + ": " + getCurrentAnimation().getPresence();
	}
}
