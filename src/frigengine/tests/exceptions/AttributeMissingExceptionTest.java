package frigengine.tests.exceptions;

import junit.framework.Assert;

import org.junit.Test;

import frigengine.exceptions.AttributeMissingException;

public class AttributeMissingExceptionTest {
	private static final String ELEMENT_NAME = "elementName";
	private static final String ATTRIBUTE_NAME = "attributeName";
	private static AttributeMissingException ex = new AttributeMissingException(ELEMENT_NAME,
			ATTRIBUTE_NAME);

	@Test
	public void testGetMessage() {
		Assert.assertEquals("Attribute '" + ATTRIBUTE_NAME + "' is required in element '"
				+ ELEMENT_NAME + "', but wasn't found", ex.getMessage());
	}

	@Test
	public void testGetExpected() {
		Assert.assertEquals(ELEMENT_NAME, ex.getElementName());
	}

	@Test
	public void testGetProvided() {
		Assert.assertEquals(ATTRIBUTE_NAME, ex.getAttributeName());
	}
}
