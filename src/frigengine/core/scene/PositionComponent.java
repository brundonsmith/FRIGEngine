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
	private int elevation;

	// Constructors and initialization
	public PositionComponent() {
		this.position = new Point(0, 0);
		this.elevation = 0;
	}
	private PositionComponent(PositionComponent other) {
		super(other);
		
		this.position = new Point(other.position.getX(), other.position.getY());
		this.elevation = other.elevation;
	}
	public PositionComponent clone() {
		return new PositionComponent(this);
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
		
		// elevation
		try {
			this.elevation = xmlElement.getIntAttribute("elevation", this.elevation);
		} catch (SlickXMLException e) {
			throw new AttributeFormatException(xmlElement.getName(), "elevation", xmlElement.getAttribute("elevation"));
		}
	}

	// Getters and setters
	public Point getPosition() {
		return this.position;
	}
	public Vector2f getPositionVector() {
		return new Vector2f(this.position.getX(), this.position.getY());
	}
	public void setPosition(Point position) {
		this.position = position;
	}
	public void setPosition(float x, float y) {
		this.position.setX(x);
		this.position.setY(y);
	}
	public void setPosition(float[] position) {
		this.position.setX(position[0]);
		this.position.setY(position[1]);
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
	public int getElevation() {
		return this.elevation;
	}
	public void setElevation(int elevation) {
		this.elevation = elevation;
	}
	
	// Operations
	public void translate(float x, float y) {
		this.position.setX(this.position.getX() + x);
		this.position.setY(this.position.getY() + y);
	}
	public void translate(Point difference) {
		this.position.setX(this.position.getX() + difference.getX());
		this.position.setY(this.position.getY()	+ difference.getY());
	}
	public void translate(Vector2f difference) {
		this.position.setX(this.position.getX() + difference.getX());
		this.position.setY(this.position.getY()	+ difference.getY());
	}

	// Utilities
	@Override
	public String toString() {
		return this.getClass().getSimpleName() + ": position=(" + this.position.getX() + ", " + this.position.getY() + "); elevation=" + this.elevation;
	}
}
