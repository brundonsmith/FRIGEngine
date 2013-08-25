package frigengine.core.scene;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import org.newdawn.slick.Font;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Transform;
import org.newdawn.slick.particles.ParticleSystem;

import frigengine.core.FRIGGame;
import frigengine.core.component.*;
import frigengine.core.geom.*;
import frigengine.core.gui.*;
import frigengine.core.idable.*;

public abstract class Scene extends IDable<String> implements GUICloseSubscriber, GUIContext {
	// Attributes
	protected float width;
	protected float height;
	protected String currentCamera;
	protected List<SceneLayer> layers;
	protected Deque<GUIFrame> guiStack;
	protected ParticleSystem particleSystem;

	// Constructors and initialization
	public Scene(String id) {
		this.setId(id);
		this.width = 1;
		this.height = 1;
		this.currentCamera = "";
		this.layers = new ArrayList<SceneLayer>();
		this.guiStack = new LinkedList<GUIFrame>();
		this.particleSystem = new ParticleSystem(Animation.getPlaceholder().getImage(0));
		
		// TEMPORARY ////////////////////////////////////////
		//this.particleSystem.addEmitter(new FireEmitter());
	}
	public abstract void onGainFocus(Scene previousScene);
	public abstract void onLoseFocus(Scene newScene);
	
	// Main loop methods
	public abstract void update(int delta, Input input);
	public void render(Graphics g) {
		// Background layers
		for (SceneLayer layer : layers) {
			if (layer.getElevation() <= 0) {
				this.renderLayer(g, layer.getAnimation(), layer.getElevation());
			} else {
				break;
			}
		}

		// Entities
		List<Entity> entities = new ArrayList<Entity>(Entity.getEntities(this, SpriteComponent.class));
		Collections.sort(entities, new Comparator<Entity>(){
			@Override
			public int compare(Entity a, Entity b) {
				return (int)a.getComponent(PositionComponent.class).getY() - (int)b.getComponent(PositionComponent.class).getY();
			}
		});
		for (Entity entity : entities) {
			this.renderObject(g, entity.getComponent(SpriteComponent.class).getCurrentAnimation(), entity.getComponent(SpriteComponent.class).getWorldDomain(), entity.getComponent(PositionComponent.class).getElevation());
		}

		// Foreground layers
		for (SceneLayer layer : layers) {
			if (layer.getElevation() > 0) {
				this.renderLayer(g, layer.getAnimation(), layer.getElevation());
			}
		}

		// Particle effects
		this.particleSystem.render();

		// GUI
		for (GUIFrame frame : guiStack) {
			frame.render(g, this);
		}
	}
	public void renderObject(Graphics g, Image image, Rectangle domain) {
		Rectangle drawDomain = domain.transform(this.getCurrentCamera().getComponent(CameraComponent.class).getTransform());
		g.drawImage(
				image,
				drawDomain.getMinX(),
				drawDomain.getMinY(),
				drawDomain.getMaxX(),
				drawDomain.getMaxY(),
				0,
				0,
				image.getWidth(),
				image.getHeight());
	}
	public void renderObject(Graphics g, Image image, Rectangle domain, int elevation) {
		Rectangle drawDomain = domain.transform(this.getCurrentCamera().getComponent(CameraComponent.class).getTransform(elevation));
		g.drawImage(
				image,
				drawDomain.getMinX(),
				drawDomain.getMinY(),
				drawDomain.getMaxX(),
				drawDomain.getMaxY(),
				0,
				0,
				image.getWidth(),
				image.getHeight());
	}
	public void renderObject(Graphics g, Animation animation, Rectangle domain) {
		Rectangle drawDomain = domain.transform(this.getCurrentCamera().getComponent(CameraComponent.class).getTransform());
		Image image = animation.getCurrentFrame();
		g.drawImage(
				image,
				drawDomain.getMinX(),
				drawDomain.getMinY(),
				drawDomain.getMaxX(),
				drawDomain.getMaxY(),
				0,
				0,
				image.getWidth(),
				image.getHeight());
	}
	public void renderObject(Graphics g, Animation animation, Rectangle domain, int elevation) {
		Rectangle drawDomain = domain.transform(this.getCurrentCamera().getComponent(CameraComponent.class).getTransform(elevation));
		Image image = animation.getCurrentFrame();
		g.drawImage(
				image,
				drawDomain.getMinX(),
				drawDomain.getMinY(),
				drawDomain.getMaxX(),
				drawDomain.getMaxY(),
				0,
				0,
				image.getWidth(),
				image.getHeight());
	}
	private Rectangle layerDrawDomain = new Rectangle(0,0,0,0);
	public void renderLayer(Graphics g, Animation animation, int elevation) {
		/*
		float areaCenterX = drawDomain.getCenterX();
		float areaCenterY = drawDomain.getCenterY();
		
		float scale = (float)Math.pow(2, elevation);
		drawDomain.scaleGrow(scale, scale);
		drawDomain.setCenterX(areaCenterX - elevation * (this.getCurrentCamera().getComponent(CameraComponent.class).getCenter().getCenterX() - areaCenterX));
		drawDomain.setCenterY(areaCenterY - elevation * (this.getCurrentCamera().getComponent(CameraComponent.class).getCenter().getCenterY() - areaCenterY));
		drawDomain = cameraTransform(drawDomain);
		*/
		
		layerDrawDomain.setX(this.getDomain().getX());
		layerDrawDomain.setY(this.getDomain().getY());
		layerDrawDomain.setWidth(this.getDomain().getWidth());
		layerDrawDomain.setHeight(this.getDomain().getHeight());
		layerDrawDomain = layerDrawDomain.transform(this.getCurrentCamera().getComponent(CameraComponent.class).getTransform(elevation));
		
		Image image = animation.getCurrentFrame();
		g.drawImage(image,
				layerDrawDomain.getMinX(),
				layerDrawDomain.getMinY(),
				layerDrawDomain.getMaxX(),
				layerDrawDomain.getMaxY(),
				0,
				0,
				image.getWidth(),
				image.getHeight());
	}

	// GUIContext
	@Override
	public void drawShape(Graphics g, Shape shape) {
		Shape realShape = shape.transform(Transform.createScaleTransform(FRIGGame.getScreenWidth(), FRIGGame.getScreenHeight()));
		g.draw(realShape);
	}
	@Override
	public void fillShape(Graphics g, Shape shape) {
		Shape realShape = shape.transform(Transform.createScaleTransform(FRIGGame.getScreenWidth(), FRIGGame.getScreenHeight()));
		g.fill(realShape);
	}
	@Override
	public void renderObjectForeground(Graphics g, Image image, Rectangle domain) {
		Rectangle realDomain = domain.transform(Transform.createScaleTransform(FRIGGame.getScreenWidth(), FRIGGame.getScreenHeight()));
		
		g.drawImage(
				image,
				realDomain.getMinX(),
				realDomain.getMinY(),
				realDomain.getMaxX(),
				realDomain.getMaxY(),
				0,
				0,
				image.getWidth(),
				image.getHeight());
	}
	@Override
	public void renderObjectForeground(Graphics g, Animation animation, Rectangle domain) {
		Rectangle realDomain = domain.transform(Transform.createScaleTransform(FRIGGame.getScreenWidth(), FRIGGame.getScreenHeight()));
		Image image = animation.getCurrentFrame();
		g.drawImage(
				image,
				realDomain.getMinX(),
				realDomain.getMinY(),
				realDomain.getMaxX(),
				realDomain.getMaxY(),
				0,
				0,
				image.getWidth(),
				image.getHeight());
	}
	@Override
	public void renderStringBoxForeground(Graphics g, String text, Rectangle domain, Font font) {
		// Save old font so it can be reset
		Font oldFont = g.getFont();
		g.setFont(font);
		
		Rectangle realDomain = domain.transform(Transform.createScaleTransform(FRIGGame.getScreenWidth(), FRIGGame.getScreenHeight()));
		
		String[] words = text.split(" ");
		int lineNumber = 0;
		int lineWidth = 0;
		for(int i = 0; i < words.length; i++) {
			String word = words[i] + " ";
			if(lineWidth + font.getWidth(word) > realDomain.getWidth()) {
				lineWidth = 0;
				lineNumber++;
			}
			g.drawString(word, realDomain.getX() + lineWidth, realDomain.getY() + lineNumber * font.getHeight("X"));
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
	private Rectangle domain = new Rectangle(0,0,0,0);
	public Rectangle getDomain() {
		this.domain.setX(0);
		this.domain.setY(0);
		this.domain.setWidth(this.width);
		this.domain.setHeight(this.height);
		return domain;
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
	public Rectangle cameraTransform(Rectangle domain) {
		return domain.transform(this.getCurrentCamera().getComponent(CameraComponent.class).getTransform());
		/*
		return new Rectangle(
				(domain.getX() - getCurrentCamera().getComponent(CameraComponent.class).getMinX()) * ((float) FRIGGame.getScreenWidth() / getCurrentCamera().getComponent(CameraComponent.class).getWidth()),
				(domain.getY() - getCurrentCamera().getComponent(CameraComponent.class).getMinY()) * ((float) FRIGGame.getScreenHeight() / getCurrentCamera().getComponent(CameraComponent.class).getHeight()),
				domain.getWidth() * ((float) FRIGGame.getScreenWidth() / getCurrentCamera().getComponent(CameraComponent.class).getWidth()),
				domain.getHeight() * ((float) FRIGGame.getScreenHeight() / getCurrentCamera().getComponent(CameraComponent.class).getHeight()));
				*/
	}
	public void openGUI(GUIFrame frame) {
		guiStack.push(frame);
		this.subscribeTo(frame);
	}
	public void closeGUI() {
		guiStack.pop();
	}
	public void closeGUI(GUIFrame guiFrame) {
		guiStack.remove(guiFrame);
	}
	public void closeGUIs(int numFrames) {
		for (int i = 0; i < numFrames && !guiStack.isEmpty(); i++) {
			guiStack.pop();
		}
	}
	public void closeAllGUIs() {
		while (!guiStack.isEmpty()) {
			guiStack.pop();
		}
	}
	public void addEntity(String entityId) {
		if(Entity.getEntity(entityId).getScene() != null) {
			Entity.getEntity(entityId).getScene().removeEntity(entityId);
		}
		Entity.getEntity(entityId).setScene(this);
	}
	public void addEntity(Entity entity) {
		if(entity.getScene() != null) {
			entity.getScene().removeEntity(entity);
		}
		entity.setScene(this);
	}
	public void removeEntity(String entityId) {
		Entity.getEntity(entityId).setScene(null);
	}
	public void removeEntity(Entity entity) {
		entity.setScene(null);
	}
	protected void setMusic(String soundId) {
	}
	protected void playSound(String soundId) {
	}
	

	// Events
	@Override
	public void subscribeTo(GUIFrame reporter) {
		reporter.addGUICloseListener(this);
	}
	@Override
	public void reportedGuiClosed(GUIFrame source) {
		if (source == guiStack.peek()) {
			guiStack.pop();
		}
	}
}
