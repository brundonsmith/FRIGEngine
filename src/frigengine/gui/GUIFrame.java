package frigengine.gui;

import java.util.ArrayList;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.geom.Rectangle;

import frigengine.FRIGAnimation;
import frigengine.FRIGGame;
import frigengine.scene.Scene;

public abstract class GUIFrame {
	// Constants
	private static final int DEFAULT_BORDER_HORIZONTAL = 50;
	private static final int DEFAULT_BORDER_VERTICAL = 30;
	
	// Attributes
	protected UnicodeFont font;
	protected FRIGAnimation background;
	protected Rectangle presence;
	protected int borderHorizontal;
	protected int borderVertical;
	protected boolean blocksTime;
	protected boolean blocksInput;
	private ArrayList<GUICloseListener> guiCloseEventListeners;

	// Constructors and initialization
	public GUIFrame() {
		this.font = FRIGGame.getInstance().getDefaultFont();
		this.background = FRIGGame.getInstance().getGuiAsset(this.getClass().getSimpleName());
		this.presence = null;
		this.borderHorizontal = DEFAULT_BORDER_HORIZONTAL;
		this.borderVertical = DEFAULT_BORDER_VERTICAL;
		this.blocksTime = true;
		this.blocksInput = true;
		this.guiCloseEventListeners = null;
	}
	public GUIFrame(GUIFrame source) {
		this.font = source.font;
		this.background = source.background;
		this.presence = source.getPresence();
		this.borderHorizontal = source.borderHorizontal;
		this.borderVertical = source.borderVertical;
		this.blocksTime = source.blocksTime;
		this.blocksInput = source.blocksInput;
		this.guiCloseEventListeners = source.guiCloseEventListeners;
	}
	/*
	public static GUIFrame from(GUIFrame template) {
		if(template instanceof AbstractDialog)
			return new AbstractDialog((AbstractDialog) template);
		else
			return null;
	}
	*/
	
	// Main loop methods
	public abstract void update(int delta, Input input);
	public abstract void render(Graphics g, Scene scene);

	// Getters and setters
	public Rectangle getPresence() {
		return this.presence;
	}
	public Rectangle getBorderedPresence() {
		return new Rectangle(presence.getX() + borderHorizontal, presence.getY() + borderVertical,
				presence.getWidth() - borderHorizontal * 2, presence.getHeight() - borderVertical * 2);
	}
	public Rectangle getRelativeBorderedPresence() {
		return new Rectangle(this.borderHorizontal,this.borderVertical,this.getBorderedPresence().getWidth(), getBorderedPresence().getHeight());
	}
	public void setPresence(Rectangle presence) {
		this.presence = presence;
	}
	public boolean getBlocksTime() {
		return this.blocksTime;
	}
	public boolean getBlocksInput() {
		return this.blocksInput;
	}
	
	// Close handling
	public void close() {
		for (GUICloseListener listener : guiCloseEventListeners)
			listener.guiClosed(this);
	}
	public void addGUICloseListener(GUICloseListener listener) {
		if (guiCloseEventListeners == null)
			guiCloseEventListeners = new ArrayList<GUICloseListener>();

		guiCloseEventListeners.add(listener);
	}
}
