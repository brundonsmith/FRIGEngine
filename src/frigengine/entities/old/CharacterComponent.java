package frigengine.entities.old;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.util.xml.SlickXMLException;
import org.newdawn.slick.util.xml.XMLElement;

import frigengine.Scene;
import frigengine.exceptions.AttributeFormatException;
import frigengine.exceptions.InvalidTagException;

public class CharacterComponent extends EntityComponent {
	// Constants
	private static final float STANDING_STILL = 0.01F;

	// Attributes
	private Vector2f moveVector;
	private float moveSpeed;
	private double direction;

	private String animationIdleNW;
	private String animationIdleN;
	private String animationIdleNE;
	private String animationIdleSW;
	private String animationIdleS;
	private String animationIdleSE;
	private String animationIdleE;
	private String animationIdleW;

	private String animationMoveNW;
	private String animationMoveN;
	private String animationMoveNE;
	private String animationMoveSW;
	private String animationMoveS;
	private String animationMoveSE;
	private String animationMoveE;
	private String animationMoveW;

	// Constructors and initialization
	public CharacterComponent(Entity entity) {
		super(entity);
	}
	@Override
	public void init(XMLElement xmlElement) {
		if (!xmlElement.getName().equals(this.getClass().getSimpleName()))
			throw new InvalidTagException(this.getClass().getSimpleName(), xmlElement.getName());

		// moveVector
		this.moveVector = new Vector2f(0, 0);
		
		// moveSpeed
		try {
			this.setMoveSpeed((float) xmlElement.getDoubleAttribute("speed", 0.005));
		} catch (SlickXMLException e) {
			throw new AttributeFormatException(xmlElement.getName(), "speed",
					xmlElement.getAttribute("speed"));
		}
		
		// direction
		try {
			this.setDirection((float) xmlElement.getDoubleAttribute("direction", 90));
		} catch (SlickXMLException e) {
			throw new AttributeFormatException(xmlElement.getName(), "direction",
					xmlElement.getAttribute("direction"));
		}
		
		// animations
		this.animationIdleNW = xmlElement.getChildrenByName("animation_idle_nw").size() > 0 ? xmlElement
				.getChildrenByName("animation_idle_nw").get(0).getAttribute("id", "")
				: null;
		this.animationIdleN = xmlElement.getChildrenByName("animation_idle_n").size() > 0 ? xmlElement
				.getChildrenByName("animation_idle_n").get(0).getAttribute("id", "")
				: null;
		this.animationIdleNE = xmlElement.getChildrenByName("animation_idle_ne").size() > 0 ? xmlElement
				.getChildrenByName("animation_idle_ne").get(0).getAttribute("id", "")
				: null;
		this.animationIdleSW = xmlElement.getChildrenByName("animation_idle_sw").size() > 0 ? xmlElement
				.getChildrenByName("animation_idle_sw").get(0).getAttribute("id", "")
				: null;
		this.animationIdleS = xmlElement.getChildrenByName("animation_idle_s").size() > 0 ? xmlElement
				.getChildrenByName("animation_idle_s").get(0).getAttribute("id", "")
				: null;
		this.animationIdleSE = xmlElement.getChildrenByName("animation_idle_se").size() > 0 ? xmlElement
				.getChildrenByName("animation_idle_se").get(0).getAttribute("id", "")
				: null;
		this.animationIdleE = xmlElement.getChildrenByName("animation_idle_e").size() > 0 ? xmlElement
				.getChildrenByName("animation_idle_e").get(0).getAttribute("id", "")
				: null;
		this.animationIdleW = xmlElement.getChildrenByName("animation_idle_w").size() > 0 ? xmlElement
				.getChildrenByName("animation_idle_w").get(0).getAttribute("id", "")
				: null;
		this.animationMoveNW = xmlElement.getChildrenByName("animation_move_nw").size() > 0 ? xmlElement
				.getChildrenByName("animation_move_nw").get(0).getAttribute("id", "")
				: null;
		this.animationMoveN = xmlElement.getChildrenByName("animation_move_n").size() > 0 ? xmlElement
				.getChildrenByName("animation_move_n").get(0).getAttribute("id", "")
				: null;
		this.animationMoveNE = xmlElement.getChildrenByName("animation_move_ne").size() > 0 ? xmlElement
				.getChildrenByName("animation_move_ne").get(0).getAttribute("id", "")
				: null;
		this.animationMoveSW = xmlElement.getChildrenByName("animation_move_sw").size() > 0 ? xmlElement
				.getChildrenByName("animation_move_sw").get(0).getAttribute("id", "")
				: null;
		this.animationMoveS = xmlElement.getChildrenByName("animation_move_s").size() > 0 ? xmlElement
				.getChildrenByName("animation_move_s").get(0).getAttribute("id", "")
				: null;
		this.animationMoveSE = xmlElement.getChildrenByName("animation_move_se").size() > 0 ? xmlElement
				.getChildrenByName("animation_move_se").get(0).getAttribute("id", "")
				: null;
		this.animationMoveE = xmlElement.getChildrenByName("animation_move_e").size() > 0 ? xmlElement
				.getChildrenByName("animation_move_e").get(0).getAttribute("id", "")
				: null;
		this.animationMoveW = xmlElement.getChildrenByName("animation_move_w").size() > 0 ? xmlElement
				.getChildrenByName("animation_move_w").get(0).getAttribute("id", "")
				: null;

		// Set default animation
		this.entity.drawable().setContinuousAnimation(getCurrentAnimation());
	}

	// Main loop methods
	@Override
	public void update(int delta, Input input, Scene area) {
		this.entity.drawable().setContinuousAnimation(getCurrentAnimation());
		if (this.getIsMoving())
			this.entity.spacial().moveBy(moveVector.copy().scale((float) delta / 1000));
		moveVector = new Vector2f();
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
		return moveVector.length() > STANDING_STILL;
	}
	private String getCurrentAnimation() {
		if (337.5 <= direction || direction < 22.5)
			return this.getIsMoving() ? this.animationMoveE : this.animationIdleE;
		if (22.5 <= this.direction && this.direction < 67.5)
			return this.getIsMoving() ? this.animationMoveSE : this.animationIdleSE;
		if (67.5 <= this.direction && this.direction < 112.5)
			return this.getIsMoving() ? this.animationMoveS : this.animationIdleS;
		if (112.5 <= this.direction && this.direction < 157.5)
			return this.getIsMoving() ? this.animationMoveSW : this.animationIdleSW;
		if (157.5 <= this.direction && this.direction < 202.5)
			return this.getIsMoving() ? this.animationMoveW : this.animationIdleW;
		if (202.5 <= this.direction && this.direction < 247.5)
			return this.getIsMoving() ? this.animationMoveNW : this.animationIdleNW;
		if (247.5 <= this.direction && this.direction < 292.5)
			return this.getIsMoving() ? this.animationMoveN : this.animationIdleN;
		if (292.5 <= this.direction && this.direction < 337.5)
			return this.getIsMoving() ? this.animationMoveNE : this.animationIdleNE;
		return "";
	}

	// Other methods
	public void move(double direction) {
		this.direction = direction;

		this.moveVector = new Vector2f(this.direction);
		this.moveVector.normalise();
		this.moveVector.scale(this.moveSpeed);
	}
	public void move(int direction, float moveSpeed) {
		this.direction = direction;
		this.moveSpeed = moveSpeed;

		this.moveVector = new Vector2f(this.direction);
		this.moveVector.normalise();
		this.moveVector.scale(this.moveSpeed);
	}
	
	// Utilities
	@Override
	public String toString() {
		return this.getId() + ": moveSpeed-" + this.moveSpeed + " direction-" + this.direction
				+ " movementVector-" + this.moveVector;
	}
	
	// EntityComponent
	public static Set<Class<?>> getComponentDependencies() {
		return new HashSet<Class<?>> ( Arrays.asList( new Class<?>[] {SpacialComponent.class, DrawableComponent.class }) );
	}
	public static Set<Class<?>> getComponentExclusives() {
		return new HashSet<Class<?>>();
	}
}
