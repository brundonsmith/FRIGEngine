package frigengine.core.component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.newdawn.slick.Input;
import org.newdawn.slick.util.xml.XMLElement;

import frigengine.battle.BattleComponent;
import frigengine.core.exceptions.component.*;
import frigengine.core.exceptions.data.*;
import frigengine.core.idable.*;
import frigengine.core.scene.*;
import frigengine.core.util.Initializable;
import frigengine.field.CharacterComponent;
import frigengine.field.ColliderComponent;
import frigengine.field.PlayerControllerComponent;
import frigengine.field.TriggerComponent;


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
		MovementComponent.class
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
			if(e.scene.equals(scene) && e.hasComponent(component)) {
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
	
	// Constructors and initialization
	public Entity() {
		this.setId("entity" + entities.size());
		this.name = this.getId();
		this.components = new IDableCollection<Class<? extends Component>, Component>();

		Entity.entities.add(this);
	}
	public Entity(String id) {
		this.setId(id);
		this.name = this.getId();
		this.components = new IDableCollection<Class<? extends Component>, Component>();

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
		for (Class<?> componentType : knownComponents) {
			if (xmlElement.getChildrenByName(componentType.getSimpleName()).size() > 0) {
				XMLElement componentElement = xmlElement.getChildrenByName(componentType.getSimpleName()).get(0);

				Component newComponent;

				// TODO Figure out a way to dynamically initialize entities using only the knowncomponents list
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
				} else {
					throw new InvalidTagException("valid component name",
							componentElement.getName());
				}

				newComponent.init(componentElement);
				this.addComponent(newComponent);
			}
		}
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
			component.setParent(this);
			this.components.add(component);
		} else {
			throw new ComponentRequirementException("Cannot add " + component.getClass().getSimpleName() + " to Entity " + this.getId() + " because not all required components exist");
		}
	}
	public void addComponents(Collection<? extends Component> components) {
		if(this.getIsValidAddition(components)) {
			for(Component c : components) {
				c.setParent(this);
				this.components.add(c);
			}
		} else {
			throw new ComponentRequirementException("Cannot add " + components + " to Entity " + this.getId() + " because not all required components exist");
		}
	}
	public void removeComponent(Class<? extends Component> component) {
		if(this.getIsValidRemoval(component)) {
			this.components.get(component).setParent(null);
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
