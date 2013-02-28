package frigengine.exceptions;

@SuppressWarnings("serial")
public class AttributeMissingException extends DataParseException {
	// Attributes
	public String elementName;
	public String attributeName;

	// COnstructors and initialization
	public AttributeMissingException(String elementName, String attribute) {
		super("Attribute '" + attribute + "' is required in element '"
				+ elementName + "', but wasn't found");
		this.elementName = elementName;
		this.attributeName = attribute;
	}

	public String getElementName() {
		return this.elementName;
	}

	public String getAttributeName() {
		return this.attributeName;
	}
}
