package frigengine.scene;

import java.util.ArrayList;
import java.util.Collections;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.util.xml.SlickXMLException;
import org.newdawn.slick.util.xml.XMLElement;

import frigengine.*;
import frigengine.entities.*;
import frigengine.exceptions.AttributeFormatException;
import frigengine.exceptions.ComponentException;
import frigengine.exceptions.DataParseException;
import frigengine.util.*;

public abstract class Scene extends IDable {
	// Attributes
	private Rectangle presence;

	private String currentCamera = "camera_1";
	private IDableCollection<Camera> cameras;
	private ArrayList<SceneLayer> layers;
	private IDableCollection<Entity> entities;

	// Constructors and initialization
	public Scene(String id) {
		this.id = id;
	}
	public void init(XMLElement xmlElement) {

		this.id = xmlElement.getAttribute("id", this.getID());

		// Presence
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
		this.presence = new Rectangle(0, 0, width, height);

		// Cameras
		cameras = new IDableCollection<Camera>();
		for (int i = 0; i < xmlElement.getChildrenByName("camera").size(); i++) {
			XMLElement child = xmlElement.getChildrenByName("camera").get(i);

			Camera camera = new Camera();
			camera.init(child);
			this.cameras.add(camera);
		}

		// Layers
		layers = new ArrayList<SceneLayer>();
		for (int i = 0; i < xmlElement.getChildrenByName("layer").size(); i++) {
			XMLElement child = xmlElement.getChildrenByName("layer").get(i);

			SceneLayer layer = new SceneLayer();
			layer.init(child);
			this.layers.add(layer);
		}
		Collections.sort(this.layers);

		// Entities
		entities = new IDableCollection<Entity>();
		for (int i = 0; i < xmlElement.getChildrenByName("entity_reference").size(); i++) {
			XMLElement child = xmlElement.getChildrenByName("entity_reference").get(i);

			// ID entity
			Entity entity;
			if (child.getAttribute("id") == null)
				throw new DataParseException("Entity ID unspecified in area '" + this.id + "'");
			entity = FRIGGame.instance.getEntity(child.getAttribute("id"));
			this.addEntityToScene(entity.getID());

			// Entity components
			if (entity.hasComponent("spacial")) {
				float x;
				try {
					x = (float) child.getDoubleAttribute("x",
							((ComponentSpacial) entity.getComponent("spacial")).getX());
				} catch (SlickXMLException e) {
					throw new AttributeFormatException("entity_reference", "x",
							child.getAttribute("x"));
				}
				float y;
				try {
					y = (float) child.getDoubleAttribute("y",
							((ComponentSpacial) entity.getComponent("spacial")).getY());
				} catch (SlickXMLException e) {
					throw new AttributeFormatException("entity_reference", "y",
							child.getAttribute("y"));
				}

				((ComponentSpacial) entity.getComponent("spacial")).moveTo(x, y);
			}
			if (entity.hasComponent("drawable")) {
				((ComponentDrawable) entity.getComponent("drawable")).setContinuousAnimation(child
						.getAttribute("animation", ((ComponentDrawable) entity
								.getComponent("drawable")).getContinuousAnimationID()));
			}
			if (entity.hasComponent("character")) {
				try {
					((ComponentCharacter) entity.getComponent("character"))
							.setMoveSpeed((float) child.getDoubleAttribute("speed",
									((ComponentCharacter) entity.getComponent("character"))
											.getMoveSpeed()));
				} catch (SlickXMLException e) {
					throw new AttributeFormatException("entity_reference", "speed",
							child.getAttribute("speed"));
				}
				try {
					((ComponentCharacter) entity.getComponent("character"))
							.setDirection((float) child.getDoubleAttribute("direction",
									((ComponentCharacter) entity.getComponent("character"))
											.getDirection()));
				} catch (SlickXMLException e) {
					throw new AttributeFormatException("entity_reference", "direction",
							child.getAttribute("direction"));
				}
			}
		}
	}

	// Main loop methods
	public void update(GameContainer container, int delta, Input input) {
		// ///////////////////////////////////////////////////////////////////////////////
		// TEMPORARY

		if (input.isKeyPressed(Input.KEY_W))
			moveCameraUp(5);
		if (input.isKeyPressed(Input.KEY_A))
			moveCameraLeft(5);
		if (input.isKeyPressed(Input.KEY_S))
			moveCameraDown(5);
		if (input.isKeyPressed(Input.KEY_D))
			moveCameraRight(5);

		if (input.isKeyPressed(Input.KEY_LSHIFT))
			zoomCamera((float) 0.9);
		if (input.isKeyPressed(Input.KEY_SPACE))
			zoomCamera((float) 1.1);
		/*
		 * if (keyboardState.GetPressedKeys().Contains(Keys.RightShift))
		 * FRIGGame.Instance .createDialog(new NotificationDialog(
		 * "hello this is a text box it has a lot of words how do you like it I like it alot I hope this works and fits all in a nice little box that would be good this needed a few more words so I thought I would add them they're pretty awesome right I like to type la la la la la"
		 * ));
		 */
		// ///////////////////////////////////////////////////////////////////////////////

		for (SceneLayer layer : layers)
			layer.update(container, delta, this);

		for (Entity entity : entities)
			entity.update(container, delta, this);
	}
	public void render(GameContainer container, Graphics g) {
		for (SceneLayer layer : layers)
			if (layer.getDepth() <= 0)
				layer.render(container, g, this);
			else
				break;

		for (Entity entity : entities)
			try {
				entity.render(container, g, this);
			} catch (ComponentException e) {
			}

		for (SceneLayer layer : layers)
			if (layer.getDepth() > 0)
				layer.render(container, g, this);
	}
	public void renderObject(GameContainer container, Graphics g, Image image, Rectangle presence) {
		g.drawImage(
				image,
				0,
				0,
				image.getWidth(),
				image.getHeight(),
				(presence.getX() - getCurrentCamera().getX())
						* ((float) container.getScreenWidth() / getCurrentCamera().getWidth()),
				(presence.getY() - getCurrentCamera().getY())
						* ((float) container.getScreenHeight() / getCurrentCamera().getHeight()),
				(presence.getX() - getCurrentCamera().getX())
						* ((float) container.getScreenWidth() / getCurrentCamera().getWidth())
						+ (presence.getWidth() * ((float) container.getScreenWidth() / getCurrentCamera()
								.getWidth())),
				(presence.getX() - getCurrentCamera().getX())
						* ((float) container.getScreenWidth() / getCurrentCamera().getWidth())
						+ (presence.getHeight() * ((float) container.getScreenHeight() / getCurrentCamera()
								.getHeight())));
	}
	public void renderObject(GameContainer container, Graphics g, FRIGAnimation animation,
			Rectangle presence) {
		Image image = animation.getCurrentFrame();
		g.drawImage(
				image,
				0,
				0,
				image.getWidth(),
				image.getHeight(),
				(presence.getX() - getCurrentCamera().getX())
						* ((float) container.getScreenWidth() / getCurrentCamera().getWidth()),
				(presence.getY() - getCurrentCamera().getY())
						* ((float) container.getScreenHeight() / getCurrentCamera().getHeight()),
				(presence.getX() - getCurrentCamera().getX())
						* ((float) container.getScreenWidth() / getCurrentCamera().getWidth())
						+ (presence.getWidth() * ((float) container.getScreenWidth() / getCurrentCamera()
								.getWidth())),
				(presence.getY() - getCurrentCamera().getY())
						* ((float) container.getScreenHeight() / getCurrentCamera().getHeight())
						+ (presence.getHeight() * ((float) container.getScreenHeight() / getCurrentCamera()
								.getHeight())));
	}
	public void renderLayer(GameContainer container, Graphics g, Image image, int depth) {
		double scale = Math.pow(2, depth);
		Rectangle destination = new Rectangle(0, 0, 0, 0);
		// IF BROKEN, investigate position, width vs position1, position2
		destination
				.setWidth((int) (scale * (float) this.getPresence().getWidth() * ((float) container
						.getScreenWidth() / (float) getCurrentCamera().getWidth())));
		destination
				.setHeight((int) (scale * (float) this.getPresence().getHeight() * ((float) container
						.getScreenHeight() / (float) getCurrentCamera().getHeight())));
		destination.setCenterX((float) (scale
				* (this.getPresence().getCenterX() - getCurrentCamera().getCenter().getX())
				+ getCurrentCamera().getWidth() / 2 - scale * this.getPresence().getWidth() / 2)
				* ((float) container.getScreenWidth() / (float) getCurrentCamera().getWidth()));
		destination.setCenterY((float) (scale
				* (this.getPresence().getCenterY() - getCurrentCamera().getCenter().getY())
				+ getCurrentCamera().getHeight() / 2 - scale * this.getPresence().getHeight() / 2)
				* ((float) container.getScreenHeight() / (float) getCurrentCamera().getHeight()));

		g.drawImage(image, 0, 0, image.getWidth(), image.getHeight(), destination.getMinX(),
				destination.getMinY(), destination.getMaxX(), destination.getMaxY());
	}
	public void renderLayer(GameContainer container, Graphics g, FRIGAnimation animation, int depth) {
		double scale = Math.pow(2, depth);
		Rectangle destination = new Rectangle(0, 0, 0, 0);

		destination
				.setWidth((int) (scale * (float) this.getPresence().getWidth() * ((float) container
						.getScreenWidth() / (float) getCurrentCamera().getWidth())));
		destination
				.setHeight((int) (scale * (float) this.getPresence().getHeight() * ((float) container
						.getScreenHeight() / (float) getCurrentCamera().getHeight())));
		destination.setCenterX((float) (scale
				* (this.getPresence().getCenterX() - getCurrentCamera().getCenter().getX())
				+ getCurrentCamera().getWidth() / 2 - scale * this.getPresence().getWidth() / 2)
				* ((float) container.getScreenWidth() / (float) getCurrentCamera().getWidth()));
		destination.setCenterY((float) (scale
				* (this.getPresence().getCenterY() - getCurrentCamera().getCenter().getY())
				+ getCurrentCamera().getHeight() / 2 - scale * this.getPresence().getHeight() / 2)
				* ((float) container.getScreenHeight() / (float) getCurrentCamera().getHeight()));

		Image image = animation.getCurrentFrame();
		g.drawImage(image, 0, 0, image.getWidth(), image.getHeight(), destination.getMinX(),
				destination.getMinY(), destination.getMaxX(), destination.getMaxY());
	}

	// Getters and setters
	public Rectangle getPresence() {
		return presence;
	}
	private Camera getCurrentCamera() {
		return cameras.get(currentCamera);
	}
	public IDableCollection<Entity> getEntities() {
		return entities;
	}

	// Commands
	protected void addEntityToScene(String entityID) {
		entities.add(FRIGGame.instance.getEntity(entityID));
	}
	protected void removeEntityFromScene(String entityID) {
		entities.add(FRIGGame.instance.getEntity(entityID));
	}
	protected void setMusic(String soundID) {
	}
	protected void playSound(String soundID) {
	}

	// Other methods
	public void moveCameraLeft(int increment) {
		getCurrentCamera().moveLeft(increment);
	}
	public void moveCameraRight(int increment) {
		getCurrentCamera().moveRight(increment);
	}
	public void moveCameraUp(int increment) {
		getCurrentCamera().moveUp(increment);
	}
	public void moveCameraDown(int increment) {
		getCurrentCamera().moveDown(increment);
	}
	public void zoomCamera(float scale) {
		getCurrentCamera().zoom(scale);
	}
}
