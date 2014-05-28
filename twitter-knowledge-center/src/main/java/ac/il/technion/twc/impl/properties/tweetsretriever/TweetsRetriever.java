package ac.il.technion.twc.impl.properties.tweetsretriever;

import java.util.List;

import ac.il.technion.twc.api.tweets.BaseTweet;
import ac.il.technion.twc.api.tweets.Retweet;

/**
 * Keep all the base tweets and retweets in the system
 * 
 * @author Ziv Ronen
 * @date 26.05.2014
 * @mail akarks@gmail.com
 * 
 * @version 2.0
 * @since 2.0
 */
public class TweetsRetriever {

  private final List<BaseTweet> baseTweets;
  private final List<Retweet> retweets;

  /**
   * @param baseTweets
   * @param retweets
   */
  public TweetsRetriever(final List<BaseTweet> baseTweets,
      final List<Retweet> retweets) {
    this.baseTweets = baseTweets;
    this.retweets = retweets;
  }

  /**
   * @return a collection of all the base tweets
   */
  public List<BaseTweet> getBaseTweets() {
    return baseTweets;
  }

  /**
   * @return a collection of all the retweets
   */
  public List<Retweet> getRetweets() {
    return retweets;
  }

}
