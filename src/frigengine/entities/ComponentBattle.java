package frigengine.entities;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.util.xml.XMLElement;

import frigengine.exceptions.DataParseException;
import frigengine.scene.Scene;

public class ComponentBattle extends EntityComponent {
	public static String getComponentID() {
		return "battleable";
	}

	public String getTagName() {
		return getComponentID();
	}

	public static String[] getComponentDependencies() {
		return new String[] {};
	}

	public static String[] getComponentExclusives() {
		return new String[] {};
	}

	// Constructors and initialization
	public ComponentBattle(Entity entity) {
		super(entity);
		this.id = getComponentID();
	}

	@Override
	public void init(XMLElement xmlElement) {
		if (!xmlElement.getName().equals(this.getID()))
			throw new DataParseException(
					"Xml node does not match component type '" + this.id + "'");
	}

	// Main loop methods
	@Override
	public void update(GameContainer container, int delta, Scene scene) {
	}
}
