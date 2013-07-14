package frigengine.field;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import org.newdawn.slick.Input;
import org.newdawn.slick.util.xml.XMLElement;

import frigengine.core.component.*;
import frigengine.core.exceptions.data.*;
import frigengine.core.scene.*;

public class TriggerComponent extends Component {
	// Required components
	@Override
	public  Collection<Class<? extends Component>> requiredComponents() {
		return new ArrayList<Class<? extends Component>>(Arrays.asList(
				PositionComponent.class,
				ColliderComponent.class
			));
	}
	
	// Attributes
	private Collection<AbstractTriggeredEvent> triggerActions;
	
	// Constructors and initialization
	public TriggerComponent() {
		this.triggerActions = new ArrayList<AbstractTriggeredEvent>(5);
	}
	@Override
	public void init(XMLElement xmlElement) {
		// Check element name
		if (!xmlElement.getName().equals(this.getClass().getSimpleName())) {
			throw new InvalidTagException(this.getClass().getSimpleName(), xmlElement.getName());
		}
		
		// triggerActions
		for (int i = 0; i < xmlElement.getChildren().size(); i++) {
			XMLElement child = xmlElement.getChildren().get(i);

			AbstractTriggeredEvent newTriggerAction; // go through all possible trigger actions
			if (child.getName().equals(ChangeAreaEvent.class.getSimpleName())) {
				newTriggerAction = new ChangeAreaEvent();
			} else {
				throw new InvalidTagException("valid trigger action",
						child.getName());
			}

			newTriggerAction.init(child);
			this.triggerActions.add(newTriggerAction);
		}
	}
	
	// Main loop methods
	@Override
	public void update(int delta, Input input) {
		for(Entity e : getLocalEntities(PlayerControllerComponent.class)) {
			if(e.getComponent(ColliderComponent.class).getCollisions().contains(this.getParent())) {
				for(AbstractTriggeredEvent a : this.triggerActions) {
					a.execute(e);
				}
			}
		}
	}
}
