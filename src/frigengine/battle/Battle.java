package frigengine.battle;

import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

import org.newdawn.slick.Input;

import frigengine.battle.actions.ActionInstance;
import frigengine.battle.gui.BattleMenu;
import frigengine.core.AnimationFinishedListener;
import frigengine.core.component.*;
import frigengine.core.gui.*;
import frigengine.core.scene.*;


public class Battle extends Scene implements BattleableMeterFilledListener, GUICloseListener, AnimationFinishedListener {
	// Attributes
	private List<Entity> playerParty;
	private List<Entity> enemies;
	private Deque<Entity> decisionQueue;
	private Deque<ActionInstance> actionQueue;

	// Constructors and initialization
	public Battle() {
		super("battle");
		this.decisionQueue = new LinkedList<Entity>();
		this.actionQueue = new LinkedList<ActionInstance>();
	}
	public  void init(BattleTemplate battleTemplate) {
		this.enemies = battleTemplate.enemies;
		this.layers = battleTemplate.layers;
		
		for(Entity e : this.enemies) {
			e.getComponent(BattleComponent.class).resetAll();
		}
	}

	// Main loop methods
	@Override
	public void update(int delta, Input input) {
		boolean timeBlocked = false;
		boolean inputBlocked = false;
		
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
			entity.update(timeBlocked ? 0 : delta, inputBlocked ? null : input);
		}

		// Layers
		for (SceneLayer layer : this.layers) {
			layer.update(timeBlocked ? 0 : delta);
		}
		
		// Open decision menu
	}
	
	// Events
	@Override
	public void battleableMeterFilled(Entity source) {
		switch(source.getComponent(BattleComponent.class).getCurrentStage()) {
		case DECIDING:
			this.decisionQueue.add(source);
			break;
		case ACTING:
			if(!this.actionQueue.isEmpty()) {
				//this.actionQueue.peek().execute();
				this.actionQueue.peek().getSource().getComponent(SpriteComponent.class).playAnimation(this.actionQueue.peek().getAction().getId());
			} else if(!this.decisionQueue.isEmpty()) {
				this.openGUI(new BattleMenu(decisionQueue.peek().getComponent(BattleComponent.class)));
				this.decisionQueue.remove();
			}
			break;
		default:
			break;
		}
		/*
		 * when wait meter fills,
			     if playerParty:
			          add entity to decision queue, open gui menu for top of stack
			     else (enemy)
			          tell enemy to choose action and return it
			when charge meter fills,
			     pause updating
			     play animation
			     apply effects to target
		 */
		
	}
	@Override
	public void guiClosed(GUIFrame source, MenuItem selected) {
		//if(source instanceof BattleMenu)
			//this.actionQueue.add(((BattleMenu) source).getSelectedAction().g);
	}
	@Override
	public void animationFinished(Animation source) {
		
	}
}
