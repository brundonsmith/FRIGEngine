package frigengine.scene;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.util.xml.SlickXMLException;
import org.newdawn.slick.util.xml.XMLElement;

import frigengine.FRIGAnimation;
import frigengine.Initializable;
import frigengine.exceptions.AttributeFormatException;
import frigengine.exceptions.InvalidTagException;

public class SceneLayer implements Comparable<SceneLayer>, Initializable {

	// Attributes
	private int depth;
	private int priority;
	private FRIGAnimation animation;

	// Constructors and initialization
	@Override
	public void init(XMLElement xmlElement) {
		if (!xmlElement.getName().equals(this.getClass().getSimpleName()))
			throw new InvalidTagException(this.getClass().getSimpleName(), xmlElement.getName());

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
		animation = new FRIGAnimation();
		animation.init(xmlElement.getChildrenByName(FRIGAnimation.class.getSimpleName()).get(0));
	}

	// Main loop methods
	public void update(int delta, Input input, Scene scene) {
		this.animation.update(delta);
	}
	public void render(Graphics g, Scene scene) {
		scene.renderLayer(g, this.animation, this.depth);
	}

	// Getters and setters
	public int getDepth() {
		return depth;
	}
	public int getPriority() {
		return priority;
	}

	// Utilities
	@Override
	public String toString() {
		return "layer: depth-" + this.getDepth() + " priority-" + this.getPriority();
	}
	@Override
	public int compareTo(SceneLayer other) {
		if (this.depth != other.depth)
			return this.depth - other.depth;
		return this.priority - other.priority;
	}
}
