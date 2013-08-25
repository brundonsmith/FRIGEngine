package frigengine.field;

import org.newdawn.slick.util.xml.XMLElement;

import frigengine.core.*;
import frigengine.core.component.*;
import frigengine.core.exceptions.data.*;
import frigengine.core.scene.*;

public class ChangeAreaEvent extends AbstractTriggeredEvent {
	// Attributes
	private String destinationEntityId;

	// Constructors and initialization
	public ChangeAreaEvent() {
		this.destinationEntityId = "";
	}
	@Override
	public void init(XMLElement xmlElement) {
		// Check element name
		if (!xmlElement.getName().equals(this.getClass().getSimpleName())) {
			throw new InvalidTagException(this.getClass().getSimpleName(), xmlElement.getName());
		}
		
		// destinationEntityId
		this.destinationEntityId = xmlElement.getAttribute("destination", this.destinationEntityId);
	}

	// Operations
	@Override
	public void execute(Entity catalyst) {
		FRIGGame.changeArea(Entity.getEntity(this.destinationEntityId).getScene().getId());
		Entity.getEntity(this.destinationEntityId).getScene().addEntity(catalyst);
		catalyst.getComponent(PositionComponent.class).setPosition(Entity.getEntity(this.destinationEntityId).getComponent(PositionComponent.class).getPosition());
	}
}
