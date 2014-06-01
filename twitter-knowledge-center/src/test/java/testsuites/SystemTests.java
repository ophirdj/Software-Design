package testsuites;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import ac.il.technion.twc.system.PersistenceTest;
import ac.il.technion.twc.system.SampleTest;

/**
 * @author Ophir De Jager
 * 
 */
@RunWith(Suite.class)
@SuiteClasses({ SampleTest.class, PersistenceTest.class })
public class SystemTests {

}
