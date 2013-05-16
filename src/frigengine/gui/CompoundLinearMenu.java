package frigengine.gui;

import java.util.HashMap;
import java.util.Map;

import frigengine.scene.Scene;

public abstract class CompoundLinearMenu extends AbstractLinearMenu implements MenuSelectListener {
	// Attributes
	protected Map<MenuItem, AbstractLinearMenu> subMenus;
	
	// Constructors and initialization
	public CompoundLinearMenu(Scene context) {
		super(context);
	
		// subMenus
		this.subMenus = new HashMap<MenuItem, AbstractLinearMenu>();
	}
	
	// Getters and setters
	public void addSubMenu(MenuItem item, AbstractLinearMenu subMenu) {
		this.items.add(item);
		this.subMenus.put(item, subMenu);
		subMenu.addSelectListener(this);
	}
	
	// Controls
	@Override
	public void select() {
		if(this.subMenus.get(this.getSelection()) != null)
			this.context.openGUI(this.subMenus.get(this.getSelection()));
		else // If selection doesn't cause launch of submenu, call event to report it
			this.selectItem(this.getSelection());
	}
	
	// Events
	@Override
	public void itemSelected(AbstractLinearMenu source, MenuItem selected) {
		this.selectItem(selected);
		this.close();
	}
}
