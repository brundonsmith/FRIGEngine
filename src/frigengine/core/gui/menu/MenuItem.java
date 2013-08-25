package frigengine.core.gui.menu;

import org.newdawn.slick.Graphics;

import frigengine.core.FRIGGame;
import frigengine.core.geom.*;
import frigengine.core.scene.*;

public class MenuItem {
	// Attributes
	private String label;
	protected AbstractLinearMenu subMenu;

	// Constructors and initialization
	public MenuItem(String label) {
		this.label = label;
		this.subMenu = null;
	}
	public MenuItem(String label, AbstractLinearMenu subMenu) {
		this.label = label;
		this.subMenu = subMenu;
	}
	
	// Main loop methods
	public void render(Graphics g, Scene scene, Rectangle domain) {
		scene.renderStringBoxForeground(g, this.label, domain, FRIGGame.getDefaultFont());
	}

	// Getters and setters
	public final String getLabel() {
		return this.label;
	}
	public AbstractLinearMenu getSubMenu() {
		return this.subMenu;
	}

	// Utilities
	@Override
	public boolean equals(Object other) {
		return other != null && other instanceof MenuItem &&
				this.label.equals(((MenuItem) other).label);
	}
	@Override
	public String toString() {
		return this.label;
	}
}
