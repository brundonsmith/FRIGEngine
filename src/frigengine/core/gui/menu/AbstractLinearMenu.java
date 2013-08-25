package frigengine.core.gui.menu;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import frigengine.core.*;
import frigengine.core.geom.*;
import frigengine.core.gui.*;
import frigengine.core.scene.Animation;

public abstract class AbstractLinearMenu extends GUIFrame implements MenuCloseSubscriber {
	// Static
	public static Animation defaultSelectionCursor = Animation.getPlaceholder();
	
	// Attributes
	protected List<MenuItem> menuItems;
	protected int selectedIndex;
	protected boolean isWrapping;
	protected boolean isVertical;
	
	// Constructors and initialization
	public AbstractLinearMenu(Rectangle domain) {
		super(domain);

		this.blocksTime = true;
		this.blocksInput = true;
		
		if(AbstractLinearMenu.defaultSelectionCursor.getId() == Animation.PLACEHOLDER_ID) {
			AbstractLinearMenu.defaultSelectionCursor = FRIGGame.getGuiAsset("DefaultSelectionCursor");
		}
		
		// items
		this.menuItems = new ArrayList<MenuItem>();
		
		// selectedIndex
		this.selectedIndex = 0;
		
		// isWrapping
		this.isWrapping = true;
		
		// isVertical
		this.isVertical = true;
		
		// menuCloseEventListeners
		this.menuCloseSubscribers = new ArrayList<MenuCloseSubscriber>();
	}
	
	// Getters and setters
	public int getNumItems() {
		return this.menuItems == null ? 0 : this.menuItems.size();
	}
	public List<MenuItem> getItems() {
		return menuItems;
	}
	public void addMenuItem(MenuItem menuItem) {
		this.menuItems.add(menuItem);
		if(menuItem.getSubMenu() != null) {
			this.subscribeTo(menuItem.getSubMenu());
		}
	}
	public MenuItem	getSelection() {
		return this.menuItems.get(selectedIndex);
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
			if(isWrapping) {
				this.selectedIndex = this.selectedIndex % this.getNumItems();
			} else {
				while(this.selectedIndex >= this.getNumItems()) {
					this.selectedIndex--;
				}
			}
		}
	}
	public void back() {
		this.selectedIndex--;
		
		if(this.selectedIndex < 0) {
			if(isWrapping) {
				this.selectedIndex = (this.selectedIndex + this.getNumItems()) % this.getNumItems();
			} else {
				while(this.selectedIndex < 0) {
					this.selectedIndex++;
				}
			}
		}
	}
	public void cancel() {
		this.reportCancel();
	}
	public void select() {
		if(this.getSelection().getSubMenu() != null) {
			FRIGGame.openGUI(this.getSelection().getSubMenu());
		} else {
			this.reportSelection(this.getSelection());
		}
	}

	// Events
	private ArrayList<MenuCloseSubscriber> menuCloseSubscribers;
	public final void addMenuCloseSubscriber(MenuCloseSubscriber listener) {
		this.menuCloseSubscribers.add(listener);
	}
	@Override
	protected final void reportCancel() {
		for (GUICloseSubscriber listener : this.guiCloseSubscribers) {
			listener.reportedGuiClosed(this);
		}
		for (MenuCloseSubscriber listener : this.menuCloseSubscribers) {
			listener.reportedMenuClose(this, null);
		}
	}
	protected final void reportSelection(MenuItem selection) {
		for (GUICloseSubscriber listener : this.guiCloseSubscribers) {
			listener.reportedGuiClosed(this);
		}
		for (MenuCloseSubscriber listener : this.menuCloseSubscribers) {
			listener.reportedMenuClose(this, selection);
		}
	}

	public void subscribeTo(AbstractLinearMenu source) {
		source.addMenuCloseSubscriber(this);
	}
	public void reportedMenuClose(AbstractLinearMenu source, MenuItem selection) {
		if(selection != null) {
			this.reportSelection(selection);
		}
	}
	
	public String toString() {
		return "menu: " + Arrays.toString(menuItems.toArray()) + "[" + selectedIndex + "]";
	}
}
