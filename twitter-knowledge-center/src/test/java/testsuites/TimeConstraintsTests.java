package testsuites;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import ac.il.technion.twc.time.TimeConstraintsTest;

/**
 * @author Ophir De Jager
 * 
 */
@RunWith(Suite.class)
@SuiteClasses({ TimeConstraintsTest.class })
public class TimeConstraintsTests {

}
