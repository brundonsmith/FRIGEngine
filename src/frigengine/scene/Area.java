package frigengine.scene;

import org.newdawn.slick.util.xml.XMLElement;

import frigengine.Initializable;
import frigengine.commands.*;
import frigengine.exceptions.InvalidTagException;

public class Area extends Scene implements Initializable {
	@Override
	public String getTagName() {
		return "area";
	}

	// Attributes
	protected String name;

	// Constructors and initialization
	public Area(String id) {
		super(id);
	}
	@Override
	public void init(XMLElement xmlElement) {
		if (!xmlElement.getName().equals(getTagName()))
			throw new InvalidTagException(getTagName(), xmlElement.getName());

		super.init(xmlElement);

		this.name = xmlElement.getAttribute("name", this.id);
	}

	// Getters and setters
	public String getName() {
		return name;
	}

	// Commands
	public void executeCommand(CommandInstance command) {
		switch (command.getCommand()) {
		case ADD_ENTITY_TO_AREA:
			this.addEntityToArea(command.getArguments()[1]);
			break;
		case REMOVE_ENTITY_FROM_AREA:
			this.removeEntityFromArea(command.getArguments()[1]);
			break;
		case SET_MUSIC:
			this.setMusic(command.getArguments()[1]);
			break;
		case PLAY_SOUND:
			this.playSound(command.getArguments()[1]);
			break;
		default:
			break;
		}
	}
	private void addEntityToArea(String entityID) {
		this.addEntityToScene(entityID);
	}
	private void removeEntityFromArea(String entityID) {
		this.removeEntityFromScene(entityID);
	}
}
