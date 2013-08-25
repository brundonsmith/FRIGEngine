package frigengine.battle.gui;

import java.util.List;

import org.lwjgl.input.Keyboard;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Transform;

import frigengine.core.*;
import frigengine.core.component.Entity;
import frigengine.core.geom.*;
import frigengine.core.gui.GUIContext;
import frigengine.core.gui.menu.*;
import frigengine.core.scene.*;

public class EnemySelectionMenu extends AbstractLinearMenu {
	// Static
	private static Animation enemySelectionCursor = Animation.getPlaceholder();
	
	// Constructors and Initialization
	public EnemySelectionMenu(List<Entity> enemies) {
		super(new Rectangle(0,0,1.0f/3, 1.0f));
		
		if(EnemySelectionMenu.enemySelectionCursor.getId() == Animation.PLACEHOLDER_ID) {
			EnemySelectionMenu.enemySelectionCursor = FRIGGame.getGuiAsset("EnemySelectionCursor");
		}
		
		this.blocksTime = false;
		this.blocksInput = true;
		this.isVertical = true;
		this.isWrapping = true;
		
		for(Entity e : enemies) {
			this.addMenuItem(new EnemySelectionMenuItem(e));
		}
	}

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
	private Rectangle cursorDomain = new Rectangle(0,0,0.08f,0.08f);
	@Override
	public void render(Graphics g, GUIContext context) {
		this.context = context;
		
		Rectangle enemyDomain = ((EnemySelectionMenuItem)this.getSelection()).getEnemy().getComponent(SpriteComponent.class).getWorldDomain().transform(FRIGGame.getCurrentScene().getCurrentCamera().getComponent(CameraComponent.class).getTransform()).transform(Transform.createScaleTransform(1.0f/FRIGGame.getScreenWidth(), 1.0f/FRIGGame.getScreenHeight()));
		cursorDomain.setX(enemyDomain.getMinX() - cursorDomain.getWidth());
		cursorDomain.setCenterY(enemyDomain.getCenterY());
		this.context.renderObjectForeground(g, EnemySelectionMenu.enemySelectionCursor, cursorDomain);
	}
}
