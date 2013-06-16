package frigengine.tests.exceptions;

import junit.framework.Assert;

import org.junit.Test;

import frigengine.exceptions.CommandException;

public class CommandExceptionTest {
	private static final String MESSAGE = "message";
	private static CommandException ex = new CommandException(MESSAGE);

	@Test
	public void testGetMessage() {
		Assert.assertEquals(MESSAGE, ex.getMessage());
	}
}
