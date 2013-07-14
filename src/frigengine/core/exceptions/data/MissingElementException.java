package frigengine.core.exceptions.data;

@SuppressWarnings("serial")
public class MissingElementException extends DataParseException {
	public MissingElementException(String expectedElement, String parentElement) {
		super("Required element '" + expectedElement + "' was not found inside parent element '" + parentElement + "'");
	}
	public MissingElementException(String expectedElement, String parentElement, int expectedCount) {
		super("Required number of elements '" + expectedElement + "' (requires " + expectedCount + ") were not found inside parent element '" + parentElement + "'");
	}
}
