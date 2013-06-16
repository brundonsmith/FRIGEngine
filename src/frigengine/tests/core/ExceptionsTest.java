package frigengine.tests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import frigengine.tests.exceptions.AttributeFormatExceptionTest;
import frigengine.tests.exceptions.AttributeMissingExceptionTest;
import frigengine.tests.exceptions.CommandExceptionTest;
import frigengine.tests.exceptions.ComponentExceptionTest;
import frigengine.tests.exceptions.DataParseExceptionTest;
import frigengine.tests.exceptions.IDableExceptionTest;
import frigengine.tests.exceptions.InvalidTagExceptionTest;

@RunWith(Suite.class)
@Suite.SuiteClasses({ AttributeFormatExceptionTest.class, AttributeMissingExceptionTest.class,
		CommandExceptionTest.class, ComponentExceptionTest.class, DataParseExceptionTest.class,
		InvalidTagExceptionTest.class, IDableExceptionTest.class })
public class ExceptionsTest {

}
