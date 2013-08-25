package frigengine.core.scene;

public interface MovementFinishedSubscriber {
	public void subscribeTo(Movement reporter);
	public void reportedMovementFinished(Movement source);
}
