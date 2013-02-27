package frigengine.gui;

import java.util.ArrayList;
import java.util.Collection;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

import frigengine.FRIGGame;

public class MenuDialog extends GUIFrame {
	// Attributes
	private static Texture2D background;
	private int columns;
	private int rows;
	private ArrayList<MenuItem> items;

	// Constructors and initialization
	public MenuDialog(int columns, int rows) {
		this.columns = columns;
		this.rows = rows;
		items = new ArrayList<MenuItem>();
	}

	public MenuDialog(int columns, int rows, MenuItem[] items) {
		this.columns = columns;
		this.rows = rows;
		this.items = new ArrayList<MenuItem>();
		for (MenuItem item : items) {
			this.items.add(item);
		}
	}

	public void loadContent() {
		if (background == null)
			background = Texture2D.FromStream(FRIGGame.Instance.GraphicsDevice,
					new FileStream("content\\gui\\NotificationDialog.png",
							FileMode.Open));
	}

	// Main loop methods
	public/* override */void update(GameTime gameTime, KeyboardState keyboardState) {
		super.update(gameTime, keyboardState);

	}

	public/* override */void draw() {
	}

	@Override
	public void update(GameContainer container, int delta, Input input) {
		// TODO Auto-generated method stub

	}

	@Override
	public void render(GameContainer container, Graphics g) {
		// TODO Auto-generated method stub

	}
}
