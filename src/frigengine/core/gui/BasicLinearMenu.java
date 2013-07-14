package frigengine.core.gui;

import org.lwjgl.input.Keyboard;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Rectangle;

import frigengine.core.scene.*;

public class BasicLinearMenu extends RecursiveLinearMenu {
	// Constructors and initialization
	public BasicLinearMenu(Rectangle presence) {
		super(presence);
		
		// blocksTime
		this.blocksTime = false;
		
		// blocksInput
		this.blocksInput = false;
	}
	
	// Main loop methods
	@Override
	public void update(int delta, Input input) {
		if (input != null && input.isKeyPressed(Keyboard.KEY_DOWN)) {
			this.forward();
		} else if (input != null && input.isKeyPressed(Keyboard.KEY_UP)) {
			this.back();
		}
		
		if(input != null && input.isKeyPressed(Keyboard.KEY_RETURN)) {
			this.select();
		}
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
				
				scene.renderStringBoxForeground(g, this.items.get(i).getLabel(), itemPresence, this.font);
			}
		} else {
			int itemWidth = (int)(this.getBorderedPresence().getHeight() / this.getNumItems());
			
			for(int i = 0; i < this.getNumItems(); i++) {
				Rectangle itemPresence = new Rectangle(
						this.getBorderedPresence().getMinX() + i * itemWidth,
						this.getBorderedPresence().getMinY(),
						itemWidth,
						this.getBorderedPresence().getHeight()
						);

				scene.renderStringBoxForeground(g, this.items.get(i).getLabel(), itemPresence, this.font);
			}
		}
	}
	
	// Events
	public void guiClosed(GUIFrame source, MenuItem selection) {
	}
}
