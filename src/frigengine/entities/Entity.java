package frigengine.entities;

import org.newdawn.slick.util.xml.XMLElement;

import frigengine.commands.CommandInstance;
import frigengine.exceptions.data.InvalidTagException;
import frigengine.util.IDable;
import frigengine.util.IDableCollection;
import frigengine.util.Initializable;

public class Entity extends IDable<String> implements Initializable {
	// Constants
	private static final Class<?>[] knownComponents = new Class<?>[]{
		PositionData.class,
		PhysicsData.class,
		GraphicsData.class,
		CharacterData.class,
		BattleData.class
	};
	private static final Class<?>[] knownCategories = new Class<?>[] {
		Positionable.class,
		Physical.class,
		Drawable.class,
		Character.class,
		Battleable.class
	};
	
	// Attributes
	private String name;
	IDableCollection<Class<? extends Component>, Component> components;
	private IDableCollection<Class<? extends Category>, Category> categories;
	
	// Constructors and initialization
	public Entity(String id) {
		this.id = id;
	}
	@Override
	public void init(XMLElement xmlElement) {
		// Check element name
		if (!xmlElement.getName().equals(this.getClass().getSimpleName())) {
			throw new InvalidTagException(this.getClass().getSimpleName(), xmlElement.getName());
		}

		// id
		this.id = xmlElement.getAttribute("id", this.getId());
		
		// name
		this.name = xmlElement.getAttribute("name", this.id);
		
		// components
		this.components = new IDableCollection<Class<? extends Component>, Component>();
		for (Class<?> componentType : knownComponents) {
			if (xmlElement.getChildrenByName(componentType.getSimpleName()).size() >= 1) {
				XMLElement componentElement = xmlElement.getChildrenByName(componentType.getSimpleName()).get(0);

				Component newComponent;
				if (componentElement.getName().equals(PositionData.class.getSimpleName())) {
					newComponent = new PositionData();
				} else if (componentElement.getName().equals(PhysicsData.class.getSimpleName())) {
					newComponent = new PhysicsData();
				} else if (componentElement.getName().equals(GraphicsData.class.getSimpleName())) {
					newComponent = new GraphicsData();
				} else if (componentElement.getName().equals(CharacterData.class.getSimpleName())) {
					newComponent = new CharacterData();
				} else if (componentElement.getName().equals(BattleData.class.getSimpleName())) {
					newComponent = new BattleData();
				} else {
					throw new InvalidTagException("valid component name",
							componentElement.getName());
				}

				newComponent.init(componentElement);
				this.components.put(newComponent);
			}
		}
		
		// categories
		this.categories = new IDableCollection<Class<? extends Category>, Category>();
		for(Class<?> category : knownCategories) {
			// get new instance of each category
			Category newCategory;
			try {
				newCategory = (Category)category.newInstance();
			} catch (IllegalAccessException | InstantiationException e) { // if can't instantiate, wrong class is in categories; warn and skip to next one
				System.err.println("WARNING: There is a problem with the list of known categories");
				continue;
			}
			
			// check if it fits, and if so add it
			boolean fits = true;
			for(Class<? extends Component> component : newCategory.getRequiredComponents()) {
				if(!this.has(component)) {
					fits = false;
				}
			}
			if(fits) {
				newCategory.init(this);
				this.categories.put(newCategory);
			}
		}
	}
	
	// Getters and setters
	public String getName() {
		return this.name;
	}
	
	// Access
	public boolean has(Class<? extends Component> component) {
		return components.contains(component);
	}
	public Component get(Class<? extends Component> component) {
		return components.get(component);
	}
	public boolean is(Class<? extends Category> category) {
		return categories.contains(category);
	}
	public Category as(Class<? extends Category> category) {
		return categories.get(category);
	}

	// Commands
	public void executeCommand(CommandInstance command) {
		/*
		if (this.is(ScriptableComponent.class))
			this.scriptable().executeCommand(command);
		else
			throw new CommandException("Entity '" + this.getID()
					+ "' cannot execute command because it does not have a scriptable component");
					*/
	}
	
	// Utilities
	@Override
	public String toString() {
		String result = "";
		for(Component c : this.components) {
			result += c.toString() + "\n";
		}
		return result;
	}
}
