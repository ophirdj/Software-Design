package ac.il.technion.twc.message.tweet.builder;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Arrays;

import ac.il.technion.twc.message.ID;
import ac.il.technion.twc.message.tweet.BaseTweet;
import ac.il.technion.twc.message.tweet.Retweet;
import ac.il.technion.twc.message.tweet.Tweet;

import com.google.inject.Inject;
import com.google.inject.name.Named;

/**
 * Parses tweets.
 * 
 * @author Ophir De Jager
 * 
 */
public class TweetBuilder {

	private final DateFormat dateFormatter;

	/**
	 * @param dateFormatter
	 *            Parses dates.
	 */
	@Inject
	public TweetBuilder(
			@Named("twitter date formatter") final DateFormat dateFormatter) {
		this.dateFormatter = dateFormatter;
	}

	/**
	 * @param tweetStr
	 *            String representing a tweet.
	 * @return A tweet corresponding to <code>tweetStr</code>.
	 * @throws ParseException
	 *             If bad tweet string.
	 */
	public Tweet parse(final String tweetStr) throws ParseException {
		final String[] f = (tweetStr + ", $").split(", ");
		final String[] fields = Arrays.copyOf(f, f.length - 1);
		try {
			if (isBaseTweet(fields))
				return parseBaseTweet(fields);
			if (isRetweet(fields))
				return parseRetweet(fields);
		} catch (final ParseException e) {
			throw new ParseException("Bad tweet format: " + tweetStr,
					e.getErrorOffset());
		}
		throw new ParseException("Bad tweet format: " + tweetStr, 0);
	}

	private boolean isRetweet(final String[] fields) {
		return fields != null && fields.length == 3;
	}

	private Retweet parseRetweet(final String[] fields) throws ParseException {
		return new Retweet(dateFormatter.parse(fields[0]), new ID(fields[1]),
				new ID(fields[2]));
	}

	private boolean isBaseTweet(final String[] fields) {
		return fields != null && fields.length == 2;
	}

	private BaseTweet parseBaseTweet(final String[] fields)
			throws ParseException {
		return new BaseTweet(dateFormatter.parse(fields[0]), new ID(fields[1]));
	}

}
