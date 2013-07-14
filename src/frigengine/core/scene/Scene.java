package frigengine.core.scene;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Deque;
import java.util.List;
import java.util.Scanner;

import org.newdawn.slick.Font;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Rectangle;

import frigengine.core.*;
import frigengine.core.component.*;
import frigengine.core.gui.*;
import frigengine.core.idable.*;

public abstract class Scene extends IDable<String> implements GUICloseListener {
	// Attributes
	protected float width;
	protected float height;
	protected String currentCamera;
	protected List<SceneLayer> layers;
	protected Deque<GUIFrame> guiStack;

	// Constructors and initialization
	public Scene(String id) {
		this.setId(id);
	}
	
	// Main loop methods
	public abstract void update(int delta, Input input);
	public void render(org.newdawn.slick.Graphics g) {
		for (SceneLayer layer : layers) {
			if (layer.getDepth() <= 0) {
				this.renderLayer(g, layer.getAnimation(), layer.getDepth());
			} else {
				break;
			}
		}

		List<Entity> entities = new ArrayList<Entity>(Entity.getEntities(this, SpriteComponent.class));
		Collections.sort(entities, new Comparator<Entity>(){
			@Override
			public int compare(Entity a, Entity b) {
				return (int)a.getComponent(PositionComponent.class).getY() - (int)b.getComponent(PositionComponent.class).getY();
			}
		});
		for (Entity entity : entities) {
			this.renderObject(g, entity.getComponent(SpriteComponent.class).getCurrentAnimation(), entity.getComponent(SpriteComponent.class).getWorldPresence());
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
	public void renderObject(org.newdawn.slick.Graphics g, Image image, Rectangle presence) {
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
	public void renderObject(org.newdawn.slick.Graphics g, Animation animation, Rectangle presence) {
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
	public void renderLayer(org.newdawn.slick.Graphics g, Animation animation, int depth) {
		Rectangle drawPresence = new Rectangle(this.getPresence().getX(), this.getPresence().getY(), this.getPresence().getWidth(), this.getPresence().getHeight());
		float areaCenterX = drawPresence.getCenterX();
		float areaCenterY = drawPresence.getCenterY();
		
		float scale = (float)Math.pow(2, depth);
		drawPresence.scaleGrow(scale, scale);
		drawPresence.setCenterX(areaCenterX - depth * (this.getCurrentCamera().getComponent(CameraComponent.class).getCenter().getCenterX() - areaCenterX));
		drawPresence.setCenterY(areaCenterY - depth * (this.getCurrentCamera().getComponent(CameraComponent.class).getCenter().getCenterY() - areaCenterY));
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
	public void renderObjectForeground(org.newdawn.slick.Graphics g, Animation animation, Rectangle presence) {
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
	public void renderStringBoxForeground(org.newdawn.slick.Graphics g, String text, Rectangle box, Font font) {
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
		return new Rectangle(0,0,this.width,this.height);
	}
	public Entity getCurrentCamera() {
		if(this.currentCamera != null) {
			return Entity.getEntity(currentCamera);
		} else {
			return null;
		}
	}
	
	// Operations
	public void moveCameraLeft(float increment) {
		getCurrentCamera().getComponent(CameraComponent.class).moveLeft(increment);
	}
	public void moveCameraRight(float increment) {
		getCurrentCamera().getComponent(CameraComponent.class).moveRight(increment);
	}
	public void moveCameraUp(float increment) {
		getCurrentCamera().getComponent(CameraComponent.class).moveUp(increment);
	}
	public void moveCameraDown(float increment) {
		getCurrentCamera().getComponent(CameraComponent.class).moveDown(increment);
	}
	public void zoomCamera(float scale) {
		getCurrentCamera().getComponent(CameraComponent.class).zoom(scale);
	}
	public Rectangle cameraTransform(Rectangle presence) {
		return new Rectangle(
				(presence.getX() - getCurrentCamera().getComponent(CameraComponent.class).getMinX()) * ((float) FRIGGame.getScreenWidth() / getCurrentCamera().getComponent(CameraComponent.class).getWidth()),
				(presence.getY() - getCurrentCamera().getComponent(CameraComponent.class).getMinY()) * ((float) FRIGGame.getScreenHeight() / getCurrentCamera().getComponent(CameraComponent.class).getHeight()),
				presence.getWidth() * ((float) FRIGGame.getScreenWidth() / getCurrentCamera().getComponent(CameraComponent.class).getWidth()),
				presence.getHeight() * ((float) FRIGGame.getScreenHeight() / getCurrentCamera().getComponent(CameraComponent.class).getHeight()));
	}
	public void openGUI(GUIFrame frame) {
		guiStack.push(frame);
		frame.addGUICloseListener(this);
	}

	// Commands
	protected void addEntityToScene(String entityId) {
		Entity.getEntity(entityId).setScene(this);
	}
	protected void removeEntityFromScene(String entityId) {
		Entity.getEntity(entityId).setScene(null);
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
