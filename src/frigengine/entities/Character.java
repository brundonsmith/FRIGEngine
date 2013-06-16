package frigengine.entities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import org.newdawn.slick.geom.Vector2f;

public class Character extends Category {
	// Constants
	@Override
	public Collection<Class<? extends Component>> getRequiredComponents() {
		return new ArrayList<Class<? extends Component>>(Arrays.asList(
				PositionData.class,
				GraphicsData.class,
				CharacterData.class
			));
	}

	// Access
	protected PositionData posData() {
		return (PositionData)this.components.get(PositionData.class);
	}
	protected GraphicsData grphcsData() {
		return (GraphicsData)this.components.get(GraphicsData.class);
	}
	protected CharacterData charData() {
		return (CharacterData)this.components.get(CharacterData.class);
	}

	// Main loop methods
	public void update(int delta) {
		this.grphcsData().setContinuousAnimation(this.getMovementAnimation());
		if (this.getIsMoving()) {
			this.posData().translate(this.charData().moveVector.copy().scale((float) delta / 1000));
		}
		this.charData().moveVector = new Vector2f();
	}

	// Getters and setters
	public void setMoveVector(Vector2f moveVector) {
		this.charData().moveVector = moveVector;
	}
	public float getMoveSpeed() {
		return this.charData().moveSpeed;
	}
	public void setMoveSpeed(float moveSpeed) {
		this.charData().moveSpeed = moveSpeed;
	}
	public double getDirection() {
		return this.charData().direction;
	}
	public void setDirection(double direction) {
		this.charData().direction = direction;
	}
	public boolean getIsMoving() {
		return this.charData().moveVector.length() > CharacterData.STANDING_STILL;
	}
	private String getMovementAnimation() {
		if (337.5 <= this.charData().direction || this.charData().direction < 22.5) {
			return this.getIsMoving() ? this.charData().animationMoveE : this.charData().animationIdleE;
		} else if (22.5 <= this.charData().direction && this.charData().direction < 67.5) {
			return this.getIsMoving() ? this.charData().animationMoveSE : this.charData().animationIdleSE;
		} else if (67.5 <= this.charData().direction && this.charData().direction < 112.5) {
			return this.getIsMoving() ? this.charData().animationMoveS : this.charData().animationIdleS;
		} else if (112.5 <= this.charData().direction && this.charData().direction < 157.5) {
			return this.getIsMoving() ? this.charData().animationMoveSW : this.charData().animationIdleSW;
		} else if (157.5 <= this.charData().direction && this.charData().direction < 202.5) {
			return this.getIsMoving() ? this.charData().animationMoveW : this.charData().animationIdleW;
		} else if (202.5 <= this.charData().direction && this.charData().direction < 247.5) {
			return this.getIsMoving() ? this.charData().animationMoveNW : this.charData().animationIdleNW;
		} else if (247.5 <= this.charData().direction && this.charData().direction < 292.5) {
			return this.getIsMoving() ? this.charData().animationMoveN : this.charData().animationIdleN;
		} else if (292.5 <= this.charData().direction && this.charData().direction < 337.5) {
			return this.getIsMoving() ? this.charData().animationMoveNE : this.charData().animationIdleNE;
		} else {
			return "";
		}
	}

	// Other methods
	public void move(double direction) {
		this.charData().direction = direction;

		this.charData().moveVector = new Vector2f(this.charData().direction);
		this.charData().moveVector.normalise();
		this.charData().moveVector.scale(this.charData().moveSpeed);
	}
	public void move(int direction, float moveSpeed) {
		this.charData().direction = direction;
		this.charData().moveSpeed = moveSpeed;

		this.charData().moveVector = new Vector2f(this.charData().direction);
		this.charData().moveVector.normalise();
		this.charData().moveVector.scale(this.charData().moveSpeed);
	}
}
