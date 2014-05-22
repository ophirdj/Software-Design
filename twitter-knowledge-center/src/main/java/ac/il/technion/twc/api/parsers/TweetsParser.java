package ac.il.technion.twc.api.parsers;

import java.text.ParseException;
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
   * @throws ParseException
   *           if the parser can't parse all the given tweets
   */
  Collection<Tweet> parse(String... lines) throws ParseException;
}