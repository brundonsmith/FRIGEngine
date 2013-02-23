package frigengine.scene;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;

import frigengine.entities.*;
import frigengine.util.*;

public class Battle extends Scene {
	// Attributes
	public IDableCollection<Entity> playerParty;
	public IDableCollection<Entity> enemies;

	// Constructors and initialization
	public Battle() {
		super("battle");
	}

	public void init(BattleTemplate battleTemplate) {
	}

	// Main loop methods
	@Override
	public void update(GameContainer container, int delta, Input input) {
	}

	public void draw() {
	}
}
