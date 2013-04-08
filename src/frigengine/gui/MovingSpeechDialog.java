package frigengine.gui;

import org.lwjgl.input.Keyboard;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Rectangle;

import frigengine.FRIGGame;
import frigengine.entities.ComponentDrawable;
import frigengine.entities.Entity;
import frigengine.scene.Scene;

public class MovingSpeechDialog extends AbstractDialog {
	// Constants
	private static final float OFFSET_FROM_SPEAKER = 5;
	private static final float DEFAULT_WIDTH = 100;
	private static final float DEFAULT_HEIGHT = 50;
	
	// Attributes
	private Entity speaker;
	
	// Constructors and initialization
	public MovingSpeechDialog(String content, String speakerID, Scene scene) {
		super(content);
		
		// presence
		this.presence = new Rectangle(0, 0, DEFAULT_WIDTH, DEFAULT_HEIGHT);
		
		// speaker
		this.speaker = FRIGGame.getInstance().getEntity(speakerID);
	}

	// Main loop methods
	@Override
	public void update(int delta, Input input) {
		this.presence.setX(((ComponentDrawable)speaker.getComponent(ComponentDrawable.class)).getMinX());
		this.presence.setY(((ComponentDrawable)speaker.getComponent(ComponentDrawable.class)).getMinY() - OFFSET_FROM_SPEAKER - this.presence.getHeight());
		
		if(input.isKeyPressed(Keyboard.KEY_SLASH))
			this.iterate();
	}
	@Override
	public void render(Graphics g, Scene scene) {
		//ImageBuffer buffer = scene.getBufferPool().getBuffer((int)this.getPresence().getWidth(), (int)this.getPresence().getHeight());
		scene.renderObject(g, this.background, this.getPresence());
		scene.renderStringBoxForeground(g, this.getCurrentPage(), scene.cameraTransform(this.getBorderedPresence()), this.font);
		//buffer.release();
	}
}
