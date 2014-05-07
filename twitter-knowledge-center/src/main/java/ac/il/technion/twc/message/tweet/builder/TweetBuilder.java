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

public class TweetBuilder {

	private final DateFormat dateFormatter;

	@Inject
	public TweetBuilder(
			@Named("twitter date formatter") final DateFormat dateFormatter) {
		this.dateFormatter = dateFormatter;
	}

	public Tweet parse(final String s) throws ParseException {
		final String[] f = (s + ", $").split(", ");
		final String[] fields = Arrays.copyOf(f, f.length - 1);
		try {
			if (isBaseTweet(fields))
				return parseBaseTweet(fields);
			if (isRetweet(fields))
				return parseRetweet(fields);
		} catch (final ParseException e) {
			throw new ParseException("Bad tweet format: " + s,
					e.getErrorOffset());
		}
		throw new ParseException("Bad tweet format: " + s, 0);
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
