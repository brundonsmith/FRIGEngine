package frigengine.core.scene;

import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.geom.Curve;
import org.newdawn.slick.geom.Point;
import org.newdawn.slick.geom.Vector2f;

/*package*/ public class Movement implements Cloneable {
	// Attributes
	private int progress;
	private int duration;
	private Curve path;
	private String animationId;
	private MoveFunction moveFunction;
	
	// Constructors and initialization
	public Movement(int duration, Curve path) {
		this.progress = 0;
		this.duration = duration;
		this.path = path;
		this.animationId = null;
		this.moveFunction = MoveFunction.LINEAR;
		this.finishedSubscribers = new ArrayList<MovementFinishedSubscriber>();
	}
	public Movement(int duration, Curve path, MoveFunction moveFunction) {
		this.progress = 0;
		this.duration = duration;
		this.path = path;
		this.animationId = null;
		this.moveFunction = moveFunction;
		this.finishedSubscribers = new ArrayList<MovementFinishedSubscriber>();
	}
	public Movement(int duration, Curve path, String animationId) {
		this.progress = 0;
		this.duration = duration;
		this.path = path;
		this.animationId = animationId;
		this.moveFunction = MoveFunction.LINEAR;
		this.finishedSubscribers = new ArrayList<MovementFinishedSubscriber>();
	}
	public Movement(int duration, Curve path, String animationId, MoveFunction moveFunction) {
		this.progress = 0;
		this.duration = duration;
		this.path = path;
		this.animationId = animationId;
		this.moveFunction = moveFunction;
		this.finishedSubscribers = new ArrayList<MovementFinishedSubscriber>();
	}
	public Movement(Movement other) {
		this.progress = other.progress;
		this.duration = other.duration;
		this.path = other.path;
		this.animationId = other.animationId;
		this.moveFunction = other.moveFunction;
		this.finishedSubscribers = new ArrayList<MovementFinishedSubscriber>();
	}
	@Override
	public Movement clone() {
		return new Movement(this);
	}
	
	// Main loop methods
	public void update(int delta) {
		this.progress += delta;
		if(this.progress > this.duration) {
			this.reportFinished();
		}
	}
	
	// Getters and setters
	public int getDuration() {
		return this.duration;
	}
	public Point getPosition() {
		float percentage = Math.min(Math.abs((float)this.progress / (float)this.duration), 1);
		percentage = getEasePosition(percentage, this.moveFunction);
		
		Vector2f pos = this.path.pointAt(percentage);
		return new Point(pos.getX(), pos.getY());
	}
	private static float getEasePosition(float linearPosition, MoveFunction moveFunction) {
		float sharpness = 5.0f;
		float easeDuration = 0.32664628279f;
		float lineSlope = 1.18142490432f;
		float easeInYIntercept = -0.181424904316f;
		
		switch(moveFunction) {
		case LINEAR:
			return linearPosition;
		case EASE_IN:
			if(linearPosition < easeDuration) {
				return (float)((Math.atan((linearPosition - 0.5f) * 5.0f) * 1.3) / Math.PI) + 0.5f;
			} else {
				return (lineSlope * linearPosition) + easeInYIntercept;
			}
		case EASE_OUT:
			if(linearPosition < 1.0f - easeDuration) {
				return lineSlope * linearPosition;
			} else {
				return (float)((Math.atan((linearPosition - 0.5f) * 5.0f) * 1.3) / Math.PI) + 0.5f;
			}
		case EASE_IN_OUT:
			return (float)((Math.atan((linearPosition - 0.5f) * 5.0f) * 1.3) / Math.PI) + 0.5f;
		default:
			return linearPosition;
		}
	}
	public String getAnimationId() {
		return this.animationId;
	}
	
	// Operations
	public void reset() {
		this.progress = 0;
	}
	
	// Utilities
	private static Vector2f stationaryPoint = new Vector2f(0,0);
	public static Curve getStationaryPath() {
		return new Curve(stationaryPoint, stationaryPoint, stationaryPoint, stationaryPoint);
	}
	public static Curve getLinearPath(Vector2f destination) {
		return new Curve(stationaryPoint, new Vector2f(stationaryPoint.getX() + ((destination.getX() - stationaryPoint.getX()) / 3), stationaryPoint.getY() + ((destination.getY() - stationaryPoint.getY()) / 3)), new Vector2f(stationaryPoint.getX() + ((destination.getX() - stationaryPoint.getX()) * 2 / 3), stationaryPoint.getY() + ((destination.getY() - stationaryPoint.getY()) / 3)), destination);
	}
	public static Curve getBezierPath(Vector2f p0, Vector2f p1, Vector2f p2, Vector2f p3) {
		return new Curve(p0, p1, p2, p3);
	}

	// Events
	private List<MovementFinishedSubscriber> finishedSubscribers;
	public void addFinishedSubscriber(MovementFinishedSubscriber subscriber) {
		this.finishedSubscribers.add(subscriber);
	}
	private void reportFinished() {
		// Notify listeners
		for(MovementFinishedSubscriber subscriber : this.finishedSubscribers) {
			subscriber.reportedMovementFinished(this);
		}
	}
	
	public enum MoveFunction {
		LINEAR,
		EASE_IN,
		EASE_OUT,
		EASE_IN_OUT
	}
}
