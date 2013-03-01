package frigengine.entities;

import java.util.Queue;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.geom.Point;
import org.newdawn.slick.util.xml.XMLElement;

import frigengine.commands.*;
import frigengine.exceptions.DataParseException;
import frigengine.scene.*;

public class ComponentScriptable extends EntityComponent {
	public static String getComponentID() {
		return "scriptable";
	}

	public String getTagName() {
		return getComponentID();
	}

	public static String[] getComponentDependencies() {
		return new String[] { "character" };
	}

	public static String[] getComponentExclusives() {
		return new String[] {};
	}

	// Attributes
	@SuppressWarnings("unused")
	private Queue<Point> waypoints;

	// Constructors and initialization
	public ComponentScriptable(Entity entity) {
		super(entity);
		this.id = getComponentID();
	}

	@Override
	public void init(XMLElement xmlElement) {
		if (!xmlElement.getName().equals(this.getID()))
			throw new DataParseException(
					"Xml node does not match component type '" + this.id + "'");
	}

	// Main loop methods
	@Override
	public void update(GameContainer container, int delta, Scene area) {
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
			break;
		default:
			
		}
	}

	@SuppressWarnings("unused")
	private void positionEntity(String x, String y) {
		/*
		 * int xVal, yVal;
		 * 
		 * try { xVal = Integer.parseInt(x); } catch (FormatException e) { throw
		 * new
		 * CommandException(com.codeporting.csharp2java.System.msString.concat(
		 * "Argument value '", x,
		 * "', passed as 'x' to command PositionEntity on entity '",
		 * this.getID(), "', isn't a valid integer")); } try { yVal =
		 * Integer.parseInt(y); } catch (FormatException e) { throw new
		 * CommandException(com.codeporting.csharp2java.System.msString.concat(
		 * "Argument value '", y,
		 * "', passed as 'y' to command PositionEntity on entity '",
		 * this.getID(), "', isn't a valid integer")); }
		 * 
		 * ((ComponentSpacial) entity.Components.get("spacial")).moveTo(xVal,
		 * yVal);
		 */
	}

	@SuppressWarnings("unused")
	private void moveEntity(String x, String y) {
		/*
		 * float xVal, yVal;
		 * 
		 * if (!entity.is("character")) throw new CommandException(
		 * com.codeporting.csharp2java.System.msString.concat( "Entity '",
		 * this.getID(),
		 * "' does not have a character component, and therefore cannot have the MoveEntity command called on it"
		 * ));
		 * 
		 * try { xVal = msFloat.parse(x); } catch (FormatException e) { throw
		 * new
		 * CommandException(com.codeporting.csharp2java.System.msString.concat(
		 * "Argument value '", x,
		 * "', passed as 'x' to command MoveEntity on entity '", this.getID(),
		 * "', isn't a valid number")); } try { yVal = msFloat.parse(y); } catch
		 * (FormatException e) { throw new
		 * CommandException(com.codeporting.csharp2java.System.msString.concat(
		 * "Argument value '", y,
		 * "', passed as 'y' to command MoveEntity on entity '", this.getID(),
		 * "', isn't a valid number")); }
		 * 
		 * waypoints.Enqueue(new Vector2D(xVal, yVal));
		 */
	}

	@SuppressWarnings("unused")
	private void giveItem(String itemID) {
	}

	@SuppressWarnings("unused")
	private void giveItems(String itemID, String numItems) {
	}

	@SuppressWarnings("unused")
	private void removeItem(String itemID) {
	}

	@SuppressWarnings("unused")
	private void removeItems(String itemID, String numItems) {
	}
}
