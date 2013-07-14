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
	private int upKey = Input.KEY_UP;
	private int downKey = Input.KEY_DOWN;
	private int leftKey = Input.KEY_LEFT;
	private int rightKey = Input.KEY_RIGHT;

	// Constructors and initialization
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
		if (input != null && input.isKeyPressed(Input.KEY_M)) {
			getComponent(SpriteComponent.class).playAnimation("fun");
		}
		if (input != null && input.isKeyPressed(Input.KEY_N)) {
			getComponent(MovementComponent.class).move(new Movement(300, Movement.getBezierPath(new Vector2f(0,0), new Vector2f(30,-20), new Vector2f(70, -20), new Vector2f(100,0)), "stand_e"));
			getComponent(MovementComponent.class).move(new Movement(200, Movement.getLinearPath(new Vector2f(0,0), new Vector2f(-100,0)), "walk_w"));
		}
		
		// Movement
		Vector2f movement = new Vector2f();
		
		if (input != null && input.isKeyDown(this.upKey)) {
			movement.add(new Vector2f(0, -1));
		}
		if (input != null && input.isKeyDown(this.downKey)) {
			movement.add(new Vector2f(0, 1));
		}
		if (input != null && input.isKeyDown(this.leftKey)) {
			movement.add(new Vector2f(-1, 0));
		}
		if (input != null && input.isKeyDown(this.rightKey)) {
			movement.add(new Vector2f(1, 0));
		}

		if (movement.length() > 0.5) {
			getComponent(CharacterComponent.class).move(movement.getTheta());
		}
	}
}
