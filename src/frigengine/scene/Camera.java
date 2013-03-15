package frigengine.scene;

import org.newdawn.slick.geom.Point;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.util.xml.SlickXMLException;
import org.newdawn.slick.util.xml.XMLElement;

import frigengine.Initializable;
import frigengine.exceptions.AttributeFormatException;
import frigengine.exceptions.InvalidTagException;
import frigengine.util.*;

public class Camera extends IDable implements Initializable {
	private static int cameraCount = 0;

	@Override
	public String getTagName() {
		return "camera";
	}

	// Attributes
	private Rectangle presence;

	// Constructors and initialization
	public Camera() {
		Camera.cameraCount++;
		this.id = "camera_" + Integer.toString(Camera.cameraCount, 4);
	}

	public void init(XMLElement xmlElement) {
		if (!xmlElement.getName().equals(getTagName()))
			throw new InvalidTagException(getTagName(), xmlElement.getName());

		// Attributes
		this.id = xmlElement.getAttribute("id", this.getID());
		
		float x;
		try {
			x = (float) xmlElement.getDoubleAttribute("x", 0);
		} catch (SlickXMLException e) {
			throw new AttributeFormatException(getTagName(), "x",
					xmlElement.getAttribute("x"));
		}
		float y;
		try {
			y = (float) xmlElement.getDoubleAttribute("y", 0);
		} catch (SlickXMLException e) {
			throw new AttributeFormatException(getTagName(), "y",
					xmlElement.getAttribute("y"));
		}
		float width;
		try {
			width = (float) xmlElement.getDoubleAttribute("width", 400);
		} catch (SlickXMLException e) {
			throw new AttributeFormatException(getTagName(), "width",
					xmlElement.getAttribute("width"));
		}
		float height;
		try {
			height = (float) xmlElement.getDoubleAttribute("height", 300);
		} catch (SlickXMLException e) {
			throw new AttributeFormatException(getTagName(), "height",
					xmlElement.getAttribute("height"));
		}
		
		this.presence = new Rectangle(0,0,0,0);
		this.presence.setX(x);
		this.presence.setY(y);
		this.presence.setWidth(width);
		this.presence.setHeight(height);
	}

	// Getters and setters
	public float getX() {
		return presence.getX();
	}

	public float getY() {
		return presence.getY();
	}

	public float getWidth() {
		return presence.getWidth();
	}

	public float getHeight() {
		return presence.getHeight();
	}

	public Point getCenter() {
		return new Point(presence.getCenterX(), presence.getCenterY());
	}

	public void setCenter(Point center) {
		presence.setCenterX(center.getX());
		presence.setCenterY(center.getY());
	}

	// Other Methods
	public void moveLeft(float increment) {
		presence.setCenterX(presence.getCenterX() - increment);
	}

	public void moveRight(float increment) {
		presence.setCenterX(presence.getCenterX() + increment);
	}

	public void moveUp(float increment) {
		presence.setCenterY(presence.getCenterY() - increment);
	}

	public void moveDown(float increment) {
		presence.setCenterY(presence.getCenterY() + increment);
	}

	public void zoom(float scale) {
		float centerX = presence.getCenterX();
		float centerY = presence.getCenterY();

		presence.setSize(presence.getWidth() * scale, presence.getHeight() * scale);
		
		presence.setCenterX(centerX);
		presence.setCenterY(centerY);
	}
	
	@Override
	public String toString() {
		return this.getID() + ": " + "(" + this.getX() + ", " + this.getY() + ") " + this.getWidth() + "x" + this.getHeight();
	}
}
