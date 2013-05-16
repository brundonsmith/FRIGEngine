package frigengine.battle;

import frigengine.entities.BattleComponent;

public enum StatsEffect {
	POISON(10, 0, 0, 0, 0);
	
	// Attributes
	private int healthChange;
	private int magicChange;
	private int attackModifier;
	private int defenseModifier;
	private int speedModifier;
	
	// Constructors and initialization
	private StatsEffect(int healthChange, int magicChange, int attackModifier, int defenseModifier, int speedModifier) {
		this.healthChange = healthChange;
		this.magicChange = magicChange;
		this.attackModifier = attackModifier;
		this.defenseModifier = defenseModifier;
		this.speedModifier = speedModifier;
	}
	
	// Getters and setters
	public int getHealthChange() {
		return this.healthChange;
	}
	public int getMagicChange() {
		return this.magicChange;
	}
	public int getAttackModifier() {
		return this.attackModifier;
	}
	public int getDefenseModifier() {
		return this.defenseModifier;
	}
	public int getSpeedModifier() {
		return this.speedModifier;
	}

	// Application
	public void applyOn(BattleComponent target) {
		target.damageHealth(this.healthChange);
		target.damageMagic(this.magicChange);
	}
}
