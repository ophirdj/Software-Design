package ac.il.technion.twc;

import java.util.List;

import ac.il.technion.twc.histogram.DayHistogramCache;
import ac.il.technion.twc.lifetime.LifeTimeCache;
import ac.il.technion.twc.message.ID;
import ac.il.technion.twc.message.tweet.Tweet;
import ac.il.technion.twc.message.tweet.builder.TweetBuilder;
import ac.il.technion.twc.message.visitor.MessagePropertyBuilder;

import com.google.inject.Key;
import com.google.inject.TypeLiteral;

/**
 * This class is meant to act as a wrapper to test your functionality. You
 * should implement all its methods and not change any of their signatures. You
 * can also implement an argumentless constructor if you wish.
 * 
 * @author Gal Lalouche
 */
public class TwitterKnowledgeCenter {

	private final TweetBuilder tweetBuilder;
	private final List<MessagePropertyBuilder<?>> propertyBuilders;
	private DayHistogramCache dayHistogram;
	private LifeTimeCache lifeTime;

	public TwitterKnowledgeCenter() {
		tweetBuilder = God.injector.getInstance(TweetBuilder.class);
		propertyBuilders = God.injector.getInstance(Key
				.get(new TypeLiteral<List<MessagePropertyBuilder<?>>>() {
				}));
	}

	/**
	 * Loads the data from an array of lines
	 * 
	 * @param lines
	 *            An array of lines, each line formatted as <time (dd/MM/yyyy
	 *            HH:mm:ss)>,<tweet id>[,original tweet]
	 * @throws Exception
	 *             If for any reason, handling the data failed
	 */
	public void importData(final String[] lines) throws Exception {
		for (final String line : lines) {
			final Tweet t = tweetBuilder.parse(line);
			for (final MessagePropertyBuilder<?> builder : propertyBuilders)
				t.accept(builder);
		}
		for (final MessagePropertyBuilder<?> builder : propertyBuilders)
			builder.saveResult();
	}

	/**
	 * Loads the index, allowing for queries on the data that was imported using
	 * {@link TwitterKnowledgeCenter#importData(String[])}. setupIndex will be
	 * called before any queries can be run on the system
	 * 
	 * @throws Exception
	 *             If for any reason, loading the index failed
	 */
	public void setupIndex() throws Exception {
		dayHistogram = God.injector.getInstance(DayHistogramCache.class);
		lifeTime = God.injector.getInstance(LifeTimeCache.class);
	}

	/**
	 * Gets the lifetime of the tweet, in milliseconds.
	 * 
	 * @param tweetId
	 *            The tweet's identifier
	 * @return A string, counting the number of milliseconds between the tweet's
	 *         publication and its last retweet
	 * @throws Exception
	 *             If it is not possible to complete the operation
	 */
	public String getLifetimeOfTweets(final String tweetId) throws Exception {
		return lifeTime.getLifetimeOfTweets(new ID(tweetId));
	}

	/**
	 * Gets the weekly histogram of all tweet data
	 * 
	 * @return An array of strings, each string in the format of
	 *         ("<number of tweets (including retweets), number of retweets only>"
	 *         ), for example: ["100, 10","250,20",...,"587,0"]. The 0th index
	 *         of the array is Sunday.
	 * @throws Exception
	 *             If it is not possible to complete the operation
	 */
	public String[] getDailyHistogram() {
		return dayHistogram.getDailyHistogram();
	}

	/**
	 * Cleans up all persistent data from the system; this method will be called
	 * before every test, to ensure that all tests are independent.
	 */
	public void cleanPersistentData() {
		throw new UnsupportedOperationException("Not implemented");
	}

}