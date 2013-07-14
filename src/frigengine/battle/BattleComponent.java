package frigengine.battle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.newdawn.slick.Input;
import org.newdawn.slick.util.xml.SlickXMLException;
import org.newdawn.slick.util.xml.XMLElement;

import frigengine.battle.actions.Action;
import frigengine.battle.statseffects.BuffEffect;
import frigengine.battle.statseffects.Effect;
import frigengine.core.AnimationFinishedListener;
import frigengine.core.component.*;
import frigengine.core.exceptions.data.*;
import frigengine.core.idable.*;
import frigengine.core.scene.*;


public class BattleComponent extends Component implements AnimationFinishedListener {
	// Required components
	@Override
	public final Collection<Class<? extends Component>> requiredComponents() {
		return new ArrayList<Class<? extends Component>>(Arrays.asList(
				SpriteComponent.class
			));
	}
	
	// Attributes
	private int maxHealth;
	private int attack;
	private int defense;
	private int magicDefense;
	private int resilience;
	private int speed;
	private int currentHealth;
	private List<BuffEffect> buffs;
	private List<Effect> conditions;
	
	private int waitMeter;
	private int waitRequirement;
	private int chargeMeter;
	private int chargeRequirement;
	private Stage currentStage;
	private IDableCollection<String, Action> actions;
	
	// Constructors and initialization
	public BattleComponent() {
		this.maxHealth = 1;
		this.attack = 1;
		this.defense = 1;
		this.magicDefense = 1;
		this.resilience = 1;
		this.speed = 1;
		this.currentHealth = this.maxHealth;
		this.buffs = new ArrayList<BuffEffect>();
		this.conditions = new ArrayList<Effect>();
		
		this.waitMeter = 0;
		this.waitRequirement = 3000;
		this.chargeMeter = 0;
		this.chargeRequirement = 1;
		this.currentStage = Stage.WAITING;
		this.actions = new IDableCollection<String, Action>();
	}
	@Override
	public void init(XMLElement xmlElement) {
		// Check element name
		if (!xmlElement.getName().equals(this.getClass().getSimpleName())) {
			throw new InvalidTagException(this.getClass().getSimpleName(), xmlElement.getName());
		}
		
		// maxHealth
		try {
			this.maxHealth = xmlElement.getIntAttribute("health", this.maxHealth);
		} catch (SlickXMLException e) {
			throw new AttributeFormatException(xmlElement.getName(), "health",
					xmlElement.getAttribute("health"));
		}
		
		// attack
		try {
			this.attack = xmlElement.getIntAttribute("attack", this.attack);
		} catch (SlickXMLException e) {
			throw new AttributeFormatException(xmlElement.getName(), "attack",
					xmlElement.getAttribute("attack"));
		}
		
		// defense
		try {
			this.defense = xmlElement.getIntAttribute("defense", this.defense);
		} catch (SlickXMLException e) {
			throw new AttributeFormatException(xmlElement.getName(), "defense",
					xmlElement.getAttribute("defense"));
		}
		
		// magicDefense
		try {
			this.magicDefense = xmlElement.getIntAttribute("magicdefense", this.defense);
		} catch (SlickXMLException e) {
			throw new AttributeFormatException(xmlElement.getName(), "magicdefense",
					xmlElement.getAttribute("magicdefense"));
		}

		// resilience
		try {
			this.resilience = xmlElement.getIntAttribute("resilience", this.speed);
		} catch (SlickXMLException e) {
			throw new AttributeFormatException(xmlElement.getName(), "resilience",
					xmlElement.getAttribute("resilience"));
		}
		
		// speed
		try {
			this.speed = xmlElement.getIntAttribute("speed", this.speed);
		} catch (SlickXMLException e) {
			throw new AttributeFormatException(xmlElement.getName(), "speed",
					xmlElement.getAttribute("speed"));
		}
		
		// currentHealth
		this.currentHealth = this.maxHealth;
		
		// buffs
		
		// conditions
		
		// waitMeter
		
		// waitRequirement
		
		// chargeMeter
		
		// chargeRequirement
		
		// currentStage
		
		// specialActions
		/*
		this.actions.put(new EffectsBasedAction("attack", EffectsBasedAction.ActionCategory.ATTACK, 10, new StatsEffect[]{}) {
			public void apply(Battleable source, Battleable target) {
				target.damageHealth(5);
			}
		});
		this.actions.put(new EffectsBasedAction("blah", EffectsBasedAction.ActionCategory.SPECIAL, 100, new StatsEffect[]{}) {
			public void apply(Battleable source, Battleable target) {
				target.damageHealth(20);
			}
		});
		*/
	}
	
	// Main loop methods
	@Override
	public void update(int delta, Input input) {
		switch(this.getCurrentStage()) {
		case WAITING:
			this.increaseWaitMeter(delta);
			if(this.getWaitMeterIsFull()) {
				this.setCurrentStage(Stage.DECIDING);
			}
			break;
		case DECIDING:
			break;
		case CHARGING:
			this.increaseChargeMeter(delta);
			if(this.getChargeMeterIsFull()) {
				this.setCurrentStage(Stage.ACTING);
			}
			break;
		case ACTING:
			break;
		}
	}
	
	// Getters and setters
	public int getHealth() {
		return this.currentHealth;
	}
	public int getAttack() {
		int attack = this.attack;
		for(BuffEffect be : this.buffs) {
			attack = be.modifyAttack(attack);
		}
		return attack;
	}
	public int getDefense() {
		int defense = this.defense;
		for(BuffEffect be : this.buffs) {
			defense = be.modifyDefense(defense);
		}
		return defense;
	}
	public int getMagicDefense() {
		int magicDefense = this.magicDefense;
		for(BuffEffect be : this.buffs) {
			magicDefense = be.modifyMagicDefense(magicDefense);
		}
		return magicDefense;
	}
	public int getResilience() {
		int resilience = this.resilience;
		for(BuffEffect be : this.buffs) {
			resilience = be.modifyResilience(resilience);
		}
		return resilience;
	}
	public int getSpeed() {
		int speed = this.speed;
		for(BuffEffect be : this.buffs) {
			speed = be.modifySpeed(speed);
		}
		return speed;
	}
	public int getCurrentHealth() {
		return this.currentHealth;
	}
	
	protected int getWaitMeter() {
		return this.waitMeter;
	}
	protected boolean getWaitMeterIsFull() {
		return this.waitMeter >= this.waitRequirement;
	}
	protected void setWaitMeter(int amount) {
		this.waitMeter = amount;
	}
	protected void increaseWaitMeter(int amount) {
		this.waitMeter += amount;
	}
	protected int getChargeMeter() {
		return this.chargeMeter;
	}
	protected boolean getChargeMeterIsFull() {
		return this.chargeMeter >= this.chargeRequirement;
	}
	protected void setChargeMeter(int amount) {
		this.chargeMeter = amount;
	}
	protected void increaseChargeMeter(int amount) {
		this.chargeMeter += amount;
	}
	protected Stage getCurrentStage() {
		return this.currentStage;
	}
	protected void setCurrentStage(Stage stage) {
		this.currentStage = stage;
		
		switch(stage) {
		case WAITING:
			this.waitMeter = 0;
			break;
		case DECIDING:
			break;
		case CHARGING:
			this.chargeMeter = 0;
			break;
		case ACTING:
			break;
		}
	}
	protected Action getAction(String name) {
		return this.actions.get(name);
	}
	public IDableCollection<String, Action> getActions() {
		return this.actions;
	}
	
	// Operations
	protected void resetAll() {
		this.resetMeters();
		this.currentHealth = this.maxHealth;
		this.buffs.clear();
		this.conditions.clear();
	}
	protected void resetMeters() {
		this.waitMeter = 0;
		this.chargeMeter = 0;
		this.currentStage = Stage.WAITING;
	}
	public void damageHealth(int amount) {
		this.currentHealth = Math.max(0, this.currentHealth - amount);
	}
	public void restoreHealth(int amount) {
		this.currentHealth = Math.min(this.currentHealth + amount, this.maxHealth);
	}
	public void applyStatsEffect(Effect effect) {
		switch(effect.getApplicationMethod()) {
		case ONCE:
			if(effect instanceof BuffEffect){
				this.buffs.add((BuffEffect)effect);
			}
			break;
		case TURN:
			this.conditions.add(effect);
			break;
		default:
			break;
		}
	}
	
	// Events
	@Override
	public void animationFinished(Animation source) {
		if(this.getCurrentStage().equals(Stage.ACTING)) {
			this.setCurrentStage(Stage.WAITING);
		}
	}
	
	// Stage
	public enum Stage {
		WAITING,
		DECIDING,
		CHARGING,
		ACTING
	}
}
