package frigengine.scene;

import org.newdawn.slick.geom.Point;
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
	private float x;
	private float y;
	private float width;
	private float height;

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
		try {
			this.x = (float) xmlElement.getDoubleAttribute("x", 0);
		} catch (SlickXMLException e) {
			throw new AttributeFormatException(getTagName(), "x",
					xmlElement.getAttribute("x"));
		}
		try {
			this.y = (float) xmlElement.getDoubleAttribute("y", 0);
		} catch (SlickXMLException e) {
			throw new AttributeFormatException(getTagName(), "y",
					xmlElement.getAttribute("y"));
		}
		try {
			this.width = (float) xmlElement.getDoubleAttribute("width", 400);
		} catch (SlickXMLException e) {
			throw new AttributeFormatException(getTagName(), "width",
					xmlElement.getAttribute("width"));
		}
		try {
			this.height = (float) xmlElement.getDoubleAttribute("height", 300);
		} catch (SlickXMLException e) {
			throw new AttributeFormatException(getTagName(), "height",
					xmlElement.getAttribute("height"));
		}
	}

	// Getters and setters
	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

	public float getWidth() {
		return width;
	}

	public float getHeight() {
		return height;
	}

	public Point getCenter() {
		return new Point(x + width / 2, y + height / 2);
	}

	public void setCenter(Point center) {
		x = center.getX() - width / 2;
		y = center.getY() - height / 2;
	}

	// Other Methods
	public void moveLeft(int increment) {
		x -= increment;
	}

	public void moveRight(int increment) {
		x += increment;
	}

	public void moveUp(int increment) {
		y -= increment;
	}

	public void moveDown(int increment) {
		y += increment;
	}

	public void zoom(float scale) {
		float diffX = width / scale - width;
		x -= diffX / 2;
		width = width / scale;

		float diffY = height / scale - height;
		y -= diffY / 2;
		height = height / scale;
	}
}
