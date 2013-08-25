package frigengine.field;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.util.xml.XMLElement;

import frigengine.core.component.*;
import frigengine.core.exceptions.data.*;
import frigengine.core.scene.*;

public class DashComponent extends Component implements MovementComponentFinishedSubscriber{
	// Required components
	@Override
	public  Collection<Class<? extends Component>> requiredComponents() {
		return new ArrayList<Class<? extends Component>>(Arrays.asList(
				MovementComponent.class
			));
	}
	
	// Attributes
	private boolean started;

	// Constructors and initialization
	public DashComponent() {
		this.started = false;
	}
	private DashComponent(DashComponent other) {
		super(other);
		
		this.started = other.started;
	}
	@Override
	public DashComponent clone() {
		return new DashComponent(this);
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
		if(!this.started) {
			this.subscribeTo(getComponent(MovementComponent.class));
			
			getComponent(MovementComponent.class)
			.move(new Movement(
					1000, 
					Movement.getLinearPath(
							new Vector2f(Math.random() * 360).scale(20.0f)),
					Movement.MoveFunction.EASE_OUT))
			.wait(200);
			
			this.started = true;
		}
	}

	// Events
	@Override
	public void subscribeTo(MovementComponent reporter) {
		reporter.addFinishedSubscriber(this);
	}
	@Override
	public void reportedMovementComponentFinished(MovementComponent source, Movement finished) {
		source
		.move(new Movement(
				1000, 
				Movement.getLinearPath(
						new Vector2f(Math.random() * 360).scale(20.0f)),
				Movement.MoveFunction.EASE_OUT))
		.wait(200);
	}
	
	
}
