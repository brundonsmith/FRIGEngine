package frigengine.entities;

import org.newdawn.slick.geom.Point;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.util.xml.SlickXMLException;
import org.newdawn.slick.util.xml.XMLElement;

import frigengine.exceptions.data.AttributeFormatException;
import frigengine.exceptions.data.InvalidTagException;

public class PositionData extends Component {
	// Attributes
	private Point position;

	// Constructors and initialization
	@Override
	public void init(XMLElement xmlElement) {
		// Check element name
		if (!xmlElement.getName().equals(this.getClass().getSimpleName())) {
			throw new InvalidTagException(this.getClass().getSimpleName(), xmlElement.getName());
		}

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

	// Getters and setters
	protected Point getPosition() {
		return this.position;
	}
	protected void setPosition(Point position) {
		this.position = position;
	}
	protected float getX() {
		return this.position.getX();
	}
	protected void setX(float x) {
		this.position.setX(x);
	}
	protected float getY() {
		return this.position.getY();
	}
	protected void setY(float y) {
		this.position.setY(y);
	}
	
	// Operations
	protected void translate(float x, float y) {
		this.position = new Point(this.position.getX() + x, this.position.getY() + y);
	}
	protected void translate(Point difference) {
		this.position = new Point(this.position.getX() + difference.getX(), this.position.getY()
				+ difference.getY());
	}
	protected void translate(Vector2f difference) {
		this.position = new Point(this.position.getX() + difference.getX(), this.position.getY()
				+ difference.getY());
	}

	// Utilities
	@Override
	public String toString() {
		return "(" + this.position.getX() + ", " + this.position.getY() + ")";
	}
}
