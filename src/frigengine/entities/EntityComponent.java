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
	
	// Getters and setters
	public Entity getEntity() {
		return this.entity;
	}

	// Main loop methods
	public abstract void update(int delta, Input input, Scene scene);
	
	// Utilities
	public static void registerComponents() {
		Component.registerComponent(SpacialComponent.class,
				SpacialComponent.getComponentDependencies(),
				SpacialComponent.getComponentExclusives());
		Component.registerComponent(DrawableComponent.class,
				DrawableComponent.getComponentDependencies(),
				DrawableComponent.getComponentExclusives());
		Component.registerComponent(PhysicalComponent.class,
				PhysicalComponent.getComponentDependencies(),
				PhysicalComponent.getComponentExclusives());
		Component.registerComponent(CharacterComponent.class,
				CharacterComponent.getComponentDependencies(),
				CharacterComponent.getComponentExclusives());
		Component.registerComponent(ScriptableComponent.class,
				ScriptableComponent.getComponentDependencies(),
				ScriptableComponent.getComponentExclusives());
		Component.registerComponent(BattleComponent.class,
				BattleComponent.getComponentDependencies(),
				BattleComponent.getComponentExclusives());
	}
}
