package frigengine.entities;

import org.newdawn.slick.Input;
import org.newdawn.slick.util.xml.XMLElement;

import frigengine.scene.Scene;

public class InventoryComponent extends EntityComponent {
	// Attributes
	// inventory of some kind?
	
	// Constructors and initialization
	public InventoryComponent(Entity entity) {
		super(entity);
	}
	@Override
	public void init(XMLElement xmlElement) {
	}
	
	// Main loop methods
	@Override
	public void update(int delta, Input input, Scene scene) {
	}
}
