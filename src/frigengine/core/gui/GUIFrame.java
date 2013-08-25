package frigengine.core.gui;

import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.Font;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Transform;

import frigengine.core.*;
import frigengine.core.geom.*;
import frigengine.core.scene.*;

public abstract class GUIFrame implements GUIContext {
	// Constants
	private static final float DEFAULT_MARGIN_HORIZONTAL = 0.01f;
	private static final float DEFAULT_MARGIN_VERTICAL = 0.03f;
	
	// Attributes
	protected UnicodeFont font;
	protected Animation background;
	protected Rectangle domain;
	protected float horizontalMargin;
	protected float verticalMargin;
	protected boolean blocksTime;
	protected boolean blocksInput;
	protected boolean visible;

	// Constructors and initialization
	public GUIFrame(Rectangle domain) {
		// font
		this.font = FRIGGame.getDefaultFont();
		
		// background
		this.background = FRIGGame.getGuiAsset(this.getClass().getSimpleName()) == null ? Animation.getPlaceholder() : FRIGGame.getGuiAsset(this.getClass().getSimpleName());
		
		// domain
		this.domain = domain;
		
		// horizontalMargin
		this.horizontalMargin = DEFAULT_MARGIN_HORIZONTAL;
		
		// verticalMargin
		this.verticalMargin = DEFAULT_MARGIN_VERTICAL;
		
		// blocksTime
		this.blocksTime = false;
		
		// blocksInput
		this.blocksInput = false;
		
		// visible
		this.visible = true;
		
		// guiCloseEventListeners
		this.guiCloseSubscribers = new ArrayList<GUICloseSubscriber>();
	}
	
	// Main loop methods
	public abstract void update(int delta, Input input);
	public abstract void render(Graphics g, GUIContext context);
	// GUIContext
	protected GUIContext context;
	@Override
	public void drawShape(Graphics g, Shape shape) {
		Shape realShape = shape.transform(Transform.createTranslateTransform(this.getBorderedDomain().getX(), this.getBorderedDomain().getY()).concatenate(
										  Transform.createScaleTransform(this.getBorderedDomain().getWidth(), this.getBorderedDomain().getHeight())));
		this.context.drawShape(g, realShape);
	}
	@Override
	public void fillShape(Graphics g, Shape shape) {
		Shape realShape = shape.transform(Transform.createTranslateTransform(this.getBorderedDomain().getX(), this.getBorderedDomain().getY()).concatenate(
				  Transform.createScaleTransform(this.getBorderedDomain().getWidth(), this.getBorderedDomain().getHeight())));
		this.context.fillShape(g, realShape);
	}
	@Override
	public void renderObjectForeground(Graphics g, Image image, Rectangle domain) {
		Rectangle realDomain = domain.transform(Transform.createTranslateTransform(this.getBorderedDomain().getX(), this.getBorderedDomain().getY()).concatenate(
				  								Transform.createScaleTransform(this.getBorderedDomain().getWidth(), this.getBorderedDomain().getHeight())));
		this.context.renderObjectForeground(g, image, realDomain.transform(Transform.createScaleTransform(this.getBorderedDomain().getWidth(), this.getBorderedDomain().getHeight()).concatenate(Transform.createTranslateTransform(this.getBorderedDomain().getX(), this.getBorderedDomain().getY()))));
	}
	@Override
	public void renderObjectForeground(Graphics g, Animation animation, Rectangle domain) {
		Rectangle realDomain = domain.transform(Transform.createTranslateTransform(this.getBorderedDomain().getX(), this.getBorderedDomain().getY()).concatenate(
					Transform.createScaleTransform(this.getBorderedDomain().getWidth(), this.getBorderedDomain().getHeight())));
		this.context.renderObjectForeground(g, animation, realDomain.transform(Transform.createScaleTransform(this.getBorderedDomain().getWidth(), this.getBorderedDomain().getHeight()).concatenate(Transform.createTranslateTransform(this.getBorderedDomain().getX(), this.getBorderedDomain().getY()))));
	}
	@Override
	public void renderStringBoxForeground(Graphics g, String text, Rectangle domain, Font font) {
		Rectangle realDomain = domain.transform(Transform.createTranslateTransform(this.getBorderedDomain().getX(), this.getBorderedDomain().getY()).concatenate(
					Transform.createScaleTransform(this.getBorderedDomain().getWidth(), this.getBorderedDomain().getHeight())));
		this.context.renderStringBoxForeground(g, text, realDomain.transform(Transform.createScaleTransform(this.getBorderedDomain().getWidth(), this.getBorderedDomain().getHeight()).concatenate(Transform.createTranslateTransform(this.getBorderedDomain().getX(), this.getBorderedDomain().getY()))), font);
	}
	
	// Getters and setters
	public Rectangle getDomain() {
		return this.domain;
	}
	private Rectangle borderedDomain = new Rectangle(0,0,0,0);
	public Rectangle getBorderedDomain() {
		this.borderedDomain.setX(this.domain.getX() + this.horizontalMargin);
		this.borderedDomain.setY(this.domain.getY() + this.verticalMargin);
		this.borderedDomain.setWidth(this.domain.getWidth() - this.horizontalMargin * 2);
		this.borderedDomain.setHeight(this.domain.getHeight() - this.verticalMargin * 2);
		return this.borderedDomain;
	}
	public void setDomain(Rectangle domain) {
		this.domain = domain;
	}
	public boolean getBlocksTime() {
		return this.blocksTime;
	}
	public boolean getBlocksInput() {
		return this.blocksInput;
	}
	public boolean isVisible() {
		return this.visible;
	}
	public void hide() {
		this.visible = false;
	}
	public void show() {
		this.visible = true;
	}
	
	// Events
	protected List<GUICloseSubscriber> guiCloseSubscribers;
	protected void reportCancel() {
		for (GUICloseSubscriber listener : guiCloseSubscribers) {
			listener.reportedGuiClosed(this);
		}
	}
	public final void addGUICloseListener(GUICloseSubscriber listener) {
		guiCloseSubscribers.add(listener);
	}
}
