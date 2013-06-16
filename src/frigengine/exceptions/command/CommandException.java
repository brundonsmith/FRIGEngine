package frigengine.exceptions.command;

@SuppressWarnings("serial")
public class CommandException extends RuntimeException {
	public CommandException(String message) {
		super(message);
	}
}
