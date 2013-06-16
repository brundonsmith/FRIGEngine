package frigengine.entities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import org.newdawn.slick.geom.Point;
import org.newdawn.slick.geom.Vector2f;

public class Positionable extends Category {
	// Constants
	@Override
	public Collection<Class<? extends Component>> getRequiredComponents() {
		return new ArrayList<Class<? extends Component>>(Arrays.asList(
				PositionData.class
			));
	}

	// Access
	protected PositionData posData() {
		return (PositionData) this.components.get(PositionData.class);
	}

	// Getters and setters
	public Point getPosition() {
		return this.posData().getPosition();
	}
	public void setPosition(Point position) {
		this.posData().setPosition(position);
	}
	public float getX() {
		return this.posData().getX();
	}
	public void setX(float x) {
		this.posData().setX(x);
	}
	public float getY() {
		return this.posData().getY();
	}
	public void setY(float y) {
		this.posData().setY(y);
	}
	
	// Operations
	public void translate(float x, float y) {
		this.posData().translate(x, y);
	}
	public void translate(Point difference) {
		this.posData().translate(difference);
	}
	public void translate(Vector2f difference) {
		this.posData().translate(difference);
	}
}
