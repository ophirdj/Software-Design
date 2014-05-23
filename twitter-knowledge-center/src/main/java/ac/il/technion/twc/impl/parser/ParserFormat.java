package ac.il.technion.twc.impl.parser;

import java.text.ParseException;

import ac.il.technion.twc.api.tweets.Tweet;

/**
 * represent a format of tweet
 * 
 * @author Ziv Ronen
 * @date 22.05.2014
 * @mail akarks@gmail.com
 * 
 * @version 2.0
 * @since 2.0
 */
public interface ParserFormat {

  /**
   * @param line
   *          String represent a tweet
   * @return The tweet corresponding to line
   * @throws ParseException
   *           if the tweet is not from current format
   */
  Tweet parse(String line) throws ParseException;

  /**
   * @param line
   *          String represent a tweet
   * @return true if the tweet if from the current format
   */
  boolean isFromFormat(String line);
}
