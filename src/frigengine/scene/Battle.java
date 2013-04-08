package frigengine.scene;

import org.newdawn.slick.Input;

import frigengine.entities.*;
import frigengine.util.*;

public class Battle extends Scene {
	// Attributes
	public IDableCollection<String, Entity> playerParty;
	public IDableCollection<String, Entity> enemies;

	// Constructors and initialization
	public Battle() {
		super("battle");
	}
	public static Battle from(BattleTemplate battleTemplate) {
		return null;
	}

	// Main loop methods
	@Override
	public void update(int delta, Input input) {
	}
	@Override
	protected void updatePlayer(int delta, Input input) {
	}
}
