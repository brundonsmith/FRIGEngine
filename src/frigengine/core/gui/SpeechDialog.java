package frigengine.core.gui;

import java.util.ArrayList;

import org.lwjgl.input.Keyboard;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

import frigengine.core.*;
import frigengine.core.geom.*;
import frigengine.core.gui.menu.*;

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

		// domain
		this.domain = new Rectangle(
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
	public void render(Graphics g, GUIContext context) {
		this.context = context;
		
		this.context.renderObjectForeground(g, this.background, this.getDomain());
		this.context.renderStringBoxForeground(g, this.getCurrentPage(), this.getBorderedDomain(), this.font);
	}
	
	// Events
	public void guiClosed(GUIFrame source, MenuItem selection) {
	}
}
