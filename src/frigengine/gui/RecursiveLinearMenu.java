package frigengine.gui;

import java.util.HashMap;
import java.util.Map;

import org.newdawn.slick.Graphics;

import frigengine.Scene;
import frigengine.events.GUICloseListener;

public abstract class RecursiveLinearMenu extends AbstractLinearMenu implements GUICloseListener {
	// Attributes
	protected MenuItem currentlyOpenSubFrame;
	protected Map<MenuItem, GUIFrame> subFrames;
	
	// Constructors and initialization
	public RecursiveLinearMenu() {
		// currentlyOpenSubMenu
		this.currentlyOpenSubFrame = null;
		
		// subMenus
		this.subFrames = new HashMap<MenuItem, GUIFrame>();
	}
	
	// Main loop methods
	@Override
	public void render(Graphics g, Scene scene) {
		this.getSelectedSubFrame().render(g, scene);
	}
	
	// Getters and setters
	@Override
	public void addMenuItem(MenuItem item) {
		super.addMenuItem(item);
		this.subFrames.put(item, null);
	}
	public void addSubMenu(MenuItem item, AbstractLinearMenu subMenu) {
		this.items.add(item);
		this.subFrames.put(item, subMenu);
		subMenu.addGUICloseListener(this);
	}
	public GUIFrame  getSelectedSubFrame() {
		return this.subFrames.get(this.currentlyOpenSubFrame);
	}
	@Override
	public boolean getBlocksTime() {
		return this.getSelectedSubFrame().getBlocksTime();
	}
	@Override
	public boolean getBlocksInput() {
		return this.getSelectedSubFrame().getBlocksInput();
	}
	
	// Controls
	@Override
	public void select() {
		if(this.subFrames.get(this.getSelection()) != null) { // If selection matches a submenu, open it
			this.currentlyOpenSubFrame = this.getSelection();
		} else { // If selection doesn't cause launch of submenu, call event to report it
			this.notifySelect(this.getSelection());
		}
	}
	
	// Events
	public void guiClosed(GUIFrame source, MenuItem selection) {
		if(selection != null) {
			this.notifySelect(selection);
		}
		this.currentlyOpenSubFrame = null;
	}
}
