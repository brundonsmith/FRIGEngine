package frigengine.entities;

import java.util.HashSet;
import java.util.Set;

import org.newdawn.slick.Input;
import org.newdawn.slick.util.xml.SlickXMLException;
import org.newdawn.slick.util.xml.XMLElement;

import frigengine.battle.Ability;
import frigengine.battle.StatsEffect;
import frigengine.exceptions.AttributeFormatException;
import frigengine.exceptions.InvalidTagException;
import frigengine.scene.Scene;

public abstract class ComponentBattle extends EntityComponent {
	// Constants
	private static final int WAIT_METER_SIZE = 100;
	
	// Attributes
	private int maxHealth;
	private int maxMagic;
	private int realAttack;
	private int realDefense;
	private int realSpeed;
	private int currentHealth;
	private int currentMagic;
	private Set<StatsEffect> effects;
	private int waitMeter;
	private int chargeRequirement;
	private int chargeMeter;
	private Ability attack;
	private Set<Ability> specialAbilities;
	
	// Constructors and initialization
	public ComponentBattle(Entity entity) {
		super(entity);
	}
	@Override
	public void init(XMLElement xmlElement) {
		if (!xmlElement.getName().equals(ComponentBattle.class.getSimpleName()))
			throw new InvalidTagException(ComponentBattle.class.getSimpleName(), xmlElement.getName());
		
		// maxHealth
		try {
			this.maxHealth = xmlElement.getIntAttribute("health", 1);
		} catch (SlickXMLException e) {
			throw new AttributeFormatException(xmlElement.getName(), "health",
					xmlElement.getAttribute("health"));
		}
		
		// maxMagic
		try {
			this.maxMagic = xmlElement.getIntAttribute("magic", 1);
		} catch (SlickXMLException e) {
			throw new AttributeFormatException(xmlElement.getName(), "magic",
					xmlElement.getAttribute("magic"));
		}
		
		// realAttack
		try {
			this.realAttack = xmlElement.getIntAttribute("attack", 1);
		} catch (SlickXMLException e) {
			throw new AttributeFormatException(xmlElement.getName(), "attack",
					xmlElement.getAttribute("attack"));
		}
		
		// realDefense
		try {
			this.realDefense = xmlElement.getIntAttribute("defense", 1);
		} catch (SlickXMLException e) {
			throw new AttributeFormatException(xmlElement.getName(), "defense",
					xmlElement.getAttribute("defense"));
		}
		
		// realSpeed
		try {
			this.realSpeed = xmlElement.getIntAttribute("speed", 1);
		} catch (SlickXMLException e) {
			throw new AttributeFormatException(xmlElement.getName(), "speed",
					xmlElement.getAttribute("speed"));
		}
		
		// currentHealth
		this.currentHealth = this.maxHealth;
		
		// currentMagic
		this.currentMagic = this.maxMagic;
		
		// effects
		this.effects = new HashSet<StatsEffect>();
		
		// waitMeter
		this.waitMeter = 0;
		
		// chargeRequirement
		this.chargeRequirement = 0;
		
		// chargeMeter
		this.chargeMeter = 0;
		
		// attack
		this.attack = new Ability(100, new StatsEffect[]{}) {
			public void applyOn(ComponentBattle target) {
				target.damageHealth(10);
			}
		};
	}

	// Main loop methods
	@Override
	public void update(int delta, Input input, Scene scene) {
		this.waitMeter += delta;
		this.chargeMeter += delta;
	}

	// Getters and setters
	public int getHealth() {
		return this.currentHealth;
	}
	public int getMagic() {
		return this.currentMagic;
	}
	public int getAttack() {
		int attack = this.realAttack;
		for(StatsEffect effect : this.effects)
			attack += effect.getAttackModifier();
		return attack;
	}
	public int getDefense() {
		int attack = this.realDefense;
		for(StatsEffect effect : this.effects)
			attack += effect.getDefenseModifier();
		return attack;
	}
	public int getSpeed() {
		int attack = this.realSpeed;
		for(StatsEffect effect : this.effects)
			attack += effect.getSpeedModifier();
		return attack;
	}
	public int getWaitMeter() {
		return this.waitMeter;
	}
	public int getChargeMeter() {
		return this.chargeMeter;
	}
	
	// Battle
	public void damageHealth(int amount) {
		this.currentHealth = Math.max(0, this.currentHealth - amount);
	}
	public void restoreHealth(int amount) {
		this.currentHealth = Math.min(this.currentHealth + amount, this.maxHealth);
	}
	public void damageMagic(int amount) {
		this.currentMagic = Math.max(0, this.currentMagic - amount);
	}
	public void restoreMagic(int amount) {
		this.currentMagic = Math.min(this.currentMagic + amount, this.maxMagic);
	}
	public void addEffect(StatsEffect effect) {
		this.effects.add(effect);
	}
	
	// Utilities
	public static Set<Class<?>> getComponentDependencies() {
		return new HashSet<Class<?>>();
	}
	public static Set<Class<?>> getComponentExclusives() {
		return new HashSet<Class<?>>();
	}

	public enum Stage {
		WAITING,
		DECIDING,
		CHARGING,
		ACTING
	}
}
