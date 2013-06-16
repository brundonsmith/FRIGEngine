package frigengine.exceptions.data;

@SuppressWarnings("serial")
public class MissingFileException extends DataParseException {
	public MissingFileException(String filename) {
		super("Expected file '" + filename + "' could not be found or was not a valid file");
	}
	public MissingFileException(String filename, Class<?> requesterType, String requesterId) {
		super("Expected file '" + filename + "', requested by " + requesterType.getSimpleName() + " '" + requesterId + "', could not be found or was not a valid file");
	}
}
