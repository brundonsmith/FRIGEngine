package frigengine.entities;

import org.newdawn.slick.util.xml.SlickXMLException;
import org.newdawn.slick.util.xml.XMLElement;

import frigengine.events.AnimationFinishedListener;
import frigengine.exceptions.data.AttributeFormatException;
import frigengine.exceptions.data.InvalidTagException;
import frigengine.util.IDableCollection;
import frigengine.util.geom.Rectangle;
import frigengine.util.graphics.Animation;

public class GraphicsData extends Component implements AnimationFinishedListener {
	// Attributes
	private Rectangle presence;
	private IDableCollection<String, Animation> animations;
	private String activeAnimation;
	private String continuousAnimation;
	
	// Constructors and initialization
	@Override
	public void init(XMLElement xmlElement) {
		// Check element name
		if (!xmlElement.getName().equals(this.getClass().getSimpleName())) {
			throw new InvalidTagException(this.getClass().getSimpleName(), xmlElement.getName());
		}

		// presence
		float presenceWidth;
		try {
			presenceWidth = (float) xmlElement.getDoubleAttribute("width", 0);
		} catch (SlickXMLException e) {
			throw new AttributeFormatException(xmlElement.getName(), "width",
					xmlElement.getAttribute("width"));
		}
		float presenceHeight;
		try {
			presenceHeight = (float) xmlElement.getDoubleAttribute("height", 0);
		} catch (SlickXMLException e) {
			throw new AttributeFormatException(xmlElement.getName(), "height",
					xmlElement.getAttribute("height"));
		}
		if(presenceWidth == 0 || presenceHeight == 0) {
			this.presence = null;
		} else {
			this.presence = new Rectangle(0, 0, 0, 0);
			this.presence.setX(0);
			this.presence.setY(0);
			this.presence.setWidth(presenceWidth);
			this.presence.setHeight(presenceHeight);
		}

		// animations
		this.animations = new IDableCollection<String, Animation>();
		this.animations.put(Animation.getPlaceholder());
		for (int i = 0; i < xmlElement.getChildrenByName(Animation.class.getSimpleName()).size(); i++) {
			XMLElement child = xmlElement.getChildrenByName(Animation.class.getSimpleName()).get(i);

			Animation newAnimation = new Animation();
			newAnimation.init(child);
			newAnimation.addFinishedListener(this);
			this.animations.put(newAnimation);
		}
		
		// activeAnimation
		this.activeAnimation = "";
		
		// continuousAnimation
		if(this.animations.size() > 1) {
			for(Animation f : this.animations) { // If has more than a placeholder, pick a random non-placeholder default animation
				if(!f.getId().equals(Animation.PLACEHOLDER_ID)) {
					this.continuousAnimation = f.getId();
				}
			}
		} else {
			this.continuousAnimation = Animation.PLACEHOLDER_ID;
		}
	}

	// Getters and setters
	protected Rectangle getPresence() {
		return this.presence;
	}
	protected boolean hasAnimation(String animationId) {
		return this.animations.contains(animationId);
	}
	protected Animation getActiveAnimation() {
		if(this.hasActiveAnimation()) {
			return this.animations.get(this.activeAnimation);
		} else {
			return null;
		}
	}
	protected void setActiveAnimation(String animation) {
		if(this.animations.contains(animation)) {
			this.activeAnimation = animation;
		} else {
			this.activeAnimation = Animation.PLACEHOLDER_ID;
		}
		this.animations.get(this.activeAnimation).setLooping(false);
		this.animations.get(this.activeAnimation).restart();
	}
	protected boolean hasActiveAnimation() {
		return this.activeAnimation != null && !this.activeAnimation.equals("");
	}
	protected void clearActiveAnimation() {
		if(this.hasActiveAnimation()) {
			this.getActiveAnimation().stop();
		}
		this.activeAnimation = "";
	}
	protected Animation getContinuousAnimation() {
		if(!this.animations.contains(this.continuousAnimation)) {
			this.continuousAnimation = Animation.PLACEHOLDER_ID;
		}
		return this.animations.get(this.continuousAnimation);
	}
	protected void setContinuousAnimation(String animation) {
		if (!this.continuousAnimation.equals(animation)) {
			this.clearActiveAnimation();
			if(this.animations.contains(animation)) {
				this.continuousAnimation = animation;
			} else {
				this.continuousAnimation = Animation.PLACEHOLDER_ID;
			}
			this.animations.get(this.continuousAnimation).setLooping(true);
			this.animations.get(this.continuousAnimation).start();
		}
	}
	protected Animation getCurrentAnimation() {
		return this.hasActiveAnimation() ? this.getActiveAnimation() : this.getContinuousAnimation();
	}
	
	// Utilities
	@Override
	public String toString() {
		return "Current animation: " + this.getCurrentAnimation().getId();
	}
	
	// Events

	@Override
	public void animationFinished(Animation source) {
		if(source.getId().equals(this.getActiveAnimation().getId())) {
			this.clearActiveAnimation();
		}
	}
}
