package frigengine.entities;

import org.newdawn.slick.geom.Point;

abstract class Movement {
	// Attributes
	protected float maxSpeed;
	protected MoveStyle style;
	protected Point currentPosition;
	
	// Constructors and initialization
	public Movement(float speed, MoveStyle style, Point startingPosition) {
		this.maxSpeed = speed;
		this.style = style;
		this.currentPosition = startingPosition;
	}
	
	// Main loop methods
	public abstract void update(int delta);
	
	// Getters and setters
	public Point getCurrentPosition() {
		return this.currentPosition;
	}
	public abstract boolean getIsFinished();
	
	public enum MoveStyle {
		INSTANT,
		LINEAR,
		EASE
	}
}
