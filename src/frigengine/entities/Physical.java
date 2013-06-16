package frigengine.entities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Set;

import org.newdawn.slick.geom.Line;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Transform;
import org.newdawn.slick.geom.Vector2f;

import frigengine.util.IDableCollection;
import frigengine.util.geom.Ellipse;
import frigengine.util.geom.Rectangle;

public class Physical extends Category {
	// Constants
	@Override
	public  Collection<Class<? extends Component>> getRequiredComponents() {
		return new ArrayList<Class<? extends Component>>(Arrays.asList(
				PositionData.class,
				PhysicsData.class
			));
	}
	
	// Access
	protected PositionData posData() {
		return (PositionData) this.components.get(PositionData.class);
	}
	protected PhysicsData physData() {
		return (PhysicsData) this.components.get(PhysicsData.class);
	}
	
	// Main loop methods
	public void update(Set<Shape> areaBoundaries, IDableCollection<String, Chunk> otherObjects) {
		if (this.physData().getIsCollidable() && this.physData().getIsMovable()) {// If collision should be considered
			// Area collision
			for(Shape s : areaBoundaries) {
				while (this.getRealCollisionArea().intersects(s)) {
					this.posData().translate(Physical.calculateCollisionFix(this.getRealCollisionArea(), s));
				}
			}
			
			// Entity collision
			for (Chunk c : otherObjects) {
				Physical p = (Physical)c.getCategory(Physical.class);
				
				// For each entity,
				if (c.getId() != this.getEntityId()  // if it's not this entity
						&& p.physData().getIsCollidable()) { // and its collision is turned on
					while (this.getRealCollisionArea().intersects( // If there's an intersection,
							p.getRealCollisionArea())) {
						this.posData().translate(Physical.calculateCollisionFix(this.getRealCollisionArea(), p.getRealCollisionArea()));
					}
				}
			}
		}
	}

	// Getters and setters
	public float getWidth() {
		return this.physData().getRelativeCollisionArea() .getWidth();
	}
	public float getHeight() {
		return this.physData().getRelativeCollisionArea() .getHeight();
	}
	private Shape getRealCollisionArea() {
		if (this.physData().getRelativeCollisionArea() instanceof Rectangle) {
			return this.physData().getRelativeCollisionArea().transform(Transform.createTranslateTransform(
					this.posData().getX() - this.physData().getRelativeCollisionArea().getWidth() / 2,
					this.posData().getY() - this.physData().getRelativeCollisionArea().getHeight() / 2));
		} else if (this.physData().getRelativeCollisionArea() instanceof Ellipse) {
			return this.physData().getRelativeCollisionArea().transform(Transform.createTranslateTransform(
					this.posData().getX(),
					this.posData().getY()));
		} else {
			return this.physData().getRelativeCollisionArea().transform(Transform.createTranslateTransform(
					this.posData().getX(),
					this.posData().getY()));
		}
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
		} else {
			while (transformedA.intersects(b)) {
				float currentIntersectRating = Physical.getIntersectRating(transformedA, b);
				
				float moveX = 0;
				float xImprovement = currentIntersectRating;
				if(Physical.getIntersectRating(
						transformedA.transform(Transform.createTranslateTransform(increment, 0)),
						b
						) < currentIntersectRating) {
					moveX = 0.01F;
					xImprovement = getIntersectRating(
							transformedA.transform(Transform.createTranslateTransform(increment, 0)),
							b
							);
				} else if(Physical.getIntersectRating(
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
				if(Physical.getIntersectRating(
						transformedA.transform(Transform.createTranslateTransform(0, increment)),
						b
						) < currentIntersectRating) {
					moveY = 0.01F;
					yImprovement = Physical.getIntersectRating(
							transformedA.transform(Transform.createTranslateTransform(0, increment)),
							b
							);
				} else if(Physical.getIntersectRating(
						transformedA.transform(Transform.createTranslateTransform(0, -1 * increment)),
						b
						) < currentIntersectRating) {
					moveY = -0.01F;
					yImprovement = Physical.getIntersectRating(
							transformedA.transform(Transform.createTranslateTransform(0, -1 * increment)),
							b
							);
				}
				
				if(!(Math.abs(moveX) < 0.001F && Math.abs(moveY) < 0.001F)) {
					if(currentIntersectRating - xImprovement >= currentIntersectRating - yImprovement) {
						fixX += moveX;
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
		return (Physical.getBoundingRectangleIntersection(a,b) + Physical.getBoundingCircleIntersection(a,b)) / 2;
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
}
