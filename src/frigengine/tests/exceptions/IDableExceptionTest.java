package frigengine.tests.exceptions;

import junit.framework.Assert;

import org.junit.Test;

import frigengine.exceptions.IDableException;

public class IDableExceptionTest {
	private static final String MESSAGE = "message";
	private static IDableException ex = new IDableException(MESSAGE);

	@Test
	public void testGetMessage() {
		Assert.assertEquals(MESSAGE, ex.getMessage());
	}

}
