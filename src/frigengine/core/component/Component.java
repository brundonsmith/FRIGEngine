package frigengine.core.component;

import java.util.Collection;

import org.newdawn.slick.Input;
import org.newdawn.slick.util.xml.XMLElement;

import frigengine.core.exceptions.data.*;
import frigengine.core.idable.*;
import frigengine.core.util.Initializable;

public abstract class Component extends IDable<Class<? extends Component>> implements Initializable {
	// Static
	protected static Entity getEntity(String id) {
		return Entity.getEntity(id);
	}
	protected static Collection<Entity> getEntities(Class<? extends Component> component) {
		return Entity.getEntities(component);
	}
	protected static Collection<Entity> getEntities(Collection<Class<? extends Component>> components) {
		return Entity.getEntities(components);
	}
	
	// Attributes
	private Entity parent;

	// Constructors and initialization
	public Component() {
		// Auto-assign ID
		this.setId(this.getClass());
	}
	public abstract void init(XMLElement xmlElement);

	// Main loop methods
	public void update(int delta, Input input) {
	}

	// Getters and setters
	protected Entity getParent() {
		return this.parent;
	}
	/*package*/void setParent(Entity parent) {
		this.parent = parent;
	}
	
	// Meta
	protected boolean hasComponent(Class<? extends Component> component) {
		return this.parent.hasComponent(component);
	}
	protected <T extends Component> T getComponent(Class<T> component) {
		if (this.requiredComponents().contains(component)) {
			return this.parent.getComponent(component);
		} else {
			throw new ComponentRequirementException("To give component " + this.getClass().getSimpleName() 
					+ " access to component " + component.getSimpleName() + ", add it to " + this.getClass().getSimpleName() 
					+ "'s required components");
		}
	}
	public abstract Collection<Class<? extends Component>> requiredComponents();
	protected  Collection<Entity> getLocalEntities() {
		return Entity.getEntities(this.parent.getScene());
	}
	protected Collection<Entity> getLocalEntities(Class<? extends Component> component) {
		return Entity.getEntities(this.parent.getScene(), component);
	}
	protected Collection<Entity> getLocalEntities(Collection<Class<? extends Component>> components) {
		return Entity.getEntities(this.parent.getScene(), components);
	}

	// Utilities
	@Override
	public boolean equals(Object other) {
		return this.getClass().equals(other.getClass()) && this.parent.equals(((Component)other).parent);
	}
}
