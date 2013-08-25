package frigengine.core.gui.menu;


public interface MenuCloseSubscriber {
	public void subscribeTo(AbstractLinearMenu reporter);
	public void reportedMenuClose(AbstractLinearMenu source, MenuItem selection);
}
