package frigengine.entities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import frigengine.entities.Movement.MoveStyle;

public class Movable extends Category {
	// Constants
	@Override
	public Collection<Class<? extends Component>> getRequiredComponents() {
		return new ArrayList<Class<? extends Component>>(Arrays.asList(
				PositionData.class,
				MovableData.class
			));
	}

	// Access
	protected PositionData posData() {
		return (PositionData) this.components.get(PositionData.class);
	}
	protected MovableData movData() {
		return (MovableData) this.components.get(MovableData.class);
	}
	
	// Main loop methods
	public void update(int delta) {
		
	}
	
	// Operations
	public void moveTo(float x, float y, MoveStyle style) {
		switch(style) {
		case INSTANT:
			this.posData().setX(x);
			this.posData().setY(y);
			break;
		case LINEAR:
			break;
		case EASE:
			break;
		}
	}
	public void moveBy(float x, float y, MoveStyle style) {
		switch(style) {
		case INSTANT:
			this.posData().setX(x);
			this.posData().setY(y);
			break;
		case LINEAR:
			break;
		case EASE:
			break;
		}
	}
}
