package frigengine.battle;

import java.util.Deque;

import org.newdawn.slick.Input;

import frigengine.Scene;
import frigengine.SceneLayer;
import frigengine.entities.*;
import frigengine.events.AnimationFinishedListener;
import frigengine.events.BattleableMeterFilledListener;
import frigengine.events.GUICloseListener;
import frigengine.gui.GUIFrame;
import frigengine.gui.MenuItem;
import frigengine.util.IDableCollection;
import frigengine.util.graphics.Animation;

public class Battle extends Scene implements BattleableMeterFilledListener, GUICloseListener, AnimationFinishedListener {
	// Attributes
	private IDableCollection<String, Entity> playerParty;
	private IDableCollection<String, Entity> enemies;
	private Deque<String> decisionQueue;
	private Deque<ActionInstance> actionQueue;

	// Constructors and initialization
	public Battle() {
		super("battle");
	}
	public static Battle from(BattleTemplate battleTemplate) {
		return null;
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

		// Layers
		for (SceneLayer layer : this.layers) {
			layer.update(timeBlocked ? 0 : delta);
		}

		// Entities
		for (Entity entity : this.entities) {
			if(entity.is(Battleable.class)) {
				((Battleable)entity.as(Battleable.class)).update(timeBlocked ? 0 : delta, this.getChunks(Battleable.class));
			}
			if(entity.is(Drawable.class)) {
				((Drawable)entity.as(Drawable.class)).update(timeBlocked ? 0 : delta);
			}
		}
		
		// Open decision menu
	}
	
	// Events
	@Override
	public void battleableMeterFilled(Battleable source) {
		switch(source.getCurrentStage()) {
		case DECIDING:
			this.decisionQueue.add(source.getEntityId());
			break;
		case WAITING:
			if(!this.actionQueue.isEmpty()) {
				//this.actionQueue.peek().execute();
				this.actionQueue.peek().getSource().animateAction(this.actionQueue.peek().getAction().getId());
			} else if(!this.decisionQueue.isEmpty()) {
				this.openGUI(new BattleMenu((Battleable)this.playerParty.get(decisionQueue.peek()).as(Battleable.class)));
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
