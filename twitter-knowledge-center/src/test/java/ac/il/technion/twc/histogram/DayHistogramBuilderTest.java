package ac.il.technion.twc.histogram;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.GregorianCalendar;

import org.junit.Test;

import ac.il.technion.twc.TwitterKnowledgeCenter;
import ac.il.technion.twc.message.ID;
import ac.il.technion.twc.message.tweet.BaseTweet;
import ac.il.technion.twc.message.tweet.Retweet;
import ac.il.technion.twc.storage.StorageHandler;

import com.google.inject.Key;
import com.google.inject.name.Names;

/**
 * Tests for {@link DayHistogramBuilder}
 * 
 * @author Ophir De Jager
 * 
 */
public class DayHistogramBuilderTest {

  private DayHistogramBuilder underTest;
  private final StorageHandler<DayHistogram> storageHandler;
  private final DayHistogram emptyHistogram;


	/**
	 * C'tor
	 */
	public DayHistogramBuilderTest() {
		storageHandler = mock(StorageHandler.class);
		emptyHistogram = TwitterKnowledgeCenter.injector.getInstance(Key.get(
				DayHistogram.class, Names.named("default")));
	}


  private void initBuilder(final DayHistogram storedHistogram) {
    when(storageHandler.load(emptyHistogram)).thenReturn(storedHistogram);
    underTest = new DayHistogramBuilder(storageHandler, emptyHistogram);
  }

  /**
   * Test method for
   * {@link DayHistogramBuilder#DayHistogramBuilder(StorageHandler, DayHistogram)}
   */
  @Test
  public final void constructorShouldCallStorageHandlerToLoadHistogram() {
    initBuilder(emptyHistogram);
    verify(storageHandler).load(emptyHistogram);
    assertNotNull(underTest);
  }

  /**
   * Test method for
   * {@link DayHistogramBuilder#DayHistogramBuilder(StorageHandler, DayHistogram)}
   */
  @Test
  public final void
      constructorShouldUseEmptyHistogramReturnedByStorageHandler() {
    initBuilder(emptyHistogram);
    verify(storageHandler).load(emptyHistogram);
    assertEquals(emptyHistogram, underTest.getResult());
  }


	/**
	 * Test method for
	 * {@link DayHistogramBuilder#DayHistogramBuilder(StorageHandler, DayHistogram)}
	 */
	@Test
	public final void constructorShouldUseHistogramReturnedByStorageHandler() {
		final DayHistogram storedHistogram = TwitterKnowledgeCenter.injector
				.getInstance(Key.get(DayHistogram.class, Names.named("default")));
		storedHistogram.basetweets.put(DayOfWeek.SUNDAY, 1);
		storedHistogram.retweets.put(DayOfWeek.MONDAY, 23);
		initBuilder(storedHistogram);
		verify(storageHandler).load(emptyHistogram);
		assertEquals(storedHistogram, underTest.getResult());
	}


  /**
   * Test method for {@link DayHistogramBuilder#visit(BaseTweet)},
   * {@link DayHistogramBuilder#getResult()}
   */
  @Test
  public final void visitBaseTweetShouldIncreaseNumberOfTweetsBy1() {
    initBuilder(emptyHistogram);
    final BaseTweet tweet =
        new BaseTweet(new GregorianCalendar(2014, 4, 1).getTime(), new ID(
            "trolololol"));
    final DayOfWeek day = DayOfWeek.fromDate(tweet.date());
    final int numTweetsBefore = emptyHistogram.tweets(day);
    underTest.visit(tweet);
    assertEquals(numTweetsBefore + 1, underTest.getResult().tweets(day));
  }

  /**
   * Test method for {@link DayHistogramBuilder#visit(BaseTweet)},
   * {@link DayHistogramBuilder#getResult()}
   */
  @Test
  public final void visitBaseTweetShouldNotChangeNumberOfRetweets() {
    initBuilder(emptyHistogram);
    final BaseTweet tweet =
        new BaseTweet(new GregorianCalendar(2014, 4, 1).getTime(), new ID(
            "trolololol"));
    final DayOfWeek day = DayOfWeek.fromDate(tweet.date());
    final int numRetweetsBefore = emptyHistogram.retweets(day);
    underTest.visit(tweet);
    assertEquals(numRetweetsBefore, underTest.getResult().retweets(day));
  }

  /**
   * Test method for {@link DayHistogramBuilder#visit(Retweet)}
   */
  @Test
  public final void visitRetweetShouldIncreaseNumberOfTweetsBy1() {
    initBuilder(emptyHistogram);
    final Retweet tweet =
        new Retweet(new GregorianCalendar(2014, 4, 1).getTime(), new ID(
            "trolololol"), new ID("lolololol"));
    final DayOfWeek day = DayOfWeek.fromDate(tweet.date());
    final int numTweetsBefore = emptyHistogram.tweets(day);
    underTest.visit(tweet);
    assertEquals(numTweetsBefore + 1, underTest.getResult().tweets(day));
  }

  /**
   * Test method for {@link DayHistogramBuilder#visit(Retweet)}
   */
  @Test
  public final void visitRetweetShouldIncreaseNumberOfRetweetsBy1() {
    initBuilder(emptyHistogram);
    final Retweet tweet =
        new Retweet(new GregorianCalendar(2014, 4, 1).getTime(), new ID(
            "trolololol"), new ID("lolololol"));
    final DayOfWeek day = DayOfWeek.fromDate(tweet.date());
    final int numTweetsBefore = emptyHistogram.retweets(day);
    underTest.visit(tweet);
    assertEquals(numTweetsBefore + 1, underTest.getResult().retweets(day));
  }

  /**
   * Test method for {@link DayHistogramBuilder#visit(BaseTweet)},
   * {@link DayHistogramBuilder#getResult()}
   */
  @Test
  public final void
      visitBaseTweetTwiceInEqualDayShouldIncreaseNumberOfTweetsBy2() {
    initBuilder(emptyHistogram);
    final BaseTweet tweet1 =
        new BaseTweet(new GregorianCalendar(2014, 4, 1).getTime(), new ID(
            "trolololol"));
    final BaseTweet tweet2 =
        new BaseTweet(new GregorianCalendar(2014, 4, 8).getTime(), new ID(
            "lolololol"));
    final DayOfWeek day = DayOfWeek.fromDate(tweet1.date());
    final int numTweetsBefore = emptyHistogram.tweets(day);
    underTest.visit(tweet1);
    underTest.visit(tweet2);
    assertEquals(numTweetsBefore + 2, underTest.getResult().tweets(day));
  }

  /**
   * Test method for {@link DayHistogramBuilder#visit(Retweet)},
   * {@link DayHistogramBuilder#getResult()}
   */
  @Test
  public final void
      visitRetweetTwiceInEqualDayShouldIncreaseNumberOfTweetsBy2() {
    initBuilder(emptyHistogram);
    final Retweet tweet1 =
        new Retweet(new GregorianCalendar(2014, 4, 1).getTime(), new ID(
            "trolololol"), new ID("lolololol"));
    final Retweet tweet2 =
        new Retweet(new GregorianCalendar(2014, 4, 1).getTime(), new ID(
            "lolololol"), new ID("lol"));
    final DayOfWeek day = DayOfWeek.fromDate(tweet1.date());
    final int numTweetsBefore = emptyHistogram.tweets(day);
    underTest.visit(tweet1);
    underTest.visit(tweet2);
    assertEquals(numTweetsBefore + 2, underTest.getResult().tweets(day));
  }

  /**
   * Test method for {@link DayHistogramBuilder#visit(Retweet)},
   * {@link DayHistogramBuilder#getResult()}
   */
  @Test
  public final void
      visitRetweetTwiceInEqualDayShouldIncreaseNumberOfRetweetsBy2() {
    initBuilder(emptyHistogram);
    final Retweet tweet1 =
        new Retweet(new GregorianCalendar(2014, 4, 1).getTime(), new ID(
            "trolololol"), new ID("lolololol"));
    final Retweet tweet2 =
        new Retweet(new GregorianCalendar(2014, 4, 1).getTime(), new ID(
            "lolololol"), new ID("lol"));
    final DayOfWeek day = DayOfWeek.fromDate(tweet1.date());
    final int numTweetsBefore = emptyHistogram.retweets(day);
    underTest.visit(tweet1);
    underTest.visit(tweet2);
    assertEquals(numTweetsBefore + 2, underTest.getResult().retweets(day));
  }

  /**
   * Test method for {@link DayHistogramBuilder#visit(BaseTweet)},
   * {@link DayHistogramBuilder#visit(Retweet)},
   * {@link DayHistogramBuilder#getResult()}
   */
  @Test
  public final void
      visitBaseTweetAndRetweetInEqualDayShouldIncreaseNumberOfTweetsBy2() {
    initBuilder(emptyHistogram);
    final BaseTweet tweet1 =
        new BaseTweet(new GregorianCalendar(2014, 4, 1).getTime(), new ID(
            "trolololol"));
    final Retweet tweet2 =
        new Retweet(new GregorianCalendar(2014, 4, 1).getTime(), new ID(
            "lolololol"), new ID("lol"));
    final DayOfWeek day = DayOfWeek.fromDate(tweet1.date());
    final int numTweetsBefore = emptyHistogram.tweets(day);
    underTest.visit(tweet1);
    underTest.visit(tweet2);
    assertEquals(numTweetsBefore + 2, underTest.getResult().tweets(day));
  }

  /**
   * Test method for {@link DayHistogramBuilder#visit(BaseTweet)},
   * {@link DayHistogramBuilder#visit(Retweet)},
   * {@link DayHistogramBuilder#getResult()}
   */
  @Test
  public final void
      visitBaseTweetAndRetweetInEqualDayShouldIncreaseNumberOfRetweetsBy1() {
    initBuilder(emptyHistogram);
    final BaseTweet tweet1 =
        new BaseTweet(new GregorianCalendar(2014, 4, 1).getTime(), new ID(
            "trolololol"));
    final Retweet tweet2 =
        new Retweet(new GregorianCalendar(2014, 4, 1).getTime(), new ID(
            "lolololol"), new ID("lol"));
    final DayOfWeek day = DayOfWeek.fromDate(tweet1.date());
    final int numTweetsBefore = emptyHistogram.retweets(day);
    underTest.visit(tweet1);
    underTest.visit(tweet2);
    assertEquals(numTweetsBefore + 1, underTest.getResult().retweets(day));
  }

}
