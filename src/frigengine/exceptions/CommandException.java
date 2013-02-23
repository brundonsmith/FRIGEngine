package frigengine.exceptions;

@SuppressWarnings("serial")
public class CommandException extends RuntimeException {
	public CommandException(String message) {
		super(message);
	}
}
