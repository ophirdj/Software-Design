package ac.il.technion.twc;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Test;

/**
 * Test that data is stored persistently.
 * 
 * @author Ophir De Jager
 * 
 */
public class PersistenceTest {

	private static final String[] lines = new String[] {
			"04/04/2014 12:00:00, iddqd", "05/04/2014 12:00:00, idkfa, iddqd",
			"06/04/2014 13:00:00, 593393706" };

	private TwitterKnowledgeCenter $;

	/**
	 * Cleanup.
	 * 
	 * @throws Exception
	 */
	@After
	public void tearDown() throws Exception {
		$.cleanPersistentData();
	}

	/**
	 * Test method for {@link TwitterKnowledgeCenter#setupIndex()}
	 * 
	 * @throws Exception
	 */
	@Test
	public final void shouldReturnCorrectResultsAfterShutdownBetweenImportDataAndSetupIndex()
			throws Exception {
		new TwitterKnowledgeCenter().importData(lines);
		// system shutdown... now back online
		$ = new TwitterKnowledgeCenter();
		$.setupIndex();
		assertEquals("86400000", $.getLifetimeOfTweets("iddqd"));
		assertArrayEquals(new String[] { "1,0", "0,0", "0,0", "0,0", "0,0",
				"1,0", "1,1" }, $.getDailyHistogram());
	}

	/**
	 * Test method for {@link TwitterKnowledgeCenter#setupIndex()}
	 * 
	 * @throws Exception
	 */
	@Test
	public final void shouldBeAbleToImportMoreTweetsAfterShutdownBetweenImportDataAndSetupIndex()
			throws Exception {
		new TwitterKnowledgeCenter().importData(lines);
		// system shutdown... now back online
		$ = new TwitterKnowledgeCenter();
		$.setupIndex();
		$.importData(new String[] { "06/04/2014 11:00:00, 40624256" });
		$.setupIndex();
		assertArrayEquals(new String[] { "2,0", "0,0", "0,0", "0,0", "0,0",
				"1,0", "1,1" }, $.getDailyHistogram());
	}

}
