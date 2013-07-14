package frigengine.core.scene;

import java.util.ArrayList;
import java.util.Collection;

import org.newdawn.slick.geom.Point;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.util.xml.SlickXMLException;
import org.newdawn.slick.util.xml.XMLElement;

import frigengine.core.component.*;
import frigengine.core.exceptions.data.*;

public class PositionComponent extends Component {
	// Required components
	@Override
	public Collection<Class<? extends Component>> requiredComponents() {
		return new ArrayList<Class<? extends Component>>();
	}
	
	// Attributes
	private Point position;

	// Constructors and initialization
	public PositionComponent() {
		this.position = new Point(0, 0);
	}
	@Override
	public void init(XMLElement xmlElement) {
		// Check element name
		if (!xmlElement.getName().equals(this.getClass().getSimpleName())) {
			throw new InvalidTagException(this.getClass().getSimpleName(), xmlElement.getName());
		}

		// position
		try {
			this.position.setX((float)xmlElement.getDoubleAttribute("x", this.position.getX()));
		} catch (SlickXMLException e) {
			throw new AttributeFormatException(xmlElement.getName(), "x", xmlElement.getAttribute("x"));
		}
		try {
			this.position.setY((float)xmlElement.getDoubleAttribute("y", this.position.getY()));
		} catch (SlickXMLException e) {
			throw new AttributeFormatException(xmlElement.getName(), "y", xmlElement.getAttribute("y"));
		}
	}

	// Getters and setters
	public Point getPosition() {
		return this.position;
	}
	public void setPosition(Point position) {
		this.position = position;
	}
	public void setPosition(float[] position) {
		this.position = new Point(position[0], position[1]);
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
	
	// Operations
	public void translate(float x, float y) {
		this.position = new Point(this.position.getX() + x, this.position.getY() + y);
	}
	public void translate(Point difference) {
		this.position = new Point(this.position.getX() + difference.getX(), this.position.getY()
				+ difference.getY());
	}
	public void translate(Vector2f difference) {
		this.position = new Point(this.position.getX() + difference.getX(), this.position.getY()
				+ difference.getY());
	}

	// Utilities
	@Override
	public String toString() {
		return "(" + this.position.getX() + ", " + this.position.getY() + ")";
	}
}
