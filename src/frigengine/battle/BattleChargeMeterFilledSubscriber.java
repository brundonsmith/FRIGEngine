package frigengine.battle;

import frigengine.core.component.*;

public interface  BattleChargeMeterFilledSubscriber {
	public void subscribeTo(BattleComponent reporter);
	public void reportedBattleChargeMeterFilled(Entity source);
}
