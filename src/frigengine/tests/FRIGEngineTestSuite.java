package frigengine.tests;

import org.junit.runner.JUnitCore;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({ ExceptionsTest.class, UtilsTest.class })
public class FRIGEngineTestSuite {
	public static void main(String[] args) throws Exception {
		JUnitCore.main(new String[] { "frigengine.tests.FRIGEngineTestSuite" });
	}
}
