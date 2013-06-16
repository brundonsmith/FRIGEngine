package frigengine.entities;

import java.util.Collection;

import frigengine.util.IDable;
import frigengine.util.IDableCollection;

public abstract class Category extends IDable<Class<? extends Category>> {
	// Attributes
	protected String entityId;
	protected IDableCollection<Class<? extends Component>, Component> components;
	
	// Constructors and initialization
	public Category() {
		// auto-assign ID
		this.id = this.getClass();
	}
	public final void init(Entity entity) {
		// entityId
		this.entityId = entity.getId();
		
		// components
		this.components = new IDableCollection<Class<? extends Component>, Component>();
		for(Class<? extends Component> c : this.getRequiredComponents()) {
			this.components.put(entity.components.get(c));
		}
	}
	
	// Getters and setters
	public String getEntityId() {
		return this.entityId;
	}
	public abstract Collection<Class<? extends Component>> getRequiredComponents();
}
