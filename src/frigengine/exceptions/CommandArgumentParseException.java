package frigengine.exceptions;

import frigengine.commands.Command;

@SuppressWarnings("serial")
public class CommandArgumentParseException extends CommandParseException {
	public CommandArgumentParseException(Command command, int argumentIndex, String argumentValue) {
		super("'" + argumentValue + "' is not a valid argument value for argument " + argumentIndex + " of command '" + command + "'");
	}
}
