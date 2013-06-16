package frigengine.util.geom;

import org.newdawn.slick.util.xml.SlickXMLException;
import org.newdawn.slick.util.xml.XMLElement;

import frigengine.exceptions.data.AttributeFormatException;
import frigengine.exceptions.data.InvalidTagException;
import frigengine.util.Initializable;

@SuppressWarnings("serial")
public class Rectangle extends org.newdawn.slick.geom.Rectangle implements Initializable {
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
		
		// width
		try {
			this.setWidth((float) xmlElement.getDoubleAttribute("width", 0));
		} catch (SlickXMLException e) {
			throw new AttributeFormatException(xmlElement.getName(), "width",
					xmlElement.getAttribute("width"));
		}
		
		// height
		try {
			this.setHeight((float) xmlElement.getDoubleAttribute("height", 0));
		} catch (SlickXMLException e) {
			throw new AttributeFormatException(xmlElement.getName(), "height",
					xmlElement.getAttribute("height"));
		}
	}
}
