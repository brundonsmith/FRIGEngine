package frigengine.battle.gui;

import org.newdawn.slick.Graphics;

import frigengine.core.component.*;
import frigengine.core.geom.*;
import frigengine.core.gui.menu.*;
import frigengine.core.scene.*;

public class EnemySelectionMenuItem extends MenuItem {
	// Attributes
	private Entity enemy;
	
	// Constructors and initialization
	public EnemySelectionMenuItem(Entity enemy) {
		super(enemy.getName());
		
		this.enemy = enemy;
	}
	
	// Main loop methods
	@Override
	public void render(Graphics g, Scene scene, Rectangle domain) {
		
	}
	
	// Getters and setters
	public Entity getEnemy() {
		return this.enemy;
	}
}
