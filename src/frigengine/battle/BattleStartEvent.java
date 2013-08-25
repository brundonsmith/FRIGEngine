package frigengine.battle;

import org.newdawn.slick.util.xml.XMLElement;

import frigengine.core.FRIGGame;
import frigengine.core.component.Entity;
import frigengine.core.exceptions.data.InvalidTagException;
import frigengine.field.AbstractTriggeredEvent;

public class BattleStartEvent extends AbstractTriggeredEvent {
	// Attributes
	private String battleTemplateId;
	
	// Constructors and initialization
	public BattleStartEvent() {
		this.battleTemplateId = "";
	}
	@Override
	public void init(XMLElement xmlElement) {
		// Check element name
		if (!xmlElement.getName().equals(this.getClass().getSimpleName())) {
			throw new InvalidTagException(this.getClass().getSimpleName(), xmlElement.getName());
		}

		// destinationEntityId
		this.battleTemplateId = xmlElement.getAttribute("battle", this.battleTemplateId);
	}

	@Override
	public void execute(Entity catalyst) {
		FRIGGame.startBattle(this.battleTemplateId);
	}
}
