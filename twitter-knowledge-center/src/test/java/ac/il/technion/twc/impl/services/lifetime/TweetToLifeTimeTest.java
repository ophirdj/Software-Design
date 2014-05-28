package ac.il.technion.twc.impl.services.lifetime;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;

import ac.il.technion.twc.api.tweets.BaseTweet;
import ac.il.technion.twc.api.tweets.ID;
import ac.il.technion.twc.api.tweets.Retweet;
import ac.il.technion.twc.impl.properties.rootfinder.TransitiveRootFinder;
import ac.il.technion.twc.impl.properties.rootfinder.TransitiveRootFinder.NoRootFoundException;
import ac.il.technion.twc.impl.properties.tweetsretriever.TweetsRetriever;
import ac.il.technion.twc.impl.services.lifetime.TweetToLifeTime.UndefinedTimeException;

/**
 * Tests for {@link TweetToLifeTime}
 * 
 * @author Ziv Ronen
 * @date 28.05.2014
 * @mail akarks@gmail.com
 * 
 * @version 2.0
 * @since 2.0
 */
public class TweetToLifeTimeTest {

  private final TransitiveRootFinder rootFinderMock;
  private final TweetsRetriever tweetsMock;

  /**
   * C'tor.
   */
  public TweetToLifeTimeTest() {
    rootFinderMock = mock(TransitiveRootFinder.class);
    tweetsMock = mock(TweetsRetriever.class);
    when(tweetsMock.getBaseTweets()).thenReturn(new ArrayList<BaseTweet>());
    when(tweetsMock.getRetweets()).thenReturn(new ArrayList<Retweet>());
  }

  /**
   * init {@literal rootFinderMock} and {@literal tweetsMock} in the following
   * way: <code>numBase</code> base tweets. Each tweet (base or retweet) has
   * <code>numRetweetsForEach</code> retweets, and each retweet has again
   * <code>numRetweetsForEach</code> retweets of its own, and so forth. There
   * are <code>numLevels</code> levels of retweets (i.e. retweet of retweet) for
   * each base tweet.
   * 
   * 
   * @param numBase
   *          Number of base tweets.
   * @param numRetweetsForEach
   *          Number of retweets each tweet has (if it has any retweets).
   * @param numLevels
   *          How many times a tweet can be recursively retweeted (i.e. retweet
   *          of retweet).
   * 
   * @throws NoRootFoundException
   *           Never.
   */
  private void initiateState(final int numBase, final int numRetweetsForEach,
      final int numLevels) throws NoRootFoundException {
    final Set<BaseTweet> baseTweets = new HashSet<>();
    final Set<Retweet> retweets = new HashSet<>();

    for (int baseTweetNum = 0; baseTweetNum < numBase; ++baseTweetNum) {
      final ID baseId = new ID("base " + baseTweetNum);
      final long baseDate = 123456789L + baseTweetNum;
      final BaseTweet baseTweet = new BaseTweet(new Date(baseDate), baseId);
      baseTweets.add(baseTweet);
      when(rootFinderMock.findRoot(baseTweet)).thenReturn(baseTweet);
      for (int level = 0; level < numLevels; ++level)
        for (int retweetNum = 0; retweetNum < numRetweetsForEach; ++retweetNum) {
          final ID originId =
              level > 0 ? new ID("retweet of base " + baseTweetNum + " level "
                  + (level - 1)) : baseId;
          final Retweet retweet =
              new Retweet(new Date(baseDate + 24 * 60 * 60 * 1000 * level + 10
                  * 1000 * retweetNum), new ID("retweet of base "
                  + baseTweetNum + " level " + level), originId);
          retweets.add(retweet);
          when(rootFinderMock.findRoot(retweet)).thenReturn(baseTweet);
        }
    }
    when(tweetsMock.getBaseTweets()).thenReturn(new ArrayList<>(baseTweets));
    when(tweetsMock.getRetweets()).thenReturn(new ArrayList<>(retweets));
  }

  /**
   * Test method for {@link TweetToLifeTime#getLifeTimeById(ID)}
   * 
   * @throws NoRootFoundException
   *           Never.
   * @throws UndefinedTimeException
   *           Shouldn't happen.
   */
  @Test
  public final void retweetShouldExtendLifeTimeOfBaseTweet()
      throws NoRootFoundException, UndefinedTimeException {
    final BaseTweet base =
        new BaseTweet(new GregorianCalendar(2014, 4, 1, 3, 00).getTime(),
            new ID("base"));
    final Retweet re =
        new Retweet(new GregorianCalendar(2014, 4, 1, 4, 30).getTime(), new ID(
            "retweet"), base.id());
    when(rootFinderMock.findRoot(re)).thenReturn(base);
    addBaseTweets(base);
    addRetweets(re);
    assertLifeTime(90L * 60L * 1000L, base.id());
  }

  /**
   * Test method for {@link TweetToLifeTime#getLifeTimeById(ID)}
   * 
   * @throws NoRootFoundException
   *           Never.
   * @throws UndefinedTimeException
   *           Shouldn't happen.
   */
  @Test
  public final
      void
      notRelatedRetweetsShouldntChangeLifeTimeOfBaseTweetLifeTimeShouldRemain24Hours()
          throws NoRootFoundException, UndefinedTimeException {
    initiateState(3, 3, 3);
    final ID reId = new ID("retweet");
    final BaseTweet base =
        new BaseTweet(new GregorianCalendar(2014, 4, 1).getTime(), new ID(
            "base"));
    final Retweet re =
        new Retweet(new GregorianCalendar(2014, 4, 2).getTime(), reId,
            base.id());
    when(rootFinderMock.findRoot(re)).thenReturn(base);
    final Retweet noRelated =
        new Retweet(new GregorianCalendar(2014, 5, 2).getTime(), new ID(
            "unrelated retweet"), new ID("not base"));
    when(rootFinderMock.findRoot(noRelated)).thenThrow(
        new NoRootFoundException());
    addBaseTweets(base);
    addRetweets(re, noRelated);
    assertLifeTime(24L * 60L * 60L * 1000L, base.id());
  }

  /**
   * Test method for {@link TweetToLifeTime#getLifeTimeById(ID)}
   * 
   * @throws NoRootFoundException
   *           Never.
   * @throws UndefinedTimeException
   *           Shouldn't happen.
   */
  @Test
  public final void lifeTimeShouldBeAccurate() throws NoRootFoundException,
      UndefinedTimeException {
    final long baseTime = 123456789L;
    final long interval = 111111111L;
    final BaseTweet base = new BaseTweet(new Date(baseTime), new ID("base"));
    final Retweet re =
        new Retweet(new Date(baseTime + interval), new ID("retweet 1"),
            base.id());
    when(rootFinderMock.findRoot(re)).thenReturn(base);
    addBaseTweets(base);
    addRetweets(re);
    assertLifeTime(interval, base.id());
  }

  /**
   * Test method for {@link TweetToLifeTime#getLifeTimeById(ID)}
   * 
   * @throws NoRootFoundException
   *           Never.
   * @throws UndefinedTimeException
   *           Shouldn't happen.
   */
  @Test
  public final void
      lifeTimeShouldBeDeterminedByTheChronologicallyLatestRetweet()
          throws NoRootFoundException, UndefinedTimeException {
    final BaseTweet base =
        new BaseTweet(new GregorianCalendar(2014, 4, 1, 10, 00).getTime(),
            new ID("base"));
    final Retweet re1 =
        new Retweet(new GregorianCalendar(2014, 4, 1, 10, 30).getTime(),
            new ID("retweet 1"), base.id());
    final Retweet re2 =
        new Retweet(new GregorianCalendar(2014, 4, 1, 14, 00).getTime(),
            new ID("retweet 2"), base.id());
    final Retweet re3 =
        new Retweet(new GregorianCalendar(2014, 4, 1, 12, 59).getTime(),
            new ID("retweet 3"), base.id());
    when(rootFinderMock.findRoot(re1)).thenReturn(base);
    when(rootFinderMock.findRoot(re2)).thenReturn(base);
    when(rootFinderMock.findRoot(re3)).thenReturn(base);
    addBaseTweets(base);
    addRetweets(re1, re2, re3);
    assertLifeTime(4L * 60L * 60L * 1000L, base.id());
  }

  private void assertLifeTime(final long value, final ID id)
      throws UndefinedTimeException {
    assertEquals(Long.valueOf(value).toString(), new TweetToLifeTime(
        rootFinderMock, tweetsMock).getLifeTimeById(id));
  }

  private void addBaseTweets(final BaseTweet... bases) {
    final List<BaseTweet> newTweets = new ArrayList<>(Arrays.asList(bases));
    newTweets.addAll(tweetsMock.getBaseTweets());
    when(tweetsMock.getBaseTweets()).thenReturn(newTweets);
  }

  private void addRetweets(final Retweet... res) {
    final List<Retweet> newTweets = new ArrayList<>(Arrays.asList(res));
    newTweets.addAll(tweetsMock.getRetweets());
    when(tweetsMock.getRetweets()).thenReturn(newTweets);
  }

}
