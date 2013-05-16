package frigengine.scene;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.Scanner;

import org.newdawn.slick.Font;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.util.xml.SlickXMLException;
import org.newdawn.slick.util.xml.XMLElement;

import frigengine.*;
import frigengine.entities.*;
import frigengine.exceptions.AttributeFormatException;
import frigengine.exceptions.ComponentException;
import frigengine.exceptions.DataParseException;
import frigengine.gui.GUICloseListener;
import frigengine.gui.GUIFrame;
import frigengine.util.*;

public abstract class Scene extends IDable<String> implements GUICloseListener {
	// Attributes
	private Rectangle presence;
	private String currentCamera;
	private IDableCollection<String, Camera> cameras;
	protected ArrayList<SceneLayer> layers;
	protected IDableCollection<String, Entity> entities;
	protected Deque<GUIFrame> guiStack;

	// Constructors and initialization
	public Scene(String id) {
		this.id = id;
	}
	public void init(XMLElement xmlElement) {
		// id
		this.id = xmlElement.getAttribute("id", this.getID());

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

		// currentCamera
		this.currentCamera = xmlElement.getAttribute("default_camera", null);
		
		// cameras
		this.cameras = new IDableCollection<String, Camera>();
		for (int i = 0; i < xmlElement.getChildrenByName(Camera.class.getSimpleName()).size(); i++) {
			XMLElement child = xmlElement.getChildrenByName(Camera.class.getSimpleName()).get(i);

			Camera camera = new Camera();
			camera.init(child);
			this.cameras.add(camera);
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

			// ID entity
			Entity entity;
			if (child.getAttribute("id") == null)
				throw new DataParseException("Entity ID unspecified in area '" + this.id + "'");
			entity = FRIGGame.getInstance().getEntity(child.getAttribute("id"));
			this.addEntityToScene(entity.getID());

			// Entity components
			if (entity.hasComponent(SpacialComponent.class)) {
				float x;
				try {
					x = (float) child.getDoubleAttribute("x",
							entity.spacial().getX());
				} catch (SlickXMLException e) {
					throw new AttributeFormatException("entity_reference", "x",
							child.getAttribute("x"));
				}
				float y;
				try {
					y = (float) child.getDoubleAttribute("y",
							entity.spacial().getY());
				} catch (SlickXMLException e) {
					throw new AttributeFormatException("entity_reference", "y",
							child.getAttribute("y"));
				}

				entity.spacial().moveTo(x, y);
			}
			if (entity.hasComponent(DrawableComponent.class)) {
				entity.drawable().setContinuousAnimation(child
						.getAttribute("animation", entity.drawable().getContinuousAnimationID()));
			}
			if (entity.hasComponent(CharacterComponent.class)) {
				try {
					entity.character().setMoveSpeed((float) child.getDoubleAttribute("speed",
									entity.character().getMoveSpeed()));
				} catch (SlickXMLException e) {
					throw new AttributeFormatException("entity_reference", "speed",
							child.getAttribute("speed"));
				}
				try {
					entity.character().setDirection((float) child.getDoubleAttribute("direction",
									entity.character().getDirection()));
				} catch (SlickXMLException e) {
					throw new AttributeFormatException("entity_reference", "direction",
							child.getAttribute("direction"));
				}
			}
		}

		// guiStack
		this.guiStack = new ArrayDeque<GUIFrame>();
	}
	
	// Main loop methods
	public abstract void update(int delta, Input input);
	public void render(Graphics g) {
		for (SceneLayer layer : layers)
			if (layer.getDepth() <= 0)
				layer.render(g, this);
			else
				break;

		for (Entity entity : entities)
			try {
				entity.render(g, this);
			} catch (ComponentException e) {
			}

		for (SceneLayer layer : layers)
			if (layer.getDepth() > 0)
				layer.render(g, this);

		// GUI
		for (GUIFrame frame : guiStack)
			frame.render(g, this);
	}
	public void renderObject(Graphics g, Image image, Rectangle presence) {
		Rectangle drawPresence = this.cameraTransform(presence);
		g.drawImage(
				image,
				drawPresence.getMinX(),
				drawPresence.getMinY(),
				drawPresence.getMaxX(),
				drawPresence.getMaxY(),
				0,
				0,
				image.getWidth(),
				image.getHeight());
	}
	public void renderObject(Graphics g, FRIGAnimation animation, Rectangle presence) {
		Rectangle drawPresence = this.cameraTransform(presence);
		Image image = animation.getCurrentFrame();
		g.drawImage(
				image,
				drawPresence.getMinX(),
				drawPresence.getMinY(),
				drawPresence.getMaxX(),
				drawPresence.getMaxY(),
				0,
				0,
				image.getWidth(),
				image.getHeight());
	}
	public void renderLayer(Graphics g, FRIGAnimation animation, int depth) {
		Rectangle drawPresence = new Rectangle(this.getPresence().getX(), this.getPresence().getY(), this.getPresence().getWidth(), this.getPresence().getHeight());
		float areaCenterX = drawPresence.getCenterX();
		float areaCenterY = drawPresence.getCenterY();
		
		float scale = (float)Math.pow(2, depth);
		drawPresence.scaleGrow(scale, scale);
		drawPresence.setCenterX(areaCenterX - depth * (this.getCurrentCamera().getCenter().getCenterX() - areaCenterX));
		drawPresence.setCenterY(areaCenterY - depth * (this.getCurrentCamera().getCenter().getCenterY() - areaCenterY));
		drawPresence = cameraTransform(drawPresence);
		
		Image image = animation.getCurrentFrame();
		g.drawImage(image,
				drawPresence.getMinX(),
				drawPresence.getMinY(),
				drawPresence.getMaxX(),
				drawPresence.getMaxY(),
				0,
				0,
				image.getWidth(),
				image.getHeight());
	}
	public void renderObjectForeground(Graphics g, FRIGAnimation animation, Rectangle presence) {
		Image image = animation.getCurrentFrame();
		g.drawImage(
				image,
				presence.getMinX(),
				presence.getMinY(),
				presence.getMaxX(),
				presence.getMaxY(),
				0,
				0,
				image.getWidth(),
				image.getHeight());
	}
	public void renderStringBoxForeground(Graphics g, String text, Rectangle box, Font font) {
		// Save old font so it can be reset
		Font oldFont = g.getFont();
		g.setFont(font);
		
		String[] words = text.split(" ");
		int lineNumber = 0;
		int lineWidth = 0;
		for(int i = 0; i < words.length; i++) {
			String word = words[i] + " ";
			if(lineWidth + font.getWidth(word) > box.getWidth()) {
				lineWidth = 0;
				lineNumber++;
			}
			g.drawString(word, box.getX() + lineWidth, box.getY() + lineNumber * font.getHeight("X"));
			lineWidth += font.getWidth(word);
		}
		
		// Reset font
		g.setFont(oldFont);
	}
	public static boolean stringFitsBox(String text, Rectangle box, Font font) {
		int lines = 0;
		Scanner textScanner = new Scanner(text);

		StringBuilder nextLine = new StringBuilder();
		while (textScanner.hasNext()) {
			String word = textScanner.next();

			nextLine.append(word + " ");

			if (font.getWidth(nextLine) >= box.getWidth()) {
				lines++;
				nextLine = new StringBuilder(word + " ");
			}
		}

		lines++;
		textScanner.close();

		return lines * font.getHeight("X") < box.getHeight();
	}

	// Getters and setters
	public Rectangle getPresence() {
		return this.presence;
	}
	public Camera getCurrentCamera() {
		if(this.currentCamera != null)
			return this.cameras.get(currentCamera);
		return this.cameras.iterator().next();
	}
	public IDableCollection<String, Entity> getEntities() {
		return this.entities;
	}

	// Commands
	protected void addEntityToScene(String entityID) {
		entities.add(FRIGGame.getInstance().getEntity(entityID));
	}
	protected void removeEntityFromScene(String entityID) {
		entities.add(FRIGGame.getInstance().getEntity(entityID));
	}
	protected void setMusic(String soundID) {
	}
	protected void playSound(String soundID) {
	}
	private void closeDialog() {
		guiStack.pop();
	}
	private void closeDialogs(String numDialogs) {
		for (int i = 0; i < Integer.parseInt(numDialogs) && !guiStack.isEmpty(); i++)
			guiStack.pop();
	}
	public void closeAllDialogs() {
		while (!guiStack.isEmpty())
			guiStack.pop();
	}

	// Other methods
	public void moveCameraLeft(float increment) {
		getCurrentCamera().moveLeft(increment);
	}
	public void moveCameraRight(float increment) {
		getCurrentCamera().moveRight(increment);
	}
	public void moveCameraUp(float increment) {
		getCurrentCamera().moveUp(increment);
	}
	public void moveCameraDown(float increment) {
		getCurrentCamera().moveDown(increment);
	}
	public void zoomCamera(float scale) {
		getCurrentCamera().zoom(scale);
	}
	public Rectangle cameraTransform(Rectangle presence) {
		return new Rectangle(
				(presence.getX() - getCurrentCamera().getX()) * ((float) FRIGGame.getInstance().getScreenWidth() / getCurrentCamera().getWidth()),
				(presence.getY() - getCurrentCamera().getY()) * ((float) FRIGGame.getInstance().getScreenHeight() / getCurrentCamera().getHeight()),
				presence.getWidth() * ((float) FRIGGame.getInstance().getScreenWidth() / getCurrentCamera().getWidth()),
				presence.getHeight() * ((float) FRIGGame.getInstance().getScreenHeight() / getCurrentCamera().getHeight()));
	}
	public void openGUI(GUIFrame frame) {
		guiStack.push(frame);
		frame.addGUICloseListener(this);
	}

	// Events
	@Override
	public void guiClosed(GUIFrame sender) {
		if (sender == guiStack.peek())
			guiStack.pop();
	}
}
