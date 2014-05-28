package ac.il.technion.twc.api.center;

import java.io.IOException;
import java.util.Collection;

import ac.il.technion.twc.api.tweets.Tweet;

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
   * @throws IOException
   *           If storing system state as failed
   */
  // TODO: should use a different exception
  void importData(Collection<Tweet> parsedTweets) throws IOException;

  /**
   * Clear the data from all builders.
   * 
   * @throws IOException
   */
  void clearSystem() throws IOException;

}
