package frigengine.battle.actions;

import java.util.List;

import frigengine.core.component.*;

public class ActionInstance {
	// Attributes
	private Action action;
	private Entity source;
	private List<Entity> targets;
	
	// Constructors and initialization
	ActionInstance(Action action, Entity source, List<Entity> targets) {
		this.action = action;
		this.source = source;
		this.targets = targets;
	}
	
	// Getters and setters
	public Action getAction() {
		return this.action;
	}
	public Entity getSource() {
		return this.source;
	}
	public List<Entity> getTargets() {
		return this.targets;
	}
	
	// Operations
	public void apply() {
		this.action.apply(source, targets);
	}
}
