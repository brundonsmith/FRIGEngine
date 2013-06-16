package frigengine.battle;

import java.util.Set;

import frigengine.entities.Battleable;
import frigengine.util.IDable;

public abstract class Action extends IDable<String> {
	// Attributes
	protected  ActionCategory category;
	protected int chargeTime;
	
	// Constructors and initialization
	protected Action(String name) {
		this.id = name;
	}
	public Action(String name, ActionCategory category, int chargeTime) {
		this.id = name;
		this.category = category;
		this.chargeTime = chargeTime;
	}
	
	// Getters and setters
	public String getName() {
		return this.getId();
	}
	public ActionCategory getCategory() {
		return this.category;
	}
	public int getChargeTime() {
		return this.chargeTime;
	}
	public ActionInstance getInstance(Battleable source, Battleable target) {
		return new ActionInstance(this, source, target);
	}

	// Application
	public abstract void apply(Battleable source, Battleable target, Set<Battleable> allies, Set<Battleable> enemi);
	
	// ActionCategory
	public enum ActionCategory {
		ATTACK,
		SPECIAL
	}
}
