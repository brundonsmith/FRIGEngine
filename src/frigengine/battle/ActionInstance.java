package frigengine.battle;

import frigengine.entities.Battleable;

public class ActionInstance {
	// Attributes
	private Action action;
	private Battleable source;
	private Battleable target;
	
	// Constructors and initialization
	ActionInstance(Action action, Battleable source, Battleable target) {
		this.action = action;
		this.source = source;
		this.target = target;
	}
	
	// Getters and setters
	public Action getAction() {
		return this.action;
	}
	public Battleable getSource() {
		return this.source;
	}
	public Battleable getTarget() {
		return this.target;
	}
	
	// Operations
	public void execute() {
		this.action.apply(source, target, null, null);
	}
}
