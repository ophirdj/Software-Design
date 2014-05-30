package ac.il.technion.twc.impl.properties.tweetsretriever;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.junit.Test;

import ac.il.technion.twc.api.tweets.BaseTweet;
import ac.il.technion.twc.api.tweets.ID;
import ac.il.technion.twc.api.tweets.Retweet;

/**
 * Test for {@link TweetsRetriever}
 * 
 * @author Ziv Ronen
 * @date 28.05.2014
 * @mail akarks@gmail.com
 * 
 * @version 2.0
 * @since 2.0
 */
public class TweetsRetrieverTest {
  private static final ArrayList<BaseTweet> EMPTY_BASES =
      new ArrayList<BaseTweet>();
  private static final ArrayList<Retweet> EMPTY_RES = new ArrayList<Retweet>();

  /**
   * Test method for {@link TweetsRetriever#getBaseTweets()}
   */
  @Test
  public final void getBaseTweetsShouldReturnBaseTweet() {
    final BaseTweet bt =
        new BaseTweet(new Date(123456789), new ID("base tweet ID"));
    assertEquals(bt, new TweetsRetriever(Arrays.asList(bt), EMPTY_RES)
        .getBaseTweets().get(0));
  }

  /**
   * Test method for {@link TweetsRetriever#getRetweets()}
   */
  @Test
  public final void getRetweetsShouldReturnRetweet() {
    final Retweet rt =
        new Retweet(new Date(123456789), new ID("retweet ID"), new ID(
            "base tweet ID"));
    assertEquals(rt, new TweetsRetriever(EMPTY_BASES, Arrays.asList(rt))
        .getRetweets().get(0));
  }

  /**
   * Test method for {@link TweetsRetriever#getRetweets()}
   */
  @Test
  public final void getRetweetsShouldReturnNoRetweets() {
    final BaseTweet bt =
        new BaseTweet(new Date(123456789), new ID("base tweet ID"));
    assertEquals(0, new TweetsRetriever(Arrays.asList(bt), EMPTY_RES)
        .getRetweets().size());
  }

  /**
   * Test method for {@link TweetsRetriever#getBaseTweets()}
   */
  @Test
  public final void getBaseTweetsShouldReturnNoBaseTweets() {
    final Retweet rt =
        new Retweet(new Date(123456789), new ID("retweet ID"), new ID(
            "base tweet ID"));
    assertEquals(0, new TweetsRetriever(EMPTY_BASES, Arrays.asList(rt))
        .getBaseTweets().size());
  }

  /**
   * Test method for {@link TweetsRetriever#getBaseTweets()}
   */
  @Test
  public final void getBaseTweetsShouldReturnBaseTweetsByReceiveOrder() {
    final BaseTweet bt1 =
        new BaseTweet(new Date(123456789), new ID("base tweet 1 ID"));
    final BaseTweet bt2 =
        new BaseTweet(new Date(123456789), new ID("base tweet 2 ID"));
    final List<BaseTweet> tweets =
        new TweetsRetriever(Arrays.asList(bt1, bt2), EMPTY_RES).getBaseTweets();
    assertEquals(bt1, tweets.get(0));
    assertEquals(bt2, tweets.get(1));
  }

  /**
   * Test method for {@link TweetsRetriever#getRetweets()}
   */
  @Test
  public final void getRetweetsShouldReturnRetweetsByReceiveOrder() {
    final Retweet rt1 =
        new Retweet(new Date(123456789), new ID("retweet 1 ID"), new ID(
            "base tweet ID"));
    final Retweet rt2 =
        new Retweet(new Date(123456789), new ID("retweet 2 ID"), new ID(
            "base tweet ID"));
    final List<Retweet> tweets =
        new TweetsRetriever(EMPTY_BASES, Arrays.asList(rt1, rt2)).getRetweets();
    assertEquals(rt1, tweets.get(0));
    assertEquals(rt2, tweets.get(1));
  }

  /**
   * Test method for {@link TweetsRetriever#getBaseTweets()},
   * {@link TweetsRetriever#getRetweets()}
   */
  @Test
  public final void
      getBaseTweetsShouldReturnBaseTweetAndGetRetweetsShouldReturnRetweet() {
    final BaseTweet bt =
        new BaseTweet(new Date(123456789), new ID("base tweet ID"));
    final Retweet rt =
        new Retweet(new Date(123456789), new ID("retweet ID"), new ID(
            "base tweet ID"));
    final TweetsRetriever tr =
        new TweetsRetriever(Arrays.asList(bt), Arrays.asList(rt));
    assertEquals(bt, tr.getBaseTweets().get(0));
    assertEquals(rt, tr.getRetweets().get(0));
  }

}
