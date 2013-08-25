package frigengine.battle;

import frigengine.core.component.*;

public interface  BattleWaitMeterFilledSubscriber {
	public void subscribeTo(BattleComponent reporter);
	public void reportedBattleWaitMeterFilled(Entity source);
}
