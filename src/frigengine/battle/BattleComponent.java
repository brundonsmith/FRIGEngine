package frigengine.battle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.newdawn.slick.Input;
import org.newdawn.slick.util.xml.SlickXMLException;
import org.newdawn.slick.util.xml.XMLElement;

import frigengine.battle.actions.*;
import frigengine.battle.gui.*;
import frigengine.battle.statseffects.*;
import frigengine.core.component.*;
import frigengine.core.exceptions.data.*;
import frigengine.core.gui.menu.*;
import frigengine.core.idable.*;
import frigengine.core.scene.*;


public class BattleComponent extends Component implements  AnimationFinishedSubscriber, MenuCloseSubscriber, MovementComponentFinishedSubscriber {
	// Required components
	@Override
	public final Collection<Class<? extends Component>> requiredComponents() {
		return new ArrayList<Class<? extends Component>>(Arrays.asList(
				PositionComponent.class,
				SpriteComponent.class,
				MovementComponent.class
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
	private int waitCapacity;
	private int chargeMeter;
	private ActionInstance selectedAction;
	private Movement finalMovement;
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
		this.waitCapacity = 3000;
		this.chargeMeter = 0;
		this.selectedAction = null;
		this.setCurrentStage(Stage.WAITING);
		this.actions = new IDableCollection<String, Action>();
		
		this.battleWaitMeterFilledSubscribers = new ArrayList<BattleWaitMeterFilledSubscriber>();
		this.battleChargeMeterFilledSubscribers = new ArrayList<BattleChargeMeterFilledSubscriber>();
		this.battleComponentActionFinishedSubscribers = new ArrayList<BattleComponentActionFinishedSubscriber>();
	}
	private BattleComponent(BattleComponent other) {
		super(other);
		
		this.maxHealth = other.maxHealth;
		this.attack = other.attack;
		this.defense = other.defense;
		this.magicDefense = other.magicDefense;
		this.resilience = other.resilience;
		this.speed = other.speed;
		this.currentHealth = other.currentHealth;
		this.buffs = new ArrayList<BuffEffect>();
		for(BuffEffect b : other.buffs) {
			this.buffs.add(b);
		}
		this.conditions = new ArrayList<Effect>();
		for(Effect e : other.conditions) {
			this.conditions.add(e);
		}
		
		this.waitMeter = other.waitMeter;
		this.waitCapacity = other.waitCapacity;
		this.chargeMeter = other.chargeMeter;
		this.selectedAction = other.selectedAction;
		this.setCurrentStage(other.getCurrentStage());
		this.actions = new IDableCollection<String, Action>();
		for(Action a : other.actions) {
			this.actions.add(a);
		}
		
		this.battleWaitMeterFilledSubscribers = other.battleWaitMeterFilledSubscribers;
		this.battleChargeMeterFilledSubscribers = other.battleChargeMeterFilledSubscribers;
		this.battleComponentActionFinishedSubscribers = other.battleComponentActionFinishedSubscribers;
	}
	@Override
	public Component clone() {
		return new BattleComponent(this);
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
		this.actions = new IDableCollection<String, Action>();
		this.actions.add(new Action("Attack", 1000, "fun") {
			public void apply(Entity source, List<Entity> targets) {
				targets.get(0).getComponent(BattleComponent.class).damageHealth(10);
			}
		});
	}
	
	// Main loop methods
	@Override
	public void update(int delta, Input input) {
		switch(this.getCurrentStage()) {
		case WAITING:
			boolean waitMeterWasFull = this.getWaitMeterIsFull();
			this.increaseWaitMeter(delta);
			if(!waitMeterWasFull && this.getWaitMeterIsFull()) {
				this.setCurrentStage(Stage.DECIDING);
				this.reportWaitMeterFilled();
			}
			break;
		case DECIDING:
			break;
		case CHARGING:
			this.increaseChargeMeter(delta);
			if(this.getChargeMeterIsFull()) {
				this.setCurrentStage(Stage.ACTING);
				this.reportChargeMeterFilled();
			}
			break;
		case ACTING:
			break;
		}
	}
	
	// Getters and setters
	public int getMaxHealth() {
		return this.maxHealth;
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
	
	public int getWaitMeter() {
		return this.waitMeter;
	}
	public int getWaitMeterCapacity() {
		return this.waitCapacity;
	}
	public boolean getWaitMeterIsFull() {
		return this.waitMeter >= this.waitCapacity;
	}
	public void setWaitMeter(int amount) {
		this.waitMeter = amount;
	}
	public void increaseWaitMeter(int amount) {
		this.waitMeter += amount;
	}
	public int getChargeMeter() {
		return this.chargeMeter;
	}
	public int getChargeMeterCapacity() {
		return this.selectedAction == null ? 0 : this.selectedAction.getAction().getChargeTime();
	}
	public boolean getChargeMeterIsFull() {
		return this.selectedAction != null && this.chargeMeter >= this.selectedAction.getAction().getChargeTime();
	}
	public String getChargeName() {
		return this.selectedAction == null ? "Charging" : this.selectedAction.getAction().getChargeName();
	}
	public void setChargeMeter(int amount) {
		this.chargeMeter = amount;
	}
	public void increaseChargeMeter(int amount) {
		this.chargeMeter += amount;
	}
	public Stage getCurrentStage() {
		if(this.waitMeter < this.waitCapacity) {
			return Stage.WAITING;
		} else if(this.chargeMeter == 0) {
			return Stage.DECIDING;
		} else if(this.chargeMeter < this.selectedAction.getAction().getChargeTime()) {
			return Stage.CHARGING;
		} else {
			return Stage.ACTING;
		}
	}
	public void setCurrentStage(Stage stage) {
		switch(stage) {
		case WAITING:
			this.waitMeter = 0;
			this.chargeMeter = 0;
			this.selectedAction = null;
			break;
		case DECIDING:
			break;
		case CHARGING:
			this.waitMeter = this.waitCapacity;
			this.chargeMeter = 0;
			break;
		case ACTING:
			break;
		}
	}
	public Action getAction(String name) {
		return this.actions.get(name);
	}
	public IDableCollection<String, Action> getActions() {
		return this.actions;
	}
	// TODO Abstract BattleComponent, then make Enemy and Player versions
	// Effects
	protected void restoreAll() {
		this.setCurrentStage(Stage.WAITING);
		this.currentHealth = this.maxHealth;
		this.buffs.clear();
		this.conditions.clear();
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
	
	// Battle interface
	public ActionMenu getBattleMenu(Battle battle) {
		ActionMenu menu = new ActionMenu(this.getContainingEntity(), battle.getEnemies());
		
		// Attack
		menu.addMenuItem(new ActionMenuItem(this.getActions().get("Attack")));
		
		// Specials
		ActionMenu specialsMenu = new ActionMenu(this.getContainingEntity(), battle.getEnemies());
		for(Action a : this.getActions()) {
			specialsMenu.addMenuItem(new MenuItem(a.getName()));
		}
		menu.addMenuItem(new MenuItem(("Specials"), specialsMenu));
		
		this.subscribeTo(menu);
		return menu;
	}
	private void selectAction(ActionMenu menu) {
		this.selectedAction = menu.getSelectedActionInstance();
		this.chargeMeter = 0;
	}
	public void executeAction() {
		Action action = this.selectedAction.getAction();
		ActionInstance actionInstance = this.selectedAction;
		
		List<Movement> movements = action.getMovements(actionInstance.getSource(), actionInstance.getTargets());
		this.finalMovement = movements.get(movements.size() - 1);
		getComponent(MovementComponent.class).move(movements);
		actionInstance.apply();
	}
	
	// Events
	private List<BattleWaitMeterFilledSubscriber> battleWaitMeterFilledSubscribers;
	private List<BattleChargeMeterFilledSubscriber> battleChargeMeterFilledSubscribers;
	private List<BattleComponentActionFinishedSubscriber> battleComponentActionFinishedSubscribers;
	public void addBattleWaitMeterFilledSubscriber(BattleWaitMeterFilledSubscriber subscriber) {
		this.battleWaitMeterFilledSubscribers.add(subscriber);
	}
	public void addBattleChargeMeterFilledSubscriber(BattleChargeMeterFilledSubscriber subscriber) {
		this.battleChargeMeterFilledSubscribers.add(subscriber);
	}
	public void addBattleComponentActionFinishedSubscriber(BattleComponentActionFinishedSubscriber subscriber) {
		this.battleComponentActionFinishedSubscribers.add(subscriber);
	}
	private void reportWaitMeterFilled() {
		for(BattleWaitMeterFilledSubscriber subscriber : this.battleWaitMeterFilledSubscribers) {
			subscriber.reportedBattleWaitMeterFilled(this.getContainingEntity());
		}
	}
	private void reportChargeMeterFilled() {
		for(BattleChargeMeterFilledSubscriber subscriber : this.battleChargeMeterFilledSubscribers) {
			subscriber.reportedBattleChargeMeterFilled(this.getContainingEntity());
		}
	}
	private void reportBattleComponentActionFinished(Action action) {
		for(BattleComponentActionFinishedSubscriber subscriber : this.battleComponentActionFinishedSubscribers) {
			subscriber.reportedBattleComponentActionFinished(this, action);
		}
	}
	
	@Override
	public void subscribeTo(Animation reporter) {
		reporter.addFinishedSubscriber(this);
	}
	@Override
	public void subscribeTo(AbstractLinearMenu reporter) {
		reporter.addMenuCloseSubscriber(this);
	}
	@Override
	public void subscribeTo(MovementComponent reporter) {
		reporter.addFinishedSubscriber(this);
	}
	@Override
	public void reportedAnimationFinished(Animation source) {
		if(this.getCurrentStage().equals(Stage.ACTING)) {
			this.setCurrentStage(Stage.WAITING);
		}
	}
	@Override
	public void reportedMenuClose(AbstractLinearMenu source, MenuItem selected) {
		this.setCurrentStage(Stage.CHARGING);
		this.selectAction((ActionMenu)source);
	}
	@Override
	public void reportedMovementComponentFinished(MovementComponent source, Movement finished) {
		if(this.finalMovement == finished) {
			ActionInstance lastAction = this.selectedAction;
			this.selectedAction = null;
			this.finalMovement = null;
			this.reportBattleComponentActionFinished(lastAction.getAction());
		}
	}
	
	// Utilities
	@Override
	public String toString() {
		return this.getClass().getSimpleName() + ": " + "health=" + this.currentHealth + "/" + this.maxHealth + "; wait=" + this.waitMeter + "/" + this.waitCapacity + "; " + this.getChargeName() + "=" + this.chargeMeter + "/" + this.getChargeMeterCapacity();
	}
	
	// Stage
	public enum Stage {
		WAITING,
		DECIDING,
		CHARGING,
		ACTING
	}
}
