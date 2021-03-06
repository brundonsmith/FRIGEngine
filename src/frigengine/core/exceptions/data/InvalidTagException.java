package frigengine.core.exceptions.data;


@SuppressWarnings("serial")
public class InvalidTagException extends DataParseException {
	// Attributes
	public String expectedTagName;
	public String providedTagName;

	// Constructors and initialization
	public InvalidTagException(String expected, String provided) {
		super("XML tag '" + provided + "' was given when '" + expected + "' was expected");
		this.expectedTagName = expected;
		this.providedTagName = provided;
	}

	public String getExpected() {
		return this.expectedTagName;
	}

	public String getProvided() {
		return this.providedTagName;
	}
}
