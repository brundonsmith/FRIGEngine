package frigengine.battle;

import org.lwjgl.input.Keyboard;
import org.newdawn.slick.Input;

import frigengine.entities.Battleable;
import frigengine.gui.BasicLinearMenu;
import frigengine.gui.RecursiveLinearMenu;
import frigengine.gui.MenuItem;

public class BattleMenu extends RecursiveLinearMenu {
	// Attributes
	private Battleable source;
	
	// Constructors and initialization
	public BattleMenu(Battleable source) {	
		// source
		this.source = source;
		
		// Attack
		this.addMenuItem(new MenuItem(source.getActions().get("attack").getName()));
		
		// Specials
		BasicLinearMenu specialsMenu = new BasicLinearMenu();
		for(Action a : source.getActions()) {
			specialsMenu.addMenuItem(new MenuItem(a.getName()));
		}
		this.addSubMenu(new MenuItem("Specials"), specialsMenu);
	}

	// Main loop methods
	@Override
	public void update(int delta, Input input) {
		if (input != null && input.isKeyPressed(Keyboard.KEY_DOWN)) {
			this.forward();
		} else if (input != null && input.isKeyPressed(Keyboard.KEY_UP)) {
			this.back();
		}
		
		if(input != null && input.isKeyPressed(Keyboard.KEY_RETURN)) {
			this.select();
		}
	}

	// Getters and setters
	public Action getSelectedAction() {
		return this.source.getActions().get(this.getSelection().getLabel());
	}
	
	// TODO Need an enemy selection menu
}
