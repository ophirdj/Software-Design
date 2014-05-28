package ac.il.technion.twc.system;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

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

import ac.il.technion.twc.FuntionalityTester;
import ac.il.technion.twc.impl.parsers.csFormat.CSFormatUtils;

/**
 * Assert time constraints of methods of {@link FuntionalityTester}.
 * 
 * @author Ophir De Jager
 * 
 */
public class TimeConstraintsTest {

  private static final int BASE_TWEETS = 322;
  private static final String[] lines = generateTweets(BASE_TWEETS, 5, 3, 0);
  private static final int linesLengthApproximation = 50232;
  private static final int TEMPORAL_NUM_SAMPLES = 10;

  // didn't import data
  private final FuntionalityTester tkcImportData = new FuntionalityTester();
  // imported data but didn't set up index
  private final FuntionalityTester tkcSetupIndex = new FuntionalityTester();
  // imported data & set up index
  private final FuntionalityTester $ = new FuntionalityTester();

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
   * Test method for {@link FuntionalityTester#importData(String[])}
   * 
   * @throws Exception
   */
  @Ignore
  @Test
  public final void importDataActualTime() throws Exception {
    final long start = System.currentTimeMillis();
    tkcImportData.importData(lines);
    final long end = System.currentTimeMillis();
    assertTrue("Took " + (end - start) + " millis instead of " + 2
        * lines.length, end - start <= 2 * lines.length);
  }

  /**
   * Test method for {@link FuntionalityTester#setupIndex()}
   * 
   * @throws Exception
   */
  @Ignore
  @Test
  public final void setupIndexActualTime() throws Exception {
    final long start = System.currentTimeMillis();
    tkcSetupIndex.setupIndex();
    final long end = System.currentTimeMillis();
    assertTrue("Took " + (end - start) + " millis instead of " + 2
        * lines.length / 1000, end - start <= 2 * lines.length / 1000);
  }

  /**
   * Test method for {@link FuntionalityTester#getDailyHistogram()}
   * 
   * @throws Exception
   */
  @Ignore
  @Test
  public final void getDailyHistogramActualTime() throws Exception {
    final long start = System.currentTimeMillis();
    for (int i = 0; i < 50000; ++i)
      $.getDailyHistogram();
    final long end = System.currentTimeMillis();
    assertTrue("Took " + (end - start) + " millis instead of 100",
        end - start <= 100);
  }

  /**
   * Test method for {@link FuntionalityTester#getLifetimeOfTweets(String)}
   * 
   * @throws Exception
   */
  @Ignore
  @Test
  public final void getLifeTimeOfTweetsActualTime() throws Exception {
    final long start = System.currentTimeMillis();
    for (int i = 0; i < 50000; ++i)
      $.getLifetimeOfTweets("base " + i % BASE_TWEETS);
    final long end = System.currentTimeMillis();
    assertTrue("Took " + (end - start) + " millis instead of 100",
        end - start <= 100);
  }

  /**
   * Test method for {@link FuntionalityTester#setupIndex()}
   * 
   * @throws Exception
   */
  @Ignore
  @Test
  public final void getTemporalHistogramActualTime() throws Exception {
    final long start = System.currentTimeMillis();
    final Random rnd = new Random(0L);
    for (int i = 0; i < TEMPORAL_NUM_SAMPLES; ++i)
      $.getTemporalHistogram(
          CSFormatUtils.getDateFormat().format(new Date(rnd.nextLong())),
          CSFormatUtils.getDateFormat().format(new Date(rnd.nextLong())));
    final long end = System.currentTimeMillis();
    assertTrue("Took " + (end - start) + " millis instead of "
        + TEMPORAL_NUM_SAMPLES * 2 * lines.length / 1000,
        end - start <= 2 * lines.length / 1000);
  }

  /**
   * Test method for {@link FuntionalityTester#importData(String[])}
   * 
   * @throws Exception
   */
  @Test(timeout = 2 * linesLengthApproximation)
  public final void importDataShouldRun2msForEachTweet() throws Exception {
    tkcImportData.importData(lines);
  }

  /**
   * Test method for {@link FuntionalityTester#setupIndex()}
   * 
   * @throws Exception
   */
  @Test(timeout = (2 * linesLengthApproximation / 1000))
  public final void setupIndexShouldRun2000nsForEachTweet() throws Exception {
    tkcSetupIndex.setupIndex();
  }

  /**
   * Test method for {@link FuntionalityTester#getDailyHistogram()}
   * 
   * @throws Exception
   */
  @Test(timeout = 100)
  public final void getDailyHistogramShouldRun2000ns() throws Exception {
    for (int i = 0; i < 50000; ++i)
      $.getDailyHistogram();
  }

  /**
   * Test method for {@link FuntionalityTester#getLifetimeOfTweets(String)}
   * 
   * @throws Exception
   */
  @Test(timeout = 100)
  public final void getLifeTimeOfTweetsShouldRun2000ns() throws Exception {
    for (int i = 0; i < 50000; ++i)
      $.getLifetimeOfTweets("base" + i % BASE_TWEETS);
  }

  /**
   * Test method for {@link FuntionalityTester#getHashtagPopularity(String)}
   * 
   * @throws Exception
   */
  @Test(timeout = 250)
  public final void getHashtagPopularityShouldRun250ms() throws Exception {
    // TODO
    fail("not implemented");
  }

  /**
   * Test method for
   * {@link FuntionalityTester#getTemporalHistogram(String, String)}
   * 
   * @throws Exception
   */
  @Test(timeout = (TEMPORAL_NUM_SAMPLES * 2 * linesLengthApproximation / 1000))
  public final void getTemporalHistogramShouldRun2000nsForEachTweet()
      throws Exception {
    final Random rnd = new Random(0L);
    for (int i = 0; i < TEMPORAL_NUM_SAMPLES; ++i)
      $.getTemporalHistogram(
          CSFormatUtils.getDateFormat().format(new Date(rnd.nextLong())),
          CSFormatUtils.getDateFormat().format(new Date(rnd.nextLong())));
  }

  /**
   * Generate tweets (base tweets and retweets) for a total of
   * <code>numBase</code> * (<code>numRetweetsForEach</code> ^
   * <code>numLevels</code> - 1) / (<code>numRetweetsForEach</code> - 1) tweets.
   * 
   * @param numBase
   *          Number of base tweets.
   * @param numRetweetsForEach
   *          Number of retweets each tweet has (if it has any retweets).
   * @param numLevels
   *          How many times a tweet can be recursively retweeted (i.e. retweet
   *          of retweet).
   * @param seed
   *          Seed for randomly generated time stamps of tweets.
   * @return <code>numBase</code> base tweets. Each tweet (base or retweet) has
   *         <code>numRetweetsForEach</code> retweets, and each retweet has
   *         again <code>numRetweetsForEach</code> retweets of its own, and so
   *         forth. There are <code>numLevels</code> levels of retweets (i.e.
   *         retweet of retweet) for each base tweet.
   */
  private static String[] generateTweets(final int numBase,
      final int numRetweetsForEach, final int numLevels, final long seed) {
    final List<String> tweets = new ArrayList<>();
    final DateFormat dateFormatter = CSFormatUtils.getDateFormat();
    final Random rnd = new Random(seed);
    for (int baseTweetNum = 0; baseTweetNum < numBase; ++baseTweetNum) {
      final String baseId = "base" + baseTweetNum;
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
      final DateFormat dateFormatter, final Random rnd, final int numRetweets,
      final int numLevels) {
    if (numLevels <= 0)
      return;
    for (int tweetNum = 0; tweetNum < numRetweets; ++tweetNum) {
      final String id = "re" + tweetNum + "of" + originId;
      final long date = originDate + rnd.nextLong();
      out.add(dateFormatter.format(new Date(date)) + ", " + id + ", "
          + originId);
      generateRetweets(out, id, date, dateFormatter, rnd, numRetweets,
          numLevels - 1);
    }
  }

}
