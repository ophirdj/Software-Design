package ac.il.technion.twc.system;

import static org.junit.Assert.assertTrue;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import ac.il.technion.twc.FuntionalityTester;
import ac.il.technion.twc.TestUtils;

/**
 * Assert time constraints of methods of {@link FuntionalityTester}.
 * 
 * @author Ophir De Jager
 * 
 */
public class TimeConstraintsTest {

	private static final int BASE_TWEETS = 322;
	private static final String[] lines = TestUtils.generateTweets(BASE_TWEETS,
			5, 3, 0);
	private static final int linesLengthApproximation = 50232;
	private static final int TEMPORAL_NUM_SAMPLES = 10;

	private static final SimpleDateFormat temporalhistogramDateFormat = new SimpleDateFormat(
			"dd/MM/yyyy HH:mm:ss");

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
				* lines.length / 1000, end - start <= 10 * lines.length / 1000);
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
		assertTrue("Took " + (end - start) + " millis instead of 100", end
				- start <= 100);
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
			$.getLifetimeOfTweets("base" + i % BASE_TWEETS);
		final long end = System.currentTimeMillis();
		assertTrue("Took " + (end - start) + " millis instead of 100", end
				- start <= 100);
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
			$.getTemporalHistogram(temporalhistogramDateFormat.format(new Date(
					rnd.nextLong())), temporalhistogramDateFormat
					.format(new Date(rnd.nextLong())));
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
	@Test(timeout = (10 * linesLengthApproximation / 1000))
	public final void setupIndexShouldRun10000nsForEachTweet() throws Exception {
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
	@Test(timeout = 5000)
	public final void getHashtagPopularityShouldRun250ms() throws Exception {
		for (int i = 0; i < 10; ++i)
			$.getHashtagPopularity("YOLO");
		for (int i = 0; i < 10; ++i)
			$.getHashtagPopularity("SWAG");
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
			$.getTemporalHistogram(temporalhistogramDateFormat.format(new Date(
					rnd.nextLong())), temporalhistogramDateFormat
					.format(new Date(rnd.nextLong())));
	}

}
