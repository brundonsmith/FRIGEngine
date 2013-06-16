package frigengine.gui;

import java.util.ArrayList;

import org.lwjgl.input.Keyboard;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

import frigengine.FRIGGame;
import frigengine.Scene;
import frigengine.util.geom.Rectangle;

public class SpeechDialog extends AbstractDialog {

	// Constructors and initialization
	public SpeechDialog(String content) {
		super(content);
		
		this.presence = new Rectangle(
				0,
				FRIGGame.getInstance().getScreenHeight() * 5 / 6,
				FRIGGame.getInstance().getScreenWidth(),
				FRIGGame.getInstance().getScreenHeight() / 6
				);
		
		this.borderHorizontal = 100;
		
		this.blocksTime = false;
		this.blocksInput = true;
		
		this.setContent(content);
	}
	public SpeechDialog(ArrayList<String> pages) {
		super(pages);

		this.presence = new Rectangle(
				0,
				FRIGGame.getInstance().getScreenHeight() * 5/ 6,
				FRIGGame.getInstance().getScreenWidth(),
				FRIGGame.getInstance().getScreenHeight() / 6
				);

		this.borderHorizontal = 100;
		
		this.blocksTime = false;
		this.blocksInput = true;
		
		this.setPages(pages);
	}

	// main loop methods
	@Override
	public void update(int delta, Input input) {
		if(input != null && input.isKeyPressed(Keyboard.KEY_RETURN)) {
			this.iterate();
		}
	}
	@Override
	public void render(Graphics g, Scene scene) {
		scene.renderObjectForeground(g, this.background, this.getPresence());
		scene.renderStringBoxForeground(g, this.getCurrentPage(), this.getBorderedPresence(), this.font);
	}
	
	// Events
	public void guiClosed(GUIFrame source, MenuItem selection) {
	}
}
