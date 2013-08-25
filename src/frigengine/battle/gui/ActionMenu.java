package frigengine.battle.gui;

import java.util.List;

import org.lwjgl.input.Keyboard;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

import frigengine.battle.*;
import frigengine.battle.actions.*;

import frigengine.core.component.*;
import frigengine.core.geom.*;
import frigengine.core.gui.*;
import frigengine.core.gui.menu.*;

public class ActionMenu extends AbstractLinearMenu {
	// Attributes
	private Entity source;
	private EnemySelectionMenu enemyMenu;
	
	// Constructors and initialization
	public ActionMenu(Entity source, List<Entity> enemies) {
		super(new Rectangle(0, 3.0f/4, 2.0f/3, 1.0f/4));
		
		this.horizontalMargin = 0.05f;
		this.verticalMargin = 0.05f;
		
		this.blocksTime = false;
		this.blocksInput = true;
		this.isVertical = false;
		
		// source
		this.source = source;
		
		// enemyMenu
		this.enemyMenu = new EnemySelectionMenu(enemies);
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
	private Rectangle cursorDomain = new Rectangle(0,0,0.05f,0.05f);
	@Override
	public void render(Graphics g, GUIContext context) {
		this.context = context;
		
		if(this.visible) {
			this.context.renderObjectForeground(g, this.background, this.getDomain());
			
			if(this.getIsVertical()) {
				float itemHeight = this.getBorderedDomain().getHeight() / this.getNumItems();
				
				for(int i = 0; i < this.getNumItems(); i++) {
					itemDomain.setX(this.getBorderedDomain().getMinX());
					itemDomain.setCenterY(this.getBorderedDomain().getMinY() + (float)i * itemHeight);
					itemDomain.setWidth(this.getBorderedDomain().getWidth());
					itemDomain.setHeight(itemHeight);
					
					this.context.renderStringBoxForeground(g, this.menuItems.get(i).getLabel(), itemDomain, this.font);
					
					if(i == this.selectedIndex) {
						cursorDomain.setX(itemDomain.getMinX() - cursorDomain.getWidth());
						cursorDomain.setY(itemDomain.getCenterY());
						this.context.renderObjectForeground(g, AbstractLinearMenu.defaultSelectionCursor, cursorDomain);
					}
				}
			} else {
				float itemWidth = this.getBorderedDomain().getWidth() / this.getNumItems();
				
				for(int i = 0; i < this.getNumItems(); i++) {
					itemDomain.setX(this.getBorderedDomain().getMinX() + (float)i * itemWidth);
					itemDomain.setY(this.getBorderedDomain().getMinY());
					itemDomain.setWidth(itemWidth);
					itemDomain.setHeight(this.getBorderedDomain().getHeight());

					this.context.renderStringBoxForeground(g, this.menuItems.get(i).getLabel(), itemDomain, this.font);
	
					if(i == this.selectedIndex) {
						cursorDomain.setX(itemDomain.getMinX() - cursorDomain.getWidth());
						cursorDomain.setCenterY(itemDomain.getCenterY());
						this.context.renderObjectForeground(g, AbstractLinearMenu.defaultSelectionCursor, cursorDomain);
					}
				}
			}
		}
	}

	// Getters and setters
	@Override
	public void addMenuItem(MenuItem item) {
		if(item instanceof ActionMenuItem) {
			((ActionMenuItem) item).setEnemySelectionMenu(this.enemyMenu);
		}
		
		super.addMenuItem(item);
	}
	public Entity getSource() {
		return this.source;
	}
	public ActionInstance getSelectedActionInstance() {
		return this.source.getComponent(BattleComponent.class).getActions().get(this.getSelection().getLabel()).getInstance(this.source, null);
	}

	// Operations
	@Override
	public void select() {
		this.hide();
		super.select();
	}
}
