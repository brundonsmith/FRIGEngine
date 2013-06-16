package frigengine;

import java.util.ArrayList;
import java.util.Deque;
import java.util.Scanner;

import org.newdawn.slick.Font;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Rectangle;

import frigengine.entities.*;
import frigengine.events.GUICloseListener;
import frigengine.gui.GUIFrame;
import frigengine.gui.MenuItem;
import frigengine.util.*;
import frigengine.util.graphics.Animation;

public abstract class Scene extends IDable<String> implements GUICloseListener {
	// Attributes
	protected Rectangle presence;
	protected SelectableCollection<String, Camera> cameras;
	protected ArrayList<SceneLayer> layers;
	protected IDableCollection<String, Entity> entities;
	protected Deque<GUIFrame> guiStack;

	// Constructors and initialization
	public Scene(String id) {
		this.id = id;
	}
	
	// Main loop methods
	public abstract void update(int delta, Input input);
	public void render(Graphics g) {
		for (SceneLayer layer : layers) {
			if (layer.getDepth() <= 0) {
				this.renderLayer(g, layer.getAnimation(), layer.getDepth());
			} else {
				break;
			}
		}

		for (Entity entity : entities) {
			this.renderObject(g, ((Drawable)entity.as(Drawable.class)).getCurrentAnimation(), ((Drawable)entity.as(Drawable.class)).getWorldPresence());
		}

		for (SceneLayer layer : layers) {
			if (layer.getDepth() > 0) {
				this.renderLayer(g, layer.getAnimation(), layer.getDepth());
			}
		}

		// GUI
		for (GUIFrame frame : guiStack) {
			frame.render(g, this);
		}
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
	public void renderObject(Graphics g, Animation animation, Rectangle presence) {
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
	public void renderLayer(Graphics g, Animation animation, int depth) {
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
	public void renderObjectForeground(Graphics g, Animation animation, Rectangle presence) {
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
		if(this.cameras.hasSelection()) {
			return this.cameras.getSelected();
		} else {
			return this.cameras.iterator().next();
		}
	}
	protected IDableCollection<String, Chunk> getChunks(Class<? extends Category> ... categories) {
		IDableCollection<String, Chunk> result = new IDableCollection<String, Chunk>();
		for(Entity e : this.entities) {
			result.put(new Chunk(e, categories));
		}
		return result;
	}
	
	// Operations
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

	// Commands
	
	protected void addEntityToScene(String entityId) {
		entities.put(FRIGGame.getInstance().getEntity(entityId));
	}
	protected void removeEntityFromScene(String entityId) {
		entities.remove(entityId);
	}
	
	protected void setMusic(String soundId) {
	}
	protected void playSound(String soundId) {
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

	// Events
	@Override
	public void guiClosed(GUIFrame sender, MenuItem selection) {
		if (sender == guiStack.peek()) {
			guiStack.pop();
		}
	}
}
