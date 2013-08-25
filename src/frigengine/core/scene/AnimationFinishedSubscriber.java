package frigengine.core.scene;

public interface AnimationFinishedSubscriber {
	public void subscribeTo(Animation reporter);
	public void reportedAnimationFinished(Animation source);
}
