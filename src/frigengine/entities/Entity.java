package frigengine.entities;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.util.xml.XMLElement;

import frigengine.Initializable;
import frigengine.commands.*;
import frigengine.exceptions.CommandException;
import frigengine.exceptions.InvalidTagException;
import frigengine.scene.*;
import frigengine.util.*;

public class Entity extends Composable<EntityComponent> implements Initializable {
	@Override
	public String getTagName() {
		return "entity";
	}

	// Attributes
	private String name;

	// Constructors and initialization
	public Entity(String id) {
		this.id = id;
	}
	public void init(XMLElement xmlElement) {
		if (!xmlElement.getName().equals(getTagName()))
			throw new InvalidTagException(getTagName(), xmlElement.getName());

		// Assign attributes
		this.id = xmlElement.getAttribute("id", this.getID());
		this.name = xmlElement.getAttribute("name", this.id);

		// Load and initialize components
		for (int i = 0; i < xmlElement.getChildren().size(); i++) {
			XMLElement componentElement = xmlElement.getChildren().get(i);

			EntityComponent newEntityComponent;
			if(componentElement.getName().equals(ComponentSpacial.getComponentID())) {
				newEntityComponent = new ComponentSpacial(this);
				((ComponentSpacial)newEntityComponent).init(componentElement);
			}
			else if(componentElement.getName().equals(ComponentDrawable.getComponentID())) {
				newEntityComponent = new ComponentDrawable(this);
				((ComponentDrawable)newEntityComponent).init(componentElement);
			}
			else if(componentElement.getName().equals(ComponentPhysical.getComponentID())) {
				newEntityComponent = new ComponentPhysical(this);
				((ComponentPhysical)newEntityComponent).init(componentElement);
			}
			else if(componentElement.getName().equals(ComponentCharacter.getComponentID())) {
				newEntityComponent = new ComponentCharacter(this);
				((ComponentCharacter)newEntityComponent).init(componentElement);
			}
			else if(componentElement.getName().equals(ComponentBattle.getComponentID())) {
				newEntityComponent = new ComponentBattle(this);
				((ComponentBattle)newEntityComponent).init(componentElement);
			}
			else if(componentElement.getName().equals(ComponentScriptable.getComponentID())) {
				newEntityComponent = new ComponentScriptable(this);
				((ComponentScriptable)newEntityComponent).init(componentElement);
			}
			else	
				throw new InvalidTagException("valid component name", componentElement.getName());
			
			Component.checkAdditionValidity(this, newEntityComponent);
			this.addComponent(newEntityComponent);
		}
	}

	// Main loop methods
	public void update(GameContainer container, int delta, Scene scene) {
		for (Component component : this)
			((EntityComponent) component).update(container, delta, scene);
	}
	public void render(GameContainer container, Graphics g, Scene scene) {
		if (this.hasComponent("drawable"))
			((ComponentDrawable) getComponent("drawable")).render(container, g, scene);
	}

	// Getters and setters
	public String getName() {
		return name;
	}

	// Commands
	public void executeCommand(CommandInstance command) {
		if (this.hasComponent("scriptable"))
			((ComponentScriptable) getComponent("scriptable")).executeCommand(command);
		else
			throw new CommandException("Entity '" + this.getID()
					+ "' cannot execute command because it does not have a scriptable component");
	}
}
