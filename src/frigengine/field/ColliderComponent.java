package frigengine.field;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Transform;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.util.xml.SlickXMLException;
import org.newdawn.slick.util.xml.XMLElement;

import frigengine.core.component.*;
import frigengine.core.exceptions.data.*;
import frigengine.core.geom.*;
import frigengine.core.scene.*;

public class ColliderComponent extends Component {
	// Required components
	@Override
	public  Collection<Class<? extends Component>> requiredComponents() {
		return new ArrayList<Class<? extends Component>>(Arrays.asList(
				PositionComponent.class
			));
	}
	
	// Attributes
	private Shape domain;
	private Set<String> collisionGroups;
	private boolean isMovable;
	private Collection<Entity> collisions;
	
	// Constructors and initialization
	public ColliderComponent() {
		this.domain = null;
		this.collisionGroups = new HashSet<String>();
		this.isMovable = false;
		this.collisions = new ArrayList<Entity>();
	}
	private ColliderComponent(ColliderComponent other) {
		super(other);
		
		this.domain = other.domain.transform(Transform.createTranslateTransform(0, 0));
		this.collisionGroups = new HashSet<String>();
		for(String s : other.collisionGroups) {
			this.collisionGroups.add(s);
		}
		this.isMovable = other.isMovable;
		this.collisions = new ArrayList<Entity>();
		for(Entity e : other.collisions) {
			this.collisions.add(e);
		}
	}
	@Override
	public ColliderComponent clone() {
		return new ColliderComponent(this);
	}
	@Override
	public void init(XMLElement xmlElement) {
		// Check element name
		if (!xmlElement.getName().equals(this.getClass().getSimpleName())) {
			throw new InvalidTagException(this.getClass().getSimpleName(), xmlElement.getName());
		}

		// domain
		if (xmlElement.getChildren().size() == 0 && this.domain == null) {
			throw new MissingElementException(Line.class.getSimpleName() + " OR " + Rectangle.class.getSimpleName() + " OR " + Ellipse.class.getSimpleName(), this.getClass().getSimpleName());
		}  else {
			XMLElement child = xmlElement.getChildren().get(0);
			if (child.getName().equals(Line.class.getSimpleName())) { // Collision box is Line
				this.domain = new Line();
				((Line) this.domain).init(child);
			} else if (child.getName().equals(Rectangle.class.getSimpleName())) { // Collision box is Rectangle
				this.domain = new Rectangle();
				((Rectangle) this.domain).init(child);
			} else if (child.getName().equals(Ellipse.class.getSimpleName())) { // Collision box is Ellipse
				this.domain = new Ellipse();
				((Ellipse) this.domain).init(child);
			} else {
				throw new MissingElementException(Line.class.getSimpleName() + " OR " + Rectangle.class.getSimpleName() + " OR " + Ellipse.class.getSimpleName(), this.getClass().getSimpleName());
			}
		}
		
		// collisionGroups
		for(String group : xmlElement.getAttribute("collisiongroups", "").split(" ")) {
			this.collisionGroups.add(group);
		}
		
		// isMovable
		try {
			this.isMovable = xmlElement.getBooleanAttribute("movable", this.isMovable);
		} catch (SlickXMLException e) {
			throw new AttributeFormatException(xmlElement.getName(), "movable",
					xmlElement.getAttribute("movable"));
		}
	}

	// Main loop methods
	@Override
	public void update(int delta, Input input) {
		this.collisions.clear();
		for(Entity e : getLocalEntities(ColliderComponent.class)) {
			ColliderComponent c = e.getComponent(ColliderComponent.class);
			// For each entity,
			if (!c.equals(this)){ // if not this entity
				if(this.getWorldDomain().intersects( // If there's an intersection,
						c.getWorldDomain())) {
					this.collisions.add(e);
				}
				while (this.isMovable && this.getIsCollidable(c) && this.getWorldDomain().intersects( // If there's a collision,
						c.getWorldDomain())) {
					getComponent(PositionComponent.class).translate(ColliderComponent.calculateCollisionFix(this.getWorldDomain(), c.getWorldDomain()));
				}
			}
		}
	}
	
	// Collision calculation
	private static Vector2f closestPoint = new Vector2f();
	private static Vector2f difference = new Vector2f();
	private static Vector2f calculateCollisionFix(Shape a, Shape b) {
		Shape transformedA = a;
		float fixX = 0;
		float fixY = 0;
		
		float increment = 0.01F;
		if(b instanceof org.newdawn.slick.geom.Line) { 	// special case for line
			while (transformedA.intersects(b)) {
				((org.newdawn.slick.geom.Line)b).getClosestPoint(new Vector2f(a.getCenterX(), a.getCenterY()), closestPoint);
				difference.set(a.getCenterX(), a.getCenterY()).sub(closestPoint).normalise().scale(increment);
				fixX = difference.getX();
				fixY = difference.getY();
				transformedA = a.transform(Transform.createTranslateTransform(fixX, fixY));
				increment += 0.01F;
			}
		} else { 							// if another shape
			while (transformedA.intersects(b)) {
				// calculate intersection amount
				float currentIntersectRating = ColliderComponent.getIntersectRating(transformedA, b);
				
				// calculate x change
				float moveX = 0;
				float xImprovement = currentIntersectRating;
				if(ColliderComponent.getIntersectRating( // try moving to the right
						transformedA.transform(Transform.createTranslateTransform(increment, 0)),
						b
						) < currentIntersectRating) {
					moveX = 0.01F;
					xImprovement = getIntersectRating(
							transformedA.transform(Transform.createTranslateTransform(increment, 0)),
							b
							);
				} else if(ColliderComponent.getIntersectRating( // try moving to the left
						transformedA.transform(Transform.createTranslateTransform(-1 * increment, 0)),
						b
						) < currentIntersectRating) {
					moveX = -0.01F;
					xImprovement = getIntersectRating(
							transformedA.transform(Transform.createTranslateTransform(-1 * increment, 0)),
							b
							);
				}
				
				// calculate y change
				float moveY = 0;
				float yImprovement = currentIntersectRating;
				if(ColliderComponent.getIntersectRating( // try moving down
						transformedA.transform(Transform.createTranslateTransform(0, increment)),
						b
						) < currentIntersectRating) {
					moveY = 0.01F;
					yImprovement = ColliderComponent.getIntersectRating(
							transformedA.transform(Transform.createTranslateTransform(0, increment)),
							b
							);
				} else if(ColliderComponent.getIntersectRating( // try moving up
						transformedA.transform(Transform.createTranslateTransform(0, -1 * increment)),
						b
						) < currentIntersectRating) {
					moveY = -0.01F;
					yImprovement = ColliderComponent.getIntersectRating(
							transformedA.transform(Transform.createTranslateTransform(0, -1 * increment)),
							b
							);
				}
				
				// apply changes
				if(!(Math.abs(moveX) < 0.001F && Math.abs(moveY) < 0.001F)) { // if moved a significant amount
					if(currentIntersectRating - xImprovement >= currentIntersectRating - yImprovement) {// and it's a good trade
						fixX += moveX; // adjust
					} else {
						fixY += moveY;
					}
					increment = 0.01F;
				} else {
					increment *= 10;
				}
				
				transformedA = transformedA.transform(Transform.createTranslateTransform(fixX, fixY));
			}
		}
		
		return new Vector2f(fixX, fixY);
	}
	private static float getIntersectRating(Shape a, Shape b) {
		return (ColliderComponent.getBoundingRectangleIntersection(a,b) + ColliderComponent.getBoundingCircleIntersection(a,b)) / 2;
	}
	private static float getBoundingRectangleIntersection(Shape a, Shape b) {
		double buffer = 0.5;
		double xOverlap = Math.max(0, Math.min(a.getMaxX() + buffer,b.getMaxX() + buffer) - Math.max(a.getMinX() - buffer,b.getMinX() - buffer));
        double yOverlap = Math.max(0, Math.min(a.getMaxY() + buffer,b.getMaxY() + buffer) - Math.max(a.getMinY() - buffer,b.getMinY() - buffer));
        return (float)(xOverlap * yOverlap);
	}
	private static float getBoundingCircleIntersection(Shape a, Shape b) {
		double buffer = 0.5;
		double r1 = a.getBoundingCircleRadius() + buffer;
		double r2 = b.getBoundingCircleRadius() + buffer;
		double d = Math.sqrt(Math.pow(a.getCenterX() - b.getCenterX(), 2) + Math.pow(a.getCenterY() - b.getCenterY(), 2));
		if(r2 < r1) {
			double temp = r2;
			r2 = r1;
			r1 = temp;
		}
		double part1 = r1*r1*Math.acos((d*d + r1*r1 - r2*r2)/(2*d*r1));
		double part2 = r2*r2*Math.acos((d*d + r2*r2 - r1*r1)/(2*d*r2));
		double part3 = 0.5*Math.sqrt((-d+r1+r2)*(d+r1-r2)*(d-r1+r2)*(d+r1+r2));

		if(Double.isNaN(part1) || Double.isNaN(part2) || Double.isNaN(part3)) {
			return 0;
		} else {
			return (float)(part1 + part2 - part3);
		}
	}
	
	// Getters and setters
	public Shape getLocalDomain() {
		return this.domain;
	}
	public Shape getWorldDomain() {
		if(this.domain instanceof Line) {
			return this.domain.transform(Transform.createTranslateTransform(getComponent(PositionComponent.class).getX(), getComponent(PositionComponent.class).getY()));
		} else {
			Shape relative = this.domain.transform(Transform.createTranslateTransform(0,0));
			relative.setCenterX(getComponent(PositionComponent.class).getX() + this.domain.getX());
			relative.setCenterY(getComponent(PositionComponent.class).getY() + this.domain.getY());
			return relative;
		}
	}
	public void setWidth(float width) {
		if(this.domain instanceof Rectangle) {
			((Rectangle) this.domain).setWidth(width);
		} else if(this.domain instanceof Ellipse) {
			((Ellipse) this.domain).setRadius1(width);
		}
	}
	public void setHeight(float height) {
		if(this.domain instanceof Rectangle) {
			((Rectangle) this.domain).setHeight(height);
		} else if(this.domain instanceof Ellipse) {
			((Ellipse) this.domain).setRadius2(height);
		}
	}
	protected boolean getIsCollidable(ColliderComponent c) {
		for(String g1 : this.collisionGroups) {
			for(String g2 : c.collisionGroups) {
				if(g1.equals(g2)) {
					return true;
				}
			}
		}
		return false;
	}
	protected boolean getIsMovable() {
		return this.isMovable;
	}
	public Collection<Entity> getCollisions() {
		return this.collisions;
	}
	
	// Utilities
	@Override
	public String toString() {
		return this.getClass().getSimpleName() + ": {" + "collision groups: " + Arrays.toString(this.collisionGroups.toArray()) + ", movable:" + this.isMovable
				+ ", domain:" + this.domain + "}";
	}
}
