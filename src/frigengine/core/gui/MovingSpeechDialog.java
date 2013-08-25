package frigengine.core.gui;

import org.lwjgl.input.Keyboard;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

import frigengine.gamefundamentals.*;
import frigengine.gamefundamentals.component.*;
import frigengine.gamefundamentals.geom.*;
import frigengine.gamefundamentals.scene.*;

public class MovingSpeechDialog extends AbstractDialog {
	// Constants
	private static final float OFFSET_FROM_SPEAKER = 5;
	private static final float DEFAULT_WIDTH = 100;
	private static final float DEFAULT_HEIGHT = 50;
	
	// Attributes
	private Entity speaker;
	
	// Constructors and initialization
	public MovingSpeechDialog(Scene context, String content, String speakerID, Scene scene) {
		super(context, content);
		
		// domain
		this.domain = new Rectangle(0, 0, DEFAULT_WIDTH, DEFAULT_HEIGHT);
		
		// speaker
		this.speaker = FRIGGame.getInstance().getEntity(speakerID);
	}

	// Main loop methods
	@Override
	public void update(int delta, Input input) {
		this.domain.setX(speaker.drawable().getMinX());
		this.domain.setY(speaker.drawable().getMinY() - OFFSET_FROM_SPEAKER - this.domain.getHeight());
		
		if(input.isKeyPressed(Keyboard.KEY_SLASH)) {
			this.iterate();
		}
	}
	@Override
	public void render(Graphics g, Scene scene) {
		//ImageBuffer buffer = scene.getBufferPool().getBuffer((int)this.getDomain().getWidth(), (int)this.getDomain().getHeight());
		scene.renderObject(g, this.background, this.getDomain());
		scene.renderStringBoxForeground(g, this.getCurrentPage(), scene.cameraTransform(this.getBorderedDomain()), this.font);
		//buffer.release();
	}
}
