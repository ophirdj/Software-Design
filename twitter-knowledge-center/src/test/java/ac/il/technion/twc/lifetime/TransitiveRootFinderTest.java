package ac.il.technion.twc.lifetime;

import static org.junit.Assert.assertSame;

import java.util.Date;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import ac.il.technion.twc.lifetime.TransitiveRootFinder.NoRootFoundException;
import ac.il.technion.twc.message.ID;
import ac.il.technion.twc.message.tweet.BaseTweet;
import ac.il.technion.twc.message.tweet.Retweet;

public class TransitiveRootFinderTest {

  public final @Rule
  ExpectedException thrown = ExpectedException.none();
  private final TransitiveRootFinder underTest;

  public TransitiveRootFinderTest() {
    underTest = new TransitiveRootFinder();
  }

  @Test
  public void initTestEnvironmentShouldSucceed() {

  }

  @Test
  public void addingBaseTweetShouldNotThrowException() {
    underTest.addTweet(new BaseTweet(new Date(1L), new ID("baseId")));
  }

  @Test
  public void addingRetweetShouldNotThrowException() {
    underTest.addTweet(new Retweet(new Date(1L), new ID("retweetId"), new ID(
        "baseId")));
  }

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

  @Test
  public void findRootOnRetweetOfRetweetShouldReturnTheBaseTweet()
      throws Exception {
    final BaseTweet basetweet = new BaseTweet(new Date(1L), new ID("otherId"));
    final Retweet retweet =
        new Retweet(new Date(1L), new ID("retweetId"), new ID("baseId"));
    underTest.addTweet(basetweet);
    underTest.addTweet(retweet);
    thrown.expect(NoRootFoundException.class);
    underTest.findRoot(retweet);
  }
}
