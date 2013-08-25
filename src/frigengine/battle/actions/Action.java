package frigengine.battle.actions;

import java.util.ArrayList;
import java.util.List;

import frigengine.battle.*;

import frigengine.core.component.*;
import frigengine.core.idable.*;
import frigengine.core.scene.*;

public abstract class Action extends IDable<String> {
	// Constants
	public static final Action ATTACK = new Action("Attack", 100, "attack") {
		public void apply(Entity source, List<Entity> targets) {
			BattleComponent sourceComponent = source.getComponent(BattleComponent.class); // for brevity
			for(Entity e : targets) {
				BattleComponent targetComponent = e.getComponent(BattleComponent.class); // for brevity
				
				targetComponent.damageHealth(sourceComponent.getAttack() - targetComponent.getDefense()); // logic
			}
		}
	};
	
	// Attribute
	protected int chargeTime;
	protected String animationId;
	protected String chargeName;
	
	// Constructors and initialization
	public Action(String name) {
		this.setId(name);
		this.chargeTime = 1;
		this.animationId = this.getId();
		this.chargeName = "Charging";
	}
	public Action(String name,  int chargeTime) {
		this.setId(name);
		this.chargeTime = chargeTime;
		this.animationId = this.getId();
		this.chargeName = "Charging";
	}
	public Action(String name,  int chargeTime, String animationId) {
		this.setId(name);
		this.chargeTime = chargeTime;
		this.animationId = animationId;
		this.chargeName = "Charging";
	}
	public Action(String name,  int chargeTime, String animationId, String chargeName) {
		this.setId(name);
		this.chargeTime = chargeTime;
		this.animationId = animationId;
		this.chargeName = chargeName;
	}
	
	// Getters and setters
	public String getName() {
		return this.getId();
	}
	public int getChargeTime() {
		return this.chargeTime;
	}
	public String getAnimationId() {
		return this.animationId;
	}
	public String getChargeName() {
		return this.chargeName;
	}
	public ActionInstance getInstance(Entity source, List<Entity> targets) {
		return new ActionInstance(this, source, targets);
	}

	// Application
	public abstract void apply(Entity source, List<Entity> targets);
	public List<Movement> getMovements(Entity source, List<Entity> targets) {
		List<Movement> movements = new ArrayList<Movement>();
		
		for(Entity e : targets) {
			// attack
			movements.add(new Movement(
					100, 
					Movement.getLinearPath(
							e.getComponent(PositionComponent.class).getPositionVector().sub(source.getComponent(PositionComponent.class).getPositionVector())
							),
					this.getId()
				));
			
			// return
			movements.add(new Movement(
					100, 
					Movement.getLinearPath(
							source.getComponent(PositionComponent.class).getPositionVector().sub(e.getComponent(PositionComponent.class).getPositionVector())
							),
					"walk_e"
				));
		}
		
		return movements;
	}
}
