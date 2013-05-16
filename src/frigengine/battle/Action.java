package frigengine.battle;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import frigengine.entities.BattleComponent;
import frigengine.util.IDable;

public abstract class Action extends IDable<String> {
	// Attributes
	private ActionType type;
	private int chargeTime;
	private Set<StatsEffect> effects;
	
	// Constructors and initialization
	public Action(String name, ActionType type, int chargeTime, StatsEffect[] effects) {
		this.id = name;
		this.type = type;
		this.chargeTime = chargeTime;
		this.effects = new HashSet<StatsEffect>(Arrays.asList(effects));
	}
	
	// Getters and setters
	public String getName() {
		return this.getID();
	}
	public ActionType getType() {
		return this.type;
	}
	public int getChargeTime() {
		return this.chargeTime;
	}
	public Set<StatsEffect> getEffects() {
		return this.effects;
	}
	public ActionInstance getInstance(BattleComponent source, BattleComponent target) {
		return new ActionInstance(this, source, target);
	}
	
	// Application
	public abstract void apply(BattleComponent source, BattleComponent target);

	// ActionType
	public enum ActionType {
		ATTACK,
		SPECIAL
	}
}
