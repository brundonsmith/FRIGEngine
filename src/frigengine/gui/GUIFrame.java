package frigengine.gui;

import java.util.ArrayList;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.util.xml.XMLElement;

public abstract class GUIFrame {
	// Attributes
	public boolean Pausing;
	public boolean Blocking;

	protected Rectangle presence;
	protected int borderSize;

	// Constructors and initialization
	public GUIFrame() {
	}
	public void init(GUIFrameTemplate template) {

	}
	public abstract void loadContent();

	// Main loop methods
	public abstract void update(GameContainer container, int delta, Input input);
	public abstract void render(GameContainer container, Graphics g);

	// Getters and setters
	protected Rectangle getBorderedArea() {
		return new Rectangle(presence.getX() + borderSize, presence.getY() + borderSize,
				presence.getWidth() - borderSize, presence.getHeight() - borderSize);
	}

	// Close handling
	private ArrayList<GUICloseEventListener> guiCloseEventListeners;

	protected void close() {
		for (GUICloseEventListener listener : guiCloseEventListeners)
			listener.onGUIClose(this);
	}
	public void addGUICloseEventListener(GUICloseEventListener listener) {
		if (guiCloseEventListeners == null)
			guiCloseEventListeners = new ArrayList<GUICloseEventListener>();

		guiCloseEventListeners.add(listener);
	}

	public interface GUICloseEventListener {
		void onGUIClose(GUIFrame sender);
	}
}
