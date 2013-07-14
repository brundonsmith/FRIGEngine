package frigengine.core.exceptions.data;

@SuppressWarnings("serial")
public class InvalidIDException extends DataParseException {
	public InvalidIDException(String message) {
		super(message);
	}
}
