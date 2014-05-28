package ac.il.technion.twc.impl.properties.rootfinder;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

import java.util.Date;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import ac.il.technion.twc.api.tweets.BaseTweet;
import ac.il.technion.twc.api.tweets.ID;
import ac.il.technion.twc.api.tweets.Retweet;
import ac.il.technion.twc.api.tweets.Tweet;
import ac.il.technion.twc.impl.properties.rootfinder.TransitiveRootFinder.NoRootFoundException;

/**
 * Tests for {@link TransitiveRootFinder} and {@link TransitivityBuilder}
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
  private final TransitivityBuilder $;

  /**
   * C'tor
   */
  public TransitiveRootFinderTest() {
    $ = new TransitivityBuilder();
  }

  /**
   * Test method for: {@link TransitivityBuilder#visit(BaseTweet)}.
   */
  @Test
  public void addingBaseTweetShouldNotThrowException() {
    $.visit(new BaseTweet(new Date(1L), new ID("baseId")));
  }

  /**
   * Test method for: {@link TransitivityBuilder#visit(Retweet)}.
   */
  @Test
  public void addingRetweetShouldNotThrowException() {
    $.visit(new Retweet(new Date(1L), new ID("retweetId"), new ID("baseId")));
  }

  /**
   * Test method for: {@link TransitivityBuilder#getResult()}.
   */
  @Test
  public void getResultShouldntReturnNull() {
    assertNotNull($.getResult());
  }

  /**
   * Test method for: {@link TransitivityBuilder#visit(Retweet)},
   * {@link TransitivityBuilder#getResult()},
   * {@link TransitiveRootFinder#findRoot(Tweet)}.
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
    $.visit(retweet);
    thrown.expect(NoRootFoundException.class);
    $.getResult().findRoot(retweet);
  }

  /**
   * Test method for: {@link TransitivityBuilder#visit(BaseTweet)},
   * {@link TransitivityBuilder#visit(Retweet)},
   * {@link TransitivityBuilder#getResult()},
   * {@link TransitiveRootFinder#findRoot(Tweet)}.
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
    $.visit(baseTweet);
    $.visit(retweet);
    assertSame(baseTweet, $.getResult().findRoot(retweet));
  }

  /**
   * Test method for: {@link TransitivityBuilder#visit(BaseTweet)},
   * {@link TransitivityBuilder#visit(Retweet)},
   * {@link TransitivityBuilder#getResult()},
   * {@link TransitiveRootFinder#findRoot(Tweet)}.
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
    $.visit(retweet);
    $.visit(baseTweet);
    assertSame(baseTweet, $.getResult().findRoot(retweet));
  }

  /**
   * Test method for: {@link TransitivityBuilder#visit(BaseTweet)},
   * {@link TransitivityBuilder#visit(Retweet)},
   * {@link TransitivityBuilder#getResult()},
   * {@link TransitiveRootFinder#findRoot(Tweet)}.
   * 
   * @throws Exception
   */
  @Test
  public void
      findRootOnRetweetWithUnrelatedBaseTweetShouldThrowNoRootFoundException()
          throws Exception {
    final BaseTweet baseTweet = new BaseTweet(new Date(1L), new ID("otherId"));
    final Retweet retweet =
        new Retweet(new Date(1L), new ID("retweetId"), new ID("baseId"));
    $.visit(baseTweet);
    $.visit(retweet);
    thrown.expect(NoRootFoundException.class);
    $.getResult().findRoot(retweet);
  }

  /**
   * Test method for: {@link TransitivityBuilder#visit(BaseTweet)},
   * {@link TransitivityBuilder#visit(Retweet)},
   * {@link TransitivityBuilder#getResult()},
   * {@link TransitiveRootFinder#findRoot(Tweet)}.
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
    $.visit(baseTweet);
    $.visit(firstRetweet);
    $.visit(secondRetweet);
    assertSame(baseTweet, $.getResult().findRoot(secondRetweet));
  }

  /**
   * Test method for: {@link TransitivityBuilder#visit(BaseTweet)},
   * {@link TransitivityBuilder#visit(Retweet)},
   * {@link TransitivityBuilder#getResult()},
   * {@link TransitivityBuilder#clear()},
   * {@link TransitiveRootFinder#findRoot(Tweet)}.
   * 
   * @throws NoRootFoundException
   */
  @Test
  public void clearShouldRestoreOriginalState() throws NoRootFoundException {
    final String baseId = "baseId";
    final String firstRetweetId = "retweetId1";
    final Retweet firstRetweet =
        new Retweet(new Date(1L), new ID(firstRetweetId), new ID(baseId));
    $.visit(new BaseTweet(new Date(1L), new ID(baseId)));
    $.visit(firstRetweet);
    $.clear();
    thrown.expect(NoRootFoundException.class);
    $.getResult().findRoot(firstRetweet);
  }

}
