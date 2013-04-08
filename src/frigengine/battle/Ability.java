package frigengine.battle;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import frigengine.entities.ComponentBattle;

public abstract class Ability {
	// Attributes
	private int chargeTime;
	private Set<StatsEffect> effects;
	
	// Constructors and initialization
	public Ability(int chargeTime, StatsEffect[] effects) {
		this.chargeTime = chargeTime;
		this.effects = new HashSet<StatsEffect>(Arrays.asList(effects));
	}
	
	// Getters and setters
	public int getChargeTime() {
		return this.chargeTime;
	}
	public Set<StatsEffect> getEffects() {
		return this.effects;
	}
	
	// Application
	public abstract void applyOn(ComponentBattle target);
}
