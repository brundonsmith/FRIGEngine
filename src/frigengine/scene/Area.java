package frigengine.scene;

import java.util.List;

import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.util.xml.XMLElement;

import frigengine.FRIGGame;
import frigengine.Initializable;
import frigengine.commands.*;
import frigengine.entities.ComponentCharacter;
import frigengine.entities.ComponentSpacial;
import frigengine.entities.Entity;
import frigengine.exceptions.DataParseException;
import frigengine.exceptions.InvalidTagException;

public class Area extends Scene implements Initializable {
	// Attributes
	private String name;
	private String playerId;

	// Constructors and initialization
	public Area(String id) {
		super(id);
	}
	@Override
	public void init(XMLElement xmlElement) {
		if (!xmlElement.getName().equals(this.getClass().getSimpleName()))
			throw new InvalidTagException(this.getClass().getSimpleName(), xmlElement.getName());

		super.init(xmlElement);

		// name
		this.name = xmlElement.getAttribute("name", this.id);
		
		// playerId
		if (xmlElement.getAttribute("player") != null) {
			this.playerId = xmlElement.getAttribute("player", "");
			Entity player = FRIGGame.getInstance().getEntity(this.playerId);
			if (!FRIGGame.getInstance().entityExists(this.playerId))
				throw new DataParseException("Chosen player ID '" + this.playerId
						+ "' is not an entity in this game");
			if (!FRIGGame.getInstance().getEntity(this.playerId).hasComponent(ComponentCharacter.class))
				throw new DataParseException("Chosen player '" + this.playerId
						+ "' does not have a character component");
		}
	}
	@Override
	public void update(int delta, Input input) {
		super.update(delta, input);
		
		this.getCurrentCamera().setCenter(((ComponentSpacial) this.getPlayer().getComponent(ComponentSpacial.class)).getPosition());
	}
	@Override
	protected void updatePlayer(int delta, Input input) {
		Vector2f movement = new Vector2f();

		if (input != null && input.isKeyDown(Input.KEY_UP))
			movement.add(new Vector2f(0, -1));
		if (input != null && input.isKeyDown(Input.KEY_DOWN))
			movement.add(new Vector2f(0, 1));
		if (input != null && input.isKeyDown(Input.KEY_LEFT))
			movement.add(new Vector2f(-1, 0));
		if (input != null && input.isKeyDown(Input.KEY_RIGHT))
			movement.add(new Vector2f(1, 0));

		if (movement.length() > 0.5)
			((ComponentCharacter) this.getPlayer().getComponent(ComponentCharacter.class)).move(movement.getTheta());
	}

	// Getters and setters
	public String getName() {
		return name;
	}
	public void setPlayerId(String playerId) {
		this.playerId = playerId;
	}
	public Entity getPlayer() {
		return FRIGGame.getInstance().getEntity(this.playerId);
	}

	// Commands
	public void executeCommand(CommandInstance command) {
		switch (command.getCommand()) {
		case ADD_ENTITY_TO_AREA:
			this.addEntityToArea(command.getArgument(1));
			break;
		case REMOVE_ENTITY_FROM_AREA:
			this.removeEntityFromArea(command.getArgument(1));
			break;
		case SET_MUSIC:
			this.setMusic(command.getArgument(1));
			break;
		case PLAY_SOUND:
			this.playSound(command.getArgument(1));
			break;
		default:
			break;
		}
	}
	private void addEntityToArea(String entityId) {
		this.addEntityToScene(entityId);
	}
	private void removeEntityFromArea(String entityId) {
		this.removeEntityFromScene(entityId);
	}
}
