package ac.il.technion.twc.api;

import java.util.Collection;

import ac.il.technion.twc.message.tweet.Tweet;

/**
 * Parse given tweets
 * 
 * @author Ziv Ronen
 * @date 22.05.2014
 * @mail akarks@gmail.com
 * 
 * @version 2.0
 * @since 2.0
 */
public interface TweetsParser {

  /**
   * @param lines
   *          a string representation of tweets
   * @return a collection of tweets objects corresponding to those tweets.
   */
  Collection<Tweet> parse(String... lines);
}
