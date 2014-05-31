package ac.il.technion.twc.api.tweet.parser;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import ac.il.technion.twc.api.tweet.Tweet;

/**
 * A parser that support multiple formats
 * 
 * @author Ziv Ronen
 * @date 22.05.2014
 * @mail akarks@gmail.com
 * 
 * @version 2.0
 * @since 2.0
 */
// TODO: find shorter name
public class MultiFormatsTweetParser implements TweetParser {

	private final List<ParseFormat> formats;

	/**
	 * @param formats
	 */
	public MultiFormatsTweetParser(final List<ParseFormat> formats) {
		this.formats = formats;
	}

	@Override
	public List<Tweet> parse(final String... tweets) throws ParseException {
		final RunningFormat possibleFormats = new RunningFormat(formats);
		for (final String tweet : tweets)
			possibleFormats.parse(tweet);
		return possibleFormats.getTweets();
	}

	private static class RunningFormat {

		private final List<Pair<ParseFormat, List<Tweet>>> formats = new ArrayList<>();

		public RunningFormat(final List<ParseFormat> formats) {
			for (final ParseFormat parserFormat : formats)
				this.formats.add(new Pair<ParseFormat, List<Tweet>>(
						parserFormat, new ArrayList<Tweet>()));
		}

		public List<Tweet> getTweets() throws ParseException {
			if (formats.isEmpty())
				throw new ParseException("No matching parser", 0);
			if (formats.size() > 1 && formats.get(0).second.size() > 0)
				throw new ParseException("Parsing ambiguity",
						formats.get(0).second.size());
			return formats.get(0).second;
		}

		public void parse(final String tweet) {
			for (final Iterator<Pair<ParseFormat, List<Tweet>>> iterator = formats
					.iterator(); iterator.hasNext();) {
				final Pair<ParseFormat, List<Tweet>> parserFormat = iterator
						.next();
				if (!parserFormat.first.isFromFormat(tweet)) {
					iterator.remove();
					continue;
				}
				try {
					parserFormat.second.add(parserFormat.first.parse(tweet));
				} catch (final ParseException e) {
					// Unreachable, Hopefully :P
				}
			}
		}

	}

	private static class Pair<T1, T2> {
		/**
		 * The first element
		 */
		public final T1 first;
		/**
		 * The second element
		 */
		public final T2 second;

		/**
		 * @param t1
		 *            value for the first element
		 * @param t2
		 *            value for the second element
		 */
		public Pair(final T1 t1, final T2 t2) {
			first = t1;
			second = t2;
		}
	}

}
