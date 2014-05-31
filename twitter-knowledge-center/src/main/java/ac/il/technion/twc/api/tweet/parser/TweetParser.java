package ac.il.technion.twc.api.tweet.parser;

import java.text.ParseException;
import java.util.List;

import ac.il.technion.twc.api.tweet.Tweet;

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
public interface TweetParser {

	/**
	 * @param lines
	 *            a string representation of tweets
	 * @return a collection of tweets objects corresponding to those tweets.
	 * @throws ParseException
	 *             if the parser can't parse all the given tweets
	 */
	List<Tweet> parse(String... lines) throws ParseException;
}
