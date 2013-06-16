package frigengine.gui;

import java.util.ArrayList;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.UnicodeFont;

import frigengine.FRIGGame;
import frigengine.Scene;
import frigengine.events.GUICloseListener;
import  frigengine.util.geom.Rectangle;
import frigengine.util.graphics.Animation;

public abstract class GUIFrame implements GUICloseListener {
	// Constants
	private static final int DEFAULT_BORDER_HORIZONTAL = 50;
	private static final int DEFAULT_BORDER_VERTICAL = 30;
	
	// Attributes
	protected UnicodeFont font;
	protected Animation background;
	protected Rectangle presence;
	protected int borderHorizontal;
	protected int borderVertical;
	protected boolean blocksTime;
	protected boolean blocksInput;

	// Constructors and initialization
	public GUIFrame() {
		// font
		this.font = FRIGGame.getInstance().getDefaultFont();
		
		// background
		this.background = FRIGGame.getInstance().getGuiAsset(this.getClass().getSimpleName());
		
		// presence
		this.presence = null;
		
		// borderHorizontal
		this.borderHorizontal = DEFAULT_BORDER_HORIZONTAL;
		
		// borderVertical
		this.borderVertical = DEFAULT_BORDER_VERTICAL;
		
		// blocksTime
		this.blocksTime = false;
		
		// blocksInput
		this.blocksInput = false;
		
		// guiCloseEventListeners
		this.guiCloseEventListeners = null;
	}
	public GUIFrame(GUIFrame source) {
		// font
		this.font = source.font;
		
		// background
		this.background = source.background;
		
		// presence
		this.presence = source.getPresence();
		
		// borderHorizontal
		this.borderHorizontal = source.borderHorizontal;
		
		// borderVertical
		this.borderVertical = source.borderVertical;
		
		// blocksTime
		this.blocksTime = source.blocksTime;
		
		// blocksInput
		this.blocksInput = source.blocksInput;
		
		// guiCloseEventListeners
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
	
	// Events
	private ArrayList<GUICloseListener> guiCloseEventListeners;
	public final void notifyClose() {
		for (GUICloseListener listener : guiCloseEventListeners) {
			listener.guiClosed(this, null);
		}
	}
	public final void notifySelect(MenuItem menuItem) {
		for (GUICloseListener listener : guiCloseEventListeners) {
			listener.guiClosed(this, menuItem);
		}
	}
	public final void addGUICloseListener(GUICloseListener listener) {
		if (guiCloseEventListeners == null) {
			guiCloseEventListeners = new ArrayList<GUICloseListener>();
		}
		guiCloseEventListeners.add(listener);
	}
}
