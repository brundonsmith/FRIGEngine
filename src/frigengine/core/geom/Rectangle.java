package frigengine.core.geom;

import org.newdawn.slick.util.xml.SlickXMLException;
import org.newdawn.slick.util.xml.XMLElement;

import frigengine.core.exceptions.data.*;
import frigengine.core.util.*;

@SuppressWarnings("serial")
public class Rectangle extends org.newdawn.slick.geom.Rectangle implements Initializable, Boundable {
	// Constructors and initialization
	public Rectangle() {
		super(0, 0, 1, 1);
	}
	public Rectangle(float x, float y, float width, float height) {
		super(x, y, width, height);
	}
	@Override
	public void init(XMLElement xmlElement) {
		// Check element name
		if (!xmlElement.getName().equals(this.getClass().getSimpleName())) {
			throw new InvalidTagException(this.getClass().getSimpleName(), xmlElement.getName());
		}
		
		// x
		try {
			this.setX((float) xmlElement.getDoubleAttribute("x", this.getX()));
		} catch (SlickXMLException e) {
			throw new AttributeFormatException(xmlElement.getName(), "x",
					xmlElement.getAttribute("x"));
		}
		
		// y
		try {
			this.setY((float) xmlElement.getDoubleAttribute("y", this.getY()));
		} catch (SlickXMLException e) {
			throw new AttributeFormatException(xmlElement.getName(), "y",
					xmlElement.getAttribute("y"));
		}
		
		// width
		try {
			this.setWidth((float) xmlElement.getDoubleAttribute("width", this.getWidth()));
		} catch (SlickXMLException e) {
			throw new AttributeFormatException(xmlElement.getName(), "width",
					xmlElement.getAttribute("width"));
		}
		
		// height
		try {
			this.setHeight((float) xmlElement.getDoubleAttribute("height", this.getHeight()));
		} catch (SlickXMLException e) {
			throw new AttributeFormatException(xmlElement.getName(), "height",
					xmlElement.getAttribute("height"));
		}
	}

	// Getters and setters
	@Override
	public org.newdawn.slick.geom.Rectangle getBoundingRectangle() {
		return new org.newdawn.slick.geom.Rectangle(
				this.getMinX(),
				this.getMinY(),
				this.getWidth(),
				this.getHeight()
				);
	}
	@Override
	public org.newdawn.slick.geom.Ellipse getBoundingEllipse() {
		return new org.newdawn.slick.geom.Ellipse(
				this.getCenterX(),
				this.getCenterY(),
				this.getWidth() / 2,
				this.getHeight() / 2
				);
	}
}
