package frigengine.exceptions.command;

@SuppressWarnings("serial")
public class CommandParseException extends CommandException {
	public CommandParseException(String message) {
		super(message);
	}
}
