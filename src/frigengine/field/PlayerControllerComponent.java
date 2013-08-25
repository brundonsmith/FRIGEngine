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

public class PlayerControllerComponent extends Component {
	// Required components
	@Override
	public Collection<Class<? extends Component>> requiredComponents() {
		return new ArrayList<Class<? extends Component>>(Arrays.asList(
				PositionComponent.class,
				SpriteComponent.class,
				CharacterComponent.class,
				MovementComponent.class
			));
	}
	
	// Attributes
	private int upKey;
	private int downKey;
	private int leftKey;
	private int rightKey;
	private boolean enabled;

	// Constructors and initialization
	public PlayerControllerComponent() {
		this.upKey = Input.KEY_UP;
		this.downKey = Input.KEY_DOWN;
		this.leftKey = Input.KEY_LEFT;
		this.rightKey = Input.KEY_RIGHT;
		this.enabled = true;
	}
	private PlayerControllerComponent(PlayerControllerComponent other) {
		super(other);
		
		this.upKey = other.upKey;
		this.downKey = other.downKey;
		this.leftKey = other.leftKey;
		this.rightKey = other.rightKey;
		this.enabled = other.enabled;
	}
	@Override
	public PlayerControllerComponent clone() {
		return new PlayerControllerComponent(this);
	}
	@Override
	public void init(XMLElement xmlElement) {
		// Check element name
		if (!xmlElement.getName().equals(this.getClass().getSimpleName())) {
			throw new InvalidTagException(this.getClass().getSimpleName(), xmlElement.getName());
		}
	}

	// Main loop methods
	private Vector2f movement = new Vector2f();
	private final Vector2f northVector = new Vector2f(0, -1);
	private final Vector2f southVector = new Vector2f(0, 1);
	private final Vector2f westVector = new Vector2f(-1, 0);
	private final Vector2f eastVector = new Vector2f(1, 0);
	@Override
	public void update(int delta, Input input) {
		if(enabled) {
			if (input != null && input.isKeyPressed(Input.KEY_M)) {
				getComponent(SpriteComponent.class).playAnimation("fun");
			}
			if (input != null && input.isKeyPressed(Input.KEY_N)) {
				getComponent(MovementComponent.class)
				.move(new Movement(300, Movement.getLinearPath(new Vector2f(100,0)), "walk_e", Movement.MoveFunction.EASE_IN))
				.move(new Movement(400, Movement.getBezierPath(new Vector2f(0,0), new Vector2f(-30,-20), new Vector2f(-70, -20), new Vector2f(-100,0)), "stand_e", Movement.MoveFunction.EASE_OUT));
			}
			if (input != null && input.isKeyPressed(Input.KEY_S)) {
				this.getContainingEntity().saveState("save", MovementComponent.class, SpriteComponent.class);
			}
			if (input != null && input.isKeyPressed(Input.KEY_R)) {
				this.getContainingEntity().revertState("save");
			}
			if (input != null && input.isKeyPressed(Input.KEY_F)) {
				if(getEntity("camera_1").getComponent(CameraComponent.class).isShaking()) {
					getEntity("camera_1").getComponent(CameraComponent.class).stopShaking();
				} else {
					getEntity("camera_1").getComponent(CameraComponent.class).startShaking();
				}
			}
			
			// Movement
			movement.set(0, 0);
			
			if (input != null && input.isKeyDown(this.upKey)) {
				movement.add(northVector);
			}
			if (input != null && input.isKeyDown(this.downKey)) {
				movement.add(southVector);
			}
			if (input != null && input.isKeyDown(this.leftKey)) {
				movement.add(westVector);
			}
			if (input != null && input.isKeyDown(this.rightKey)) {
				movement.add(eastVector);
			}
	
			if (movement.length() > 0.5) {
				getComponent(CharacterComponent.class).move(movement.getTheta());
			}
		}
	}

	// Getters and setters
	public boolean getEnabled() {
		return this.enabled;
	}
	public void enable() {
		this.enabled = true;
	}
	public void disable() {
		this.enabled = false;
	}

	// Utilities
	@Override
	public String toString() {
		return this.getClass().getSimpleName() + ": enabled=" + this.enabled;
	}
}
