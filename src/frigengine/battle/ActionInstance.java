package frigengine.battle;

import frigengine.entities.BattleComponent;

public class ActionInstance {
	// Attributes
	private Action action;
	private BattleComponent source;
	private BattleComponent target;
	
	// Constructors and initialization
	ActionInstance(Action action, BattleComponent source, BattleComponent target) {
		this.action = action;
		this.source = source;
		this.target = target;
	}
	
	// Getters and setters
	public Action getAction() {
		return this.action;
	}
	public BattleComponent getSource() {
		return this.source;
	}
	public BattleComponent getTarget() {
		return this.target;
	}
	
	// Other methods
	public void execute() {
		this.action.apply(this.source, this.target);
	}
}
