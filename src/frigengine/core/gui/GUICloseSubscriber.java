package frigengine.core.gui;

public interface GUICloseSubscriber {
	public void subscribeTo(GUIFrame reporter);
	public void reportedGuiClosed(GUIFrame source);
}
