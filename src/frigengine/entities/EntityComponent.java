package frigengine.entities;

import org.newdawn.slick.Input;

import frigengine.Initializable;
import frigengine.scene.*;
import frigengine.util.*;

public abstract class EntityComponent extends Component implements Initializable {
	// Attributes
	protected Entity entity;

	// Constructors and initialization
	public EntityComponent(Entity entity) {
		this.entity = entity;
	}

	// Main loop methods
	public abstract void update(int delta, Input input, Scene scene);
	
	// Utilities
	public static void registerComponents() {
		Component.registerComponent(ComponentSpacial.class,
				ComponentSpacial.getComponentDependencies(),
				ComponentSpacial.getComponentExclusives());
		Component.registerComponent(ComponentDrawable.class,
				ComponentDrawable.getComponentDependencies(),
				ComponentDrawable.getComponentExclusives());
		Component.registerComponent(ComponentPhysical.class,
				ComponentPhysical.getComponentDependencies(),
				ComponentPhysical.getComponentExclusives());
		Component.registerComponent(ComponentCharacter.class,
				ComponentCharacter.getComponentDependencies(),
				ComponentCharacter.getComponentExclusives());
		Component.registerComponent(ComponentScriptable.class,
				ComponentScriptable.getComponentDependencies(),
				ComponentScriptable.getComponentExclusives());
		Component.registerComponent(ComponentBattle.class,
				ComponentBattle.getComponentDependencies(),
				ComponentBattle.getComponentExclusives());
	}
}
