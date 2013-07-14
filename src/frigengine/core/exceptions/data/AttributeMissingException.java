package frigengine.core.exceptions.data;

@SuppressWarnings("serial")
public class AttributeMissingException extends DataParseException {
	// Attributes
	public String elementName;
	public String attributeName;

	// Constructors and initialization
	public AttributeMissingException(String elementName, String attribute) {
		super("Attribute '" + attribute + "' is required in element '" + elementName
				+ "', but wasn't found");
		this.elementName = elementName;
		this.attributeName = attribute;
	}

	// Getters and setters
	public String getElementName() {
		return this.elementName;
	}
	public String getAttributeName() {
		return this.attributeName;
	}
}
