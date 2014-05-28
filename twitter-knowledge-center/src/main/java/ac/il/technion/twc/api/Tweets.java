package ac.il.technion.twc.api;

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

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + (baseTweets == null ? 0 : baseTweets.hashCode());
    result = prime * result + (retweets == null ? 0 : retweets.hashCode());
    return result;
  }

  @Override
  public boolean equals(final Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    final Tweets other = (Tweets) obj;
    if (baseTweets == null) {
      if (other.baseTweets != null)
        return false;
    } else if (!baseTweets.equals(other.baseTweets))
      return false;
    if (retweets == null) {
      if (other.retweets != null)
        return false;
    } else if (!retweets.equals(other.retweets))
      return false;
    return true;
  }

}
