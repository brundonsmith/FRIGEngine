package frigengine.tests.exceptions;

import junit.framework.Assert;

import org.junit.Test;

import frigengine.exceptions.InvalidTagException;

public class InvalidTagExceptionTest {
	private static final String EXPECTED = "expectedTag";
	private static final String PROVIDED = "providedTag";
	private static InvalidTagException ex = new InvalidTagException(EXPECTED, PROVIDED);

	@Test
	public void testGetMessage() {
		Assert.assertEquals("XML tag '" + PROVIDED + "' was given when '" + EXPECTED
				+ "' was expected", ex.getMessage());
	}

	@Test
	public void testGetExpected() {
		Assert.assertEquals(EXPECTED, ex.getExpected());
	}

	@Test
	public void testGetProvided() {
		Assert.assertEquals(PROVIDED, ex.getProvided());
	}
}
