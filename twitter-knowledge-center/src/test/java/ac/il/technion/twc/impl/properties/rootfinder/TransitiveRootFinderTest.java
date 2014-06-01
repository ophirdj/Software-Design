package ac.il.technion.twc.impl.properties.rootfinder;

import static org.junit.Assert.assertSame;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import ac.il.technion.twc.api.properties.OriginFinder;
import ac.il.technion.twc.api.properties.OriginFinder.NotFoundException;
import ac.il.technion.twc.api.tweet.BaseTweet;
import ac.il.technion.twc.api.tweet.ID;
import ac.il.technion.twc.api.tweet.Retweet;
import ac.il.technion.twc.api.tweet.Tweet;

/**
 * Tests for {@link OriginFinder}
 * 
 * @author Ziv Ronen
 * @date 07.05.2014
 * @mail akarks@gmail.com
 */
public class TransitiveRootFinderTest {

  private static final ArrayList<BaseTweet> EMPTY_BASES =
      new ArrayList<BaseTweet>();

  /**
   * 
   */
  public final @Rule
  ExpectedException thrown = ExpectedException.none();

  /**
   * Test method for: {@link OriginFinder#origin(Tweet)}.
   * 
   * @throws Exception
   */
  @Test
  public
      void
      findRootOnRetweetAfterAddingOnlyTheRetweetShouldThrowNoRootFoundException()
          throws Exception {
    final Retweet rt =
        new Retweet(new Date(1L), new ID("retweetId"), new ID("baseId"));
    thrown.expect(NotFoundException.class);
    new OriginFinder(EMPTY_BASES, Arrays.asList(rt)).origin(rt);
  }

  /**
   * Test method for: {@link OriginFinder#origin(Tweet)}.
   * 
   * @throws Exception
   */
  @Test
  public
      void
      findRootOnRetweetAfterAddingFirstTheOriginalTweetShouldReturnTheOriginalTweet()
          throws Exception {
    final BaseTweet bt = new BaseTweet(new Date(1L), new ID("baseId"));
    final Retweet rt = new Retweet(new Date(1L), new ID("retweetId"), bt.id);
    assertSame(bt,
        new OriginFinder(Arrays.asList(bt), Arrays.asList(rt)).origin(rt));
  }

  /**
   * Test method for: {@link OriginFinder#origin(Tweet)}.
   * 
   * @throws Exception
   */
  @Test
  public
      void
      findRootOnRetweetAfterAddingFirstTheTheRetweetShouldReturnTheOriginalTweet()
          throws Exception {
    final BaseTweet bt = new BaseTweet(new Date(1L), new ID("baseId"));
    final Retweet rt = new Retweet(new Date(1L), new ID("retweetId"), bt.id);
    assertSame(bt,
        new OriginFinder(Arrays.asList(bt), Arrays.asList(rt)).origin(rt));
  }

  /**
   * Test method for: {@link OriginFinder#origin(Tweet)}.
   * 
   * @throws Exception
   */
  @Test
  public void
      findRootOnRetweetWithUnrelatedBaseTweetShouldThrowNoRootFoundException()
          throws Exception {
    final BaseTweet bt = new BaseTweet(new Date(1L), new ID("otherId"));
    final Retweet rt =
        new Retweet(new Date(1L), new ID("retweetId"), new ID("baseId"));
    thrown.expect(NotFoundException.class);
    new OriginFinder(Arrays.asList(bt), Arrays.asList(rt)).origin(rt);
  }

  /**
   * Test method for: {@link OriginFinder#origin(Tweet)}.
   * 
   * @throws Exception
   */
  @Test
  public void findRootOnRetweetOfRetweetShouldReturnTheBaseTweet()
      throws Exception {
    final BaseTweet bt = new BaseTweet(new Date(1L), new ID("baseId"));
    final Retweet rt1 = new Retweet(new Date(1L), new ID("retweetId1"), bt.id);
    final Retweet rt2 = new Retweet(new Date(1L), new ID("retweetId2"), rt1.id);
    assertSame(bt,
        new OriginFinder(Arrays.asList(bt), Arrays.asList(rt1, rt2))
            .origin(rt2));
  }

}
