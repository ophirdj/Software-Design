package testsuites;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * @author Ophir De Jager
 * 
 */
@RunWith(Suite.class)
@SuiteClasses({ AcceptanceTests.class, SystemTests.class, UnitTests.class })
public class AllButTimeConstraintsTests {

}
