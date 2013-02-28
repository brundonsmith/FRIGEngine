package frigengine.entities;

import org.newdawn.slick.GameContainer;

import frigengine.Initializable;
import frigengine.scene.*;
import frigengine.util.*;

public abstract class EntityComponent extends Component implements
		Initializable {
	public static void registerComponents() {
		Component.registerComponent(ComponentSpacial.getComponentID(),
				ComponentSpacial.getComponentDependencies(),
				ComponentSpacial.getComponentExclusives());
		Component.registerComponent(ComponentDrawable.getComponentID(),
				ComponentDrawable.getComponentDependencies(),
				ComponentDrawable.getComponentExclusives());
		Component.registerComponent(ComponentPhysical.getComponentID(),
				ComponentPhysical.getComponentDependencies(),
				ComponentPhysical.getComponentExclusives());
		Component.registerComponent(ComponentCharacter.getComponentID(),
				ComponentCharacter.getComponentDependencies(),
				ComponentCharacter.getComponentExclusives());
		Component.registerComponent(ComponentScriptable.getComponentID(),
				ComponentScriptable.getComponentDependencies(),
				ComponentScriptable.getComponentExclusives());
		Component.registerComponent(ComponentBattle.getComponentID(),
				ComponentBattle.getComponentDependencies(),
				ComponentBattle.getComponentExclusives());
	}

	// Attributes
	protected Entity entity;

	// Constructors and initialization
	public EntityComponent(Entity entity) {
		this.entity = entity;
	}

	// Main loop methods
	public abstract void update(GameContainer container, int delta, Scene scene);
}
