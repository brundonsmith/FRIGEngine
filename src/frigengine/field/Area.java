package frigengine.field;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.lwjgl.input.Keyboard;
import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.util.xml.SlickXMLException;
import org.newdawn.slick.util.xml.XMLElement;

import frigengine.Camera;
import frigengine.FRIGGame;
import frigengine.Scene;
import frigengine.SceneLayer;
import frigengine.commands.*;
import frigengine.entities.Character;
import frigengine.entities.Drawable;
import frigengine.entities.Entity;
import frigengine.entities.Physical;
import frigengine.entities.Positionable;
import frigengine.exceptions.data.AttributeFormatException;
import frigengine.exceptions.data.InvalidIDException;
import frigengine.exceptions.data.InvalidTagException;
import frigengine.gui.GUIFrame;
import frigengine.gui.SpeechDialog;
import frigengine.util.IDableCollection;
import frigengine.util.Initializable;
import frigengine.util.SelectableCollection;
import frigengine.util.geom.Ellipse;
import frigengine.util.geom.Line;
import frigengine.util.geom.Rectangle;

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
		// Check element name
		if (!xmlElement.getName().equals(this.getClass().getSimpleName())) {
			throw new InvalidTagException(this.getClass().getSimpleName(), xmlElement.getName());
		}

		// id
		this.id = xmlElement.getAttribute("id", this.getId());
		
		// presence
		float width;
		try {
			width = (float) xmlElement.getDoubleAttribute("width", 1);
		} catch (SlickXMLException e) {
			throw new AttributeFormatException(xmlElement.getName(), "width",
					xmlElement.getAttribute("width"));
		}
		float height;
		try {
			height = (float) xmlElement.getDoubleAttribute("height", 1);
		} catch (SlickXMLException e) {
			throw new AttributeFormatException(xmlElement.getName(), "height",
					xmlElement.getAttribute("height"));
		}
		this.presence = new Rectangle(0, 0, 0, 0);
		this.presence.setX(0);
		this.presence.setY(0);
		this.presence.setWidth(width);
		this.presence.setHeight(height);

		// cameras
		this.cameras = new SelectableCollection<String, Camera>();
		this.cameras.select(xmlElement.getAttribute("default_camera", null));
		for (int i = 0; i < xmlElement.getChildrenByName(Camera.class.getSimpleName()).size(); i++) {
			XMLElement child = xmlElement.getChildrenByName(Camera.class.getSimpleName()).get(i);

			Camera camera = new Camera();
			camera.init(child);
			this.cameras.put(camera);
		}

		// layers
		this.layers = new ArrayList<SceneLayer>();
		for (int i = 0; i < xmlElement.getChildrenByName(SceneLayer.class.getSimpleName()).size(); i++) {
			XMLElement child = xmlElement.getChildrenByName(SceneLayer.class.getSimpleName()).get(i);

			SceneLayer layer = new SceneLayer();
			layer.init(child);
			this.layers.add(layer);
		}
		Collections.sort(this.layers);

		// entities
		this.entities = new IDableCollection<String, Entity>();
		for (int i = 0; i < xmlElement.getChildrenByName("entity_reference").size(); i++) {
			XMLElement child = xmlElement.getChildrenByName("entity_reference").get(i);

			// id match
			Entity entity;
			if (child.getAttribute("id") == null) {
				throw new InvalidIDException("Entity ID unspecified in area '" + this.getId() + "'");
			}
			entity = FRIGGame.getInstance().getEntity(child.getAttribute("id"));
			FRIGGame game = FRIGGame.getInstance();
			this.addEntityToScene(entity.getId());

			// component modification
			if (entity.is(Positionable.class)) {
				float x;
				try {
					x = (float) child.getDoubleAttribute("x",
							((Positionable)entity.as(Positionable.class)).getX());
				} catch (SlickXMLException e) {
					throw new AttributeFormatException("entity_reference", "x",
							child.getAttribute("x"));
				}
				float y;
				try {
					y = (float) child.getDoubleAttribute("y",
							((Positionable)entity.as(Positionable.class)).getY());
				} catch (SlickXMLException e) {
					throw new AttributeFormatException("entity_reference", "y",
							child.getAttribute("y"));
				}
				((Positionable)entity.as(Positionable.class)).setX(x);
				((Positionable)entity.as(Positionable.class)).setY(y);
			}
			if (entity.is(Drawable.class)) {
				((Drawable)entity.as(Drawable.class)).setAnimation(child
						.getAttribute("animation", ((Drawable)entity.as(Drawable.class)).getAnimation().getId()));
			}
			if (entity.is(Character.class)) {
				try {
					((Character)entity.as(Character.class)).setMoveSpeed((float) child.getDoubleAttribute("speed",
							((Character)entity.as(Character.class)).getMoveSpeed()));
				} catch (SlickXMLException e) {
					throw new AttributeFormatException("entity_reference", "speed",
							child.getAttribute("speed"));
				}
				try {
					((Character)entity.as(Character.class)).setDirection((float) child.getDoubleAttribute("direction",
							((Character)entity.as(Character.class)).getDirection()));
				} catch (SlickXMLException e) {
					throw new AttributeFormatException("entity_reference", "direction",
							child.getAttribute("direction"));
				}
			}
		}

		// guiStack
		this.guiStack = new ArrayDeque<GUIFrame>();

		// name
		this.name = xmlElement.getAttribute("name", this.id);
		
		// playerId
		if (xmlElement.getAttribute("player") != null) {
			this.playerId = xmlElement.getAttribute("player", "");
			if (!this.entities.contains(this.playerId)) {
				throw new InvalidIDException("Chosen player with ID '" + this.playerId + "' is not an entity in area '" + this.getId() + "'");
			} else if (!this.entities.get(this.playerId).is(Character.class)) {
				throw new InvalidIDException("Chosen player with ID '" + this.playerId + "' does not have a character component");
			}
		}

		// boundaries
		this.boundaries = new HashSet<Shape>();
		this.boundaries.add(new Line(this.getPresence().getMinX(), this.getPresence().getMinY(), this.getPresence().getMinX(), this.getPresence().getMaxY()));
		this.boundaries.add(new Line(this.getPresence().getMinX(), this.getPresence().getMinY(), this.getPresence().getMaxX(), this.getPresence().getMinY()));
		this.boundaries.add(new Line(this.getPresence().getMaxX(), this.getPresence().getMinY(), this.getPresence().getMaxX(), this.getPresence().getMaxY()));
		this.boundaries.add(new Line(this.getPresence().getMinX(), this.getPresence().getMaxY(), this.getPresence().getMaxX(), this.getPresence().getMaxY()));
		for (int i = 0; i < xmlElement.getChildren().size(); i++) {
			XMLElement child = xmlElement.getChildren().get(i);
			
			Shape newBoundary;
			if(child.getName().equals(Line.class.getSimpleName())) {
				newBoundary = new Line();
				((Line)newBoundary).init(child);
				this.boundaries.add(newBoundary);
			} else if(child.getName().equals(Rectangle.class.getSimpleName())) {
				newBoundary = new Rectangle();
				((Rectangle)newBoundary).init(child);
				this.boundaries.add(newBoundary);
			} else if(child.getName().equals(Ellipse.class.getSimpleName())) {
				newBoundary = new Ellipse();
				((Ellipse)newBoundary).init(child);
				this.boundaries.add(newBoundary);
			}
		}
	}
	@Override
	public void update(int delta, Input input) {
		boolean timeBlocked = false;
		boolean inputBlocked = false;
		
		//////////////////////////////////////////
		// TEMPORARY
		if(input.isKeyPressed(Keyboard.KEY_RSHIFT)) {
			this.openGUI(new SpeechDialog("Hallo thar this is me speaking sup"));
		}
		if(input.isKeyDown(Keyboard.KEY_LSHIFT)) {
			this.zoomCamera(0.99F);
		}
		if(input.isKeyDown(Keyboard.KEY_SPACE)) {
			this.zoomCamera(1.01F);
		}
		//////////////////////////////////////////
		
		
		// GUI
		for (Object o : this.guiStack.toArray()) {
			GUIFrame frame = (GUIFrame) o;
			frame.update(timeBlocked ? 0 : delta, inputBlocked ? null : input);
			if (frame.getBlocksTime()) {
				timeBlocked = true;
			}
			if (frame.getBlocksInput()) {
				inputBlocked = true;
			}
		}

		// Layers
		for (SceneLayer layer : this.layers) {
			layer.update(timeBlocked ? 0 : delta);
		}

		// Player
		this.updatePlayer(timeBlocked ? 0 : delta, inputBlocked ? null : input);

		// Entities
		for (Entity entity : this.entities) {
			System.out.println(entity.toString());
			
			if(entity.is(Character.class)) {
				((Character)entity.as(Character.class)).update(timeBlocked ? 0 : delta);
			}
			if(entity.is(Physical.class)) {
				((Physical)entity.as(Physical.class)).update(this.boundaries, this.getChunks(Physical.class));
			}
			if(entity.is(Drawable.class)) {
				((Drawable)entity.as(Drawable.class)).update(timeBlocked ? 0 : delta);
			}
		}
		
		this.getCurrentCamera().setCenter(((Positionable)this.getPlayer().as(Positionable.class)).getPosition());
	}
	protected void updatePlayer(int delta, Input input) {
		
		if (input != null && input.isKeyPressed(Input.KEY_M)) {
			((Drawable)this.getPlayer().as(Drawable.class)).playAnimation("fun");
		}
		
		// Movement
		Vector2f movement = new Vector2f();

		if (input != null && input.isKeyDown(Input.KEY_UP)) {
			movement.add(new Vector2f(0, -1));
		}
		if (input != null && input.isKeyDown(Input.KEY_DOWN)) {
			movement.add(new Vector2f(0, 1));
		}
		if (input != null && input.isKeyDown(Input.KEY_LEFT)) {
			movement.add(new Vector2f(-1, 0));
		}
		if (input != null && input.isKeyDown(Input.KEY_RIGHT)) {
			movement.add(new Vector2f(1, 0));
		}

		if (movement.length() > 0.5) {
			((Character)this.getPlayer().as(Character.class)).move(movement.getTheta());
		}
	}

	// Getters and setters
	public String getName() {
		return name;
	}
	public String getPlayerId() {
		return this.playerId;
	}
	public void setPlayerId(String playerId) {
		this.playerId = playerId;
	}
	public Entity getPlayer() {
		return this.entities.get(this.playerId);
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
