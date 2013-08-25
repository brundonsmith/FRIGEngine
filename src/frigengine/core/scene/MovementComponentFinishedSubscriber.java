package frigengine.core.scene;

public interface MovementComponentFinishedSubscriber {
	public void subscribeTo(MovementComponent reporter);
	public void reportedMovementComponentFinished(MovementComponent source, Movement finished);
}
