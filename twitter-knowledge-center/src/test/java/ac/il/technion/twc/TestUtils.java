package ac.il.technion.twc;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

/**
 * @author Ophir De Jager
 * 
 */
public class TestUtils {

	/**
	 * Generate tweets (base tweets and retweets) for a total of
	 * <code>numBase</code> * (<code>numRetweetsForEach</code> ^
	 * <code>numLevels</code> - 1) / (<code>numRetweetsForEach</code> - 1)
	 * tweets.
	 * 
	 * @param numBase
	 *            Number of base tweets.
	 * @param numRetweetsForEach
	 *            Number of retweets each tweet has (if it has any retweets).
	 * @param numLevels
	 *            How many times a tweet can be recursively retweeted (i.e.
	 *            retweet of retweet).
	 * @param seed
	 *            Seed for randomly generated time stamps of tweets.
	 * @return <code>numBase</code> base tweets. Each tweet (base or retweet)
	 *         has <code>numRetweetsForEach</code> retweets, and each retweet
	 *         has again <code>numRetweetsForEach</code> retweets of its own,
	 *         and so forth. There are <code>numLevels</code> levels of retweets
	 *         (i.e. retweet of retweet) for each base tweet.
	 */
	public static String[] generateTweets(final int numBase,
			final int numRetweetsForEach, final int numLevels, final long seed) {
		final List<String> tweets = new ArrayList<>();
		final Random rnd = new Random(seed);
		for (int baseTweetNum = 0; baseTweetNum < numBase; ++baseTweetNum) {
			final String baseId = "base" + baseTweetNum;
			final long baseDate = rnd.nextLong();
			final SimpleDateFormat jsonTweetDateFormat = new SimpleDateFormat(
					"EEE MMM d HH:mm:ss Z yyyy", Locale.ENGLISH);
			tweets.add("{'created_at':'"
					+ jsonTweetDateFormat.format(new Date(baseDate))
					+ "','id_str':'"
					+ baseId
					+ "','text':'#YOLO #SWAG','in_reply_to_status_id_str':null}");
			generateRetweets(tweets, baseId, baseDate, jsonTweetDateFormat,
					rnd, numRetweetsForEach, numLevels);
		}
		Collections.shuffle(tweets, rnd);
		return tweets.toArray(new String[0]);
	}

	private static void generateRetweets(final List<String> out,
			final String originId, final long originDate,
			final DateFormat dateFormatter, final Random rnd,
			final int numRetweets, final int numLevels) {
		if (numLevels <= 0)
			return;
		for (int tweetNum = 0; tweetNum < numRetweets; ++tweetNum) {
			final String id = "re" + tweetNum + "of" + originId;
			final long date = originDate + rnd.nextLong();
			out.add("{'created_at':'" + dateFormatter.format(new Date(date))
					+ "','id_str':'" + id
					+ "','text':'','in_reply_to_status_id_str':'" + originId
					+ "'}");
			generateRetweets(out, id, date, dateFormatter, rnd, numRetweets,
					numLevels - 1);
		}
	}

}
