package frigengine.battle;

import frigengine.battle.actions.*;

public interface BattleComponentActionFinishedSubscriber {
	public void subscribeTo(BattleComponent reporter);
	public void reportedBattleComponentActionFinished(BattleComponent source, Action finished);
}
