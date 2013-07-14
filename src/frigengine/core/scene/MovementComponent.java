package frigengine.core.scene;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Deque;
import java.util.LinkedList;

import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Point;
import org.newdawn.slick.util.xml.XMLElement;

import frigengine.core.component.*;
import frigengine.core.exceptions.data.*;

public class MovementComponent extends Component {
	// Required components
	@Override
	public  Collection<Class<? extends Component>> requiredComponents() {
		return new ArrayList<Class<? extends Component>>(Arrays.asList(
				PositionComponent.class,
				SpriteComponent.class
			));
	}
	
	// Attributes
	private int lastMovementStarted;
	private int currentTime;
	private Point lastMovementStartingPosition;
	private Deque<Movement> movements;
	private Movement currentMovement;

	// Constructors and initialization
	public MovementComponent() {
		this.lastMovementStarted = 0;
		this.currentTime = 0;
		this.lastMovementStartingPosition = null;
		this.movements = new LinkedList<Movement>();
		currentMovement = null;
	}
	@Override
	public void init(XMLElement xmlElement) {
		// Check element name
		if (!xmlElement.getName().equals(this.getClass().getSimpleName())) {
			throw new InvalidTagException(this.getClass().getSimpleName(), xmlElement.getName());
		}
	}
	
	// Main loop methods
	@Override
	public void update(int delta, Input input) {
		this.currentTime += delta;
		
		if(this.currentMovement != null && this.currentTime - this.lastMovementStarted >= this.currentMovement.getDuration()) {
			this.currentMovement = null;
		} else if(this.currentMovement != null) {
			getComponent(PositionComponent.class).setX(this.lastMovementStartingPosition.getX() + this.currentMovement.getPosition(this.currentTime - this.lastMovementStarted).getX());
			getComponent(PositionComponent.class).setY(this.lastMovementStartingPosition.getY() + this.currentMovement.getPosition(this.currentTime - this.lastMovementStarted).getY());
		}
		
		if(this.currentMovement == null && !this.movements.isEmpty()) {
			this.currentMovement = this.movements.removeFirst();
			this.lastMovementStarted = this.currentTime;
			this.lastMovementStartingPosition = new Point(getComponent(PositionComponent.class).getPosition().getX(), getComponent(PositionComponent.class).getPosition().getY());
			getComponent(SpriteComponent.class).playAnimation(this.currentMovement.getAnimationId(), this.currentMovement.getDuration());
		}
	}
	
	// Operations
	public void move(Movement movement) {
		this.movements.add(movement);
	}
}
