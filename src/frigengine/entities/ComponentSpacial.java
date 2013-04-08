package frigengine.entities;

import java.util.HashSet;
import java.util.Set;

import org.newdawn.slick.Input;
import org.newdawn.slick.geom.*;
import org.newdawn.slick.util.xml.SlickXMLException;
import org.newdawn.slick.util.xml.XMLElement;

import frigengine.exceptions.AttributeFormatException;
import frigengine.exceptions.InvalidTagException;
import frigengine.scene.*;

public class ComponentSpacial extends EntityComponent {
	// Attributes
	private Point position;

	// Constructors and initialization
	public ComponentSpacial(Entity entity) {
		super(entity);
	}
	@Override
	public void init(XMLElement xmlElement) {
		if (!xmlElement.getName().equals(this.getClass().getSimpleName()))
			throw new InvalidTagException(this.getClass().getSimpleName(), xmlElement.getName());

		// position
		int x;
		try {
			x = xmlElement.getIntAttribute("x", 0);
		} catch (SlickXMLException e) {
			throw new AttributeFormatException(xmlElement.getName(), "x", xmlElement.getAttribute("x"));
		}
		int y;
		try {
			y = xmlElement.getIntAttribute("y", 0);
		} catch (SlickXMLException e) {
			throw new AttributeFormatException(xmlElement.getName(), "y", xmlElement.getAttribute("y"));
		}
		this.position = new Point(0, 0);
		this.position.setX(x);
		this.position.setY(y);
	}

	// Main loop methods
	@Override
	public void update(int delta, Input input, Scene scene) {
	}

	// Getters and setters
	public Point getPosition() {
		return this.position;
	}
	public void setPosition(Point position) {
		this.position = position;
	}
	public float getX() {
		return this.position.getX();
	}
	public void setX(float x) {
		this.position.setX(x);
	}
	public float getY() {
		return this.position.getY();
	}
	public void setY(float y) {
		this.position.setY(y);
	}

	// Movement
	public void moveTo(float x, float y) {
		this.setPosition(new Point(x, y));
	}
	public void moveTo(Point position) {
		this.setPosition(position);
	}
	public void moveBy(float x, float y) {
		this.position = new Point(this.position.getX() + x, this.position.getY() + y);
	}
	public void moveBy(Point difference) {
		this.position = new Point(this.position.getX() + difference.getX(), this.position.getY()
				+ difference.getY());
	}
	public void moveBy(Vector2f difference) {
		this.position = new Point(this.position.getX() + difference.getX(), this.position.getY()
				+ difference.getY());
	}
	
	// Utilities
	@Override
	public String toString() {
		return this.getID() + ": " + "(" + this.getX() + ", " + this.getY() + ")";
	}
	public static Set<Class<?>> getComponentDependencies() {
		return new HashSet<Class<?>>();
	}
	public static Set<Class<?>> getComponentExclusives() {
		return new HashSet<Class<?>>();
	}
}
