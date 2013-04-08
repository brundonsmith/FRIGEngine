package frigengine.scene;

import org.newdawn.slick.geom.Point;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.util.xml.SlickXMLException;
import org.newdawn.slick.util.xml.XMLElement;

import frigengine.Initializable;
import frigengine.exceptions.AttributeFormatException;
import frigengine.exceptions.InvalidTagException;
import frigengine.util.*;

public class Camera extends IDable<String> implements Initializable {
	private static int cameraCount = 0;

	// Attributes
	private Rectangle presence;

	// Constructors and initialization
	public Camera() {
		Camera.cameraCount++;
		this.id = "camera_" + Integer.toString(Camera.cameraCount, 4);
	}
	public void init(XMLElement xmlElement) {
		if (!xmlElement.getName().equals(this.getClass().getSimpleName()))
			throw new InvalidTagException(this.getClass().getSimpleName(), xmlElement.getName());

		// id
		this.id = xmlElement.getAttribute("id", this.getID());

		// presence
		float x;
		try {
			x = (float) xmlElement.getDoubleAttribute("x", 0);
		} catch (SlickXMLException e) {
			throw new AttributeFormatException(xmlElement.getName(), "x", xmlElement.getAttribute("x"));
		}
		float y;
		try {
			y = (float) xmlElement.getDoubleAttribute("y", 0);
		} catch (SlickXMLException e) {
			throw new AttributeFormatException(xmlElement.getName(), "y", xmlElement.getAttribute("y"));
		}
		float width;
		try {
			width = (float) xmlElement.getDoubleAttribute("width", 400);
		} catch (SlickXMLException e) {
			throw new AttributeFormatException(xmlElement.getName(), "width",
					xmlElement.getAttribute("width"));
		}
		float height;
		try {
			height = (float) xmlElement.getDoubleAttribute("height", 300);
		} catch (SlickXMLException e) {
			throw new AttributeFormatException(xmlElement.getName(), "height",
					xmlElement.getAttribute("height"));
		}
		this.presence = new Rectangle(0, 0, 0, 0);
		this.presence.setX(x);
		this.presence.setY(y);
		this.presence.setWidth(width);
		this.presence.setHeight(height);
	}

	// Getters and setters
	public float getX() {
		return this.presence.getX();
	}
	public float getY() {
		return this.presence.getY();
	}
	public float getWidth() {
		return this.presence.getWidth();
	}
	public float getHeight() {
		return this.presence.getHeight();
	}
	public Point getCenter() {
		return new Point(this.presence.getCenterX(), this.presence.getCenterY());
	}
	public void setCenter(Point center) {
		this.presence.setCenterX(center.getX());
		this.presence.setCenterY(center.getY());
	}

	// Other Methods
	public void moveLeft(float increment) {
		this.presence.setCenterX(this.presence.getCenterX() - increment);
	}
	public void moveRight(float increment) {
		this.presence.setCenterX(this.presence.getCenterX() + increment);
	}
	public void moveUp(float increment) {
		this.presence.setCenterY(this.presence.getCenterY() - increment);
	}
	public void moveDown(float increment) {
		this.presence.setCenterY(this.presence.getCenterY() + increment);
	}
	public void zoom(float scale) {
		float centerX = this.presence.getCenterX();
		float centerY = this.presence.getCenterY();

		this.presence.setSize(this.presence.getWidth() * scale, this.presence.getHeight() * scale);

		this.presence.setCenterX(centerX);
		this.presence.setCenterY(centerY);
	}

	// Utilities
	@Override
	public String toString() {
		return this.getID() + ": " + "(" + this.getX() + ", " + this.getY() + ") "
				+ this.getWidth() + "x" + this.getHeight();
	}
}
