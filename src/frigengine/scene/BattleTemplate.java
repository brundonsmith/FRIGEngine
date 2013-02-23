package frigengine.scene;

import java.util.ArrayList;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.util.xml.XMLElement;
import org.newdawn.slick.util.xml.XMLParser;

import frigengine.Initializable;
import frigengine.exceptions.DataParseException;
import frigengine.exceptions.InvalidTagException;
import frigengine.util.*;

public class BattleTemplate extends IDable implements Initializable {
	@Override
	public String getTagName() {
		return "battle";
	}
	
	// Attributes
	private ArrayList<String> playerParty;
	private ArrayList<String> enemies;

	// Constructors and initialization
	public BattleTemplate(String id) {
		this.id = id;
	}

	public void init(XMLElement xmlElement) {
		if (!xmlElement.getName().equals("battle"))
			throw new InvalidTagException("battle", xmlElement.getName());

		// Assign attributes
		this.id = xmlElement.getAttribute("id", this.getID());
	}
}
