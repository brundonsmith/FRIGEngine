package frigengine.core.geom;

import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.util.xml.SlickXMLException;
import org.newdawn.slick.util.xml.XMLElement;

import frigengine.core.exceptions.data.*;
import frigengine.core.util.*;

@SuppressWarnings("serial")
public class Line extends org.newdawn.slick.geom.Line implements Initializable, Boundable {
	// Constructors and initialization
	public Line() {
		super(0,0,0,0);
	}
	public Line(float x, float y) {
		super(x, y);
	}
	public Line(float x, float y, boolean inner, boolean outer) {
		super(x, y, inner, outer);
	}
	public Line(float x1, float y1, float x2, float y2) {
		super(x1, y1, x2, y2);
	}
	public Line(float x1, float y1, float dx, float dy, boolean dummy) {
		super(x1, y1, dx, dy, dummy);
	}
	public Line(float[] start, float[] end) {
		super(start, end);
	}
	public Line(Vector2f start, Vector2f end) {
		super(start, end);
	}
	@Override
	public void init(XMLElement xmlElement) {
		// Check element name
		if (!xmlElement.getName().equals(this.getClass().getSimpleName())) {
			throw new InvalidTagException(this.getClass().getSimpleName(), xmlElement.getName());
		}
		
		// x1
		float x1;
		try {
			x1 = ((float) xmlElement.getDoubleAttribute("x1", this.getX1()));
		} catch (SlickXMLException e) {
			throw new AttributeFormatException(xmlElement.getName(), "x1",
					xmlElement.getAttribute("x1"));
		}
		
		// y1
		float y1;
		try {
			y1 = ((float) xmlElement.getDoubleAttribute("y1", this.getY1()));
		} catch (SlickXMLException e) {
			throw new AttributeFormatException(xmlElement.getName(), "y1",
					xmlElement.getAttribute("y1"));
		}

		// x2
		float x2;
		try {
			x2 = ((float) xmlElement.getDoubleAttribute("x2", this.getX2()));
		} catch (SlickXMLException e) {
			throw new AttributeFormatException(xmlElement.getName(), "x2",
					xmlElement.getAttribute("x2"));
		}
		
		// y2
		float y2;
		try {
			y2 = ((float) xmlElement.getDoubleAttribute("y2", this.getY2()));
		} catch (SlickXMLException e) {
			throw new AttributeFormatException(xmlElement.getName(), "y2",
					xmlElement.getAttribute("y2"));
		}
		
		this.set(x1, y1, x2, y2);
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
}
