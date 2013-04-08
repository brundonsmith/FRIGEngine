package frigengine.gui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class AbstractLinearMenu extends GUIFrame {
	// Attributes
	protected List<MenuItem> items;
	protected int selectedIndex;
	protected boolean isWrapping;
	protected boolean isVertical;
	
	// Constructors and initialization
	public AbstractLinearMenu() {
		super();
		
		this.items = null;
		this.selectedIndex = 0;
		this.isWrapping = true;
		this.isVertical = true;
	}
	
	// Getters and setters
	public MenuItem[] getMenuItems() {
		MenuItem[] items = new MenuItem[this.items.size()];
		for(int i = 0; i < items.length; i++)
			items[i] = this.items.get(i);
		return items;
	}
	public void setMenuItems(MenuItem[] items) {
		this.items = Arrays.asList(items);
	}
	public int getNumItems() {
		return this.items == null ? 0 : this.items.size();
	}
	public void addMenuItem(MenuItem item) {
		if(this.items == null)
			this.items = new ArrayList<MenuItem>();
		
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
}
