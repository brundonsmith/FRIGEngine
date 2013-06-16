package frigengine.entities;

import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.util.xml.SlickXMLException;
import org.newdawn.slick.util.xml.XMLElement;

import frigengine.exceptions.data.AttributeFormatException;
import frigengine.exceptions.data.InvalidTagException;
import frigengine.exceptions.data.MissingElementException;
import frigengine.util.geom.Ellipse;
import frigengine.util.geom.Rectangle;

public class PhysicsData extends Component {
	// Attributes
	private boolean isCollidable;
	private boolean isMovable;
	private Shape relativeCollisionArea;
	
	// Constructors and initialization
	@Override
	public void init(XMLElement xmlElement) {
		// Check element name
		if (!xmlElement.getName().equals(this.getClass().getSimpleName())) {
			throw new InvalidTagException(this.getClass().getSimpleName(), xmlElement.getName());
		}

		// isCollidable
		try {
			this.isCollidable = xmlElement.getBooleanAttribute("collidable", true);
		} catch (SlickXMLException e) {
			throw new AttributeFormatException(xmlElement.getName(), "collidable",
					xmlElement.getAttribute("collidable"));
		}
		
		// isMovable
		try {
			this.isMovable = xmlElement.getBooleanAttribute("movable", false);
		} catch (SlickXMLException e) {
			throw new AttributeFormatException(xmlElement.getName(), "movable",
					xmlElement.getAttribute("movable"));
		}

		// relativeCollisionArea
		if (xmlElement.getChildren().size() == 0) {
			throw new MissingElementException(Rectangle.class.getSimpleName() + " OR " + Ellipse.class.getSimpleName(), this.getClass().getSimpleName());
		}  else {
			XMLElement child = xmlElement.getChildren().get(0);
			if (child.getName().equals(Rectangle.class.getSimpleName())) { // Collision box is Rectangle
				this.relativeCollisionArea = new Rectangle();
				((Rectangle) this.relativeCollisionArea).init(child);
			} else if (child.getName().equals(Ellipse.class.getSimpleName())) { // Collision box is Ellipse
				this.relativeCollisionArea = new Ellipse();
				((Ellipse) this.relativeCollisionArea).init(child);
			} else {
				throw new MissingElementException(Rectangle.class.getSimpleName() + " OR " + Ellipse.class.getSimpleName(), this.getClass().getSimpleName());
			}
		}
	}

	// Getters and setters
	protected boolean getIsCollidable() {
		return this.isCollidable;
	}
	protected boolean getIsMovable() {
		return this.isMovable;
	}
	protected Shape getRelativeCollisionArea() {
		return this.relativeCollisionArea;
	}
	
	// Utilities
	@Override
	public String toString() {
		return this.getId() + ": collidable-" + this.isCollidable + " movable-" + this.isMovable
				+ " collisionArea-" + this.relativeCollisionArea;
	}
}
