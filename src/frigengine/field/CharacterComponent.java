package frigengine.field;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.util.xml.SlickXMLException;
import org.newdawn.slick.util.xml.XMLElement;

import frigengine.core.component.*;
import frigengine.core.exceptions.data.*;
import frigengine.core.scene.*;

public class CharacterComponent extends Component {
	// Required components
	@Override
	public Collection<Class<? extends Component>> requiredComponents() {
		return new ArrayList<Class<? extends Component>>(Arrays.asList(
				PositionComponent.class,
				SpriteComponent.class
			));
	}
	
	// Constants
	protected static final float STANDING_STILL = 0.01F;

	// Attributes
	protected Vector2f moveVector;
	protected float moveSpeed;
	protected double direction;

	protected String animationIdleNW;
	protected String animationIdleN;
	protected String animationIdleNE;
	protected String animationIdleSW;
	protected String animationIdleS;
	protected String animationIdleSE;
	protected String animationIdleE;
	protected String animationIdleW;

	protected String animationMoveNW;
	protected String animationMoveN;
	protected String animationMoveNE;
	protected String animationMoveSW;
	protected String animationMoveS;
	protected String animationMoveSE;
	protected String animationMoveE;
	protected String animationMoveW;

	// Constructors and initialization
	public CharacterComponent() {
		this.moveVector = new Vector2f(0, 0);
		this.moveSpeed = 0.005F;
		this.direction = 90;
		
		this.animationIdleNW = Animation.PLACEHOLDER_ID;
		this.animationIdleN = Animation.PLACEHOLDER_ID;
		this.animationIdleNE = Animation.PLACEHOLDER_ID;
		this.animationIdleSW = Animation.PLACEHOLDER_ID;
		this.animationIdleS = Animation.PLACEHOLDER_ID;
		this.animationIdleSE = Animation.PLACEHOLDER_ID;
		this.animationIdleE = Animation.PLACEHOLDER_ID;
		this.animationIdleW = Animation.PLACEHOLDER_ID;

		this.animationMoveNW = Animation.PLACEHOLDER_ID;
		this.animationMoveN = Animation.PLACEHOLDER_ID;
		this.animationMoveNE = Animation.PLACEHOLDER_ID;
		this.animationMoveSW = Animation.PLACEHOLDER_ID;
		this.animationMoveS = Animation.PLACEHOLDER_ID;
		this.animationMoveSE = Animation.PLACEHOLDER_ID;
		this.animationMoveE = Animation.PLACEHOLDER_ID;
		this.animationMoveW = Animation.PLACEHOLDER_ID;
	}
	private CharacterComponent(CharacterComponent other) {
		this.moveVector = other.moveVector.copy();
		this.moveSpeed = other.moveSpeed;
		this.direction = other.direction;
		
		this.animationIdleNW = other.animationIdleNW;
		this.animationIdleN = other.animationIdleN;
		this.animationIdleNE = other.animationIdleNE;
		this.animationIdleSW = other.animationIdleSW;
		this.animationIdleS = other.animationIdleS;
		this.animationIdleSE = other.animationIdleSE;
		this.animationIdleE = other.animationIdleE;
		this.animationIdleW = other.animationIdleW;

		this.animationMoveNW = other.animationMoveNW;
		this.animationMoveN = other.animationMoveN;
		this.animationMoveNE = other.animationMoveNE;
		this.animationMoveSW = other.animationMoveSW;
		this.animationMoveS = other.animationMoveS;
		this.animationMoveSE = other.animationMoveSE;
		this.animationMoveE = other.animationMoveE;
		this.animationMoveW = other.animationMoveW;
	}
	@Override
	public CharacterComponent clone() {
		return new CharacterComponent(this);
	}
	@Override
	public void init(XMLElement xmlElement) {
		// Check element name
		if (!xmlElement.getName().equals(this.getClass().getSimpleName())) {
			throw new InvalidTagException(this.getClass().getSimpleName(), xmlElement.getName());
		}

		// moveVector

		// moveSpeed
		try {
			this.moveSpeed = ((float) xmlElement.getDoubleAttribute("speed", this.moveSpeed));
		} catch (SlickXMLException e) {
			throw new AttributeFormatException(xmlElement.getName(), "speed",
					xmlElement.getAttribute("speed"));
		}

		// direction
		try {
			this.direction = ((float) xmlElement.getDoubleAttribute("direction", this.direction));
		} catch (SlickXMLException e) {
			throw new AttributeFormatException(xmlElement.getName(), "direction",
					xmlElement.getAttribute("direction"));
		}

		// animations
		if( xmlElement.getChildrenByName("animation_idle_nw").size() > 0) {
				this.animationIdleNW = xmlElement.getChildrenByName("animation_idle_nw").get(0).getAttribute("id", this.animationIdleNW);
		}
		if( xmlElement.getChildrenByName("animation_idle_n").size() > 0) {
			this.animationIdleN = xmlElement.getChildrenByName("animation_idle_n").get(0).getAttribute("id", this.animationIdleN);
		}
		if( xmlElement.getChildrenByName("animation_idle_ne").size() > 0) {
			this.animationIdleNE = xmlElement.getChildrenByName("animation_idle_ne").get(0).getAttribute("id", this.animationIdleNE);
		}
		if( xmlElement.getChildrenByName("animation_idle_sw").size() > 0) {
			this.animationIdleSW = xmlElement.getChildrenByName("animation_idle_sw").get(0).getAttribute("id", this.animationIdleSW);
		}
		if( xmlElement.getChildrenByName("animation_idle_s").size() > 0) {
			this.animationIdleS = xmlElement.getChildrenByName("animation_idle_s").get(0).getAttribute("id", this.animationIdleS);
		}
		if( xmlElement.getChildrenByName("animation_idle_se").size() > 0) {
			this.animationIdleSE = xmlElement.getChildrenByName("animation_idle_se").get(0).getAttribute("id", this.animationIdleSE);
		}
		if( xmlElement.getChildrenByName("animation_idle_e").size() > 0) {
			this.animationIdleE = xmlElement.getChildrenByName("animation_idle_e").get(0).getAttribute("id", this.animationIdleE);
		}
		if( xmlElement.getChildrenByName("animation_idle_w").size() > 0) {
			this.animationIdleW = xmlElement.getChildrenByName("animation_idle_w").get(0).getAttribute("id", this.animationIdleW);
		}
		
		if( xmlElement.getChildrenByName("animation_move_nw").size() > 0) {
			this.animationMoveNW = xmlElement.getChildrenByName("animation_move_nw").get(0).getAttribute("id", this.animationMoveNW);
		}
		if( xmlElement.getChildrenByName("animation_move_n").size() > 0) {
			this.animationMoveN = xmlElement.getChildrenByName("animation_move_n").get(0).getAttribute("id", this.animationMoveN);
		}
		if( xmlElement.getChildrenByName("animation_move_ne").size() > 0) {
			this.animationMoveNE = xmlElement.getChildrenByName("animation_move_ne").get(0).getAttribute("id", this.animationMoveNE);
		}
		if( xmlElement.getChildrenByName("animation_move_sw").size() > 0) {
			this.animationMoveSW = xmlElement.getChildrenByName("animation_move_sw").get(0).getAttribute("id", this.animationMoveSW);
		}
		if( xmlElement.getChildrenByName("animation_move_s").size() > 0) {
			this.animationMoveS = xmlElement.getChildrenByName("animation_move_s").get(0).getAttribute("id", this.animationMoveS);
		}
		if( xmlElement.getChildrenByName("animation_move_se").size() > 0) {
			this.animationMoveSE = xmlElement.getChildrenByName("animation_move_se").get(0).getAttribute("id", this.animationMoveSE);
		}
		if( xmlElement.getChildrenByName("animation_move_e").size() > 0) {
			this.animationMoveE = xmlElement.getChildrenByName("animation_move_e").get(0).getAttribute("id", this.animationMoveE);
		}
		if( xmlElement.getChildrenByName("animation_move_w").size() > 0) {
			this.animationMoveW = xmlElement.getChildrenByName("animation_move_w").get(0).getAttribute("id", this.animationMoveW);
		}
	}

	// Main loop methods
	@Override
	public void update(int delta, Input input) {
		getComponent(SpriteComponent.class).loopAnimation(this.getMovementAnimation());
		if (this.getIsMoving()) {
			getComponent(PositionComponent.class).translate(this.moveVector.copy().scale((float) delta / 1000));
		}
		this.moveVector.set(0, 0);
	}
	
	// Getters and setters
	public void setMoveVector(Vector2f moveVector) {
		this.moveVector = moveVector;
	}
	public float getMoveSpeed() {
		return this.moveSpeed;
	}
	public void setMoveSpeed(float moveSpeed) {
		this.moveSpeed = moveSpeed;
	}
	public double getDirection() {
		return this.direction;
	}
	public void setDirection(double direction) {
		this.direction = direction;
	}
	public boolean getIsMoving() {
		return this.moveVector.length() > CharacterComponent.STANDING_STILL;
	}
	private String getMovementAnimation() {
		if (337.5 <= this.direction || this.direction < 22.5) {
			return this.getIsMoving() ? this.animationMoveE : this.animationIdleE;
		} else if (22.5 <= this.direction && this.direction < 67.5) {
			return this.getIsMoving() ? this.animationMoveSE : this.animationIdleSE;
		} else if (67.5 <= this.direction && this.direction < 112.5) {
			return this.getIsMoving() ? this.animationMoveS : this.animationIdleS;
		} else if (112.5 <= this.direction && this.direction < 157.5) {
			return this.getIsMoving() ? this.animationMoveSW : this.animationIdleSW;
		} else if (157.5 <= this.direction && this.direction < 202.5) {
			return this.getIsMoving() ? this.animationMoveW : this.animationIdleW;
		} else if (202.5 <= this.direction && this.direction < 247.5) {
			return this.getIsMoving() ? this.animationMoveNW : this.animationIdleNW;
		} else if (247.5 <= this.direction && this.direction < 292.5) {
			return this.getIsMoving() ? this.animationMoveN : this.animationIdleN;
		} else if (292.5 <= this.direction && this.direction < 337.5) {
			return this.getIsMoving() ? this.animationMoveNE : this.animationIdleNE;
		} else {
			return "";
		}
	}
	
	// Operations
	public void move(double direction) {
		this.direction = direction;

		this.moveVector.set(0, 1);
		this.moveVector.setTheta(this.direction);
		this.moveVector.normalise();
		this.moveVector.scale(this.moveSpeed);
	}
	public void move(int direction, float moveSpeed) {
		this.direction = direction;
		this.moveSpeed = moveSpeed;

		this.moveVector.set(0, 1);
		this.moveVector.setTheta(this.direction);
		this.moveVector.normalise();
		this.moveVector.scale(this.moveSpeed);
	}
	
	// Utilities
	@Override
	public String toString() {
		return this.getClass().getSimpleName() + ": moveSpeed=" + this.moveSpeed + " direction=" + this.direction
				+ " movementVector=" + this.moveVector;
	}
}
