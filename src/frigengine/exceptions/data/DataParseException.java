package frigengine.exceptions.data;

@SuppressWarnings("serial")
public abstract class DataParseException extends RuntimeException {
	public DataParseException(String message) {
		super(message);
	}
}
