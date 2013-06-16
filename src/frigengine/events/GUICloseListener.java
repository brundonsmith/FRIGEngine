package frigengine.events;

import frigengine.gui.GUIFrame;
import frigengine.gui.MenuItem;

public interface GUICloseListener {
	void guiClosed(GUIFrame source, MenuItem selection);
}
