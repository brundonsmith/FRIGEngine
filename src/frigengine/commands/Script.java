package frigengine.commands;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import frigengine.util.IDable;

public class Script extends IDable {
	// Attributes
	private ArrayList<CommandInstance> commands;

	// Constructors and initialization
	public Script() {
	}
	public void init(String scriptPath) throws FileNotFoundException {
		this.id = IDable.iDFromPath(scriptPath);

		Scanner scriptScanner = new Scanner(new File(scriptPath));

		commands = new ArrayList<CommandInstance>();
		while (scriptScanner.hasNext())
			commands.add(CommandInstance.parseCommand(scriptScanner.nextLine()));

		scriptScanner.close();
	}

	// Other methods
	public ScriptThread getInstance(String[] args) {
		return null;
	}
}
