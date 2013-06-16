package frigengine.entities;

import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.util.xml.SlickXMLException;
import org.newdawn.slick.util.xml.XMLElement;

import frigengine.exceptions.data.AttributeFormatException;
import frigengine.exceptions.data.InvalidTagException;
import frigengine.util.graphics.Animation;

public class CharacterData extends Component {
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
	@Override
	public void init(XMLElement xmlElement) {
		// Check element name
		if (!xmlElement.getName().equals(this.getClass().getSimpleName())) {
			throw new InvalidTagException(this.getClass().getSimpleName(), xmlElement.getName());
		}

		// moveVector
		this.moveVector = new Vector2f(0, 0);

		// moveSpeed
		try {
			this.moveSpeed = ((float) xmlElement.getDoubleAttribute("speed", 0.005));
		} catch (SlickXMLException e) {
			throw new AttributeFormatException(xmlElement.getName(), "speed",
					xmlElement.getAttribute("speed"));
		}

		// direction
		try {
			this.direction = ((float) xmlElement.getDoubleAttribute("direction", 90));
		} catch (SlickXMLException e) {
			throw new AttributeFormatException(xmlElement.getName(), "direction",
					xmlElement.getAttribute("direction"));
		}

		// animations
		this.animationIdleNW = xmlElement.getChildrenByName("animation_idle_nw").size() > 0
				? xmlElement.getChildrenByName("animation_idle_nw").get(0).getAttribute("id", Animation.PLACEHOLDER_ID)
				: Animation.PLACEHOLDER_ID;
		this.animationIdleN = xmlElement.getChildrenByName("animation_idle_n").size() > 0
				? xmlElement.getChildrenByName("animation_idle_n").get(0).getAttribute("id", Animation.PLACEHOLDER_ID)
				: Animation.PLACEHOLDER_ID;
		this.animationIdleNE = xmlElement.getChildrenByName("animation_idle_ne").size() > 0
				? xmlElement.getChildrenByName("animation_idle_ne").get(0).getAttribute("id", Animation.PLACEHOLDER_ID)
				: Animation.PLACEHOLDER_ID;
		this.animationIdleSW = xmlElement.getChildrenByName("animation_idle_sw").size() > 0
				? xmlElement.getChildrenByName("animation_idle_sw").get(0).getAttribute("id", Animation.PLACEHOLDER_ID)
				: Animation.PLACEHOLDER_ID;
		this.animationIdleS = xmlElement.getChildrenByName("animation_idle_s").size() > 0
				? xmlElement.getChildrenByName("animation_idle_s").get(0).getAttribute("id", Animation.PLACEHOLDER_ID)
				: Animation.PLACEHOLDER_ID;
		this.animationIdleSE = xmlElement.getChildrenByName("animation_idle_se").size() > 0
				? xmlElement.getChildrenByName("animation_idle_se").get(0).getAttribute("id", Animation.PLACEHOLDER_ID)
				: Animation.PLACEHOLDER_ID;
		this.animationIdleE = xmlElement.getChildrenByName("animation_idle_e").size() > 0
				? xmlElement.getChildrenByName("animation_idle_e").get(0).getAttribute("id", Animation.PLACEHOLDER_ID)
				: Animation.PLACEHOLDER_ID;
		this.animationIdleW = xmlElement.getChildrenByName("animation_idle_w").size() > 0
				? xmlElement.getChildrenByName("animation_idle_w").get(0).getAttribute("id", Animation.PLACEHOLDER_ID)
				: Animation.PLACEHOLDER_ID;
		this.animationMoveNW = xmlElement.getChildrenByName("animation_move_nw").size() > 0
				? xmlElement.getChildrenByName("animation_move_nw").get(0).getAttribute("id", Animation.PLACEHOLDER_ID)
				: Animation.PLACEHOLDER_ID;
		this.animationMoveN = xmlElement.getChildrenByName("animation_move_n").size() > 0
				? xmlElement.getChildrenByName("animation_move_n").get(0).getAttribute("id", Animation.PLACEHOLDER_ID)
				: Animation.PLACEHOLDER_ID;
		this.animationMoveNE = xmlElement.getChildrenByName("animation_move_ne").size() > 0
				? xmlElement.getChildrenByName("animation_move_ne").get(0).getAttribute("id", Animation.PLACEHOLDER_ID)
				: Animation.PLACEHOLDER_ID;
		this.animationMoveSW = xmlElement.getChildrenByName("animation_move_sw").size() > 0
				? xmlElement.getChildrenByName("animation_move_sw").get(0).getAttribute("id", Animation.PLACEHOLDER_ID)
				: Animation.PLACEHOLDER_ID;
		this.animationMoveS = xmlElement.getChildrenByName("animation_move_s").size() > 0
				? xmlElement.getChildrenByName("animation_move_s").get(0).getAttribute("id", Animation.PLACEHOLDER_ID)
				: Animation.PLACEHOLDER_ID;
		this.animationMoveSE = xmlElement.getChildrenByName("animation_move_se").size() > 0
				? xmlElement.getChildrenByName("animation_move_se").get(0).getAttribute("id", Animation.PLACEHOLDER_ID)
				: Animation.PLACEHOLDER_ID;
		this.animationMoveE = xmlElement.getChildrenByName("animation_move_e").size() > 0
				? xmlElement.getChildrenByName("animation_move_e").get(0).getAttribute("id", Animation.PLACEHOLDER_ID)
				: Animation.PLACEHOLDER_ID;
		this.animationMoveW = xmlElement.getChildrenByName("animation_move_w").size() > 0
				? xmlElement.getChildrenByName("animation_move_w").get(0).getAttribute("id", Animation.PLACEHOLDER_ID)
				: Animation.PLACEHOLDER_ID;
	}

	// Utilities
	@Override
	public String toString() {
		return this.getId() + ": moveSpeed-" + this.moveSpeed + " direction-" + this.direction
				+ " movementVector-" + this.moveVector;
	}
}
