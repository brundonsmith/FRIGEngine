package frigengine.exceptions;

@SuppressWarnings("serial")
public class AttributeFormatException extends DataParseException {
	// Attributes
	public String elementName;
	public String attributeName;
	public String givenValue;

	// Constructors and initialization
	public AttributeFormatException(String element, String attribute, String value) {
		super("Attribute '" + attribute + "' in XML element '" + element
				+ "' was given invalid value '" + value + "'");
		this.elementName = element;
		this.attributeName = attribute;
		this.givenValue = value;
	}
}
