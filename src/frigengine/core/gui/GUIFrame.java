package frigengine.core.gui;

import java.util.ArrayList;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.geom.Rectangle;

import frigengine.core.*;
import frigengine.core.scene.*;


public abstract class GUIFrame implements GUICloseListener {
	// Constants
	private static final int DEFAULT_MARGIN_HORIZONTAL = 50;
	private static final int DEFAULT_MARGIN_VERTICAL = 30;
	
	// Attributes
	protected UnicodeFont font;
	protected Animation background;
	protected Rectangle presence;
	protected int horizontalMargin;
	protected int verticalMargin;
	protected boolean blocksTime;
	protected boolean blocksInput;

	// Constructors and initialization
	public GUIFrame(Rectangle presence) {
		// font
		this.font = FRIGGame.getDefaultFont();
		
		// background
		this.background = FRIGGame.getGuiAsset(this.getClass().getSimpleName());
		
		// presence
		this.presence = presence;
		
		// horizontalMargin
		this.horizontalMargin = DEFAULT_MARGIN_HORIZONTAL;
		
		// verticalMargin
		this.verticalMargin = DEFAULT_MARGIN_VERTICAL;
		
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
		this.horizontalMargin = source.horizontalMargin;
		
		// borderVertical
		this.verticalMargin = source.verticalMargin;
		
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
		return new Rectangle(presence.getX() + horizontalMargin, presence.getY() + verticalMargin,
				presence.getWidth() - horizontalMargin * 2, presence.getHeight() - verticalMargin * 2);
	}
	public Rectangle getRelativeBorderedPresence() {
		return new Rectangle(this.horizontalMargin,this.verticalMargin,this.getBorderedPresence().getWidth(), getBorderedPresence().getHeight());
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
