package ac.il.technion.twc.lifetime;

import static org.junit.Assert.assertSame;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import ac.il.technion.twc.lifetime.TransitiveRootFinder.NoRootFoundException;
import ac.il.technion.twc.message.ID;
import ac.il.technion.twc.message.tweet.BaseTweet;
import ac.il.technion.twc.message.tweet.Retweet;

/**
 * Test for the TransitiveRootFinder class
 * 
 * @author Ziv Ronen
 * @date 07.05.2014
 * @mail akarks@gmail.com
 * 
 * @version 2.0
 * @since 2.0
 */
public class TransitiveRootFinderTest {

  /**
   * 
   */
  public final @Rule
  ExpectedException thrown = ExpectedException.none();
  private final TransitiveRootFinder underTest;

  /**
   * C'tor
   */
  public TransitiveRootFinderTest() {
    underTest = new TransitiveRootFinder();
  }

  /**
   * Test method for {@link TransitiveRootFinder#addTweet(BaseTweet)}
   */
  @Test
  public void addingBaseTweetShouldNotThrowException() {
    underTest.addTweet(new BaseTweet(new Date(1L), new ID("baseId")));
  }

  /**
   * Test method for {@link TransitiveRootFinder#addTweet(Retweet)}.
   */
  @Test
  public void addingRetweetShouldNotThrowException() {
    underTest.addTweet(new Retweet(new Date(1L), new ID("retweetId"), new ID(
        "baseId")));
  }

  /**
   * Test method for: {@link TransitiveRootFinder#addTweet(Retweet)},
   * {@link TransitiveRootFinder#findRoot(ac.il.technion.twc.message.tweet.Tweet)}
   * .
   * 
   * 
   * @throws Exception
   */
  @Test
  public
      void
      findRootOnRetweetAfterAddingOnlyTheRetweetShouldThrowNoRootFoundException()
          throws Exception {
    final Retweet retweet =
        new Retweet(new Date(1L), new ID("retweetId"), new ID("baseId"));
    underTest.addTweet(retweet);
    thrown.expect(NoRootFoundException.class);
    underTest.findRoot(retweet);
  }

  /**
   * Test method for: {@link TransitiveRootFinder#addTweet(Retweet)},
   * {@link TransitiveRootFinder#addTweet(BaseTweet)},
   * {@link TransitiveRootFinder#findRoot(ac.il.technion.twc.message.tweet.Tweet)}
   * .
   * 
   * @throws Exception
   */
  @Test
  public
      void
      findRootOnRetweetAfterAddingFirstTheOriginalTweetShouldReturnTheOriginalTweet()
          throws Exception {
    final ID baseId = new ID("baseId");
    final BaseTweet baseTweet = new BaseTweet(new Date(1L), baseId);
    final Retweet retweet =
        new Retweet(new Date(1L), new ID("retweetId"), baseId);
    underTest.addTweet(baseTweet);
    underTest.addTweet(retweet);
    assertSame(baseTweet, underTest.findRoot(retweet));
  }

  /**
   * Test method for: {@link TransitiveRootFinder#addTweet(Retweet)},
   * {@link TransitiveRootFinder#addTweet(BaseTweet)},
   * {@link TransitiveRootFinder#findRoot(ac.il.technion.twc.message.tweet.Tweet)}
   * .
   * 
   * @throws Exception
   */
  @Test
  public
      void
      findRootOnRetweetAfterAddingFirstTheTheRetweetShouldReturnTheOriginalTweet()
          throws Exception {
    final ID baseId = new ID("baseId");
    final BaseTweet baseTweet = new BaseTweet(new Date(1L), baseId);
    final Retweet retweet =
        new Retweet(new Date(1L), new ID("retweetId"), baseId);
    underTest.addTweet(retweet);
    underTest.addTweet(baseTweet);
    assertSame(baseTweet, underTest.findRoot(retweet));
  }

  /**
   * Test method for: {@link TransitiveRootFinder#addTweet(Retweet)},
   * {@link TransitiveRootFinder#addTweet(BaseTweet)},
   * {@link TransitiveRootFinder#findRoot(ac.il.technion.twc.message.tweet.Tweet)}
   * .
   * 
   * @throws Exception
   */
  @Test
  public void
      findRootOnRetweetWithUnrelatedBaseTweetShouldThrowNoRootFoundException()
          throws Exception {
    final BaseTweet basetweet = new BaseTweet(new Date(1L), new ID("otherId"));
    final Retweet retweet =
        new Retweet(new Date(1L), new ID("retweetId"), new ID("baseId"));
    underTest.addTweet(basetweet);
    underTest.addTweet(retweet);
    thrown.expect(NoRootFoundException.class);
    underTest.findRoot(retweet);
  }

  /**
   * Test method for: {@link TransitiveRootFinder#addTweet(Retweet)},
   * {@link TransitiveRootFinder#addTweet(BaseTweet)},
   * {@link TransitiveRootFinder#findRoot(ac.il.technion.twc.message.tweet.Tweet)}
   * .
   * 
   * @throws Exception
   */
  @Test
  public void findRootOnRetweetOfRetweetShouldReturnTheBaseTweet()
      throws Exception {
    final String baseId = "baseId";
    final BaseTweet baseTweet = new BaseTweet(new Date(1L), new ID(baseId));
    final String firstRetweetId = "retweetId1";
    final Retweet firstRetweet =
        new Retweet(new Date(1L), new ID(firstRetweetId), new ID(baseId));
    final Retweet secondRetweet =
        new Retweet(new Date(1L), new ID("retweetId2"), new ID(firstRetweetId));
    underTest.addTweet(baseTweet);
    underTest.addTweet(firstRetweet);
    underTest.addTweet(secondRetweet);
    assertSame(baseTweet, underTest.findRoot(secondRetweet));
  }

  /**
   * Test method for:
   * {@link TransitiveRootFinder#addRetweets(java.util.Collection)},
   * {@link TransitiveRootFinder#addTweet(BaseTweet)},
   * {@link TransitiveRootFinder#findRoot(ac.il.technion.twc.message.tweet.Tweet)}
   * .
   * 
   * @throws Exception
   */
  @Test
  public void
      findRootOnRetweetOfRetweetAddAsCollectionShouldReturnTheBaseTweet()
          throws Exception {
    final String baseId = "baseId";
    final BaseTweet baseTweet = new BaseTweet(new Date(1L), new ID(baseId));
    final String firstRetweetId = "retweetId1";
    final List<Retweet> retweets = new ArrayList<>();
    retweets.add(new Retweet(new Date(1L), new ID(firstRetweetId), new ID(
        baseId)));
    final Retweet secondRetweet =
        new Retweet(new Date(1L), new ID("retweetId2"), new ID(firstRetweetId));
    retweets.add(secondRetweet);
    underTest.addTweet(baseTweet);
    underTest.addRetweets(retweets);
    assertSame(baseTweet, underTest.findRoot(secondRetweet));
  }

  /**
   * Test method for:
   * {@link TransitiveRootFinder#addRetweets(java.util.Collection)},
   * {@link TransitiveRootFinder#findRoot(ac.il.technion.twc.message.tweet.Tweet)}
   * .
   * 
   * @throws Exception
   */
  @Test
  public
      void
      findRootOnRetweetOfRetweetAddAsCollectionWithoutOriginalShouldThrowNoRootFoundException()
          throws Exception {
    final String firstRetweetId = "retweetId1";
    final List<Retweet> retweets = new ArrayList<>();
    retweets.add(new Retweet(new Date(1L), new ID(firstRetweetId), new ID(
        "baseId")));
    final Retweet secondRetweet =
        new Retweet(new Date(1L), new ID("retweetId2"), new ID(firstRetweetId));
    retweets.add(secondRetweet);
    underTest.addRetweets(retweets);
    thrown.expect(NoRootFoundException.class);
    underTest.findRoot(secondRetweet);
  }

  /**
   * Test method for:
   * {@link TransitiveRootFinder#addRetweets(java.util.Collection)},
   * {@link TransitiveRootFinder#addBaseTweets(java.util.Collection)},
   * {@link TransitiveRootFinder#findRoot(ac.il.technion.twc.message.tweet.Tweet)}
   * .
   * 
   * @throws Exception
   */
  @Test
  public void
      findRootOnRetweetWhenOriginalAddedAsCollectionShouldReturnTheBaseTweets()
          throws Exception {
    final BaseTweet firstBaseTweet =
        new BaseTweet(new Date(1L), new ID("baseId1"));
    final Retweet firstRetweet =
        new Retweet(new Date(1L), new ID("retweet1"), new ID("baseId1"));

    final BaseTweet secondBaseTweet =
        new BaseTweet(new Date(1L), new ID("baseId2"));
    final Retweet secondRetweet =
        new Retweet(new Date(1L), new ID("retweet2"), new ID("baseId2"));

    final List<BaseTweet> basetweets = new ArrayList<>();
    basetweets.add(firstBaseTweet);
    basetweets.add(secondBaseTweet);
    final List<Retweet> retweets = new ArrayList<>();
    retweets.add(firstRetweet);
    retweets.add(secondRetweet);

    underTest.addRetweets(retweets);
    underTest.addBaseTweets(basetweets);
    assertSame(firstBaseTweet, underTest.findRoot(firstRetweet));
    assertSame(secondBaseTweet, underTest.findRoot(secondRetweet));
  }
}
