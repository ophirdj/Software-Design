package ac.il.technion.twc.impl.parser;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;

import ac.il.technion.twc.api.tweet.BaseTweet;
import ac.il.technion.twc.api.tweet.ID;
import ac.il.technion.twc.api.tweet.Retweet;
import ac.il.technion.twc.api.tweet.Tweet;
import ac.il.technion.twc.api.tweet.parser.TweetFormat;

/**
 * @author Ziv Ronen
 * @date 29.05.2014
 * @mail akarks@gmail.com
 * 
 * @version 2.0
 * @since 2.0
 */
public class CommaSeparatedTweetFormat implements TweetFormat {

	private static final SimpleDateFormat csTweetDateFormat = new SimpleDateFormat(
			"dd/MM/yyyy HH:mm:ss");

	@Override
	public Tweet parse(final String line) throws ParseException {
		if (null == line)
			throw new ParseException("Bad tweet format: " + line, 0);
		final String[] fields = extractFields(line);
		if (isBaseTweet(fields))
			return new BaseTweet(csTweetDateFormat.parse(fields[0]), new ID(
					fields[1]));
		if (isRetweet(fields))
			return new Retweet(csTweetDateFormat.parse(fields[0]), new ID(
					fields[1]), new ID(fields[2]));
		throw new ParseException("Bad tweet format: " + line, 0);
	}

	private String[] extractFields(final String line) {
		final String[] f = (line + ", $").split(", ");
		final String[] fields = Arrays.copyOf(f, f.length - 1);
		return fields;
	}

	private boolean isRetweet(final String[] fields) {
		return fields != null && 3 == fields.length && isDate(fields[0])
				&& ID.isID(fields[1]) & ID.isID(fields[2]);
	}

	private boolean isBaseTweet(final String[] fields) {
		return fields != null && 2 == fields.length && isDate(fields[0])
				&& ID.isID(fields[1]);
	}

	private static boolean isDate(final String field) {
		try {
			csTweetDateFormat.parse(field);
			return true;
		} catch (final ParseException e) {
			return false;
		}
	}

}
