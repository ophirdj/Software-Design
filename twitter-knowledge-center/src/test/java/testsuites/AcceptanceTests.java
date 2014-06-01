package testsuites;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import acceptance.AdvancedExample;
import acceptance.RealWorldExample;
import acceptance.SimpleExample;

/**
 * @author Ophir De Jager
 * 
 */
@RunWith(Suite.class)
@SuiteClasses({ SimpleExample.class, AdvancedExample.class,
		RealWorldExample.class })
public class AcceptanceTests {

}
