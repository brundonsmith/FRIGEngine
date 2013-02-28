package frigengine.exceptions.tests;

import junit.framework.Assert;

import org.junit.Test;

import frigengine.exceptions.AttributeFormatException;

public class AttributeFormatExceptionTest {
	private static final String ELEMENT_NAME = "elementName";
	private static final String ATTRIBUTE_NAME = "attributeName";
	private static final String GIVEN_VALUE = "givenValue";
	private static AttributeFormatException ex = new AttributeFormatException(
			ELEMENT_NAME, ATTRIBUTE_NAME, GIVEN_VALUE);

	@Test
	public void testGetMessage() {
		Assert.assertEquals("Attribute '" + ATTRIBUTE_NAME
				+ "' in XML element '" + ELEMENT_NAME
				+ "' was given invalid value '" + GIVEN_VALUE + "'",
				ex.getMessage());
	}

	@Test
	public void testGetExpected() {
		Assert.assertEquals(ELEMENT_NAME, ex.getElementName());
	}

	@Test
	public void testGetProvided() {
		Assert.assertEquals(ATTRIBUTE_NAME, ex.getAttributeName());
	}

	@Test
	public void testGetGivenValue() {
		Assert.assertEquals(GIVEN_VALUE, ex.getGivenValue());
	}
}
