package frigengine.scene;

import java.util.HashSet;
import java.util.Set;

import org.lwjgl.input.Keyboard;
import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Line;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.util.xml.XMLElement;

import frigengine.FRIGGame;
import frigengine.Initializable;
import frigengine.commands.*;
import frigengine.entities.CharacterComponent;
import frigengine.entities.Entity;
import frigengine.exceptions.DataParseException;
import frigengine.exceptions.InvalidTagException;
import frigengine.gui.GUIFrame;
import frigengine.gui.SpeechDialog;

public class Area extends Scene implements Initializable {
	// Attributes
	private String name;
	private String playerId;
	private Set<Shape> boundaries;

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
			if (!FRIGGame.getInstance().entityExists(this.playerId))
				throw new DataParseException("Chosen player ID '" + this.playerId
						+ "' is not an entity in this game");
			if (!FRIGGame.getInstance().getEntity(this.playerId).hasComponent(CharacterComponent.class))
				throw new DataParseException("Chosen player '" + this.playerId
						+ "' does not have a character component");
		}

		// boundaries
		this.boundaries = new HashSet<Shape>();
		this.boundaries.add(new Line(this.getPresence().getMinX(), this.getPresence().getMinY(), this.getPresence().getMinX(), this.getPresence().getMaxY()));
		this.boundaries.add(new Line(this.getPresence().getMinX(), this.getPresence().getMinY(), this.getPresence().getMaxX(), this.getPresence().getMinY()));
		this.boundaries.add(new Line(this.getPresence().getMaxX(), this.getPresence().getMinY(), this.getPresence().getMaxX(), this.getPresence().getMaxY()));
		this.boundaries.add(new Line(this.getPresence().getMinX(), this.getPresence().getMaxY(), this.getPresence().getMaxX(), this.getPresence().getMaxY()));
	}
	@Override
	public void update(int delta, Input input) {
		boolean timeBlocked = false;
		boolean inputBlocked = false;
		
		//////////////////////////////////////////
		// TEMPORARY
		if(input.isKeyPressed(Keyboard.KEY_RSHIFT)) {
			this.openGUI(new SpeechDialog(this, "Hallo thar this is me speaking sup"));
		}
		if(input.isKeyDown(Keyboard.KEY_LSHIFT))
			this.zoomCamera(0.99F);
		if(input.isKeyDown(Keyboard.KEY_SPACE))
			this.zoomCamera(1.01F);
		//////////////////////////////////////////
		
		
		// GUI
		for (Object o : this.guiStack.toArray()) {
			GUIFrame frame = (GUIFrame) o;
			frame.update(timeBlocked ? 0 : delta, inputBlocked ? null : input);
			if (frame.getBlocksTime())
				timeBlocked = true;
			if (frame.getBlocksInput())
				inputBlocked = true;
		}

		// Layers
		for (SceneLayer layer : this.layers)
			layer.update(timeBlocked ? 0 : delta, inputBlocked ? null : input, this);

		// Player
		this.updatePlayer(timeBlocked ? 0 : delta, inputBlocked ? null : input);

		// Entities
		for (Entity entity : this.entities)
			entity.update(timeBlocked ? 0 : delta, inputBlocked ? null : input, this);
		
		this.getCurrentCamera().setCenter(this.getPlayer().spacial().getPosition());
	}
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
			this.getPlayer().character().move(movement.getTheta());
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
	public Set<Shape> getBoundaries() {
		return this.boundaries;
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
