package ac.il.technion.twc;

import static org.junit.Assert.assertTrue;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.google.inject.Key;
import com.google.inject.name.Names;

/**
 * Assert time constraints of methods of {@link TwitterKnowledgeCenter}.
 * 
 * @author Ophir De Jager
 * 
 */
public class TimeConstraintsTest {

	private static final int BASE_TWEETS = 322;
	private static String[] lines = generateTweets(BASE_TWEETS, 5, 3, 0);
	private static final int linesLengthApproximation = 10000;

	// didn't import data
	private final TwitterKnowledgeCenter tkcImportData = new TwitterKnowledgeCenter();
	// imported data but didn't set up index
	private final TwitterKnowledgeCenter tkcSetupIndex = new TwitterKnowledgeCenter();
	// imported data & set up index
	private final TwitterKnowledgeCenter $ = new TwitterKnowledgeCenter();

	/**
	 * Set needed data
	 * 
	 * @throws Exception
	 */
	@Before
	public void setUp() throws Exception {
		tkcSetupIndex.importData(lines);
		$.importData(lines);
		$.setupIndex();
	}

	/**
	 * Cleanup.
	 * 
	 * @throws Exception
	 */
	@After
	public void tearDown() throws Exception {
		tkcSetupIndex.cleanPersistentData();
		$.cleanPersistentData();
	}

	/**
	 * Test method for {@link TwitterKnowledgeCenter#importData(String[])}
	 * 
	 * @throws Exception
	 */
	@Ignore
	@Test
	public final void importDataActualTime() throws Exception {
		final long start = System.currentTimeMillis();
		$.importData(lines);
		final long end = System.currentTimeMillis();
		assertTrue("Took " + (end - start) + " millis instead of " + 2
				* lines.length, end - start <= 2 * lines.length);
	}

	/**
	 * Test method for {@link TwitterKnowledgeCenter#setupIndex()}
	 * 
	 * @throws Exception
	 */
	@Ignore
	@Test
	public final void setupIndexActualTime() throws Exception {
		$.importData(lines);
		final long start = System.currentTimeMillis();
		$.setupIndex();
		final long end = System.currentTimeMillis();
		assertTrue("Took " + (end - start) + " millis instead of " + 2
				* lines.length / 1000, end - start <= 2 * lines.length / 1000);
	}

	/**
	 * Test method for {@link TwitterKnowledgeCenter#getDailyHistogram()}
	 * 
	 * @throws Exception
	 */
	@Ignore
	@Test
	public final void getDailyHistogramActualTime() throws Exception {
		$.importData(lines);
		$.setupIndex();
		final long start = System.currentTimeMillis();
		for (int i = 0; i < 50000; ++i)
			$.getDailyHistogram();
		final long end = System.currentTimeMillis();
		assertTrue("Took " + (end - start) + " millis instead of 100", end
				- start <= 100);
	}

	/**
	 * Test method for
	 * {@link TwitterKnowledgeCenter#getLifetimeOfTweets(String)}
	 * 
	 * @throws Exception
	 */
	@Ignore
	@Test
	public final void getLifeTimeOfTweetsActualTime() throws Exception {
		$.importData(lines);
		$.setupIndex();
		final long start = System.currentTimeMillis();
		for (int i = 0; i < 50000; ++i)
			$.getLifetimeOfTweets("base " + i % BASE_TWEETS);
		final long end = System.currentTimeMillis();
		assertTrue("Took " + (end - start) + " millis instead of 100", end
				- start <= 100);
	}

	/**
	 * Test method for {@link TwitterKnowledgeCenter#importData(String[])}
	 * 
	 * @throws Exception
	 */
	@Test(timeout = 2 * linesLengthApproximation)
	public final void importDataShouldRun2msForEachTweet() throws Exception {
		tkcImportData.importData(lines);
	}

	/**
	 * Test method for {@link TwitterKnowledgeCenter#setupIndex()}
	 * 
	 * @throws Exception
	 */
	@Test(timeout = (2 * linesLengthApproximation / 1000))
	public final void setupIndexSouldRun2000nsForEachTweet() throws Exception {
		tkcSetupIndex.setupIndex();
	}

	/**
	 * Test method for {@link TwitterKnowledgeCenter#getDailyHistogram()}
	 * 
	 * @throws Exception
	 */
	@Test(timeout = 100)
	public final void getDailyHistogramSouldRun2000ns() throws Exception {
		for (int i = 0; i < 50000; ++i)
			$.getDailyHistogram();
	}

	/**
	 * Test method for
	 * {@link TwitterKnowledgeCenter#getLifetimeOfTweets(String)}
	 * 
	 * @throws Exception
	 */
	@Test(timeout = 100)
	public final void getLifeTimeOfTweetsSouldRun2000ns() throws Exception {
		for (int i = 0; i < 50000; ++i)
			$.getLifetimeOfTweets("base " + i % BASE_TWEETS);
	}

	/**
	 * Generate tweets (base tweets and retweets) for a total of
	 * <code>numBase</code> * (<code>numRetweetsForEach</code> ^
	 * <code>numLevels</code> - 1) / (<code>numRetweetsForEach</code> - 1)
	 * tweets.
	 * 
	 * @param numBase
	 *            Number of base tweets.
	 * @param numRetweetsForEach
	 *            Number of retweets each tweet has (if it has any retweets).
	 * @param numLevels
	 *            How many times a tweet can be recursively retweeted (i.e.
	 *            retweet of retweet).
	 * @param seed
	 *            Seed for randomly generated time stamps of tweets.
	 * @return <code>numBase</code> base tweets. Each tweet (base or retweet)
	 *         has <code>numRetweetsForEach</code> retweets, and each retweet
	 *         has again <code>numRetweetsForEach</code> retweets of its own,
	 *         and so forth. There are <code>numLevels</code> levels of retweets
	 *         (i.e. retweet of retweet) for each base tweet.
	 */
	private static String[] generateTweets(final int numBase,
			final int numRetweetsForEach, final int numLevels, final long seed) {
		final List<String> tweets = new ArrayList<>();
		final DateFormat dateFormatter = TwitterKnowledgeCenter.injector
				.getInstance(Key.get(DateFormat.class,
						Names.named("twitter date formatter")));
		final Random rnd = new Random(seed);
		for (int baseTweetNum = 0; baseTweetNum < numBase; ++baseTweetNum) {
			final String baseId = "base " + baseTweetNum;
			final long baseDate = rnd.nextLong();
			tweets.add(dateFormatter.format(new Date(baseDate)) + ", " + baseId);
			generateRetweets(tweets, baseId, baseDate, dateFormatter, rnd,
					numRetweetsForEach, numLevels);
		}
		Collections.shuffle(tweets, rnd);
		return tweets.toArray(new String[0]);
	}

	private static void generateRetweets(final List<String> out,
			final String originId, final long originDate,
			final DateFormat dateFormatter, final Random rnd,
			final int numRetweets, final int numLevels) {
		if (numLevels <= 0)
			return;
		for (int tweetNum = 0; tweetNum < numRetweets; ++tweetNum) {
			final String id = "re " + tweetNum + " of: " + originId;
			final long date = originDate + rnd.nextLong();
			out.add(dateFormatter.format(new Date(date)) + ", " + id + ", "
					+ originId);
			generateRetweets(out, id, date, dateFormatter, rnd, numRetweets,
					numLevels - 1);
		}
	}

}
