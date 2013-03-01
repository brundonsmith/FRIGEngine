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
			throw new DataParseException(
					"Xml node does not match component type '" + this.id + "'");

		// Assign attributes
		try {
			this.collidable = xmlElement
					.getBooleanAttribute("collidable", true);
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
			throw new DataParseException("Physical component in entity '"
					+ this.entity.getID() + "' must contain a shape");
		if (xmlElement.getChildren().size() > 1)
			throw new DataParseException("Physical component in entity '"
					+ this.entity.getID() + "' can't have more than one shape");

		if (xmlElement.getChildren().get(0).getName().equals("rectangle")) {
			float x;
			try {
				x = (float) xmlElement.getDoubleAttribute("x", 0);
			} catch (SlickXMLException e) {
				throw new AttributeFormatException(this.getTagName(), "x",
						xmlElement.getAttribute("x"));
			}
			float y;
			try {
				y = (float) xmlElement.getDoubleAttribute("y", 0);
			} catch (SlickXMLException e) {
				throw new AttributeFormatException(this.getTagName(), "y",
						xmlElement.getAttribute("y"));
			}
			float width;
			try {
				width = (float) xmlElement.getDoubleAttribute("width", 1);
			} catch (SlickXMLException e) {
				throw new AttributeFormatException(this.getTagName(), "width",
						xmlElement.getAttribute("width"));
			}
			float height;
			try {
				height = (float) xmlElement.getDoubleAttribute("height", 1);
			} catch (SlickXMLException e) {
				throw new AttributeFormatException(this.getTagName(), "height",
						xmlElement.getAttribute("height"));
			}

			this.collisionArea = new Rectangle(x, y, width, height);
		} else if (xmlElement.getChildren().get(0).getName().equals("ellipse")) {
			float x;
			try {
				x = (float) xmlElement.getDoubleAttribute("x", 0);
			} catch (SlickXMLException e) {
				throw new AttributeFormatException(this.getTagName(), "x",
						xmlElement.getAttribute("x"));
			}
			float y;
			try {
				y = (float) xmlElement.getDoubleAttribute("y", 0);
			} catch (SlickXMLException e) {
				throw new AttributeFormatException(this.getTagName(), "y",
						xmlElement.getAttribute("y"));
			}
			float radius_x;
			try {
				radius_x = (float) xmlElement.getDoubleAttribute("radius_x", 1);
			} catch (SlickXMLException e) {
				throw new AttributeFormatException(this.getTagName(), "radius_x",
						xmlElement.getAttribute("radius_x"));
			}
			float radius_y;
			try {
				radius_y = (float) xmlElement.getDoubleAttribute("radius_y", 1);
			} catch (SlickXMLException e) {
				throw new AttributeFormatException(this.getTagName(), "radius_y",
						xmlElement.getAttribute("radius_y"));
			}

			this.collisionArea = new Ellipse(x, y, radius_x, radius_y);
		}
	}

	// Main loop methods
	@Override
	public void update(GameContainer container, int delta, Scene scene) {
		if (collidable && movable)
			for (Entity otherEntity : scene.getEntities())
				if (otherEntity != this.entity
						&& otherEntity.hasComponent("physical")
						&& ((ComponentPhysical) otherEntity
								.getComponent("physical")).collidable)
					while (this.getCollisionArea().intersects(
							((ComponentPhysical) otherEntity
									.getComponent("physical"))
									.getCollisionArea()))
						((ComponentSpacial) entity.getComponent("spacial"))
								.moveBy(new Vector2f(
										((ComponentPhysical) otherEntity
												.getComponent("physical"))
												.getCollisionArea()
												.getCenterX(),
										((ComponentPhysical) otherEntity
												.getComponent("physical"))
												.getCollisionArea()
												.getCenterY())
										.sub(new Vector2f(this
												.getCollisionArea()
												.getCenterX(), this
												.getCollisionArea()
												.getCenterY())).normalise()
										.scale(0.01F));
	}

	// Other methods
	public Shape getCollisionArea() {
		return this.collisionArea
				.transform(Transform.createTranslateTransform(
						((ComponentSpacial) this.entity.getComponent("spaciaL"))
								.getX(), ((ComponentSpacial) this.entity
								.getComponent("spaciaL")).getY()));
	}
}
