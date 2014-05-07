package ac.il.technion.twc;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import org.apache.commons.io.FileUtils;

import ac.il.technion.twc.histogram.DayHistogramCache;
import ac.il.technion.twc.lifetime.LifeTimeCache;
import ac.il.technion.twc.lifetime.LifeTimeData.UndefinedTimeException;
import ac.il.technion.twc.message.ID;
import ac.il.technion.twc.message.tweet.Tweet;
import ac.il.technion.twc.message.tweet.builder.TweetBuilder;
import ac.il.technion.twc.message.visitor.MessagePropertyBuilder;
import ac.il.technion.twc.modules.DayHistogramModule;
import ac.il.technion.twc.modules.LifeTimeModule;
import ac.il.technion.twc.modules.MessagePropertyBuildersModule;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.TypeLiteral;
import com.google.inject.name.Names;

/**
 * This class is meant to act as a wrapper to test your functionality. You
 * should implement all its methods and not change any of their signatures. You
 * can also implement an argumentless constructor if you wish.
 * 
 * @author Gal Lalouche
 */
public class TwitterKnowledgeCenter {

	public static final Injector injector = Guice.createInjector(
			new MessagePropertyBuildersModule(), new DayHistogramModule(),
			new LifeTimeModule());

	private final TweetBuilder tweetBuilder;
	private List<MessagePropertyBuilder<?>> propertyBuilders;
	private DayHistogramCache dayHistogram;
	private LifeTimeCache lifeTime;

	/**
	 * C'tor.
	 */
	public TwitterKnowledgeCenter() {
		tweetBuilder = injector.getInstance(TweetBuilder.class);
		propertyBuilders = injector.getInstance(Key
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
		dayHistogram = injector.getInstance(DayHistogramCache.class);
		lifeTime = injector.getInstance(LifeTimeCache.class);
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
	public String getLifetimeOfTweets(final String tweetId) {
		try {
			return lifeTime.getLifetimeOfTweets(new ID(tweetId));
		} catch (final UndefinedTimeException e) {
			throw new RuntimeException(e);
		}
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
	public String[] getDailyHistogram() throws Exception {
		return dayHistogram.getDailyHistogram();
	}

	/**
	 * Cleans up all persistent data from the system; this method will be called
	 * before every test, to ensure that all tests are independent.
	 */
	public void cleanPersistentData() {
		try {
			FileUtils.cleanDirectory(injector.getInstance(
					Key.get(Path.class, Names.named("storage directory")))
					.toFile());
			propertyBuilders = injector.getInstance(Key
					.get(new TypeLiteral<List<MessagePropertyBuilder<?>>>() {
					}));
		} catch (final IOException e) {
			// can't find/clean the folder
		}
	}

}
