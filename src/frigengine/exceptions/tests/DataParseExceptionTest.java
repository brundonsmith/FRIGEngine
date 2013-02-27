package frigengine.exceptions.tests;

import junit.framework.Assert;

import org.junit.Test;

import frigengine.exceptions.DataParseException;

public class DataParseExceptionTest {
	private static final String MESSAGE = "message";
	private static DataParseException ex = new DataParseException(MESSAGE);

	@Test
	public void testGetMessage() {
		Assert.assertEquals(MESSAGE, ex.getMessage());
	}
}
