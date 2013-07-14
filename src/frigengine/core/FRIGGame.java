package frigengine.core;

import java.awt.Color;
import java.awt.Font;
import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
import frigengine.core.component.*;
import frigengine.core.exceptions.data.*;
import frigengine.core.idable.*;
import frigengine.core.scene.*;
import frigengine.field.Area;
import frigengine.field.AreaChangeRequestListener;


public class FRIGGame implements Game, AreaChangeRequestListener {
	// Singleton
	private static FRIGGame instance;
	
	// Attributes
	private GameContainer container;
	private String title;
	private UnicodeFont defaultFont;
	private IDableCollection<String, Animation> guiAssets;

	private SelectableCollection<String, Area> areas;
	private Battle currentBattle;
	private IDableCollection<String, BattleTemplate> battleTemplates;

	// Constructors and initialization
	public FRIGGame() {
		FRIGGame.instance = this;
	}
	@Override
	public void init(GameContainer container) throws SlickException {
		// Parse
		XMLParser config = new XMLParser();
		XMLElement rootElement;
		try {
			rootElement = config.parse("content/initialization.xml");
		} catch (SlickException e) {
			throw new MissingFileException("content/initialization.xml");
		}
		
		// Check element name
		if (!rootElement.getName().equals(this.getClass().getSimpleName())) {
			throw new InvalidTagException(this.getClass().getSimpleName(), rootElement.getName());
		}
		
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
		
		// guiAssets
		this.guiAssets = new IDableCollection<String, Animation>();
		XMLElement guiAssetRegistry;
		try {
			guiAssetRegistry = config.parse("content/gui/gui_asset_registry.xml");
		} catch (SlickException e) {
			throw new MissingFileException("content/gui/gui_asset_registry.xml");
		}
		for (int i = 0; i < guiAssetRegistry.getChildrenByName(Animation.class.getSimpleName()).size(); i++) {
			XMLElement child = guiAssetRegistry.getChildrenByName(Animation.class.getSimpleName()).get(i);
			Animation image = new Animation();
			image.init(child);
			this.guiAssets.add(image);
		}
		
		// entities
		for (File xmlFile : FRIGGame.listXMLFilesRecursive("content/entities")) {
			Entity newEntity = new Entity(FRIGGame.idFromAbsolutePath(xmlFile.getAbsolutePath()));
			newEntity.init(config.parse(xmlFile.getAbsolutePath()));
		}
		
		// areas
		this.areas = new SelectableCollection<String, Area>();
		for (File xmlFile : FRIGGame.listXMLFilesRecursive("content/areas")) {
			Area newArea = new Area(FRIGGame.idFromAbsolutePath(xmlFile.getAbsolutePath()));
			newArea.init(config.parse(xmlFile.getAbsolutePath()));
			this.areas.add(newArea);
		}
		this.areas.select((rootElement.getAttribute("starting_area", "")));
		
		// battleTemplates
		this.battleTemplates = new IDableCollection<String, BattleTemplate>();
		for (File xmlFile : FRIGGame.listXMLFilesRecursive("content/battles")) {
			BattleTemplate newBattleTemplate = new BattleTemplate(FRIGGame.idFromAbsolutePath(xmlFile.getAbsolutePath()));
			newBattleTemplate.init(config.parse(xmlFile.getAbsolutePath()));
			this.battleTemplates.add(newBattleTemplate);
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
		if(container.getInput().isKeyPressed(Keyboard.KEY_ESCAPE)) {
			container.exit();
		}
		
		if (currentBattle != null) {				// If in battle
			this.currentBattle.update(delta, container.getInput());
		} else {										// If in field
			this.areas.getSelected().update(delta, container.getInput());
		}
	}
	@Override
	public void render(GameContainer container, Graphics g) {
		if (this.currentBattle != null) {		// If in battle
			this.currentBattle.render(g);
		} else {										// If in field
			this.areas.getSelected().render(g);
		}
	}

	// Getters and setters
	@Override
	public String getTitle() {
		return this.title;
	}
	public static UnicodeFont getDefaultFont() {
		return instance.defaultFont;
	}
	private void setCurrentArea(String areaId) {
		if(this.areas.hasSelection() && !this.areas.getSelectionId().equals(areaId)) {
			this.areas.getSelected().closeAllDialogs();
		}
		this.areas.select(areaId);
	}
	public static void changeArea(String areaID) {
		FRIGGame.instance.setCurrentArea(areaID);
	}
	public static Animation getGuiAsset(String id) {
		return instance.guiAssets.get(id).copy();
	}
	
	// Exposed GameContainer attributes
	public static int getScreenWidth() {
		return instance.container.getWidth();
	}
	public static int getScreenHeight() {
		return instance.container.getHeight();
	}
	
	// Commands
	/*
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
		} else if (command.getCommandType() == CommandType.AREA_COMMAND) {
			this.areas.get(command.getArguments()[0]).executeCommand(command);
		} else if (command.getCommandType() == CommandType.ENTITY_COMMAND) {
			this.entities.get(command.getArguments()[0]).executeCommand(command);
		}
	}
	public void executeScript(String scriptID, String[] args) {
		runningScripts.execute(scripts.get(scriptID).getInstance(args));
	}
	private void startBattle(String battleTemplate) {
	}
*/
	
	// Utilities
	public static String idFromAbsolutePath(String path) {
		String endOfForwardSlash = path.split("/")[path.split("/").length-1];
		String endOfBackSlash = endOfForwardSlash.split("\\\\")[endOfForwardSlash.split("\\\\").length-1];
		return endOfBackSlash.split("\\.")[0];
	}
	private static List<File> listXMLFilesRecursive(String directory) {
		List<File> result = new ArrayList<File>();
		result.addAll(FRIGGame.listXMLFiles(directory));
		for(File subDirectory : FRIGGame.listDirectories(directory)) {
			result.addAll(FRIGGame.listXMLFilesRecursive(subDirectory.getPath()));
		}
		return result;
	}
	private static List<File> listXMLFiles(String directory) {
		return Arrays.asList(new File(directory).listFiles(FRIGGame.xmlFilter()));
	}
	private static List<File> listDirectories(String directory) {
		List<File> directories = new ArrayList<File>();
		for(File f : new File(directory).listFiles()) {
			if(f.isDirectory()) {
				directories.add(f);
			}
		}
		return directories;
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
	public void areaChangeRequested(Area source, String newAreaId) {
 		this.changeArea(newAreaId);
	}
}
