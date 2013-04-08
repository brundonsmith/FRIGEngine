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
				if (componentElement.getName().equals(ComponentSpacial.class.getSimpleName()))
					newEntityComponent = new ComponentSpacial(this);
				else if (componentElement.getName().equals(ComponentDrawable.class.getSimpleName()))
					newEntityComponent = new ComponentDrawable(this);
				else if (componentElement.getName().equals(ComponentPhysical.class.getSimpleName()))
					newEntityComponent = new ComponentPhysical(this);
				else if (componentElement.getName().equals(ComponentCharacter.class.getSimpleName()))
					newEntityComponent = new ComponentCharacter(this);
				else if (componentElement.getName().equals(ComponentScriptable.class.getSimpleName()))
					newEntityComponent = new ComponentScriptable(this);
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
		if (this.hasComponent(ComponentDrawable.class))
			((ComponentDrawable) getComponent(ComponentDrawable.class)).render(g, scene);
	}

	// Getters and setters
	public String getName() {
		return name;
	}

	// Commands
	public void executeCommand(CommandInstance command) {
		if (this.hasComponent(ComponentScriptable.class))
			((ComponentScriptable) this.getComponent(ComponentScriptable.class)).executeCommand(command);
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
