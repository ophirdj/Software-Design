package ac.il.technion.twc.api;

import java.util.Collection;

import ac.il.technion.twc.message.tweet.Tweet;

/**
 * Our main api for supporting tweets
 * 
 * @author Ziv Ronen
 * @date 22.05.2014
 * @mail akarks@gmail.com
 * 
 * @version 2.0
 * @since 2.0
 */
public interface TwitterServicesCenter {

  /**
   * Add the given tweets to the system
   * 
   * @param parsedTweets
   *          new tweets
   */
  void importData(Collection<Tweet> parsedTweets);

  /**
   * Clear the data from all builders.
   */
  void clearSystem();

}
