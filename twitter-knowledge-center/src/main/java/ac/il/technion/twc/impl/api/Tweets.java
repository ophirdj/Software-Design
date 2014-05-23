package ac.il.technion.twc.impl.api;

import java.util.ArrayList;
import java.util.List;

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
  private List<Tweet> tweets;

  /**
   * 
   */
  public Tweets() {
    tweets = new ArrayList<>();
  }

  /**
   * @param tweets
   */
  public Tweets(final List<Tweet> tweets) {
    setTweets(tweets);
  }

  /**
   * @return the tweets
   */
  public List<Tweet> getTweets() {
    return tweets;
  }

  /**
   * set the tweets to the given value
   * 
   * @param tweets
   */
  public void setTweets(final List<Tweet> tweets) {
    this.tweets = tweets;
  }

}
