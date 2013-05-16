package frigengine.entities;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.util.xml.XMLElement;

import frigengine.Initializable;
import frigengine.commands.*;
import frigengine.exceptions.CommandException;
import frigengine.exceptions.DataParseException;
import frigengine.exceptions.InvalidTagException;
import frigengine.scene.*;
import frigengine.util.*;

public class Entity extends Composable<EntityComponent, String> implements Initializable {
	// Attributes
	private String name;

	// Constructors and initialization
	public Entity(String id) {
		this.id = id;
	}
	public void init(XMLElement xmlElement) {
		if (!xmlElement.getName().equals(this.getClass().getSimpleName()))
			throw new InvalidTagException(this.getClass().getSimpleName(), xmlElement.getName());

		// id
		this.id = xmlElement.getAttribute("id", this.getID());
		
		// name
		this.name = xmlElement.getAttribute("name", this.id);

		// components
		for (Class<?> componentType : Component.getRegisteredComponents()) {
			if (xmlElement.getChildrenByName(componentType.getName()).size() > 1)
				throw new DataParseException("Entity '" + this.getID() + "' has more than one "
						+ componentType.getName() + " component defined");
			if (xmlElement.getChildrenByName(componentType.getSimpleName()).size() == 1) {
				XMLElement componentElement = xmlElement.getChildrenByName(componentType.getSimpleName()).get(0);

				EntityComponent newEntityComponent;
				if (componentElement.getName().equals(SpacialComponent.class.getSimpleName()))
					newEntityComponent = new SpacialComponent(this);
				else if (componentElement.getName().equals(DrawableComponent.class.getSimpleName()))
					newEntityComponent = new DrawableComponent(this);
				else if (componentElement.getName().equals(PhysicalComponent.class.getSimpleName()))
					newEntityComponent = new PhysicalComponent(this);
				else if (componentElement.getName().equals(CharacterComponent.class.getSimpleName()))
					newEntityComponent = new CharacterComponent(this);
				else if (componentElement.getName().equals(BattleComponent.class.getSimpleName()))
					newEntityComponent = new BattleComponent(this);
				else if (componentElement.getName().equals(ScriptableComponent.class.getSimpleName()))
					newEntityComponent = new ScriptableComponent(this);
				else
					throw new InvalidTagException("valid component name",
							componentElement.getName());

				Component.checkAdditionValidity(this, newEntityComponent);
				newEntityComponent.init(componentElement);
				this.addComponent(newEntityComponent);
			}
		}
	}

	// Main loop methods
	public void update(int delta, Input input, Scene scene) {
		for (Component component : this)
			((EntityComponent) component).update(delta, input, scene);
	}
	public void render(Graphics g, Scene scene) {
		if (this.hasComponent(DrawableComponent.class))
			this.drawable().render(g, scene);
	}

	// Getters and setters
	public String getName() {
		return name;
	}
	
	// Get components
	public SpacialComponent spacial() {
		return (SpacialComponent)this.getComponent(SpacialComponent.class);
	}
	public DrawableComponent drawable() {
		return (DrawableComponent)this.getComponent(DrawableComponent.class);
	}
	public PhysicalComponent physics() {
		return (PhysicalComponent)this.getComponent(PhysicalComponent.class);
	}
	public CharacterComponent character() {
		return (CharacterComponent)this.getComponent(CharacterComponent.class);
	}
	public ScriptableComponent scriptable() {
		return (ScriptableComponent)this.getComponent(ScriptableComponent.class);
	}
	public BattleComponent battleable() {
		return (BattleComponent)this.getComponent(BattleComponent.class);
	}
	
	// Commands
	public void executeCommand(CommandInstance command) {
		if (this.hasComponent(ScriptableComponent.class))
			this.scriptable().executeCommand(command);
		else
			throw new CommandException("Entity '" + this.getID()
					+ "' cannot execute command because it does not have a scriptable component");
	}

	// Utilities
	@Override
	public String toString() {
		String result = this.getID() + ": {\n";
		for (Component c : this) {
			result += "\t" + c.toString() + "\n";
		}
		return result + "}";
	}
}
