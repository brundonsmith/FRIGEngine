package frigengine;

import java.awt.Color;
import java.awt.Font;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.util.Arrays;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import org.lwjgl.input.Keyboard;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.Game;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;
import org.newdawn.slick.util.xml.SlickXMLException;
import org.newdawn.slick.util.xml.XMLElement;
import org.newdawn.slick.util.xml.XMLParser;

import frigengine.battle.Battle;
import frigengine.battle.BattleTemplate;
import frigengine.commands.*;
import frigengine.entities.*;
import frigengine.exceptions.AttributeFormatException;
import frigengine.exceptions.DataParseException;
import frigengine.exceptions.InvalidTagException;
import frigengine.scene.*;
import frigengine.util.*;

public class FRIGGame implements Game {
	// Singleton
	private static FRIGGame instance;
	public static FRIGGame getInstance() {
		if(instance == null)
			instance = new FRIGGame();
		return instance;
	}

	// Attributes
	private GameContainer container;
	private String title;
	private UnicodeFont defaultFont;

	private IDableCollection<String, Entity> entities;

	private String currentArea;
	private IDableCollection<String, Area> areas;

	private IDableCollection<String, Script> scripts;
	private ThreadPoolExecutor runningScripts;

	private Battle currentBattle;
	private IDableCollection<String, BattleTemplate> battleTemplates;
	
	private IDableCollection<String, FRIGAnimation> guiAssets;

	// Constructors and initialization
	public FRIGGame() {
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
		if (!rootElement.getName().equals(FRIGGame.getTagName()))
			throw new InvalidTagException(FRIGGame.getTagName(), rootElement.getName());
		
		// container
		this.container = container;
		
		// title
		((AppGameContainer) container).setTitle(rootElement.getAttribute("title", "Game"));
		this.title = rootElement.getAttribute("title", "Game");
		
		// defaultFont
		String name = rootElement.getAttribute("font_name", "Calibri");
		int style = Font.PLAIN;
		String styleString = rootElement.getAttribute("font_style", "PLAIN");
		for(String s : styleString.split(" "))
			switch(s) {
			case "BOLD":
				style += Font.BOLD;
				break;
			case "ITALIC":
				style += Font.ITALIC;
				break;
			default:
				style = Font.PLAIN;
			}
		int size;
		try {
			size = rootElement.getIntAttribute("font_size", 30);
		} catch (SlickXMLException e) {
			throw new AttributeFormatException(rootElement.getName(), "font_size",
					rootElement.getAttribute("font_size"));
		}
		this.defaultFont = new UnicodeFont(new Font(name, style, size));
		this.defaultFont.getEffects().add(new ColorEffect(Color.black));
		this.defaultFont.addAsciiGlyphs();
		this.defaultFont.loadGlyphs();
		
		// entities
		this.entities = new IDableCollection<String, Entity>();
		for (String xmlName : new File("content/entities").list(xmlFilter())) {
			Entity newEntity = new Entity(IDable.iDFromPath(xmlName));
			newEntity.init(config.parse("content/entities/" + xmlName));
			this.entities.add(newEntity);
		}
		// areas
		this.areas = new IDableCollection<String, Area>();
		for (String xmlName : new File("content/areas").list(xmlFilter())) {
			Area newArea = new Area(IDable.iDFromPath(xmlName));
			newArea.init(config.parse("content/areas/" + xmlName));
			this.areas.add(newArea);
		}
		// Current area
		this.setCurrentArea(rootElement.getAttribute("starting_area", ""));

		// scripts
		this.scripts = new IDableCollection<String, Script>();
		for (String xmlName : new File("content/scripts").list(xmlFilter())) {
			Script newScript = new Script();
			try {
				newScript.init("content/scripts/" + xmlName);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			this.scripts.add(newScript);
		}
		// runningScripts
		this.runningScripts = (ThreadPoolExecutor) Executors.newCachedThreadPool();

		// battleTemplates
		this.battleTemplates = new IDableCollection<String, BattleTemplate>();
		for (String xmlPath : new File("content/battles").list(xmlFilter())) {
			BattleTemplate newBattleTemplate = new BattleTemplate(IDable.iDFromPath(xmlPath));
			newBattleTemplate.init(config.parse("content/battles/" + xmlPath));
			this.battleTemplates.add(newBattleTemplate);
		}
		
		// guiImages
		this.guiAssets = new IDableCollection<String, FRIGAnimation>();
		XMLElement guiAssetRegistry;
		try {
			guiAssetRegistry = config.parse("content/gui/gui_asset_registry.xml");
		} catch (SlickException e) {
			throw new DataParseException(
					"Game doesn't have a valid gui_asset_registry.xml file in the content/gui directory");
		}
		for (int i = 0; i < guiAssetRegistry.getChildrenByName(FRIGAnimation.class.getSimpleName()).size(); i++) {
			XMLElement child = guiAssetRegistry.getChildrenByName(FRIGAnimation.class.getSimpleName()).get(i);
			FRIGAnimation image = new FRIGAnimation();
			image.init(child);
			this.guiAssets.add(image);
		}

		// A quick update to let everything settle
		this.update(container, 0);
	}
	@Override
	public boolean closeRequested() {
		return true;
	}

	// Main loop methods
	@Override
	public void update(GameContainer container, int delta) {
		if(container.getInput().isKeyPressed(Keyboard.KEY_ESCAPE))
			container.exit();
		
		// Battle or Area
		if (currentBattle != null)
			this.currentBattle.update(delta, container.getInput());
		else
			this.getCurrentArea().update(delta, container.getInput());
	}
	@Override
	public void render(GameContainer container, Graphics g) {
		// Area/Battle
		if (this.currentBattle != null)
			this.currentBattle.render(g);
		else
			this.getCurrentArea().render(g);
	}

	// Getters and setters
	@Override
	public String getTitle() {
		return this.title;
	}
	public UnicodeFont getDefaultFont() {
		return this.defaultFont;
	}
	public Entity getEntity(String id) {
		return this.entities.get(id);
	}
	public boolean entityExists(String id) {
		return this.entities.contains(id);
	}
	public void setCurrentArea(String areaID) {
		if(currentArea != null && !currentArea.equals(areaID))
			getCurrentArea().closeAllDialogs();
		currentArea = areaID;
	}
	public Area getCurrentArea() {
		return this.areas.get(this.currentArea);
	}
	public FRIGAnimation getGuiAsset(String id) {
		return this.guiAssets.get(id).copy();
	}
	
	// Exposed GameContainer attributes
	public int getScreenWidth() {
		return this.container.getWidth();
	}
	public int getScreenHeight() {
		return this.container.getHeight();
	}
	
	// Commands
	public void executeCommand(CommandInstance command) {
		if (command.getCommandType() == CommandType.GAME_COMMAND) {
			switch (command.getCommand()) {
			case EXECUTE_SCRIPT:
				executeScript(
						command.getArgument(0),
						command.getArguments().length == 1 ? null : Arrays.copyOfRange(
								command.getArguments(), 1, command.getArguments().length));
				break;
			case CHANGE_AREA:
				this.changeArea(command.getArgument(0));
				break;
			case START_BATTLE:
				this.startBattle(command.getArgument(0));
				break;
			default:
				break;
			}
		} else if (command.getCommandType() == CommandType.AREA_COMMAND)
			this.areas.get(command.getArguments()[0]).executeCommand(command);
		else if (command.getCommandType() == CommandType.ENTITY_COMMAND)
			this.entities.get(command.getArguments()[0]).executeCommand(command);
	}
	public void executeScript(String scriptID, String[] args) {
		runningScripts.execute(scripts.get(scriptID).getInstance(args));
	}
	private void changeArea(String areaID) {
		this.setCurrentArea(areaID);
	}
	private void startBattle(String battleTemplate) {
	}

	// Utilities
	public static String getTagName() {
		return "game";
	}
	private static FilenameFilter xmlFilter() {
		return new FilenameFilter() {
			public boolean accept(File dir, String name) {
				return name.endsWith(".xml");
			}
		};
	}
}
