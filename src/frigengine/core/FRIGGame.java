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

import frigengine.battle.*;
import frigengine.core.component.*;
import frigengine.core.exceptions.data.*;
import frigengine.core.gui.GUIFrame;
import frigengine.core.idable.*;
import frigengine.core.scene.*;
import frigengine.field.*;

public class FRIGGame implements Game {
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
			this.areas.getSelection().update(delta, container.getInput());
		}
		
		// TEMPORARY ///////////////////////////////////////////////////////////////////////
		if(container.getInput().isKeyPressed(Keyboard.KEY_RCONTROL)) {
			this.currentBattle = null;
		}
	}
	@Override
	public void render(GameContainer container, Graphics g) {
		if (this.currentBattle != null) {		// If in battle
			this.currentBattle.render(g);
		} else {										// If in field
			this.areas.getSelection().render(g);
		}
	}

	// Getters and setters
	@Override
	public String getTitle() {
		return this.title;
	}
	
	// Global
	public static String getGameTitle() {
		return FRIGGame.instance.getTitle();
	}
	public static int getScreenWidth() {
		return instance.container.getWidth();
	}
	public static int getScreenHeight() {
		return instance.container.getHeight();
	}
	public static UnicodeFont getDefaultFont() {
		return instance.defaultFont;
	}
	public static Scene getCurrentScene() {
		if(FRIGGame.instance.currentBattle != null) {
			return FRIGGame.instance.currentBattle;
		} else {
			return FRIGGame.instance.areas.getSelection();
		}
	}
	public static void changeArea(String areaId) {
		Scene oldScene = FRIGGame.instance.areas.getSelection();
		FRIGGame.instance.areas.select(areaId);
		Scene newScene = FRIGGame.instance.areas.getSelection();
		
		oldScene.onLoseFocus(newScene);
		newScene.onGainFocus(oldScene);
	}
	public static void startBattle(String battleTemplateId) {
		BattleTemplate battleTemplate = FRIGGame.instance.battleTemplates.get(battleTemplateId);

		FRIGGame.instance.currentBattle = new Battle();
		FRIGGame.instance.currentBattle.init(battleTemplate);
		
		Scene oldScene = FRIGGame.instance.areas.getSelection();
		Scene newScene =FRIGGame.instance.currentBattle;
		
		oldScene.onLoseFocus(newScene);
		newScene.onGainFocus(oldScene);
	}
	public static void endBattle() {
		Scene oldScene = FRIGGame.instance.currentBattle;
		FRIGGame.instance.currentBattle = null;
		Scene newScene = FRIGGame.instance.areas.getSelection();
		
		oldScene.onLoseFocus(newScene);
		newScene.onGainFocus(oldScene);
	}
	public static Animation getGuiAsset(String id) {
		return !FRIGGame.instance.guiAssets.contains(id) ? Animation.getPlaceholder() : FRIGGame.instance.guiAssets.get(id).copy();
	}
	public static void openGUI(GUIFrame guiFrame) {
		FRIGGame.instance.getCurrentScene().openGUI(guiFrame);
	}
	public static void closeGUI(GUIFrame guiFrame) {
		FRIGGame.instance.getCurrentScene().closeGUI(guiFrame);
	}
	
	
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
}
