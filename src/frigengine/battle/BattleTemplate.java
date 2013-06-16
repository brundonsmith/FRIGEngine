package frigengine.battle;

import java.util.ArrayList;

import org.newdawn.slick.util.xml.XMLElement;

import frigengine.exceptions.data.InvalidTagException;
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
		// Check element name
		if (!xmlElement.getName().equals(this.getClass().getSimpleName())) {
			throw new InvalidTagException(this.getClass().getSimpleName(), xmlElement.getName());
		}

		// Assign attributes
		this.id = xmlElement.getAttribute("id", (String)this.getId());
	}
}
