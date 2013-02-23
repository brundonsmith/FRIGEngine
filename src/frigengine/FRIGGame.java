package frigengine;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.util.Stack;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.Game;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.util.xml.XMLElement;
import org.newdawn.slick.util.xml.XMLParser;

import frigengine.commands.*;
import frigengine.entities.*;
import frigengine.exceptions.DataParseException;
import frigengine.exceptions.InvalidTagException;
import frigengine.gui.*;
import frigengine.scene.*;
import frigengine.util.*;

public class FRIGGame implements Game, GUIFrame.GUICloseEventListener {
	// Singleton
	public static FRIGGame instance;

	// Attributes
	private String title;

	private String player;
	private IDableCollection<Entity> entities;

	private String currentArea;
	private IDableCollection<Area> areas;

	public IDableCollection<Script> scripts;

	private Stack<GUIFrame> guiStack;
	// private CollectibleCollection<

	private Battle currentBattle;
	private IDableCollection<BattleTemplate> battleTemplates;

	// Constructors and initialization
	public FRIGGame() {
		FRIGGame.instance = this;
		EntityComponent.registerComponents();
	}
	@Override
	public void init(GameContainer container) throws SlickException {
		// Parse
		XMLParser config = new XMLParser();
		XMLElement rootElement;
		try {
			rootElement = config.parse("content/initialization.xml");
		} catch (SlickException e) {
			throw new DataParseException(
					"Game doesn't have a valid initialization.xml file in the content directory");
		}
		if (!rootElement.getName().equals("game"))
			throw new InvalidTagException("game", rootElement.getName());

		// Assign attributes
		((AppGameContainer) container).setTitle(rootElement.getAttribute("title", "Game"));
		this.title = rootElement.getAttribute("title", "Game");
		this.setCurrentArea(rootElement.getAttribute("starting_area", ""));

		// Entities
		entities = new IDableCollection<Entity>();
		for (String xmlName : new File("content/entities").list(xmlFilter())) {
			Entity newEntity = new Entity(IDable.iDFromPath(xmlName));
			newEntity.init(config.parse("content/entities/" + xmlName));
			entities.add(newEntity);
		}
		if (rootElement.getAttribute("player") != null) {
			this.player = rootElement.getAttribute("player");

			if (!entities.contains(this.player))
				throw new DataParseException("Chosen player ID '" + this.player
						+ "' is not an entity in this game");
			if (!entities.get(this.player).hasComponent("character"))
				throw new DataParseException("Chosen player '" + this.player
						+ "' does not have a character component");
		}

		// Areas
		areas = new IDableCollection<Area>();
		for (String xmlName : new File("content/areas").list(xmlFilter())) {
			Area newArea = new Area(IDable.iDFromPath(xmlName));
			newArea.init(config.parse("content/areas/" + xmlName));
			areas.add(newArea);
		}

		// Scripts
		scripts = new IDableCollection<Script>();
		for (String xmlName : new File("content/scripts").list(xmlFilter())) {
			Script newScript = new Script();
			try {
				newScript.init("content/scripts/" + xmlName);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			scripts.add(newScript);
		}

		// GUI
		guiStack = new Stack<GUIFrame>();

		// Battles
		battleTemplates = new IDableCollection<BattleTemplate>();
		for (String xmlPath : new File("content/battles").list(xmlFilter())) {
			BattleTemplate newBattleTemplate = new BattleTemplate(IDable.iDFromPath(xmlPath));
			newBattleTemplate.init(config.parse("content/battles/" + xmlPath));
			battleTemplates.add(newBattleTemplate);
		}
		
		this.update(container, 0);
	}
	@Override
	public boolean closeRequested() {
		return true;
	}

	// Main loop methods
	@Override
	public void update(GameContainer container, int delta) {
		Input noInput = new Input(container.getHeight());
		Input input = container.getInput();
		boolean timeStopped = false;
		boolean inputBlocked = false;

		// GUI
		for (GUIFrame frame : guiStack) {
			frame.update(container, timeStopped ? 0 : delta, inputBlocked ? noInput : input);
			if (frame.Pausing)
				timeStopped = true;
			if (frame.Blocking)
				inputBlocked = true;
		}

		// Battle
		if (currentBattle != null)
			currentBattle
					.update(container, timeStopped ? 0 : delta, inputBlocked ? noInput : input);

		// Area
		updatePlayer(container, timeStopped ? 0 : delta, inputBlocked ? noInput : input);
		getCurrentArea().update(container, timeStopped ? 0 : delta, inputBlocked ? noInput : input);
	}
	@Override
	public void render(GameContainer container, Graphics g) {
		// Area/Battle
		if (currentBattle != null)
			currentBattle.render(container, g);
		else
			getCurrentArea().render(container, g);

		// GUI
		for (GUIFrame frame : guiStack)
			frame.render(container, g);
	}

	// Getters and setters
	@Override
	public String getTitle() {
		return this.title;
	}
	public Entity getPlayer() {
		return entities.get(player);
	}
	public Entity getEntity(String id) {
		return entities.get(id);
	}
	public void setCurrentArea(String areaID) {
		currentArea = areaID;
	}
	public Area getCurrentArea() {
		return areas.get(currentArea);
	}

	// Commands
	public void executeCommand(CommandInstance command) {
		if (command.getCommandType() == CommandType.GAME_COMMAND) {
			switch (command.getCommand()) {
			case OPEN_DIALOG:
				openDialog(command.getArguments()[0]);
				break;
			case CLOSE_DIALOG:
				closeDialog();
				break;
			case CLOSE_ALL_DIALOGS:
				closeAllDialogs();
				break;
			case EXECUTE_SCRIPT:
				executeScript(command.getArguments()[0]);
				break;
			case CHANGE_AREA:
				changeArea(command.getArguments()[0]);
				break;
			case START_BATTLE:
				startBattle(command.getArguments()[0]);
				break;
			default:
				break;
			}
		} else if (command.getCommandType() == CommandType.AREA_COMMAND)
			areas.get(command.getArguments()[0]).executeCommand(command);
		else if (command.getCommandType() == CommandType.ENTITY_COMMAND)
			entities.get(command.getArguments()[0]).executeCommand(command);
	}
	private void openDialog(String dialogTemplateID) {
	}
	private void closeDialog() {
	}
	private void closeDialogs(String numDialogs) {
	}
	private void closeAllDialogs() {
	}
	private void executeScript(String scriptID) {
	}
	private void changeArea(String areaID) {
		setCurrentArea(areaID);
	}
	private void startBattle(String battleTemplate) {
	}

	// Other Methods
	private void updatePlayer(GameContainer container, int delta, Input input) {
		Vector2f movement = new Vector2f();

		if (input.isKeyPressed(Input.KEY_UP))
			movement.add(new Vector2f(0, 1));
		if (input.isKeyPressed(Input.KEY_DOWN))
			movement.add(new Vector2f(0, -1));
		if (input.isKeyPressed(Input.KEY_LEFT))
			movement.add(new Vector2f(-1, 0));
		if (input.isKeyPressed(Input.KEY_RIGHT))
			movement.add(new Vector2f(1, 0));

		((ComponentCharacter) getPlayer().getComponent("character")).move(movement.getTheta());
	}
	private static FilenameFilter xmlFilter() {
		return new FilenameFilter() {
			public boolean accept(File dir, String name) {
				return name.endsWith(".xml");
			}
		};
	}

	// Events
	@Override
	public void onGUIClose(GUIFrame sender) {
		if (sender == guiStack.peek())
			guiStack.pop();
	}
}
