package ac.il.technion.twc.api.center.impl;

import static junitparams.JUnitParamsRunner.$;
import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

import org.junit.Test;
import org.junit.runner.RunWith;

import ac.il.technion.twc.api.center.impl.TwitterSystemHandler.Tweets;
import ac.il.technion.twc.api.tweets.BaseTweet;
import ac.il.technion.twc.api.tweets.ID;
import ac.il.technion.twc.api.tweets.Retweet;
import ac.il.technion.twc.api.tweets.Tweet;

/**
 * Test for {@link Tweets}
 * 
 * @author Ziv Ronen
 * @date 28.05.2014
 * @mail akarks@gmail.com
 * 
 * @version 2.0
 * @since 2.0
 */
@RunWith(JUnitParamsRunner.class)
public class TweetsTest {

  @SuppressWarnings("unused")
  // used by JunitParams
      private
      Object[] testTweets() {
    final BaseTweet baseTweet1 = new BaseTweet(new Date(), new ID("b"));
    final Retweet reTweet1 = new Retweet(new Date(), new ID("c"), new ID("b"));
    final BaseTweet baseTweet2 = new BaseTweet(new Date(), new ID("d"));
    final Retweet reTweet2 = new Retweet(new Date(), new ID("e"), new ID("c"));
    return $(
        $(Arrays.asList(new BaseTweet[0]), Arrays.asList(new Retweet[0]),
            Arrays.asList(new Tweet[0])),
        $(Arrays.asList(new BaseTweet[] { baseTweet1 }),
            Arrays.asList(new Retweet[0]),
            Arrays.asList(new Tweet[] { baseTweet1 })),
        $(Arrays.asList(new BaseTweet[] {}),
            Arrays.asList(new Retweet[] { reTweet1 }),
            Arrays.asList(new Tweet[] { reTweet1 })),
        $(Arrays.asList(new BaseTweet[] { baseTweet1 }),
            Arrays.asList(new Retweet[] { reTweet1 }),
            Arrays.asList(new Tweet[] { baseTweet1, reTweet1 })),
        $(Arrays.asList(new BaseTweet[] { baseTweet1, baseTweet2 }),
            Arrays.asList(new Retweet[] { reTweet1, reTweet2 }),
            Arrays.asList(new Tweet[] { baseTweet1, reTweet1, baseTweet2,
                reTweet2 })));
  }

  /**
   * Test that the constructor split tweets to base tweets and retweets
   * 
   * @param expectedBase
   *          The base tweets in tweets
   * @param expectedRe
   *          The retweets in tweets
   * @param tweets
   *          All the tweets
   */
  @Parameters(method = "testTweets")
  @Test
  public void runTweetsTest(final Collection<BaseTweet> expectedBase,
      final Collection<Retweet> expectedRe, final List<Tweet> tweets) {
    final Tweets $ = new Tweets(tweets);
    assertEquals(new HashSet<>(expectedBase), new HashSet<>($.getBaseTweets()));
    assertEquals(new HashSet<>(expectedRe), new HashSet<>($.getRetweets()));
  }

}
