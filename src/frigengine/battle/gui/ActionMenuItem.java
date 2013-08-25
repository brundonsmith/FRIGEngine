package frigengine.battle.gui;


import frigengine.battle.actions.*;
import frigengine.core.gui.menu.*;

public class ActionMenuItem extends MenuItem {
	// Constructors and initialization
	public ActionMenuItem(Action action) {
		super(action.getName());
	}
	
	// Getters and setters
	/*package*/ void setEnemySelectionMenu(EnemySelectionMenu enemySelectionMenu) {
		this.subMenu = enemySelectionMenu;
	}
}
