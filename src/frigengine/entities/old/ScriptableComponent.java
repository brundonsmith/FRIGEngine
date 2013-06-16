package frigengine.entities.old;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Queue;
import java.util.Set;

import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Point;
import org.newdawn.slick.util.xml.XMLElement;

import frigengine.Scene;
import frigengine.commands.*;
import frigengine.exceptions.CommandArgumentParseException;
import frigengine.exceptions.InvalidTagException;

public class ScriptableComponent extends EntityComponent {
	// Attributes
	private Queue<Point> waypoints;

	// Constructors and initialization
	public ScriptableComponent(Entity entity) {
		super(entity);
	}
	@Override
	public void init(XMLElement xmlElement) {
		if (!xmlElement.getName().equals(this.getClass().getSimpleName()))
			throw new InvalidTagException(this.getClass().getSimpleName(), xmlElement.getName());
	}

	// Main loop methods
	@Override
	public void update(int delta, Input input, Scene area) {
		/*
		 * if (entity.is("character") && waypoints.Count > 0) {
		 * ((ComponentCharacter) entity.Components.get("character"))
		 * .move(waypoints.Peek().directionTo( ((ComponentSpacial)
		 * entity.Components.get("spacial")).Position.Clone())); if
		 * (((ComponentSpacial)
		 * entity.Components.get("spacial")).Position.distanceTo(waypoints
		 * .Peek().Clone()) <= 1) waypoints.Dequeue();
		 * 
		 * }
		 */
	}

	// Commands
	public void executeCommand(CommandInstance command) {
		switch (command.getCommand()) {
		case POSITION_ENTITY:
			this.positionEntity(command.getArgument(1), command.getArgument(2));
			break;
		case MOVE_ENTITY:
			this.moveEntity(command.getArgument(1), command.getArgument(2));
			break;
		case GIVE_ITEM:
			this.giveItem(command.getArgument(1));
			break;
		case GIVE_ITEMS:
			this.giveItems(command.getArgument(1), command.getArgument(2));
			break;
		case REMOVE_ITEM:
			this.removeItem(command.getArgument(1));
			break;
		case REMOVE_ITEMS:
			this.removeItems(command.getArgument(1), command.getArgument(2));
			break;
		default:
			break;
		}
	}
	private void positionEntity(String x, String y) {
		float parsedX;
		try {
			parsedX = Float.parseFloat(x);
		} catch (NumberFormatException e) {
			throw new CommandArgumentParseException(Command.POSITION_ENTITY, 0, x);
		}

		float parsedY;
		try {
			parsedY = Float.parseFloat(y);
		} catch (NumberFormatException e) {
			throw new CommandArgumentParseException(Command.POSITION_ENTITY, 1, y);
		}

		this.entity.spacial().moveTo(parsedX, parsedY);
	}
	private void moveEntity(String x, String y) {
		float parsedX;
		try {
			parsedX = Float.parseFloat(x);
		} catch (NumberFormatException e) {
			throw new CommandArgumentParseException(Command.MOVE_ENTITY, 0, x);
		}

		float parsedY;
		try {
			parsedY = Float.parseFloat(y);
		} catch (NumberFormatException e) {
			throw new CommandArgumentParseException(Command.MOVE_ENTITY, 1, y);
		}

		waypoints.add(new Point(parsedX, parsedY));
	}
	private void giveItem(String itemID) {
	}
	private void giveItems(String itemID, String numItems) {
	}
	private void removeItem(String itemID) {
	}
	private void removeItems(String itemID, String numItems) {
	}

	// EntityComponent
	public static Set<Class<?>> getComponentDependencies() {
		return new HashSet<Class<?>>( Arrays.asList( new Class<?>[] {CharacterComponent.class}) );
	}
	public static Set<Class<?>> getComponentExclusives() {
		return new HashSet<Class<?>>();
	}
}
