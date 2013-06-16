package frigengine.entities;

import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.util.xml.SlickXMLException;
import org.newdawn.slick.util.xml.XMLElement;

import frigengine.battle.Action;
import frigengine.battle.BuffEffect;
import frigengine.battle.HealthMagicEffect;
import frigengine.battle.Effect;
import frigengine.exceptions.data.AttributeFormatException;
import frigengine.exceptions.data.InvalidTagException;
import frigengine.util.IDableCollection;

public class BattleData extends Component {
	// Attributes
	private int maxHealth;
	private int maxMagic;
	private int realAttack;
	private int realDefense;
	private int realSpeed;
	private int currentHealth;
	private int currentMagic;
	private List<BuffEffect> buffs;
	private List<Effect> conditions;
	
	private int waitMeter;
	private int waitRequirement;
	private int chargeMeter;
	private int chargeRequirement;
	private Stage currentStage;
	private IDableCollection<String, Action> actions;
	
	// Constructors and initialization
	@Override
	public void init(XMLElement xmlElement) {
		// Check element name
		if (!xmlElement.getName().equals(this.getClass().getSimpleName())) {
			throw new InvalidTagException(this.getClass().getSimpleName(), xmlElement.getName());
		}
		
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
		
		// buffs
		this.buffs = new ArrayList<BuffEffect>();
		
		// conditions
		this.conditions = new ArrayList<Effect>();
		
		// waitMeter
		this.waitMeter = 0;
		
		// waitRequirement
		this.waitRequirement = 3000;
		
		// chargeMeter
		this.chargeMeter = 0;
		
		// chargeRequirement
		this.chargeRequirement = 1;
		
		// currentStage
		this.currentStage = Stage.WAITING;
		
		// specialActions
		this.actions = new IDableCollection<String, Action>();
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
	
	// Getters and setters
	protected int getHealth() {
		return this.currentHealth;
	}
	protected int getMagic() {
		return this.currentMagic;
	}
	protected int getCurrentAttack() {
		int attack = this.realAttack;
		for(BuffEffect be : this.buffs) {
			attack = be.modifyAttack(attack);
		}
		return attack;
	}
	protected int getCurrentDefense() {
		int defense = this.realDefense;
		for(BuffEffect be : this.buffs) {
			defense = be.modifyDefense(defense);
		}
		return defense;
	}
	protected int getCurrentSpeed() {
		int speed = this.realSpeed;
		for(BuffEffect be : this.buffs) {
			speed = be.modifySpeed(speed);
		}
		return speed;
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
	protected IDableCollection<String, Action> getActions() {
		return this.actions;
	}
	
	// Operations
	protected void damageHealth(int amount) {
		this.currentHealth = Math.max(0, this.currentHealth - amount);
	}
	protected void restoreHealth(int amount) {
		this.currentHealth = Math.min(this.currentHealth + amount, this.maxHealth);
	}
	protected void damageMagic(int amount) {
		this.currentMagic = Math.max(0, this.currentMagic - amount);
	}
	protected void restoreMagic(int amount) {
		this.currentMagic = Math.min(this.currentMagic + amount, this.maxMagic);
	}
	protected void applyStatsEffect(Effect effect) {
		switch(effect.getApplicationMethod()) {
		case ONCE:
			if(effect instanceof HealthMagicEffect) {
				this.currentHealth = ((HealthMagicEffect) effect).modifyHealth(currentHealth);
				this.currentMagic = ((HealthMagicEffect) effect).modifyMagic(currentMagic);
			} else if(effect instanceof BuffEffect){
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
	
	// Stage
	public enum Stage {
		WAITING,
		DECIDING,
		CHARGING,
		ACTING
	}
}
