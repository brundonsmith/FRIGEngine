package frigengine.battle.gui;

import org.lwjgl.input.Keyboard;
import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Rectangle;

import frigengine.battle.*;
import frigengine.battle.actions.*;

import frigengine.core.*;
import frigengine.core.gui.*;

public class BattleMenu extends RecursiveLinearMenu {
	// Attributes
	private BattleComponent source;
	
	// Constructors and initialization
	public BattleMenu(BattleComponent source) {	
		super(
				new Rectangle(
					0,
					FRIGGame.getScreenHeight() * 4 / 5,
					FRIGGame.getScreenWidth(),
					FRIGGame.getScreenHeight() / 5
				));
		
		// source
		this.source = source;
		
		// Attack
		this.addMenuItem(new MenuItem(source.getActions().get("attack").getName()));
		
		// Specials
		BasicLinearMenu specialsMenu = new BasicLinearMenu(new Rectangle(this.presence.getX(), this.presence.getY(), this.presence.getWidth() / 4, this.presence.getHeight()));
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
