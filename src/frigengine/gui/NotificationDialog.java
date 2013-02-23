package frigengine.gui;

public class NotificationDialog extends GUIFrame {
	// Attributes
	private static Texture2D background;
	private String content;

	// Constructors and initialization
	public NotificationDialog(String content) {
		Blocking = true;
		Pausing = true;
		presence = new ShapeRectangle(FRIGGame.Instance.GraphicsDevice.Viewport.Bounds.Width / 2
				- FRIGGame.Instance.GraphicsDevice.Viewport.Bounds.Width / 8,
				FRIGGame.Instance.GraphicsDevice.Viewport.Bounds.Height / 2
						- FRIGGame.Instance.GraphicsDevice.Viewport.Bounds.Height / 8,
				FRIGGame.Instance.GraphicsDevice.Viewport.Bounds.Width / 4,
				FRIGGame.Instance.GraphicsDevice.Viewport.Bounds.Height / 4);

		this.content = content;
	}

	public/* override */void loadContent() {
		if (background == null)
			background = Texture2D.FromStream(FRIGGame.Instance.GraphicsDevice, new FileStream(
					"content\\gui\\NotificationDialog.png", FileMode.Open));
	}

	// Main loop methods
	public/* override */void update(GameTime gameTime, KeyboardState keyboardState) {
		super.update(gameTime, keyboardState);

		/*
		 * if (Keyboard.GetState().GetPressedKeys().Contains(Keys.W))
		 * area.Height--; if
		 * (Keyboard.GetState().GetPressedKeys().Contains(Keys.A)) area.Width--;
		 * if (Keyboard.GetState().GetPressedKeys().Contains(Keys.S))
		 * area.Height++; if
		 * (Keyboard.GetState().GetPressedKeys().Contains(Keys.D)) area.Width++;
		 */

		if (this.getCurrentKeyboardState().GetPressedKeys().Contains(Keys.Enter))
			close();
	}

	public/* override */void draw() {
		FRIGGame.Instance.getSpriteBatch().Draw(background, presence.getRectangle(), Color.White);
		FRIGGame.Instance.getSpriteBatch().DrawTextBox(FRIGGame.Instance.getGameFont(), content,
				getborderedArea().Clone(), Color.White);
	}
}
