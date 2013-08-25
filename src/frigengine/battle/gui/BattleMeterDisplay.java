package frigengine.battle.gui;

import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

import frigengine.battle.*;
import frigengine.core.component.*;
import frigengine.core.geom.*;
import frigengine.core.gui.*;
import frigengine.core.scene.*;

public class BattleMeterDisplay extends GUIFrame {
	// Static
	private static int maxEntities = 4;
	
	// Attributes
	private List<Entity> battleEntities;
	private List<BasicMeter> waitMeters;
	private List<BasicMeter> chargeMeters;
	private List<BasicMeter> healthMeters;

	// Constructors and initialization
	public BattleMeterDisplay(List<Entity> battleEntities) {
		super(new Rectangle(2.0f/3, 2.0f/3, 1.0f/3, 1.0f/3));
		
		// battleEntities
		this.battleEntities = battleEntities;
		
		// waitMeters
		this.waitMeters = new ArrayList<BasicMeter>();
		
		// chargeMeters
		this.chargeMeters = new ArrayList<BasicMeter>();
		
		// healthMeters
		this.healthMeters = new ArrayList<BasicMeter>();
		
		// Create meters
		for(int i = 0; i < this.battleEntities.size(); i++) {
			Entity e = this.battleEntities.get(i);
			
			this.waitMeters.add(new BasicMeter(
					new Rectangle(0.0f, 1.0f / BattleMeterDisplay.maxEntities * (float)i, 1.0f/3 * 0.95f, 1.0f / BattleMeterDisplay.maxEntities),
					e.getComponent(BattleComponent.class).getWaitMeterCapacity(),
					Color.green));
			this.chargeMeters.add(new BasicMeter(
					new Rectangle(1.0f/3, 1.0f / BattleMeterDisplay.maxEntities * (float)i, 1.0f/3 * 0.95f, 1.0f / BattleMeterDisplay.maxEntities),
					e.getComponent(BattleComponent.class).getChargeMeterCapacity(),
					Color.blue));
			this.healthMeters.add(new BasicMeter(
					new Rectangle(2.0f/3, 1.0f / BattleMeterDisplay.maxEntities * (float)i, 1.0f/3 * 0.95f, 1.0f / BattleMeterDisplay.maxEntities),
					e.getComponent(BattleComponent.class).getMaxHealth(),
					Color.red));
		}
	}

	// Main loop methods
	@Override
	public void update(int delta, Input input) {
		for(int i = 0; i < this.battleEntities.size(); i++) {
			Entity e = this.battleEntities.get(i);
			
			this.waitMeters.get(i).setValue(e.getComponent(BattleComponent.class).getWaitMeter());
			this.chargeMeters.get(i).setValue(e.getComponent(BattleComponent.class).getChargeMeter());
			this.healthMeters.get(i).setValue(e.getComponent(BattleComponent.class).getCurrentHealth());
		}

		for(BasicMeter m : this.waitMeters) {
			m.update(delta, input);
		}
		for(BasicMeter m : this.chargeMeters) {
			m.update(delta, input);
		}
		for(BasicMeter m : this.healthMeters) {
			m.update(delta, input);
		}
	}
	@Override
	public void render(Graphics g, GUIContext context) {
		this.context = context;
		
		this.context.renderObjectForeground(g, Animation.getPlaceholder(), this.getDomain());
		
		for(BasicMeter m : this.waitMeters) {
			m.render(g, this);
		}
		for(BasicMeter m : this.chargeMeters) {
			m.render(g, this);
		}
		for(BasicMeter m : this.healthMeters) {
			m.render(g, this);
		}
	}
}
