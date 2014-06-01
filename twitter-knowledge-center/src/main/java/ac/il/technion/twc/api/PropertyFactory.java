package ac.il.technion.twc.api;

import java.util.List;

import ac.il.technion.twc.api.tweet.BaseTweet;
import ac.il.technion.twc.api.tweet.Retweet;

/**
 * @author Ziv Ronen
 * @date 31.05.2014
 * @mail akarks@gmail.com
 * 
 * @param <T>
 *          The type of the created property
 */
public interface PropertyFactory<T extends Property> {

  /**
   * @param baseTweets
   *          The base tweets in the system
   * @param retweets
   *          The retweet in the system
   * @return A property
   */
  T get(List<BaseTweet> baseTweets, List<Retweet> retweets);

}
