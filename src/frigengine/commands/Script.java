package frigengine.commands;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import frigengine.util.IDable;

public class Script extends IDable<String> {
	// Attributes
	private ArrayList<CommandInstance> commands;

	// Constructors and initialization
	public Script() {
	}
	public void init(String scriptPath) throws FileNotFoundException {
		this.id = IDable.iDFromPath(scriptPath);

		Scanner scriptScanner = new Scanner(new File(scriptPath));

		this.commands = new ArrayList<CommandInstance>();
		while (scriptScanner.hasNextLine())
			this.commands.add(CommandInstance.parseCommand(scriptScanner.nextLine()));

		scriptScanner.close();
	}
	public ScriptThread getInstance(String[] args) {
		ScriptThread instance = new ScriptThread();

		instance.setArguments(args);
		for (CommandInstance c : this.commands)
			instance.addCommand(c);

		return instance;
	}
}
