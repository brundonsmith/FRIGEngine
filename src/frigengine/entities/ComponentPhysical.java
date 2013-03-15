package frigengine.entities;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Ellipse;
import org.newdawn.slick.geom.Transform;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.util.xml.SlickXMLException;
import org.newdawn.slick.util.xml.XMLElement;

import frigengine.exceptions.AttributeFormatException;
import frigengine.exceptions.DataParseException;
import frigengine.scene.*;

public class ComponentPhysical extends EntityComponent {
	public static String getComponentID() {
		return "physical";
	}

	public String getTagName() {
		return getComponentID();
	}

	public static String[] getComponentDependencies() {
		return new String[] { "spacial" };
	}

	public static String[] getComponentExclusives() {
		return new String[] {};
	}

	// Attributes
	public boolean collidable;
	public boolean movable;
	private Shape collisionArea; // relative to spacial position

	// Constructors and initialization
	public ComponentPhysical(Entity entity) {
		super(entity);
		this.id = getComponentID();
	}

	@Override
	public void init(XMLElement xmlElement) {
		if (!xmlElement.getName().equals(this.getID()))
			throw new DataParseException("Xml node does not match component type '" + this.id + "'");

		// Assign attributes
		try {
			this.collidable = xmlElement.getBooleanAttribute("collidable", true);
		} catch (SlickXMLException e) {
			throw new AttributeFormatException(this.getTagName(), "collidable",
					xmlElement.getAttribute("collidable"));
		}
		try {
			this.movable = xmlElement.getBooleanAttribute("movable", false);
		} catch (SlickXMLException e) {
			throw new AttributeFormatException(this.getTagName(), "movable",
					xmlElement.getAttribute("movable"));
		}

		// Shape
		if (xmlElement.getChildren().size() == 0)
			throw new DataParseException("Physical component in entity '" + this.entity.getID()
					+ "' must contain a shape");
		if (xmlElement.getChildren().size() > 1)
			throw new DataParseException("Physical component in entity '" + this.entity.getID()
					+ "' can't have more than one shape");

		XMLElement child = xmlElement.getChildren().get(0);
		if (child.getName().equals("rectangle")) { // Collision box is Rectangle
			float x;
			try {
				x = (float) child.getDoubleAttribute("offset_x", 0);
			} catch (SlickXMLException e) {
				throw new AttributeFormatException(this.getTagName(), "offset_x",
						child.getAttribute("offset_x"));
			}
			float y;
			try {
				y = (float) child.getDoubleAttribute("offset_y", 0);
			} catch (SlickXMLException e) {
				throw new AttributeFormatException(this.getTagName(), "offset_y",
						child.getAttribute("offset_y"));
			}
			float width;
			try {
				width = (float) child.getDoubleAttribute("width", 1);
			} catch (SlickXMLException e) {
				throw new AttributeFormatException(this.getTagName(), "width",
						child.getAttribute("width"));
			}
			float height;
			try {
				height = (float) child.getDoubleAttribute("height", 1);
			} catch (SlickXMLException e) {
				throw new AttributeFormatException(this.getTagName(), "height",
						child.getAttribute("height"));
			}

			this.collisionArea = new Rectangle(0, 0, 0, 0);
			this.collisionArea.setX(x);
			this.collisionArea.setY(y);
			((Rectangle) this.collisionArea).setWidth(width);
			((Rectangle) this.collisionArea).setHeight(height);
		} else if (child.getName().equals("ellipse")) { // Collision box is Ellipse
			float x;
			try {
				x = (float) child.getDoubleAttribute("offset_x", 0);
			} catch (SlickXMLException e) {
				throw new AttributeFormatException(this.getTagName(), "offset_x",
						child.getAttribute("offset_x"));
			}
			float y;
			try {
				y = (float) child.getDoubleAttribute("offset_y", 0);
			} catch (SlickXMLException e) {
				throw new AttributeFormatException(this.getTagName(), "offset_y",
						child.getAttribute("offset_y"));
			}
			float radius_x;
			try {
				radius_x = (float) child.getDoubleAttribute("radius_x", 1);
			} catch (SlickXMLException e) {
				throw new AttributeFormatException(this.getTagName(), "radius_x",
						child.getAttribute("radius_x"));
			}
			float radius_y;
			try {
				radius_y = (float) child.getDoubleAttribute("radius_y", 1);
			} catch (SlickXMLException e) {
				throw new AttributeFormatException(this.getTagName(), "radius_y",
						child.getAttribute("radius_y"));
			}

			this.collisionArea = new Ellipse(x, y, radius_x, radius_y);
		}
	}

	// Main loop methods
	@Override
	public void update(GameContainer container, int delta, Scene scene) {
		if (collidable && movable) // If collision should be considered
			for (Entity otherEntity : scene.getEntities())
				// For each entity,
				if (otherEntity != this.entity // if it's not this entity
						&& otherEntity.hasComponent("physical") // and it's physical
						&& ((ComponentPhysical) otherEntity // and its collision is turned on
								.getComponent("physical")).collidable)
					while (this.getCollisionArea().intersects( // If there's an intersection,
							((ComponentPhysical) otherEntity.getComponent("physical"))
									.getCollisionArea())) {
						((ComponentSpacial) entity.getComponent("spacial")) // fix its position
								.moveBy(new Vector2f(this.getCollisionArea().getCenterX(), this
										.getCollisionArea().getCenterY())
										.sub(new Vector2f(((ComponentPhysical) otherEntity
												.getComponent("physical")).getCollisionArea()
												.getCenterX(), ((ComponentPhysical) otherEntity
												.getComponent("physical")).getCollisionArea()
												.getCenterY())).normalise().scale(0.01F));
					}
	}

	// Other methods
	public Shape getCollisionArea() {
		if (collisionArea instanceof Rectangle)
			return this.collisionArea.transform(Transform.createTranslateTransform(
					((ComponentSpacial) this.entity.getComponent("spacial")).getX()
							- this.collisionArea.getWidth() / 2,
					((ComponentSpacial) this.entity.getComponent("spacial")).getY()
							- this.collisionArea.getHeight() / 2));
		else
			return this.collisionArea.transform(Transform.createTranslateTransform(
					((ComponentSpacial) this.entity.getComponent("spacial")).getX(),
					((ComponentSpacial) this.entity.getComponent("spacial")).getY()));
	}

	@Override
	public String toString() {
		return this.getID() + ": collidable-" + this.collidable + " movable-" + this.movable
				+ " collisionArea-" + this.collisionArea;
	}
}
