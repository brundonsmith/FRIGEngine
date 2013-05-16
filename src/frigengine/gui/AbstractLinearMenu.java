package frigengine.gui;

import java.util.ArrayList;
import java.util.List;

import frigengine.scene.Scene;

public abstract class AbstractLinearMenu extends GUIFrame {
	// Attributes
	protected List<MenuItem> items;
	protected int selectedIndex;
	protected boolean isWrapping;
	protected boolean isVertical;
	protected List<MenuSelectListener> selectListeners;
	
	// Constructors and initialization
	public AbstractLinearMenu(Scene context) {
		super(context);
		
		// items
		this.items = new ArrayList<MenuItem>();
		
		// selectedIndex
		this.selectedIndex = 0;
		
		// isWrapping
		this.isWrapping = true;
		
		// isVertical
		this.isVertical = true;
		
		// selectListeners
		this.selectListeners = new ArrayList<MenuSelectListener>();
	}
	
	// Getters and setters
	public int getNumItems() {
		return this.items == null ? 0 : this.items.size();
	}
	public List<MenuItem> getItems() {
		return items;
	}
	public void addItem(MenuItem item) {
		this.items.add(item);
	}
	public MenuItem	getSelection() {
		return this.items.get(selectedIndex);
	}
	public boolean getIsWrapping() {
		return this.isWrapping;
	}
	public boolean getIsVertical() {
		return this.isVertical;
	}
	public boolean getIsHorizontal() {
		return !this.isVertical;
	}

	// Controls
	public void forward() {
		this.selectedIndex++;
		
		if(this.selectedIndex >= this.getNumItems()) {
			if(isWrapping)
				this.selectedIndex = this.selectedIndex % this.getNumItems();
			else
				while(this.selectedIndex >= this.getNumItems())
					this.selectedIndex--;
		}
	}
	public void back() {
		this.selectedIndex--;
		
		if(this.selectedIndex < 0) {
			if(isWrapping)
				this.selectedIndex = (this.selectedIndex + this.getNumItems()) % this.getNumItems();
			else 
				while(this.selectedIndex < 0)
					this.selectedIndex++;
		}
	}
	public void select() {
		for(MenuSelectListener listener : selectListeners)
			listener.itemSelected(this, this.getSelection());
		this.close();
	}
	
	// Events
	public final void addSelectListener(MenuSelectListener listener) {
		selectListeners.add(listener);
	}
	protected final void selectItem(MenuItem selected) {
		for(MenuSelectListener listener : this.selectListeners)
			listener.itemSelected(this, selected);
		this.close();
	}
}
