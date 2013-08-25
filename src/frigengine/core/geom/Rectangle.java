package frigengine.core.geom;

import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Transform;
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
	public Rectangle(org.newdawn.slick.geom.Rectangle rectangle) {
		super(rectangle.getX(), rectangle.getY(), rectangle.getWidth(), rectangle.getHeight());
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
	private org.newdawn.slick.geom.Rectangle boundingRectangle = new org.newdawn.slick.geom.Rectangle(0,0,0,0);
	@Override
	public org.newdawn.slick.geom.Rectangle getBoundingRectangle() {
		boundingRectangle.setX(this.getMinX());
		boundingRectangle.setY(this.getMinY());
		boundingRectangle.setWidth(this.getWidth());
		boundingRectangle.setHeight(this.getHeight());
		return boundingRectangle;
	}
	private org.newdawn.slick.geom.Ellipse boundingEllipse = new org.newdawn.slick.geom.Ellipse(0,0,0,0);
	@Override
	public org.newdawn.slick.geom.Ellipse getBoundingEllipse() {
		boundingEllipse.setCenterX(this.getCenterX());
		boundingEllipse.setCenterY(this.getCenterY());
		boundingEllipse.setRadius1(this.getWidth() / 2);
		boundingEllipse.setRadius2(this.getHeight() / 2);
		return boundingEllipse;
	}

	// Operations
	@Override
	public Rectangle transform(Transform transform) {
		Shape tranformed = super.transform(transform);

		return new Rectangle(
				tranformed.getMinX(),
				tranformed.getMinY(),
				tranformed.getWidth(),
				tranformed.getHeight()
				);
	}
	
	// Utilities
	@Override
	public String toString() {
		return "Rectangle: x=" + this.getX() + " y=" + this.getY() + " " + this.getWidth() + "x" + this.getHeight(); 
	}
}
