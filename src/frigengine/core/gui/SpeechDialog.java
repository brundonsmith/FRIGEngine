package frigengine.core.gui;

import java.util.ArrayList;

import org.lwjgl.input.Keyboard;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Rectangle;

import frigengine.core.*;
import frigengine.core.scene.*;

public class SpeechDialog extends AbstractSpeechDialog {
	// Attributes
	private int iterateKey;

	// Constructors and initialization
	public SpeechDialog(String content) {
		super(
				new Rectangle(
					0,
					FRIGGame.getScreenHeight() * 5 / 6,
					FRIGGame.getScreenWidth(),
					FRIGGame.getScreenHeight() / 6
				),
				content);
		
		// horizontalMargin
		this.horizontalMargin = 100;
		
		// blocksTime
		this.blocksTime = false;
		
		// blocksInput
		this.blocksInput = true;
		
		// content
		this.setContent(content);
		
		// iterateKey
		this.iterateKey = Keyboard.KEY_RETURN;
	}
	public SpeechDialog(ArrayList<String> pages) {
		super(null, pages);

		// presence
		this.presence = new Rectangle(
				0,
				FRIGGame.getScreenHeight() * 5/ 6,
				FRIGGame.getScreenWidth(),
				FRIGGame.getScreenHeight() / 6
				);

		// horizontalMargin
		this.horizontalMargin = 100;
		
		// blocksTime
		this.blocksTime = false;
		
		// blocksInput
		this.blocksInput = true;
		
		// content
		this.setPages(pages);
		
		// iterateKey
		this.iterateKey = Keyboard.KEY_RETURN;
	}

	// main loop methods
	@Override
	public void update(int delta, Input input) {
		if(input != null && input.isKeyPressed(this.iterateKey)) {
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
