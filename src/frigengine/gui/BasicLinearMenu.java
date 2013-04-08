package frigengine.gui;

import org.lwjgl.input.Keyboard;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Rectangle;

import frigengine.scene.Scene;

public class BasicLinearMenu extends AbstractLinearMenu {
	// Constructors and initialization
	public BasicLinearMenu() {
		super();
		
		this.blocksInput = false;
		this.blocksTime = false;
	}
	
	// Main loop methods
	@Override
	public void update(int delta, Input input) {
		if (input != null && input.isKeyPressed(Keyboard.KEY_DOWN))
			this.forward();
		else if (input != null && input.isKeyPressed(Keyboard.KEY_UP))
			this.back();
	}
	@Override
	public void render(Graphics g, Scene scene) {
		
		this.background.draw( 
				this.getPresence().getX(), 
				this.getPresence().getY(), 
				this.getPresence().getWidth(), 
				this.getPresence().getHeight());
		
		if(this.getIsVertical()) {
			int itemHeight = (int)(this.getBorderedPresence().getHeight() / this.getNumItems());
			
			for(int i = 0; i < this.getNumItems(); i++) {
				Rectangle itemPresence = new Rectangle(
						this.getBorderedPresence().getMinX(),
						this.getBorderedPresence().getMinY() + i * itemHeight,
						this.getBorderedPresence().getWidth(),
						itemHeight
						);
				
				scene.renderStringBoxForeground(g, this.getMenuItems()[i].getLabel(), itemPresence, this.font);
			}
		}
	}
}
