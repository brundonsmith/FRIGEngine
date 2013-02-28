package frigengine.tests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import frigengine.exceptions.tests.AttributeFormatExceptionTest;
import frigengine.exceptions.tests.AttributeMissingExceptionTest;
import frigengine.exceptions.tests.CommandExceptionTest;
import frigengine.exceptions.tests.ComponentExceptionTest;
import frigengine.exceptions.tests.DataParseExceptionTest;
import frigengine.exceptions.tests.InvalidTagExceptionTest;

@RunWith(Suite.class)
@Suite.SuiteClasses({ AttributeFormatExceptionTest.class,
		AttributeMissingExceptionTest.class, CommandExceptionTest.class,
		ComponentExceptionTest.class, DataParseExceptionTest.class,
		InvalidTagExceptionTest.class })
public class ExceptionsTest {

}
