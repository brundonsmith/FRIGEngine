package frigengine.battle;

import java.util.Deque;

import org.newdawn.slick.Input;

import frigengine.AnimationFinishedListener;
import frigengine.FRIGAnimation;
import frigengine.entities.*;
import frigengine.gui.AbstractLinearMenu;
import frigengine.gui.GUIFrame;
import frigengine.gui.MenuItem;
import frigengine.gui.MenuSelectListener;
import frigengine.scene.Scene;
import frigengine.scene.SceneLayer;
import frigengine.util.IDableCollection;

public class Battle extends Scene implements StageChangeListener, MenuSelectListener, AnimationFinishedListener {
	// Attributes
	private IDableCollection<String, Entity> playerParty;
	private IDableCollection<String, Entity> enemies;
	private Deque<BattleComponent> decisionQueue;
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
			if (frame.getBlocksTime())
				timeBlocked = true;
			if (frame.getBlocksInput())
				inputBlocked = true;
		}

		// Layers
		for (SceneLayer layer : this.layers)
			layer.update(timeBlocked ? 0 : delta, inputBlocked ? null : input, this);

		// Entities
		for (Entity entity : this.entities)
			entity.update(timeBlocked ? 0 : delta, inputBlocked ? null : input, this);
	}
	
	// Events
	@Override
	public void stageChanged(BattleComponent source) {
		switch(source.getCurrentStage()) {
		case DECIDING:
			this.decisionQueue.add(source);
			break;
		case WAITING:
			if(!this.actionQueue.isEmpty()) {
				this.actionQueue.peek().execute();
				this.actionQueue.peek().getSource().animateAction(this.actionQueue.peek().getAction().getID());
			}
			else if(!this.decisionQueue.isEmpty()) {
				this.openGUI(new BattleMenu(this, decisionQueue.peek()));
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
	public void itemSelected(AbstractLinearMenu source, MenuItem selected) {
		//if(source instanceof BattleMenu)
			//this.actionQueue.add(((BattleMenu) source).getSelectedAction().g);
	}
	@Override
	public void animationFinished(FRIGAnimation source) {
		
	}
}
