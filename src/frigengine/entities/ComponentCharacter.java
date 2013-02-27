package frigengine.entities;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.util.xml.SlickXMLException;
import org.newdawn.slick.util.xml.XMLElement;

import frigengine.exceptions.AttributeFormatException;
import frigengine.exceptions.DataParseException;
import frigengine.scene.*;

public class ComponentCharacter extends EntityComponent {
	public static String getComponentID() {
		return "character";
	}

	public String getTagName() {
		return getComponentID();
	}

	public static String[] getComponentDependencies() {
		return new String[] { "spacial", "drawable" };
	}

	public static String[] getComponentExclusives() {
		return new String[] {};
	}

	// Constants
	private static final float DEFAULT_MOVE_SPEED = 0.08F;
	private static final double DEFAULT_DIRECTION = 270;
	private static final float STANDING_STILL = 0.01F;

	// Attributes
	private Vector2f moveVector;
	private float moveSpeed = DEFAULT_MOVE_SPEED;
	private double direction = DEFAULT_DIRECTION;

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
	public ComponentCharacter(Entity entity) {
		super(entity);
		this.id = getComponentID();
	}

	@Override
	public void init(XMLElement xmlElement) {
		if (!xmlElement.getName().equals(this.getID()))
			throw new DataParseException(
					"Xml node does not match component type '" + this.id + "'");

		// Assign attributes
		moveVector = new Vector2f(0, 0);
		try {
			this.setMoveSpeed((float) xmlElement.getDoubleAttribute("speed", 0));
		} catch (SlickXMLException e) {
			throw new AttributeFormatException(this.getTagName(), "speed",
					xmlElement.getAttribute("speed"));
		}
		try {
			this.setMoveSpeed((float) xmlElement.getDoubleAttribute(
					"direction", 0));
		} catch (SlickXMLException e) {
			throw new AttributeFormatException(this.getTagName(), "direction",
					xmlElement.getAttribute("direction"));
		}

		this.animationIdleNW = xmlElement
				.getChildrenByName("animation_idle_nw").size() > 0 ? xmlElement
				.getChildrenByName("animation_idle_nw").get(0)
				.getAttribute("id", "") : null;
		this.animationIdleNW = xmlElement.getChildrenByName("animation_idle_n")
				.size() > 0 ? xmlElement.getChildrenByName("animation_idle_n")
				.get(0).getAttribute("id", "") : null;
		this.animationIdleNW = xmlElement
				.getChildrenByName("animation_idle_ne").size() > 0 ? xmlElement
				.getChildrenByName("animation_idle_ne").get(0)
				.getAttribute("id", "") : null;
		this.animationIdleNW = xmlElement
				.getChildrenByName("animation_idle_sw").size() > 0 ? xmlElement
				.getChildrenByName("animation_idle_sw").get(0)
				.getAttribute("id", "") : null;
		this.animationIdleNW = xmlElement.getChildrenByName("animation_idle_s")
				.size() > 0 ? xmlElement.getChildrenByName("animation_idle_s")
				.get(0).getAttribute("id", "") : null;
		this.animationIdleNW = xmlElement
				.getChildrenByName("animation_idle_se").size() > 0 ? xmlElement
				.getChildrenByName("animation_idle_se").get(0)
				.getAttribute("id", "") : null;
		this.animationIdleNW = xmlElement.getChildrenByName("animation_idle_e")
				.size() > 0 ? xmlElement.getChildrenByName("animation_idle_e")
				.get(0).getAttribute("id", "") : null;
		this.animationIdleNW = xmlElement.getChildrenByName("animation_idle_w")
				.size() > 0 ? xmlElement.getChildrenByName("animation_idle_w")
				.get(0).getAttribute("id", "") : null;

		this.animationMoveNW = xmlElement
				.getChildrenByName("animation_move_nw").size() > 0 ? xmlElement
				.getChildrenByName("animation_move_nw").get(0)
				.getAttribute("id", "") : null;
		this.animationMoveNW = xmlElement.getChildrenByName("animation_move_n")
				.size() > 0 ? xmlElement.getChildrenByName("animation_move_n")
				.get(0).getAttribute("id", "") : null;
		this.animationMoveNW = xmlElement
				.getChildrenByName("animation_move_ne").size() > 0 ? xmlElement
				.getChildrenByName("animation_move_ne").get(0)
				.getAttribute("id", "") : null;
		this.animationMoveNW = xmlElement
				.getChildrenByName("animation_move_sw").size() > 0 ? xmlElement
				.getChildrenByName("animation_move_sw").get(0)
				.getAttribute("id", "") : null;
		this.animationMoveNW = xmlElement.getChildrenByName("animation_move_s")
				.size() > 0 ? xmlElement.getChildrenByName("animation_move_s")
				.get(0).getAttribute("id", "") : null;
		this.animationMoveNW = xmlElement
				.getChildrenByName("animation_move_se").size() > 0 ? xmlElement
				.getChildrenByName("animation_move_se").get(0)
				.getAttribute("id", "") : null;
		this.animationMoveNW = xmlElement.getChildrenByName("animation_move_e")
				.size() > 0 ? xmlElement.getChildrenByName("animation_move_e")
				.get(0).getAttribute("id", "") : null;
		this.animationMoveNW = xmlElement.getChildrenByName("animation_move_w")
				.size() > 0 ? xmlElement.getChildrenByName("animation_move_w")
				.get(0).getAttribute("id", "") : null;

	}

	// Main loop methods
	@Override
	public void update(GameContainer container, int delta, Scene area) {
		((ComponentDrawable) entity.getComponent("drawable"))
				.setContinuousAnimation(getCurrentAnimation());
		if (isMoving())
			((ComponentSpacial) entity.getComponent("spacial"))
					.moveBy(moveVector.copy().scale(delta));
		moveVector = new Vector2f();
	}

	// Getters and setters
	public void setMoveVector(Vector2f moveVector) {
		this.moveVector = moveVector;
	}

	public void setMoveSpeed(float moveSpeed) {
		this.moveSpeed = moveSpeed;
	}

	public float getMoveSpeed() {
		return this.moveSpeed;
	}

	public void setDirection(double direction) {
		this.moveVector.setTheta(direction);
	}

	public double getDirection() {
		return this.direction;
	}

	public boolean isMoving() {
		return moveVector.length() > STANDING_STILL;
	}

	private String getCurrentAnimation() {
		if (337.5 <= moveVector.getTheta() || moveVector.getTheta() < 22.5)
			return isMoving() ? animationMoveE : animationIdleE;
		if (22.5 <= moveVector.getTheta() && moveVector.getTheta() < 67.5)
			return isMoving() ? animationMoveNE : animationIdleNE;
		if (67.5 <= moveVector.getTheta() && moveVector.getTheta() < 112.5)
			return isMoving() ? animationMoveN : animationIdleN;
		if (112.5 <= moveVector.getTheta() && moveVector.getTheta() < 157.5)
			return isMoving() ? animationMoveNW : animationIdleNW;
		if (157.5 <= moveVector.getTheta() && moveVector.getTheta() < 202.5)
			return isMoving() ? animationMoveW : animationIdleW;
		if (202.5 <= moveVector.getTheta() && moveVector.getTheta() < 247.5)
			return isMoving() ? animationMoveSW : animationIdleSW;
		if (247.5 <= moveVector.getTheta() && moveVector.getTheta() < 292.5)
			return isMoving() ? animationMoveS : animationIdleS;
		if (292.5 <= moveVector.getTheta() && moveVector.getTheta() < 337.5)
			return isMoving() ? animationMoveSE : animationIdleSE;
		return "";
	}

	// Other methods
	public void move(double direction) {
		this.direction = direction;

		this.moveVector = new Vector2f();
		this.moveVector.set(this.moveSpeed, 0);
		this.moveVector.setTheta(this.direction);
	}

	public void move(int direction, float moveSpeed) {
		this.direction = direction;
		this.moveSpeed = moveSpeed;

		this.moveVector = new Vector2f();
		this.moveVector.set(this.moveSpeed, 0);
		this.moveVector.setTheta(this.direction);
	}

}
