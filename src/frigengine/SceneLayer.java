package frigengine;

import org.newdawn.slick.util.xml.SlickXMLException;
import org.newdawn.slick.util.xml.XMLElement;

import frigengine.exceptions.data.AttributeFormatException;
import frigengine.exceptions.data.InvalidTagException;
import frigengine.exceptions.data.MissingElementException;
import frigengine.util.Initializable;
import frigengine.util.graphics.Animation;

public class SceneLayer implements Comparable<SceneLayer>, Initializable {
	// Attributes
	private int depth;
	private int priority;
	private Animation animation;

	// Constructors and initialization
	@Override
	public void init(XMLElement xmlElement) {
		// Check element name
		if (!xmlElement.getName().equals(this.getClass().getSimpleName())) {
			throw new InvalidTagException(this.getClass().getSimpleName(), xmlElement.getName());
		}

		// depth
		try {
			this.depth = xmlElement.getIntAttribute("depth", 0);
		} catch (SlickXMLException e) {
			throw new AttributeFormatException(xmlElement.getName(), "depth",
					xmlElement.getAttribute("depth"));
		}
		
		// priority
		try {
			this.priority = xmlElement.getIntAttribute("priority", 0);
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
	public int getDepth() {
		return depth;
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
		return "layer: depth-" + this.getDepth() + " priority-" + this.getPriority();
	}
	@Override
	public int compareTo(SceneLayer other) {
		if (this.depth != other.depth) {
			return this.depth - other.depth;
		} else {
			return this.priority - other.priority;
		}
	}
}
