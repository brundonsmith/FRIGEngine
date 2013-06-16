package frigengine.tests.exceptions;

import junit.framework.Assert;

import org.junit.Test;

import frigengine.exceptions.ComponentException;

public class ComponentExceptionTest {
	private static final String MESSAGE = "message";
	private static ComponentException ex = new ComponentException(MESSAGE);

	@Test
	public void testGetMessage() {
		Assert.assertEquals(MESSAGE, ex.getMessage());
	}
}
