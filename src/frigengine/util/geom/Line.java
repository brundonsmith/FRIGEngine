package frigengine.util.geom;

import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.util.xml.SlickXMLException;
import org.newdawn.slick.util.xml.XMLElement;

import frigengine.exceptions.data.AttributeFormatException;
import frigengine.exceptions.data.InvalidTagException;
import frigengine.util.Initializable;

@SuppressWarnings("serial")
public class Line extends org.newdawn.slick.geom.Line implements Initializable {
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
			x1 = ((float) xmlElement.getDoubleAttribute("x1", 0));
		} catch (SlickXMLException e) {
			throw new AttributeFormatException(xmlElement.getName(), "x1",
					xmlElement.getAttribute("x1"));
		}
		
		// y1
		float y1;
		try {
			y1 = ((float) xmlElement.getDoubleAttribute("y1", 0));
		} catch (SlickXMLException e) {
			throw new AttributeFormatException(xmlElement.getName(), "y1",
					xmlElement.getAttribute("y1"));
		}

		// x2
		float x2;
		try {
			x2 = ((float) xmlElement.getDoubleAttribute("x2", 0));
		} catch (SlickXMLException e) {
			throw new AttributeFormatException(xmlElement.getName(), "x2",
					xmlElement.getAttribute("x2"));
		}
		
		// y2
		float y2;
		try {
			y2 = ((float) xmlElement.getDoubleAttribute("y2", 0));
		} catch (SlickXMLException e) {
			throw new AttributeFormatException(xmlElement.getName(), "y2",
					xmlElement.getAttribute("y2"));
		}
		
		this.set(x1, y1, x2, y2);
	}

}
