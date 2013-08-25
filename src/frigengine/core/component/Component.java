package frigengine.core.component;

import java.util.Collection;

import org.newdawn.slick.Input;
import org.newdawn.slick.util.xml.XMLElement;

import frigengine.core.exceptions.data.*;
import frigengine.core.idable.*;
import frigengine.core.util.Initializable;

public abstract class Component extends IDable<Class<? extends Component>> implements Initializable, Cloneable {
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
	private Entity containingEntity;

	// Constructors and initialization
	public Component() {
		// Auto-assign ID
		this.setId(this.getClass());
	}
	protected Component(Component other) {
		super(other);
		
		this.containingEntity = other.containingEntity;
	}
	public abstract Component clone();
	public abstract void init(XMLElement xmlElement);

	// Main loop methods
	public void update(int delta, Input input) {
	}

	// Getters and setters
	protected Entity getContainingEntity() {
		return this.containingEntity;
	}
	/*package*/void setContainingEntity(Entity parent) {
		this.containingEntity = parent;
	}
	
	// Meta
	protected boolean hasComponent(Class<? extends Component> component) {
		return this.containingEntity.hasComponent(component);
	}
	protected <T extends Component> T getComponent(Class<T> component) {
		if(this.containingEntity.hasComponent(component)) {
			return this.containingEntity.getComponent(component);
		} else {
			throw new ComponentRequirementException("Component " + this.getClass().getSimpleName() 
					+ " needs to access component " + component.getSimpleName() + ", but it does not exist in entity " +
					this.containingEntity.getId());
		}
		/*
		if (this.requiredComponents().contains(component)) {
			return this.containingEntity.getComponent(component);
		} else {
			throw new ComponentRequirementException("To give component " + this.getClass().getSimpleName() 
					+ " access to component " + component.getSimpleName() + ", add it to " + this.getClass().getSimpleName() 
					+ "'s required components");
		}
		*/
	}
	public abstract Collection<Class<? extends Component>> requiredComponents();
	protected  Collection<Entity> getLocalEntities() {
		return Entity.getEntities(this.containingEntity.getScene());
	}
	protected Collection<Entity> getLocalEntities(Class<? extends Component> component) {
		return Entity.getEntities(this.containingEntity.getScene(), component);
	}
	protected Collection<Entity> getLocalEntities(Collection<Class<? extends Component>> components) {
		return Entity.getEntities(this.containingEntity.getScene(), components);
	}

	// Utilities
	@Override
	public boolean equals(Object other) {
		return this.getClass().equals(other.getClass()) && this.containingEntity.equals(((Component)other).containingEntity);
	}
}
