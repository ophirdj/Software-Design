package ac.il.technion.twc.api;

import java.util.List;

import ac.il.technion.twc.api.tweet.BaseTweet;
import ac.il.technion.twc.api.tweet.Retweet;

/**
 * @author Ziv Ronen
 * @date 31.05.2014
 * @mail akarks@gmail.com
 * 
 * @version 2.0
 * @since 2.0
 * @param <T>
 */
public interface PropertyFactory<T> {

  /**
   * @param baseTweets
   *          The base tweets in the system
   * @param retweets
   *          The retweet in the system
   * @return A property
   */
  T get(List<BaseTweet> baseTweets, List<Retweet> retweets);

}
