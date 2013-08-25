package frigengine.core.gui.menu;

import org.lwjgl.input.Keyboard;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

import frigengine.core.geom.*;
import frigengine.core.gui.*;

public class BasicLinearMenu extends AbstractLinearMenu {
	// Constructors and initialization
	public BasicLinearMenu(Rectangle domain) {
		super(domain);
		
		// blocksTime
		this.blocksTime = false;
		
		// blocksInput
		this.blocksInput = true;
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
	private Rectangle itemDomain = new Rectangle(0,0,0,0);
	@Override
	public void render(Graphics g, GUIContext context) {
		this.context = context;
		
		this.background.draw( 
				this.getDomain().getX(), 
				this.getDomain().getY(), 
				this.getDomain().getWidth(), 
				this.getDomain().getHeight());
		
		if(this.getIsVertical()) {
			int itemHeight = (int)(this.getBorderedDomain().getHeight() / this.getNumItems());
			
			for(int i = 0; i < this.getNumItems(); i++) {
				itemDomain.setX(this.getBorderedDomain().getMinX());
				itemDomain.setY(this.getBorderedDomain().getMinY() + i * itemHeight);
				itemDomain.setWidth(this.getBorderedDomain().getWidth());
				itemDomain.setHeight(itemHeight);
				
				this.context.renderStringBoxForeground(g, this.menuItems.get(i).getLabel(), itemDomain, this.font);
			}
		} else {
			int itemWidth = (int)(this.getBorderedDomain().getHeight() / this.getNumItems());
			
			for(int i = 0; i < this.getNumItems(); i++) {
				itemDomain.setX(this.getBorderedDomain().getMinX() + i * itemWidth);
				itemDomain.setY(this.getBorderedDomain().getMinY());
				itemDomain.setWidth(itemWidth);
				itemDomain.setHeight(this.getBorderedDomain().getHeight());

				this.context.renderStringBoxForeground(g, this.menuItems.get(i).getLabel(), itemDomain, this.font);
			}
		}
	}
}
