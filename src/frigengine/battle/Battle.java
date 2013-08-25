package frigengine.battle;

import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Point;
import org.newdawn.slick.geom.Vector2f;

import frigengine.battle.actions.*;
import frigengine.battle.gui.*;
import frigengine.core.component.*;
import frigengine.core.gui.*;
import frigengine.core.gui.menu.AbstractLinearMenu;
import frigengine.core.gui.menu.MenuCloseSubscriber;
import frigengine.core.gui.menu.MenuItem;
import frigengine.core.scene.*;

public class Battle extends Scene implements BattleWaitMeterFilledSubscriber, BattleChargeMeterFilledSubscriber, BattleComponentActionFinishedSubscriber, MenuCloseSubscriber {
	// Attributes
	private List<Entity> playerParty;
	private List<Entity> enemies;
	private Deque<Entity> decisionQueue;
	private boolean menuOpen;
	private Deque<Entity> actionQueue;
	private boolean entityActing;
	private HashMap<Entity,Scene> fieldLocations;

	// Constructors and initialization
	public Battle() {
		super("battle");
		this.currentCamera = "battlecam";
		this.playerParty = new ArrayList<Entity>();
		this.enemies = new ArrayList<Entity>();
		this.decisionQueue = new LinkedList<Entity>();
		this.menuOpen = false;
		this.actionQueue = new LinkedList<Entity>();
		this.entityActing = false;
		this.fieldLocations = new HashMap<Entity,Scene>();
	}
	public void init(BattleTemplate battleTemplate) {
		this.width = 400;
		this.height = 250;
		
		
		this.playerParty.add(Entity.getEntity("george"));
		
		this.enemies = battleTemplate.enemies;
		
		this.layers = battleTemplate.layers;
	}
	@Override
	public void onGainFocus(Scene previousScene) {
		this.addEntity("battlecam");
		Entity.getEntity("battlecam").getComponent(CameraComponent.class).zoomFitScene();
		Entity.getEntity("battlecam").getComponent(CameraComponent.class).setCenter(-1 * this.width / 2, this.height / 2);
		Entity.getEntity("battlecam").getComponent(MovementComponent.class).move(new Movement(1500, Movement.getLinearPath(new Vector2f(this.width, 0)), "", Movement.MoveFunction.EASE_OUT));
		
		// Position and set up player party
		for(int i = 0; i < this.playerParty.size(); i++) {
			Entity e = this.playerParty.get(i);
			this.borrowEntity(e);
			
			if(e.hasComponent(MovementComponent.class)) {
				e.getComponent(MovementComponent.class).stop();
			}
			
			e.getComponent(BattleComponent.class).restoreAll();
			this.subscribeTo(e.getComponent(BattleComponent.class));
			
			e.getComponent(PositionComponent.class).setX(this.width - 100);
			e.getComponent(PositionComponent.class).setY((this.height / (this.playerParty.size()+1)) * (i+1));
		}
		
		// Position and set up enemies
		for(int i = 0; i < this.enemies.size(); i++) {
			Entity e = this.enemies.get(i);
			this.borrowEntity(e);
			
			if(e.hasComponent(MovementComponent.class)) {
				e.getComponent(MovementComponent.class).stop();
			}
			
			e.getComponent(BattleComponent.class).restoreAll();
			this.subscribeTo(e.getComponent(BattleComponent.class));
			
			e.getComponent(PositionComponent.class).setX(100);
			e.getComponent(PositionComponent.class).setY(this.height / (this.enemies.size()+1) * (i+1));
		}
		
		// GUI
		this.openGUI(new BattleMeterDisplay(this.playerParty));
	}
	@Override
	public void onLoseFocus(Scene newScene) {
		this.returnEntities();
	}

	// Main loop methods
	@Override
	public void update(int delta, Input input) {
		boolean timeBlocked = false;
		boolean inputBlocked = false;
		// TODO see if we can come up with a better system for time/input blocking
		// GUI
		for (Object o : this.guiStack.toArray()) {
			GUIFrame frame = (GUIFrame) o;
			frame.update(timeBlocked ? 0 : delta, inputBlocked ? null : input);
			if (frame.getBlocksTime()) {
				timeBlocked = true;
			}
			if (frame.getBlocksInput()) {
				inputBlocked = true;
			}
		}

		// Entities
		for (Entity entity : Entity.getEntities(this)) {
			entity.update(timeBlocked ? 0 : delta, null);
		}
		
		// Layers
		for (SceneLayer layer : this.layers) {
			layer.update(timeBlocked ? 0 : delta);
		}

		// Open action menu
		if(!this.menuOpen && !this.decisionQueue.isEmpty()) {
			ActionMenu newMenu = this.decisionQueue.removeFirst().getComponent(BattleComponent.class).getBattleMenu(this);
			this.subscribeTo(newMenu);
			this.openGUI(newMenu);
			this.menuOpen = true;
		}
		
		// Activate action
		if(!this.entityActing && !this.actionQueue.isEmpty()) {
			Entity acting = this.actionQueue.removeFirst();
			this.subscribeTo(acting.getComponent(BattleComponent.class));
			acting.getComponent(BattleComponent.class).executeAction();
		}
	}
	
	// Getters and setters
	public List<Entity> getEnemies() {
		return this.enemies;
	}
	
	// Operations
	private void borrowEntity(String entityId) {
		Entity entity = Entity.getEntity(entityId);
		this.fieldLocations.put(entity, entity.getScene());
		this.addEntity(entity);
	}
	private void borrowEntity(Entity entity) {
		this.fieldLocations.put(entity, entity.getScene());
		this.addEntity(entity);
	}
	private void returnEntities() {
		for(Entity e : this.fieldLocations.keySet()) {
			this.fieldLocations.get(e).addEntity(e);
		}
	}
	
	// Events
	@Override
	public void subscribeTo(BattleComponent reporter) {
		reporter.addBattleWaitMeterFilledSubscriber(this);
		reporter.addBattleChargeMeterFilledSubscriber(this);
		reporter.addBattleComponentActionFinishedSubscriber(this);
	}
	@Override
	public void subscribeTo(AbstractLinearMenu reporter) {
		reporter.addMenuCloseSubscriber(this);
	}
	@Override
	public void reportedBattleWaitMeterFilled(Entity source) {
		this.decisionQueue.addLast(source);
	}
	@Override
	public void reportedBattleChargeMeterFilled(Entity source) {
		this.actionQueue.addLast(source);
	}
	@Override
	public void reportedBattleComponentActionFinished(BattleComponent source, Action finished) {
		this.entityActing = false;
	}
	@Override
	public void reportedMenuClose(AbstractLinearMenu source, MenuItem selected) {
		this.menuOpen = false;
	}
}
