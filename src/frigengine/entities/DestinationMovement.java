package frigengine.entities;

import org.newdawn.slick.geom.Point;
import org.newdawn.slick.geom.Transform;

class DestinationMovement extends Movement {
	// Attributes
	protected float currentSpeed;
	// TODO Function of current speed
	// Constructors and initialization
	public DestinationMovement(float speed, MoveStyle style, Point startingPosition) {
		super(speed, style, startingPosition);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void update(int delta) {
		float changeX;
		float changeY;
		
		this.currentPosition.transform(Transform.createTranslateTransform(changeX, changeY));
	}

	@Override
	public boolean getIsFinished() {
		// TODO Auto-generated method stub
		return false;
	}

}
