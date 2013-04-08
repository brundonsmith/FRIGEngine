package frigengine.scene;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

import org.lwjgl.input.Keyboard;
import org.newdawn.slick.Font;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Line;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.util.xml.SlickXMLException;
import org.newdawn.slick.util.xml.XMLElement;

import frigengine.*;
import frigengine.entities.*;
import frigengine.exceptions.AttributeFormatException;
import frigengine.exceptions.ComponentException;
import frigengine.exceptions.DataParseException;
import frigengine.graphics.BufferPool;
import frigengine.gui.GUICloseListener;
import frigengine.gui.GUIFrame;
import frigengine.gui.SpeechDialog;
import frigengine.util.*;

public abstract class Scene extends IDable<String> implements GUICloseListener {
	// Attributes
	private Rectangle presence;
	private Set<Shape> boundaries;
	private String currentCamera = "camera_1";
	private IDableCollection<String, Camera> cameras;
	private ArrayList<SceneLayer> layers;
	private IDableCollection<String, Entity> entities;
	private Deque<GUIFrame> guiStack;
	private BufferPool bufferPool;

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

		// boundaries
		this.boundaries = new HashSet<Shape>();
		this.boundaries.add(new Line(this.getPresence().getMinX(), this.getPresence().getMinY(), this.getPresence().getMinX(), this.getPresence().getMaxY()));
		this.boundaries.add(new Line(this.getPresence().getMinX(), this.getPresence().getMinY(), this.getPresence().getMaxX(), this.getPresence().getMinY()));
		this.boundaries.add(new Line(this.getPresence().getMaxX(), this.getPresence().getMinY(), this.getPresence().getMaxX(), this.getPresence().getMaxY()));
		this.boundaries.add(new Line(this.getPresence().getMinX(), this.getPresence().getMaxY(), this.getPresence().getMaxX(), this.getPresence().getMaxY()));
		
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
			if (entity.hasComponent(ComponentSpacial.class)) {
				float x;
				try {
					x = (float) child.getDoubleAttribute("x",
							((ComponentSpacial) entity.getComponent(ComponentSpacial.class)).getX());
				} catch (SlickXMLException e) {
					throw new AttributeFormatException("entity_reference", "x",
							child.getAttribute("x"));
				}
				float y;
				try {
					y = (float) child.getDoubleAttribute("y",
							((ComponentSpacial) entity.getComponent(ComponentSpacial.class)).getY());
				} catch (SlickXMLException e) {
					throw new AttributeFormatException("entity_reference", "y",
							child.getAttribute("y"));
				}

				((ComponentSpacial) entity.getComponent(ComponentSpacial.class)).moveTo(x, y);
			}
			if (entity.hasComponent(ComponentDrawable.class)) {
				((ComponentDrawable) entity.getComponent(ComponentDrawable.class)).setContinuousAnimation(child
						.getAttribute("animation", ((ComponentDrawable) entity
								.getComponent(ComponentDrawable.class)).getContinuousAnimationID()));
			}
			if (entity.hasComponent(ComponentCharacter.class)) {
				try {
					((ComponentCharacter) entity.getComponent(ComponentCharacter.class))
							.setMoveSpeed((float) child.getDoubleAttribute("speed",
									((ComponentCharacter) entity.getComponent(ComponentCharacter.class))
											.getMoveSpeed()));
				} catch (SlickXMLException e) {
					throw new AttributeFormatException("entity_reference", "speed",
							child.getAttribute("speed"));
				}
				try {
					((ComponentCharacter) entity.getComponent(ComponentCharacter.class))
							.setDirection((float) child.getDoubleAttribute("direction",
									((ComponentCharacter) entity.getComponent(ComponentCharacter.class))
											.getDirection()));
				} catch (SlickXMLException e) {
					throw new AttributeFormatException("entity_reference", "direction",
							child.getAttribute("direction"));
				}
			}
		}

		// guiStack
		this.guiStack = new ArrayDeque<GUIFrame>();

		// bufferPool
		this.bufferPool = new BufferPool((int) this.presence.getWidth(),
				(int) this.presence.getHeight(), 5);
	}
	
	// Main loop methods
	public void update(int delta, Input input) {
		boolean timeBlocked = false;
		boolean inputBlocked = false;
		
		//////////////////////////////////////////
		// TEMPORARY
		if(input.isKeyPressed(Keyboard.KEY_RSHIFT)) {
			this.openGUI(new SpeechDialog("Hallo thar this is me speaking sup"));
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
	}
	protected abstract void updatePlayer(int delta, Input input);
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
	public Set<Shape> getBoundaries() {
		return this.boundaries;
	}
	public Camera getCurrentCamera() {
		return this.cameras.get(currentCamera);
	}
	public IDableCollection<String, Entity> getEntities() {
		return this.entities;
	}
	public BufferPool getBufferPool() {
		return this.bufferPool;
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
