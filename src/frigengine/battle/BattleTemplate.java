package frigengine.battle;

import java.util.ArrayList;

import org.newdawn.slick.util.xml.XMLElement;
import frigengine.Initializable;
import frigengine.exceptions.InvalidTagException;
import frigengine.util.*;

public class BattleTemplate extends IDable<String> implements Initializable {
	// Attributes
	private ArrayList<String> playerParty;
	private ArrayList<String> enemies;

	// Constructors and initialization
	public BattleTemplate(String id) {
		this.id = id;
	}
	public void init(XMLElement xmlElement) {
		if (!xmlElement.getName().equals(BattleTemplate.getTagName()))
			throw new InvalidTagException(BattleTemplate.getTagName(), xmlElement.getName());

		// Assign attributes
		this.id = xmlElement.getAttribute("id", (String)this.getID());
	}
	
	// Utilities
	public static String getTagName() {
		return "battle";
	}
}
