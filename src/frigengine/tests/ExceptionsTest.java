package frigengine.tests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({ AttributeFormatExceptionTest.class,
		AttributeMissingExceptionTest.class, CommandExceptionTest.class,
		ComponentExceptionTest.class, DataParseExceptionTest.class,
		InvalidTagExceptionTest.class })
public class ExceptionsTest {

}
