package frigengine.util.geom;

import org.newdawn.slick.util.xml.SlickXMLException;
import org.newdawn.slick.util.xml.XMLElement;

import frigengine.exceptions.data.AttributeFormatException;
import frigengine.exceptions.data.InvalidTagException;
import frigengine.util.Initializable;

@SuppressWarnings("serial")
public class Ellipse extends org.newdawn.slick.geom.Ellipse implements Initializable {
	// Constructors and initialization
	public Ellipse() {
		super(0, 0, 1, 1);
	}
	public Ellipse(float centerPointX, float centerPointY, float radius1, float radius2) {
		super(centerPointX, centerPointY, radius1, radius2);
	}
	public Ellipse(float centerPointX, float centerPointY, float radius1, float radius2, int segmentCount) {
		super(centerPointX, centerPointY, radius1, radius2, segmentCount);
	}
	@Override
	public void init(XMLElement xmlElement) {
		// Check element name
		if (!xmlElement.getName().equals(this.getClass().getSimpleName())) {
			throw new InvalidTagException(this.getClass().getSimpleName(), xmlElement.getName());
		}
		
		// x
		try {
			this.setX((float) xmlElement.getDoubleAttribute("x", 0));
		} catch (SlickXMLException e) {
			throw new AttributeFormatException(xmlElement.getName(), "x",
					xmlElement.getAttribute("x"));
		}
		
		// y
		try {
			this.setY((float) xmlElement.getDoubleAttribute("y", 0));
		} catch (SlickXMLException e) {
			throw new AttributeFormatException(xmlElement.getName(), "y",
					xmlElement.getAttribute("y"));
		}
		
		// radius1
		try {
			this.setRadius1((float) xmlElement.getDoubleAttribute("radiusX", 0));
		} catch (SlickXMLException e) {
			throw new AttributeFormatException(xmlElement.getName(), "radiusX",
					xmlElement.getAttribute("radiusX"));
		}
		
		// radius2
		try {
			this.setRadius2((float) xmlElement.getDoubleAttribute("radiusY", 0));
		} catch (SlickXMLException e) {
			throw new AttributeFormatException(xmlElement.getName(), "radiusY",
					xmlElement.getAttribute("radiusY"));
		}
	}
}
