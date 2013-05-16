package frigengine.battle;

import frigengine.entities.BattleComponent;
import frigengine.gui.BasicLinearMenu;
import frigengine.gui.MenuItem;
import frigengine.gui.MenuSelectListener;

public class BattleMenu extends BasicLinearMenu implements MenuSelectListener {
	// Attributes
	private BattleComponent source;
	
	// Constructors and initialization
	public BattleMenu(Battle battle, BattleComponent source) {	
		super(battle);
		
		// source
		this.source = source;
		
		// Attack
		this.addItem(new MenuItem(source.getAction("attack").getName()));
		
		// Specials
		BasicLinearMenu specialsMenu = new BasicLinearMenu(this.context);
		for(Action a : source.getActions()) {
			specialsMenu.addItem(new MenuItem(a.getName()));
		}
		this.addSubMenu(new MenuItem("Specials"), specialsMenu);
	}

	// Getters and setters
	public Action getSelectedAction() {
		return this.source.getAction(this.getSelection().getLabel());
	}
}
