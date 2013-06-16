package frigengine.commands;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

import frigengine.FRIGGame;
import frigengine.exceptions.command.CommandArgumentParseException;

public class ScriptThread implements Runnable {
	// Attributes
	private String[] args;
	private Queue<CommandInstance> commands;

	// Constructors and initialization
	ScriptThread() {
		this.args = null;
		this.commands = new LinkedList<CommandInstance>();
	}
	void setArguments(String[] args) {
		this.args = Arrays.copyOf(args, args.length);
	}
	void addCommand(CommandInstance command) {
		CommandInstance newCommand = new CommandInstance(command);
		for (int i = 0; i < newCommand.getArguments().length; i++) {
			if (newCommand.getArgument(i).matches("\\$[0-9]+*")) {
				newCommand.setArgument(i,
						args[Integer.parseInt(newCommand.getArgument(i).substring(1))]);
			}
		}
		this.commands.add(newCommand);
	}

	// Thread
	@Override
	public void run() {
		while (!this.commands.isEmpty()) {
			if (this.commands.peek().getCommandType() == CommandType.META_COMMAND) {
				switch (this.commands.peek().getCommand()) {
				case WAIT:
					wait(this.commands.peek().getArgument(0));
					break;
				default:
					break;
				}
			} else
				FRIGGame.getInstance().executeCommand(this.commands.peek());

			this.commands.remove();
		}
	}
	private void wait(String seconds) {
		float parsedSeconds;
		try {
			parsedSeconds = Float.parseFloat(seconds);
		} catch (NumberFormatException e) {
			throw new CommandArgumentParseException(Command.WAIT, 0, commands.peek().getArgument(0));
		}
		
		try {
			Thread.sleep((int) Math.round(parsedSeconds * 1000));
		} catch (InterruptedException e) {
		}
	}
}
