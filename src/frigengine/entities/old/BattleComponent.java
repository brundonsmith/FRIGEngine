package frigengine.entities.old;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.newdawn.slick.Input;
import org.newdawn.slick.util.xml.SlickXMLException;
import org.newdawn.slick.util.xml.XMLElement;

import frigengine.Scene;
import frigengine.battle.Action;
import frigengine.events.StageChangeListener;
import frigengine.battle.StatsEffect;
import frigengine.exceptions.AttributeFormatException;
import frigengine.exceptions.InvalidTagException;
import frigengine.util.IDableCollection;
import frigengine.util.graphics.AnimationFinishedListener;
import frigengine.util.graphics.FRIGAnimation;

public class BattleComponent extends EntityComponent implements AnimationFinishedListener {
	// Constants
	private static final int DEFAULT_WAIT_METER_REQUIREMENT = 3000;
	
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
	private int waitRequirement;
	private int chargeMeter;
	private int chargeRequirement;
	private Stage currentStage;
	private IDableCollection<String, Action> actions;
	private List<StageChangeListener> stageChangeListeners;
	
	// Constructors and initialization
	public BattleComponent(Entity entity) {
		super(entity);
	}
	@Override
	public void init(XMLElement xmlElement) {
		if (!xmlElement.getName().equals(BattleComponent.class.getSimpleName())) {
			throw new InvalidTagException(BattleComponent.class.getSimpleName(), xmlElement.getName());
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
		
		// effects
		this.effects = new HashSet<StatsEffect>();
		
		// waitMeter
		this.waitMeter = 0;
		
		// waitRequirement
		this.waitRequirement = DEFAULT_WAIT_METER_REQUIREMENT;
		
		// chargeMeter
		this.chargeMeter = 0;
		
		// chargeRequirement
		this.chargeRequirement = 1;
		
		// currentStage
		this.currentStage = Stage.WAITING;
		
		// specialActions
		this.actions = new IDableCollection<String, Action>();
		this.actions.add(new Action("attack", Action.ActionType.ATTACK, 10, new StatsEffect[]{}) {
			public void apply(BattleComponent source, BattleComponent target) {
				target.damageHealth(5);
			}
		});
		this.actions.add(new Action("blah", Action.ActionType.SPECIAL, 100, new StatsEffect[]{}) {
			public void apply(BattleComponent source, BattleComponent target) {
				target.damageHealth(20);
			}
		});
	}

	// Main loop methods
	@Override
	public void update(int delta, Input input, Scene scene) {
		switch(this.getCurrentStage()) {
		case WAITING:
			this.waitMeter += delta;
			if(this.waitMeter >= this.waitRequirement) {
				this.setCurrentStage(Stage.DECIDING);
			}
			break;
		case DECIDING:
			break;
		case CHARGING:
			this.chargeMeter += delta;
			if(this.chargeMeter >= this.chargeRequirement) {
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
	public Stage getCurrentStage() {
		return this.currentStage;
	}
	private void setCurrentStage(Stage stage) {
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
		
		this.stageChanged();
	}
	public Action getAction(String name) {
		return this.actions.get(name);
	}
	
	public IDableCollection<String, Action> getActions() {
		return this.actions;
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
	public void addStatsEffect(StatsEffect effect) {
		this.effects.add(effect);
	}
	public void animateAction(String actionId) {
		this.entity.drawable().setActiveAnimation("action_" + this.getAction(actionId).getId());
	}

	// Events
	public void stageChanged() {
		for(StageChangeListener listener : stageChangeListeners)
			listener.stageChanged(this);
	}
	public void addStageChangeListener(StageChangeListener listener) {
		if (stageChangeListeners == null)
			stageChangeListeners = new ArrayList<StageChangeListener>();

		stageChangeListeners.add(listener);
	}
	@Override
	public void animationFinished(FRIGAnimation source) {
		this.setCurrentStage(Stage.WAITING);
	}
	
	// EntityComponent
	public static Set<Class<?>> getComponentDependencies() {
		return new HashSet<Class<?>>( Arrays.asList( new Class<?>[] { DrawableComponent.class }) );
	}
	public static Set<Class<?>> getComponentExclusives() {
		return new HashSet<Class<?>>();
	}
	
	// Stage
	public enum Stage {
		WAITING,
		DECIDING,
		CHARGING,
		ACTING
	}
}
