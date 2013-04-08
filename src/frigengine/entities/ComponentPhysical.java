package frigengine.entities;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Ellipse;
import org.newdawn.slick.geom.Line;
import org.newdawn.slick.geom.Transform;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.util.xml.SlickXMLException;
import org.newdawn.slick.util.xml.XMLElement;

import frigengine.exceptions.AttributeFormatException;
import frigengine.exceptions.DataParseException;
import frigengine.exceptions.InvalidTagException;
import frigengine.scene.*;

public class ComponentPhysical extends EntityComponent {
	// Attributes
	public boolean collidable;
	public boolean movable;
	private Shape collisionArea; // relative to spacial position

	// Constructors and initialization
	public ComponentPhysical(Entity entity) {
		super(entity);
	}
	@Override
	public void init(XMLElement xmlElement) {
		if (!xmlElement.getName().equals(this.getClass().getSimpleName()))
			throw new InvalidTagException(this.getClass().getSimpleName(), xmlElement.getName());

		// collidable
		try {
			this.collidable = xmlElement.getBooleanAttribute("collidable", true);
		} catch (SlickXMLException e) {
			throw new AttributeFormatException(xmlElement.getName(), "collidable",
					xmlElement.getAttribute("collidable"));
		}
		
		// movable
		try {
			this.movable = xmlElement.getBooleanAttribute("movable", false);
		} catch (SlickXMLException e) {
			throw new AttributeFormatException(xmlElement.getName(), "movable",
					xmlElement.getAttribute("movable"));
		}

		// collisionArea
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
				throw new AttributeFormatException(xmlElement.getName(), "offset_x",
						child.getAttribute("offset_x"));
			}
			float y;
			try {
				y = (float) child.getDoubleAttribute("offset_y", 0);
			} catch (SlickXMLException e) {
				throw new AttributeFormatException(xmlElement.getName(), "offset_y",
						child.getAttribute("offset_y"));
			}
			float width;
			try {
				width = (float) child.getDoubleAttribute("width", 1);
			} catch (SlickXMLException e) {
				throw new AttributeFormatException(xmlElement.getName(), "width",
						child.getAttribute("width"));
			}
			float height;
			try {
				height = (float) child.getDoubleAttribute("height", 1);
			} catch (SlickXMLException e) {
				throw new AttributeFormatException(xmlElement.getName(), "height",
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
				throw new AttributeFormatException(xmlElement.getName(), "offset_x",
						child.getAttribute("offset_x"));
			}
			float y;
			try {
				y = (float) child.getDoubleAttribute("offset_y", 0);
			} catch (SlickXMLException e) {
				throw new AttributeFormatException(xmlElement.getName(), "offset_y",
						child.getAttribute("offset_y"));
			}
			float radius_x;
			try {
				radius_x = (float) child.getDoubleAttribute("radius_x", 1);
			} catch (SlickXMLException e) {
				throw new AttributeFormatException(xmlElement.getName(), "radius_x",
						child.getAttribute("radius_x"));
			}
			float radius_y;
			try {
				radius_y = (float) child.getDoubleAttribute("radius_y", 1);
			} catch (SlickXMLException e) {
				throw new AttributeFormatException(xmlElement.getName(), "radius_y",
						child.getAttribute("radius_y"));
			}
			this.collisionArea = new Ellipse(0, 0, 0, 0);
			this.collisionArea.setCenterX(x);
			this.collisionArea.setCenterY(y);
			((Ellipse)this.collisionArea).setRadius1(radius_x);
			((Ellipse)this.collisionArea).setRadius2(radius_y);
		}
	}

	// Main loop methods
	@Override
	public void update(int delta, Input input, Scene scene) {
		if (this.collidable && this.movable) {// If collision should be considered
			// Area collision
			for(Shape shape : scene.getBoundaries()) {
				float increment = 0.01F;
				while (this.getCollisionArea().intersects(shape)) {
					((ComponentSpacial)this.entity.getComponent(ComponentSpacial.class)).moveBy(calculateCollisionFix(this.getCollisionArea(), shape));
					/*
					((ComponentSpacial) this.entity.getComponent(ComponentSpacial.class)) // fix its position
					.moveBy(new Vector2f(this.getCollisionArea().getCenterX(), this.getCollisionArea().getCenterY())
							.sub(new Vector2f(shape.getCenterX(), shape.getCenterY())).normalise().scale(0.01F));
					*/
				}
			}
			// Entity collision
			for (Entity otherEntity : scene.getEntities())
				// For each entity,
				if (otherEntity != this.entity // if it's not this entity
						&& otherEntity.hasComponent(ComponentPhysical.class) // and it's physical
						&& ((ComponentPhysical) otherEntity // and its collision is turned on
								.getComponent(ComponentPhysical.class)).collidable) {
					float increment = 0.01F;
					while (this.getCollisionArea().intersects( // If there's an intersection,
							((ComponentPhysical) otherEntity.getComponent(ComponentPhysical.class))
									.getCollisionArea())) {
						((ComponentSpacial)this.entity.getComponent(ComponentSpacial.class)).moveBy(calculateCollisionFix(this.getCollisionArea(), ((ComponentPhysical) otherEntity.getComponent(ComponentPhysical.class)).getCollisionArea()));
						/*
						((ComponentSpacial) this.entity.getComponent(ComponentSpacial.class)) // fix its position
								.moveBy(new Vector2f(this.getCollisionArea().getCenterX(), this
										.getCollisionArea().getCenterY())
										.sub(new Vector2f(((ComponentPhysical) otherEntity
												.getComponent(ComponentPhysical.class)).getCollisionArea()
												.getCenterX(), ((ComponentPhysical) otherEntity
												.getComponent(ComponentPhysical.class)).getCollisionArea()
												.getCenterY())).normalise().scale(0.01F));
												*/
					}
				}
		}
	}

	// Getters and setters
	public Shape getCollisionArea() {
		if (collisionArea instanceof Rectangle)
			return this.collisionArea.transform(Transform.createTranslateTransform(
					((ComponentSpacial) this.entity.getComponent(ComponentSpacial.class)).getX()
							- this.collisionArea.getWidth() / 2,
					((ComponentSpacial) this.entity.getComponent(ComponentSpacial.class)).getY()
							- this.collisionArea.getHeight() / 2));
		else
			return this.collisionArea.transform(Transform.createTranslateTransform(
					((ComponentSpacial) this.entity.getComponent(ComponentSpacial.class)).getX(),
					((ComponentSpacial) this.entity.getComponent(ComponentSpacial.class)).getY()));
	}
	
	// Collision calculation
	private static Vector2f calculateCollisionFix(Shape a, Shape b) {
		Shape transformedA = a;
		float fixX = 0;
		float fixY = 0;
		
		float increment = 0.01F;
		if(b instanceof Line) {
			while (transformedA.intersects(b)) {
				Vector2f closest = new Vector2f();
				((Line)b).getClosestPoint(new Vector2f(a.getCenterX(), a.getCenterY()), closest);
				Vector2f difference = new Vector2f(a.getCenterX(), a.getCenterY()).sub(closest).normalise().scale(increment);
				fixX = difference.getX();
				fixY = difference.getY();
				transformedA = a.transform(Transform.createTranslateTransform(fixX, fixY));
				increment += 0.01F;
			}
		}
		else {
			while (transformedA.intersects(b)) {
				float currentIntersectRating = getIntersectRating(transformedA, b);
				
				float moveX = 0;
				float xImprovement = currentIntersectRating;
				if(getIntersectRating(
						transformedA.transform(Transform.createTranslateTransform(increment, 0)),
						b
						) < currentIntersectRating) {
					moveX = 0.01F;
					xImprovement = getIntersectRating(
							transformedA.transform(Transform.createTranslateTransform(increment, 0)),
							b
							);
				}
				else if(getIntersectRating(
						transformedA.transform(Transform.createTranslateTransform(-1 * increment, 0)),
						b
						) < currentIntersectRating) {
					moveX = -0.01F;
					xImprovement = getIntersectRating(
							transformedA.transform(Transform.createTranslateTransform(-1 * increment, 0)),
							b
							);
				}
				
				float moveY = 0;
				float yImprovement = currentIntersectRating;
				if(getIntersectRating(
						transformedA.transform(Transform.createTranslateTransform(0, increment)),
						b
						) < currentIntersectRating) {
					moveY = 0.01F;
					yImprovement = getIntersectRating(
							transformedA.transform(Transform.createTranslateTransform(0, increment)),
							b
							);
				}
				else if(getIntersectRating(
						transformedA.transform(Transform.createTranslateTransform(0, -1 * increment)),
						b
						) < currentIntersectRating) {
					moveY = -0.01F;
					yImprovement = getIntersectRating(
							transformedA.transform(Transform.createTranslateTransform(0, -1 * increment)),
							b
							);
				}
				
				if(!(Math.abs(moveX) < 0.001F && Math.abs(moveY) < 0.001F)) {
					if(currentIntersectRating - xImprovement >= currentIntersectRating - yImprovement)
						fixX += moveX;
					else
						fixY += moveY;
					increment = 0.01F;
				}
				else {
					increment *= 10;
				}
				
				transformedA = transformedA.transform(Transform.createTranslateTransform(fixX, fixY));
			}
		}
		
		return new Vector2f(fixX, fixY);
	}
	private static float getIntersectRating(Shape a, Shape b) {
		return (getBoundingRectangleIntersection(a,b) + getBoundingCircleIntersection(a,b)) / 2;
	}
	private static float getBoundingRectangleIntersection(Shape a, Shape b) {
		double xOverlap = Math.max(0, Math.min(a.getMaxX(),b.getMaxX()) - Math.max(a.getMinX(),b.getMinX()));
        double yOverlap = Math.max(0, Math.min(a.getMaxY(),b.getMaxY()) - Math.max(a.getMinY(),b.getMinY()));
        return (float)(xOverlap * yOverlap);
	}
	private static float getBoundingCircleIntersection(Shape a, Shape b) {
		double r1 = a.getBoundingCircleRadius();
		double r2 = b.getBoundingCircleRadius();
		double d = Math.sqrt(Math.pow(a.getCenterX() - b.getCenterX(), 2) + Math.pow(a.getCenterY() - b.getCenterY(), 2));
		if(r2 < r1) {
			double temp = r2;
			r2 = r1;
			r1 = temp;
		}
		double part1 = r1*r1*Math.acos((d*d + r1*r1 - r2*r2)/(2*d*r1));
		double part2 = r2*r2*Math.acos((d*d + r2*r2 - r1*r1)/(2*d*r2));
		double part3 = 0.5*Math.sqrt((-d+r1+r2)*(d+r1-r2)*(d-r1+r2)*(d+r1+r2));

		return (float)(part1 + part2 - part3);
	}

	// Utilities
	@Override
	public String toString() {
		return this.getID() + ": collidable-" + this.collidable + " movable-" + this.movable
				+ " collisionArea-" + this.collisionArea;
	}
	public static Set<Class<?>> getComponentDependencies() {
		return new HashSet<Class<?>>( Arrays.asList(new Class<?>[] { ComponentSpacial.class }) );
	}
	public static Set<Class<?>> getComponentExclusives() {
		return new HashSet<Class<?>>();
	}
}
