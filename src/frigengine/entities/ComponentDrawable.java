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
			throw new DataParseException("Xml node does not match component type '" + this.id + "'");

		// Attributes
		float presenceX;
		try {
			presenceX = (float) xmlElement.getDoubleAttribute("width", 0);
		} catch (SlickXMLException e) {
			throw new AttributeFormatException(this.getTagName(), "width", xmlElement.getAttribute("width"));
		}
		float presenceY;
		try {
			presenceY = (float) xmlElement.getDoubleAttribute("height", 0);
		} catch (SlickXMLException e) {
			throw new AttributeFormatException(this.getTagName(), "height", xmlElement.getAttribute("height"));
		}
		presence = new Rectangle(0, 0, presenceX, presenceY);
		
		try {
			presence.setCenterX((float) xmlElement.getDoubleAttribute("x_offset", 0));
		} catch (SlickXMLException e) {
			throw new AttributeFormatException(this.getTagName(), "x_offset", xmlElement.getAttribute("x_offset"));
		}
		try {
			presence.setCenterY((float) xmlElement.getDoubleAttribute("y_offset", 0));
		} catch (SlickXMLException e) {
			throw new AttributeFormatException(this.getTagName(), "y_offset", xmlElement.getAttribute("y_offset"));
		}
		
		if ((int) presence.getWidth() == 0 || (int) presence.getHeight() == 0)
			presence = null;

		this.continuousAnimation = xmlElement.getAttribute("default_animation", "animation_0001");

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
		scene.renderObject(
				container,
				g,
				getCurrentAnimation(),
				new Rectangle(((ComponentSpacial) entity.getComponent("spacial")).getX()
						+ getCurrentAnimation().getPresence().getX(), ((ComponentSpacial) entity
						.getComponent("spacial")).getY()
						+ getCurrentAnimation().getPresence().getY(), getCurrentAnimation()
						.getPresence().getWidth(), getCurrentAnimation().getPresence().getHeight()));
	}

	// Getters and setters
	public FRIGAnimation getCurrentAnimation() {
		return activeAnimation.equals("") ? getContinuousAnimation() : getActiveAnimation();
	}
	public String getCurrentAnimationID() {
		return activeAnimation.equals("") ? continuousAnimation : activeAnimation;
	}
	public void setActiveAnimation(String animation) {
		activeAnimation = animation;
		getActiveAnimation().setLooping(false);
		getActiveAnimation().restart();
	}
	public FRIGAnimation getActiveAnimation() {
		if(animations.contains(activeAnimation))
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
			if(getContinuousAnimation() == null)
				throw new ComponentException("Drawable component in entity '" + entity.getID() + "' doesn't contain animation '" + animation + "'");
			getContinuousAnimation().setLooping(true);
			getContinuousAnimation().start();
		}
	}
	public FRIGAnimation getContinuousAnimation() {
		String s = continuousAnimation;
		if(animations.contains(continuousAnimation))
			return animations.get(continuousAnimation);
		return null;
	}
	public String getContinuousAnimationID() {
		return continuousAnimation;
	}
}
