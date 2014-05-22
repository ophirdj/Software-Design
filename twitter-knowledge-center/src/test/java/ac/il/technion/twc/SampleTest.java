package ac.il.technion.twc;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Small test given by the course staff
 */
public class SampleTest {
	/**
   * 
   */
	TwitterKnowledgeCenter $ = new TwitterKnowledgeCenter();

	/**
	 * Clean data before test
	 * 
	 * @throws Exception
	 */
	@Before
	public void setup() throws Exception {
		$.cleanPersistentData();
	}

	/**
	 * Clean data after test
	 * 
	 * @throws Exception
	 */
	@After
	public void tearDown() throws Exception {
		$.cleanPersistentData();
	}

	/**
	 * @throws Exception
	 */
	@Test
	public void sampleTest() throws Exception {
		String[] lines = new String[] { "04/04/2014 12:00:00, iddqd",
				"05/04/2014 12:00:00, idkfa, iddqd" };
		$.importData(lines);
		lines = new String[] { "06/04/2014 13:00:00, 593393706" };
		$.importData(lines);
		$.setupIndex();
		assertEquals("86400000", $.getLifetimeOfTweets("iddqd"));
		assertArrayEquals(new String[] { "1,0", "0,0", "0,0", "0,0", "0,0",
				"1,0", "1,1" }, $.getDailyHistogram());
		lines = new String[] { "06/04/2014 11:00:00, 40624256" };
		$.importData(lines);
		$.setupIndex();
		assertArrayEquals(new String[] { "2,0", "0,0", "0,0", "0,0", "0,0",
				"1,0", "1,1" }, $.getDailyHistogram());
	}

	/**
	 * @throws Exception
	 */
	@Test(expected = Exception.class)
	public void mySimpleTest() throws Exception {
		$.getLifetimeOfTweets("some id");
	}

	/**
	 * @throws Exception
	 */
	@Test(expected = Exception.class)
	public void myTest() throws Exception {
		String[] lines = new String[] { "04/04/2014 12:00:00, iddqd",
				"05/04/2014 12:00:00, idkfa, iddqd" };
		$.importData(lines);
		lines = new String[] { "06/04/2014 13:00:00, 593393706" };
		$.importData(lines);
		$.setupIndex();
		$.getLifetimeOfTweets("not iddqd");
	}
}