package frigengine.scene;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.util.xml.SlickXMLException;
import org.newdawn.slick.util.xml.XMLElement;

import frigengine.FRIGAnimation;
import frigengine.Initializable;
import frigengine.exceptions.AttributeFormatException;
import frigengine.exceptions.InvalidTagException;

public class SceneLayer implements Comparable<SceneLayer>, Initializable {
	@Override
	public String getTagName() {
		return "layer";
	}

	// Attributes
	private int depth;
	private int priority;
	private FRIGAnimation animation;

	// Constructors and initialization
	@Override
	public void init(XMLElement xmlElement) {
		if (!xmlElement.getName().equals(getTagName()))
			throw new InvalidTagException(getTagName(), xmlElement.getName());

		try {
			this.depth = xmlElement.getIntAttribute("depth", 0);
		} catch (SlickXMLException e) {
			throw new AttributeFormatException(getTagName(), "depth",
					xmlElement.getAttribute("depth"));
		}
		try {
			this.priority = xmlElement.getIntAttribute("priority", 0);
		} catch (SlickXMLException e) {
			throw new AttributeFormatException(getTagName(), "priority",
					xmlElement.getAttribute("priority"));
		}

		animation = new FRIGAnimation();
		animation.init(xmlElement.getChildrenByName("animation").get(0));
	}

	// Main loop methods
	public void update(GameContainer container, int delta, Scene scene) {
		animation.update(delta);
	}

	public void render(GameContainer container, Graphics g, Scene scene) {
		scene.renderLayer(container, g, this.animation, this.depth);
	}

	// Getters and setters
	public int getDepth() {
		return depth;
	}

	// Other methods
	@Override
	public int compareTo(SceneLayer other) {
		if (this.depth != other.depth)
			return this.depth - other.depth;
		return this.priority - other.priority;
	}
}
