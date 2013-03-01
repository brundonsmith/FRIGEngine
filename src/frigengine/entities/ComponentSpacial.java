package frigengine.entities;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.geom.*;
import org.newdawn.slick.util.xml.SlickXMLException;
import org.newdawn.slick.util.xml.XMLElement;

import frigengine.exceptions.AttributeFormatException;
import frigengine.exceptions.DataParseException;
import frigengine.scene.*;

public class ComponentSpacial extends EntityComponent {
	public static String getComponentID() {
		return "spacial";
	}

	public String getTagName() {
		return getComponentID();
	}

	public static String[] getComponentDependencies() {
		return new String[] {};
	}

	public static String[] getComponentExclusives() {
		return new String[] {};
	}

	// Attributes
	private Point position;

	// Constructors and initialization
	public ComponentSpacial(Entity entity) {
		super(entity);
		this.id = getComponentID();
	}

	@Override
	public void init(XMLElement xmlElement) {
		if (!xmlElement.getName().equals(this.getID()))
			throw new DataParseException(
					"Xml node does not match component type '" + this.id + "'");

		// Assign attributes
		int x;
		try {
			x = xmlElement.getIntAttribute("x", 0);
		} catch (SlickXMLException e) {
			throw new AttributeFormatException(this.getTagName(), "x",
					xmlElement.getAttribute("x"));
		}
		int y;
		try {
			y = xmlElement.getIntAttribute("y", 0);
		} catch (SlickXMLException e) {
			throw new AttributeFormatException(this.getTagName(), "y",
					xmlElement.getAttribute("y"));
		}
		this.position = new Point(x, y);
	}

	// Main loop methods
	@Override
	public void update(GameContainer container, int delta, Scene scene) {
	}

	// Getters and setters
	public void setPosition(Point position) {
		this.position = position;
	}

	public Point getPosition() {
		return position;
	}

	public void setX(float x) {
		position.setX(x);
	}

	public void setY(float y) {
		position.setY(y);
	}

	public float getX() {
		return position.getX();
	}

	public float getY() {
		return position.getY();
	}

	// Other Methods
	public void moveTo(float x, float y) {
		this.setPosition(new Point(x, y));
	}

	public void moveTo(Point position) {
		this.setPosition(position);
	}

	public void moveBy(float x, float y) {
		this.position = new Point(this.position.getX() + x,
				this.position.getY() + y);
	}

	public void moveBy(Point difference) {
		this.position = new Point(this.position.getX() + difference.getX(),
				this.position.getY() + difference.getY());
	}

	public void moveBy(Vector2f difference) {
		this.position = new Point(this.position.getX() + difference.getX(),
				this.position.getY() + difference.getY());
	}
	
	@Override
	public String toString() {
		return this.getID() + ": " + "(" + this.getX() + ", " + this.getY() + ")";
	}
}
