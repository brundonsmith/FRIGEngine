package frigengine.entities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import frigengine.battle.Action;
import frigengine.battle.Effect;
import frigengine.entities.BattleData.Stage;
import frigengine.events.AnimationFinishedListener;
import frigengine.util.IDableCollection;
import frigengine.util.graphics.Animation;

public class Battleable extends Category implements AnimationFinishedListener {
	// Constants
	@Override
	public final Collection<Class<? extends Component>> getRequiredComponents() {
		return new ArrayList<Class<? extends Component>>(Arrays.asList(
				GraphicsData.class,
				BattleData.class
			));
	}

	// Access
	protected PositionData posData() {
		return (PositionData)this.components.get(PositionData.class);
	}
	protected GraphicsData grphcsData() {
		return (GraphicsData)this.components.get(GraphicsData.class);
	}
	protected BattleData btlData() {
		return (BattleData)this.components.get(BattleData.class);
	}

	// Main loop methods
	public void update(int delta, IDableCollection<String, Chunk> otherObjects) {
		switch(this.getCurrentStage()) {
		case WAITING:
			this.btlData().increaseWaitMeter(delta);
			if(this.btlData().getWaitMeterIsFull()) {
				this.setCurrentStage(Stage.DECIDING);
			}
			break;
		case DECIDING:
			break;
		case CHARGING:
			this.btlData().increaseChargeMeter(delta);
			if(this.btlData().getChargeMeterIsFull()) {
				this.setCurrentStage(Stage.ACTING);
			}
			break;
		case ACTING:
			break;
		}
	}
	
	// Getters and setters
	public int getHealth() {
		return this.btlData().getHealth();
	}
	public int getMagic() {
		return this.btlData().getMagic();
	}
	public int getCurrentAttack() {
		return this.btlData().getCurrentAttack();
	}
	public int getCurrentDefense() {
		return this.btlData().getCurrentDefense();
	}
	public int getCurrentSpeed() {
		return this.btlData().getCurrentSpeed();
	}
	public int getWaitMeter() {
		return this.btlData().getWaitMeter();
	}
	public int getChargeMeter() {
		return this.btlData().getChargeMeter();
	}
	public Stage getCurrentStage() {
		return this.btlData().getCurrentStage();
	}
	public void setCurrentStage(Stage stage) {
		this.btlData().setCurrentStage(stage);
	}
	public IDableCollection<String, Action> getActions() {
		return this.btlData().getActions();
	}
	
	// Operations
	public void damageHealth(int amount) {
		this.btlData().damageHealth(amount);
	}
	public void restoreHealth(int amount) {
		this.btlData().restoreHealth(amount);
	}
	public void damageMagic(int amount) {
		this.btlData().damageMagic(amount);
	}
	public void restoreMagic(int amount) {
		this.btlData().restoreMagic(amount);
	}
	public void applyStatsEffect(Effect effect) {
		this.btlData().applyStatsEffect(effect);
	}
	public void animateAction(String actionId) {
		this.grphcsData().setActiveAnimation("action_" + actionId);
	}

	
	// Events
	@Override
	public void animationFinished(Animation source) {
		if(this.getCurrentStage().equals(Stage.ACTING)) {
			this.setCurrentStage(Stage.WAITING);
		}
	}
}
