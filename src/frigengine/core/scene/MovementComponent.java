package frigengine.core.scene;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Point;
import org.newdawn.slick.util.xml.XMLElement;

import frigengine.core.component.*;
import frigengine.core.exceptions.data.*;
import frigengine.field.PlayerControllerComponent;

public class MovementComponent extends Component implements MovementFinishedSubscriber {
	// Required components
	@Override
	public  Collection<Class<? extends Component>> requiredComponents() {
		return new ArrayList<Class<? extends Component>>(Arrays.asList(
				PositionComponent.class
			));
	}
	
	// Attributes
	private Point lastMovementStartingPosition;
	private Deque<Movement> movements;
	private Movement currentMovement;

	// Constructors and initialization
	public MovementComponent() {
		this.lastMovementStartingPosition = new Point(0,0);
		this.movements = new LinkedList<Movement>();
		this.currentMovement = null;
		this.finishedSubscribers = new ArrayList<MovementComponentFinishedSubscriber>();
	}
	private MovementComponent(MovementComponent other) {
		super(other);
		
		this.lastMovementStartingPosition = other.lastMovementStartingPosition == null ? null : new Point(other.lastMovementStartingPosition.getX(), other.lastMovementStartingPosition.getY());
		this.movements = new LinkedList<Movement>();
		for(Movement m : other.movements) {
			this.movements.add(m.clone());
			this.subscribeTo(this.movements.getLast());
		}
		
		this.currentMovement = other.currentMovement == null ? null : other.currentMovement.clone();
		if(this.currentMovement != null) {
			this.subscribeTo(this.currentMovement);
		}
		
		this.finishedSubscribers = new ArrayList<MovementComponentFinishedSubscriber>();
		for(MovementComponentFinishedSubscriber m : other.finishedSubscribers) {
			this.finishedSubscribers.add(m);
		}
	}
	@Override
	public MovementComponent clone() {
		return new MovementComponent(this);
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
		if(this.currentMovement != null) {			// if there is a movement being performed
			if(hasComponent(PlayerControllerComponent.class)) {
				getComponent(PlayerControllerComponent.class).disable(); // disable controls
			}
			
			getComponent(PositionComponent.class).setX(this.lastMovementStartingPosition.getX() + this.currentMovement.getPosition().getX());
			getComponent(PositionComponent.class).setY(this.lastMovementStartingPosition.getY() + this.currentMovement.getPosition().getY());
			this.currentMovement.update(delta);
		} else if(!this.movements.isEmpty()) {	// if there is no movement being performed and there is one in the queue
			if(hasComponent(PlayerControllerComponent.class)) {
				getComponent(PlayerControllerComponent.class).disable(); // disable controls
			}
			
			this.currentMovement = this.movements.removeFirst();
			this.currentMovement.addFinishedSubscriber(this);
			this.currentMovement.reset();
			this.lastMovementStartingPosition.setX(getComponent(PositionComponent.class).getPosition().getX());
			this.lastMovementStartingPosition.setY(getComponent(PositionComponent.class).getPosition().getY());
			if(this.currentMovement.getAnimationId() != null && hasComponent(SpriteComponent.class)) {
				getComponent(SpriteComponent.class).playAnimation(this.currentMovement.getAnimationId(), this.currentMovement.getDuration());
			}
		}
	}
	
	// Operations
	public MovementComponent move(Movement movement) {
		this.movements.add(movement);
		return this;
	}
	public MovementComponent move(List<Movement> movements) {
		for(Movement m : movements) {
			this.movements.add(m);
		}
		return this;
	}
	public MovementComponent wait(int duration) {
		this.movements.add(new Movement(duration, Movement.getStationaryPath()));
		return this;
	}
	public void stop() {
		this.currentMovement = null;
		this.movements.clear();
	}
	
	// Events
	@Override
	public void subscribeTo(Movement reporter) {
		reporter.addFinishedSubscriber(this);
	}
	@Override
	public void reportedMovementFinished(Movement source) {
		if(source == this.currentMovement) {
			if(this.movements.isEmpty() && hasComponent(PlayerControllerComponent.class)) {
				getComponent(PlayerControllerComponent.class).enable(); // restore controls
			}
			
			this.currentMovement = null;
			this.reportFinished(source);
		}
	}

	private List<MovementComponentFinishedSubscriber> finishedSubscribers;
	public void addFinishedSubscriber(MovementComponentFinishedSubscriber subscriber) {
		this.finishedSubscribers.add(subscriber);
	}
	private void reportFinished(Movement movement) {
		// Notify listeners
		for(MovementComponentFinishedSubscriber subscriber : this.finishedSubscribers) {
			subscriber.reportedMovementComponentFinished(this, movement);
		}
	}
	
	// Utilities
	@Override
	public String toString() {
		return this.getClass().getSimpleName() + ": " + this.currentMovement;
	}
}
