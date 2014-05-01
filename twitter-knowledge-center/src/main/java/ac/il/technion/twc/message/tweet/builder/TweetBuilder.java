package ac.il.technion.twc.message.tweet.builder;

import java.text.DateFormat;
import java.text.ParseException;

import com.google.inject.Inject;
import com.google.inject.name.Named;

import ac.il.technion.twc.message.ID;
import ac.il.technion.twc.message.tweet.BaseTweet;
import ac.il.technion.twc.message.tweet.Retweet;
import ac.il.technion.twc.message.tweet.Tweet;

public class TweetBuilder {
	
	private final DateFormat dateFormatter;
	
	@Inject
	public TweetBuilder(@Named("twitter date formatter") DateFormat dateFormatter) {
		this.dateFormatter = dateFormatter;
	}

	public Tweet parse(String s) throws ParseException {
		String[] fields = s.split(", ");
		try {
		if(isBaseTweet(fields)) return parseBaseTweet(fields);
		if(isRetweet(fields)) return parseRetweet(fields);
		} catch(ParseException e) {
			throw new ParseException("Bad tweet format: " + s, e.getErrorOffset());
		}
		throw new ParseException("Bad tweet format: " + s, 0);
	}

	private boolean isRetweet(String[] fields) {
		return fields != null && fields.length == 3;
	}
	
	private Retweet parseRetweet(String[] fields) throws ParseException {
		return new Retweet(dateFormatter.parse(fields[0]), new ID(fields[1]), new ID(fields[2]));
	}

	private boolean isBaseTweet(String[] fields) {
		return fields != null && fields.length == 2;
	}
	
	private BaseTweet parseBaseTweet(String[] fields) throws ParseException {
		return new BaseTweet(dateFormatter.parse(fields[0]), new ID(fields[1]));
	}

}
