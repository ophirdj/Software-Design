package ac.il.technion.twc.histogram;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.GregorianCalendar;

import org.junit.Test;

import ac.il.technion.twc.TwitterKnowledgeCenter;
import ac.il.technion.twc.api.tweets.BaseTweet;
import ac.il.technion.twc.api.tweets.ID;
import ac.il.technion.twc.api.tweets.Retweet;
import ac.il.technion.twc.impl.properties.DayOfWeek;

import com.google.inject.Key;
import com.google.inject.name.Names;

/**
 * Tests for {@link DayHistogramBuilder}
 * 
 * @author Ophir De Jager
 * 
 */
public class DayHistogramBuilderTest {

  private final DayHistogramBuilder underTest;
  private final DayHistogram emptyHistogram;

  /**
   * C'tor
   */
  public DayHistogramBuilderTest() {
    underTest = new DayHistogramBuilder();
    emptyHistogram =
        TwitterKnowledgeCenter.injector.getInstance(Key.get(DayHistogram.class,
            Names.named("default")));
  }

  private void initBuilder(final DayHistogram storedHistogram) {
    underTest.initializeFromState(storedHistogram);
  }

  /**
   * Test method for
   * {@link DayHistogramBuilder#initializeFromState(DayHistogram)}
   */
  @Test
  public final void constructorShouldCallStorageHandlerToLoadHistogram() {
    initBuilder(emptyHistogram);
    assertNotNull(underTest);
  }

  /**
   * Test method for
   * {@link DayHistogramBuilder#initializeFromState(DayHistogram)},
   * {@link DayHistogramBuilder#getState()}.
   */
  @Test
  public final void
      constructorShouldUseEmptyHistogramReturnedByStorageHandler() {
    initBuilder(emptyHistogram);
    assertEquals(emptyHistogram, underTest.getState());
  }

  /**
   * Test method for
   * {@link DayHistogramBuilder#initializeFromState(DayHistogram)},
   * {@link DayHistogramBuilder#getState()}
   */
  @Test
  public final void constructorShouldUseHistogramReturnedByStorageHandler() {
    final DayHistogram storedHistogram =
        TwitterKnowledgeCenter.injector.getInstance(Key.get(DayHistogram.class,
            Names.named("default")));
    storedHistogram.basetweets.put(DayOfWeek.SUNDAY, 1);
    storedHistogram.retweets.put(DayOfWeek.MONDAY, 23);
    initBuilder(storedHistogram);
    assertEquals(storedHistogram, underTest.getState());
  }

  /**
   * Test method for {@link DayHistogramBuilder#visit(BaseTweet)},
   * {@link DayHistogramBuilder#getState()}
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
    assertEquals(numTweetsBefore + 1, underTest.getState().tweets(day));
  }

  /**
   * Test method for {@link DayHistogramBuilder#visit(BaseTweet)},
   * {@link DayHistogramBuilder#getState()}
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
    assertEquals(numRetweetsBefore, underTest.getState().retweets(day));
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
    assertEquals(numTweetsBefore + 1, underTest.getState().tweets(day));
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
    assertEquals(numTweetsBefore + 1, underTest.getState().retweets(day));
  }

  /**
   * Test method for {@link DayHistogramBuilder#visit(BaseTweet)},
   * {@link DayHistogramBuilder#getState()}
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
    assertEquals(numTweetsBefore + 2, underTest.getState().tweets(day));
  }

  /**
   * Test method for {@link DayHistogramBuilder#visit(Retweet)},
   * {@link DayHistogramBuilder#getState()}
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
    assertEquals(numTweetsBefore + 2, underTest.getState().tweets(day));
  }

  /**
   * Test method for {@link DayHistogramBuilder#visit(Retweet)},
   * {@link DayHistogramBuilder#getState()}
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
    assertEquals(numTweetsBefore + 2, underTest.getState().retweets(day));
  }

  /**
   * Test method for {@link DayHistogramBuilder#visit(BaseTweet)},
   * {@link DayHistogramBuilder#visit(Retweet)},
   * {@link DayHistogramBuilder#getState()}
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
    assertEquals(numTweetsBefore + 2, underTest.getState().tweets(day));
  }

  /**
   * Test method for {@link DayHistogramBuilder#visit(BaseTweet)},
   * {@link DayHistogramBuilder#visit(Retweet)},
   * {@link DayHistogramBuilder#getState()}
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
    assertEquals(numTweetsBefore + 1, underTest.getState().retweets(day));
  }

}
