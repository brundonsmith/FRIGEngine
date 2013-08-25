package frigengine.core.component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;

import org.newdawn.slick.Input;
import org.newdawn.slick.util.xml.XMLElement;

import frigengine.battle.*;
import frigengine.core.exceptions.component.*;
import frigengine.core.exceptions.data.*;
import frigengine.core.idable.*;
import frigengine.core.scene.*;
import frigengine.core.util.*;
import frigengine.field.*;


public class Entity extends IDable<String> implements Initializable, Iterable<Component> {
	// Constants
	private static final Class<?>[] knownComponents = new Class<?>[]{
		PositionComponent.class,
		ColliderComponent.class,
		SpriteComponent.class,
		CharacterComponent.class,
		BattleComponent.class,
		CameraComponent.class,
		PlayerControllerComponent.class,
		TriggerComponent.class,
		MovementComponent.class,
		DashComponent.class
	};
	
	// Static
	public static IDableCollection<String, Entity> entities = new IDableCollection<String, Entity>();
	public static boolean entityExists(String id) {
		return entities.contains(id);
	}
	public static Entity getEntity(String id) {
		if(Entity.entities.contains(id)) {
			return Entity.entities.get(id);
		} else {
			throw new EntityNotFoundException(id);
		}
	}
	public static Collection<Entity> getEntities(Class<? extends Component> component) {
		Collection<Entity> results = new ArrayList<Entity>();
		for(Entity e : entities) {
			if(e.hasComponent(component)) {
				results.add(e);
			}
		}
		return results;
	}
	public static Collection<Entity> getEntities(Collection<Class<? extends Component>> components) {
		Collection<Entity> results = new ArrayList<Entity>();
		for(Entity e : entities) {
			if(e.hasComponents(components)) {
				results.add(e);
			}
		}
		return results;
	}
	public static Collection<Entity> getEntities(Scene scene) {
		Collection<Entity> results = new ArrayList<Entity>();
		for(Entity e : entities) {
			if(e.scene != null && e.scene.equals(scene)) {
				results.add(e);
			}
		}
		return results;
	}
	public static Collection<Entity> getEntities(Scene scene, Class<? extends Component> component) {
		Collection<Entity> results = new ArrayList<Entity>();
		for(Entity e : entities) {
			if(e.scene != null && e.scene.equals(scene) && e.hasComponent(component)) {
				results.add(e);
			}
		}
		return results;
	}
	public static Collection<Entity> getEntities(Scene scene, Collection<Class<? extends Component>> components) {
		Collection<Entity> results = new ArrayList<Entity>();
		for(Entity e : entities) {
			if(e.scene.equals(scene) && e.hasComponents(components)) {
				results.add(e);
			}
		}
		return results;
	}
	
	// Attributes
	private String name;
	private Scene scene;
	private IDableCollection<Class<? extends Component>, Component> components;
	private Hashtable<String, IDableCollection<Class<? extends Component>, Component>> saveStates;
	
	// Constructors and initialization
 	public Entity() {
		this.setId("entity" + entities.size());
		this.name = this.getId();
		this.components = new IDableCollection<Class<? extends Component>, Component>();
		this.saveStates = new Hashtable<String, IDableCollection<Class<? extends Component>, Component>>();

		Entity.entities.add(this);
	}
	public Entity(String id) {
		this.setId(id);
		this.name = this.getId();
		this.components = new IDableCollection<Class<? extends Component>, Component>();
		this.saveStates = new Hashtable<String, IDableCollection<Class<? extends Component>, Component>>();

		Entity.entities.add(this);
	}
	@Override
	public void init(XMLElement xmlElement) {
		// Check element name
		if (!xmlElement.getName().equals(this.getClass().getSimpleName())) {
			throw new InvalidTagException(this.getClass().getSimpleName(), xmlElement.getName());
		}

		// id
		this.setId(xmlElement.getAttribute("id", this.getId()));
		
		// name
		this.name = xmlElement.getAttribute("name", this.getId());
		
		// components
		Collection<Component> newComponents = new ArrayList<Component>();
		for (Class<?> componentType : knownComponents) {
			if (xmlElement.getChildrenByName(componentType.getSimpleName()).size() > 0) {
				XMLElement componentElement = xmlElement.getChildrenByName(componentType.getSimpleName()).get(0);

				Component newComponent;

				// TODO Figure out a way to dynamically initialize entities using only the knownComponents list
				/*
				if(this.hasComponent((Class<? extends Component>)componentType)) {
					newComponent = this.getComponent((Class<? extends Component>)componentType);
				} else {
					newComponent = (Component) componentType.newInstance();
				}*/
				
				if (componentElement.getName().equals(PositionComponent.class.getSimpleName())) {
					if(this.hasComponent(PositionComponent.class)) {
						newComponent = this.getComponent(PositionComponent.class);
					} else {
						newComponent = new PositionComponent();
					}
				} else if (componentElement.getName().equals(ColliderComponent.class.getSimpleName())) {
					if(this.hasComponent(ColliderComponent.class)) {
						newComponent = this.getComponent(ColliderComponent.class);
					} else {
						newComponent = new ColliderComponent();
					}
				} else if (componentElement.getName().equals(SpriteComponent.class.getSimpleName())) {
					if(this.hasComponent(SpriteComponent.class)) {
						newComponent = this.getComponent(SpriteComponent.class);
					} else {
						newComponent = new SpriteComponent();
					}
				} else if (componentElement.getName().equals(CharacterComponent.class.getSimpleName())) {
					if(this.hasComponent(CharacterComponent.class)) {
						newComponent = this.getComponent(CharacterComponent.class);
					} else {
						newComponent = new CharacterComponent();
					}
				} else if (componentElement.getName().equals(BattleComponent.class.getSimpleName())) {
					if(this.hasComponent(BattleComponent.class)) {
						newComponent = this.getComponent(BattleComponent.class);
					} else {
						newComponent = new BattleComponent();
					}
				} else if (componentElement.getName().equals(CameraComponent.class.getSimpleName())) {
					if(this.hasComponent(CameraComponent.class)) {
						newComponent = this.getComponent(CameraComponent.class);
					} else {
						newComponent = new CameraComponent();
					}
				} else if (componentElement.getName().equals(PlayerControllerComponent.class.getSimpleName())) {
					if(this.hasComponent(PlayerControllerComponent.class)) {
						newComponent = this.getComponent(PlayerControllerComponent.class);
					} else {
						newComponent = new PlayerControllerComponent();
					}
				} else if (componentElement.getName().equals(TriggerComponent.class.getSimpleName())) {
					if(this.hasComponent(TriggerComponent.class)) {
						newComponent = this.getComponent(TriggerComponent.class);
					} else {
						newComponent = new TriggerComponent();
					}
				} else if (componentElement.getName().equals(MovementComponent.class.getSimpleName())) {
					if(this.hasComponent(MovementComponent.class)) {
						newComponent = this.getComponent(MovementComponent.class);
					} else {
						newComponent = new MovementComponent();
					}
				} else if (componentElement.getName().equals(DashComponent.class.getSimpleName())) {
					if(this.hasComponent(DashComponent.class)) {
						newComponent = this.getComponent(DashComponent.class);
					} else {
						newComponent = new DashComponent();
					}
				} else {
					throw new InvalidTagException("valid component name",
							componentElement.getName());
				}
				
				newComponent.init(componentElement);
				newComponents.add(newComponent);
			}
		}
		this.addComponents(newComponents);
	}
	
	// Main loop methods
	public void update(int delta, Input input) {
		for(Component c : this) {
			c.update(delta, input);
		}
	}
	
	// Getters and setters
	public String getName() {
		return this.name;
	}
	public Scene getScene() {
		return this.scene;
	}
	public void setScene(Scene scene) {
		this.scene = scene;
	}
	public <T extends Component> T getComponent(Class<T> component) {
		if(this.components.contains(component)) {
			return (T)this.components.get(component);
		} else {
			throw new ComponentNotFoundException(this, component);
		}
	}
	public void addComponent(Component component) {
		if(this.getIsValidAddition(component)) {
			component.setContainingEntity(this);
			this.components.add(component);
		} else {
			throw new ComponentRequirementException("Cannot add " + component.getClass().getSimpleName() + " to Entity " + this.getId() + " because not all required components exist");
		}
	}
	public void addComponents(Collection<? extends Component> components) {
		if(this.getIsValidAddition(components)) {
			for(Component c : components) {
				c.setContainingEntity(this);
				this.components.add(c);
			}
		} else {
			throw new ComponentRequirementException("Cannot add " + components + " to Entity " + this.getId() + " because not all required components exist");
		}
	}
	public void removeComponent(Class<? extends Component> component) {
		if(this.getIsValidRemoval(component)) {
			this.components.get(component).setContainingEntity(null);
			this.components.remove(component);
		} else {
			throw new ComponentRequirementException("Cannot remove " + component.getSimpleName() + " from Entity " + this.getId() + " because other component(s) depend on it");
		}
	}
	public boolean hasComponent(Class<? extends Component> component) {
		return this.components.contains(component);
	}
	public boolean hasComponents(Collection<Class<? extends Component>> components) {
		return this.components.containsAll(components);
	}
	private boolean getIsValidAddition(Component newComponent) {
		for(Class<? extends Component> requirement : newComponent.requiredComponents()) {
			boolean good = false;
			for(Component existingComponent : this.components) {
				if(requirement.equals(existingComponent.getId())) {
					good = true;
				}
			}
			if(!good) {
				return false;
			}
		}
		return true;
	}
	private boolean getIsValidAddition(Collection<? extends Component> newComponents) {
		for(Component c : newComponents) {
			for(Class<? extends Component> requirement : c.requiredComponents()) {
				boolean good = false;
				for(Component existingComponent : this.components) {
					if(requirement.equals(existingComponent.getId())) {
						good = true;
					}
				}
				for(Component newComponent : newComponents) {
					if(requirement.equals(newComponent.getClass())) {
						good = true;
					}
				}
				if(!good) {
					return false;
				}
			}
		}
		
		return true;
	}
	private boolean getIsValidRemoval(Class<? extends Component> newComponent) {
		for(Component c : this.components) {
			for(Class<? extends Component> requirement : c.requiredComponents()) {
				if(requirement.equals(newComponent)) {
					return false;
				}
			}
		}
		return true;
	}
	
	// Operations
	@SuppressWarnings("unchecked")
	public final void saveState(String stateId) {
		this.saveState(stateId, (Class<? extends Component>[])Entity.knownComponents);
	}
	@SafeVarargs
	public final void saveState(String stateId, Class<? extends Component> ... components) {
		IDableCollection<Class<? extends Component>, Component> state = new IDableCollection<Class<? extends Component>, Component>();
		
		for(Class<? extends Component> component : components) {
			if(this.hasComponent(component)) {
				state.add(this.getComponent(component).clone());
			}
		}
		
		this.saveStates.put(stateId, state);
	}
	public void revertState(String stateId) {
		if(this.saveStates.containsKey(stateId)) {
			for(Component component : this.saveStates.get(stateId)) {
				this.addComponent(component.clone());
			}
		}
	}
	public void deleteState(String stateId) {
		this.saveStates.remove(stateId);
	}
	
	// Utilities
	@Override
	public String toString() {
		String result = this.getId() + ":\n";
		for(Component c : this.components) {
			result += c.toString() + "\n";
		}
		return result;
	}
	@Override
	public Iterator<Component> iterator() {
		return this.components.iterator();
	}
}
