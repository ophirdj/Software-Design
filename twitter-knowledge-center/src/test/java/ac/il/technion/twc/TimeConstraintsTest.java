package ac.il.technion.twc;

import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

/**
 * Assert time constraints of methods of {@link TwitterKnowledgeCenter}.
 * 
 * @author Ophir De Jager
 * 
 */
public class TimeConstraintsTest {

	@Rule
	public Timeout globalTimeout = new Timeout(2000); // FIXME change to ns
														// (currently ms)

	private static String[] lines;
	private static final int linesLength = 98; // must define as constant for
												// JUnit annotations to use.

	private final TwitterKnowledgeCenter $ = new TwitterKnowledgeCenter();

	/**
	 * Load tweets.
	 * 
	 * @throws Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		lines = Files.readAllLines(
				Paths.get("src/test/resources/small_sample.txt"),
				Charset.defaultCharset()).toArray(new String[0]);
	}

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
	 * Test method for {@link TwitterKnowledgeCenter#importData(String[])}
	 * 
	 * @throws Exception
	 */
	@Test(timeout = 2 * linesLength)
	public final void importDataShouldRun2msForEachTweet() throws Exception {
		$.importData(lines);
	}

	/**
	 * Test method for {@link TwitterKnowledgeCenter#setupIndex()}
	 * 
	 * @throws Exception
	 */
	public final void setupIndexSouldRun2000nsForEachTweet() throws Exception {
		$.importData(lines);
		// TODO: setup timeout
		$.setupIndex();
	}

	/**
	 * Test method for {@link TwitterKnowledgeCenter#getDailyHistogram()}
	 * 
	 * @throws Exception
	 */
	public final void getDailyHistogramSouldRun2000ns() throws Exception {
		$.importData(lines);
		$.setupIndex();
		// TODO: setup timeout
		$.setupIndex();
	}

	/**
	 * Test method for
	 * {@link TwitterKnowledgeCenter#getLifetimeOfTweets(String)}
	 * 
	 * @throws Exception
	 */
	public final void getLifeTimeOfTweetsSouldRun2000ns() throws Exception {
		$.importData(lines);
		$.setupIndex();
		// TODO: setup timeout
		$.setupIndex();
	}

}
