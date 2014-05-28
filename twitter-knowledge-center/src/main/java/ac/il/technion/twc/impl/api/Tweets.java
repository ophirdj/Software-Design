package ac.il.technion.twc.impl.api;

import java.util.ArrayList;
import java.util.List;

import ac.il.technion.twc.api.tweets.BaseTweet;
import ac.il.technion.twc.api.tweets.Retweet;
import ac.il.technion.twc.api.tweets.Tweet;

/**
 * Wrap class for list of tweets.
 * 
 * @author Ziv Ronen
 * @date 22.05.2014
 * @mail akarks@gmail.com
 * 
 * @version 2.0
 * @since 2.0
 */
public class Tweets {
  private List<BaseTweet> baseTweets;
  private List<Retweet> retweets;

  /**
   * @param tweets
   *          get the tweets and split them to base tweets and retweets
   */
  public Tweets(final List<Tweet> tweets) {
    baseTweets = new ArrayList<>();
    retweets = new ArrayList<>();
    for (final Tweet tweet : tweets)
      if (tweet instanceof BaseTweet)
        baseTweets.add((BaseTweet) tweet);
      else
        retweets.add((Retweet) tweet);
  }

  /**
   * 
   */
  public Tweets() {
    baseTweets = new ArrayList<>();
    retweets = new ArrayList<>();
  }

  /**
   * @return the base tweets
   */
  public List<BaseTweet> getBaseTweets() {
    return baseTweets;
  }

  /**
   * set the base tweets
   * 
   * @param baseTweets
   *          new value
   */
  public void setBaseTweets(final List<BaseTweet> baseTweets) {
    this.baseTweets = baseTweets;
  }

  /**
   * @return the retweets
   */
  public List<Retweet> getRetweets() {
    return retweets;
  }

  /**
   * set the retweets
   * 
   * @param retweets
   *          new value
   */
  public void setRetweets(final List<Retweet> retweets) {
    this.retweets = retweets;
  }

}
