package ac.il.technion.twc.api.tweet.parser;

import java.text.ParseException;

import ac.il.technion.twc.api.tweet.Tweet;

/**
 * represent a format of tweet
 * 
 * @author Ziv Ronen
 * @date 22.05.2014
 * @mail akarks@gmail.com
 * 
 */
public interface TweetFormat {

  /**
   * @param tweet
   *          String representing a tweet
   * @return A tweet corresponding to given string
   * @throws ParseException
   */
  Tweet parse(String tweet) throws ParseException;

}
