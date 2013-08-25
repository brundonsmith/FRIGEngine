package frigengine.core.scene;

import org.newdawn.slick.util.xml.SlickXMLException;
import org.newdawn.slick.util.xml.XMLElement;

import frigengine.core.exceptions.data.*;
import frigengine.core.util.Initializable;

public class SceneLayer implements Comparable<SceneLayer>, Initializable {
	// Attributes
	private int elevation;
	private int priority;
	private Animation animation;

	// Constructors and initialization
	public SceneLayer() {
		this.elevation = 0;
		this.priority = 0;
	}
	@Override
	public void init(XMLElement xmlElement) {
		// Check element name
		if (!xmlElement.getName().equals(this.getClass().getSimpleName())) {
			throw new InvalidTagException(this.getClass().getSimpleName(), xmlElement.getName());
		}

		// depth
		try {
			this.elevation = xmlElement.getIntAttribute("elevation", this.getElevation());
		} catch (SlickXMLException e) {
			throw new AttributeFormatException(xmlElement.getName(), "elevation",
					xmlElement.getAttribute("elevation"));
		}
		
		// priority
		try {
			this.priority = xmlElement.getIntAttribute("priority", this.getPriority());
		} catch (SlickXMLException e) {
			throw new AttributeFormatException(xmlElement.getName(), "priority",
					xmlElement.getAttribute("priority"));
		}
		
		// animation
		if(xmlElement.getChildrenByName(Animation.class.getSimpleName()).size() > 0) {
			this.animation = new Animation();
			this.animation.init(xmlElement.getChildrenByName(Animation.class.getSimpleName()).get(0));
		} else {
			this.animation = null;
			throw new MissingElementException(Animation.class.getSimpleName(), xmlElement.getName());
		}
	}

	// Main loop methods
	public void update(int delta) {
		this.animation.update(delta);
	}

	// Getters and setters
	public int getElevation() {
		return elevation;
	}
	public int getPriority() {
		return priority;
	}
	public Animation getAnimation() {
		return this.animation;
	}
	
	// Utilities
	@Override
	public String toString() {
		return "layer: depth-" + this.getElevation() + " priority-" + this.getPriority();
	}
	@Override
	public int compareTo(SceneLayer other) {
		if (this.elevation != other.elevation) {
			return this.elevation - other.elevation;
		} else {
			return this.priority - other.priority;
		}
	}
}
