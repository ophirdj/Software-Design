package acceptance;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ac.il.technion.twc.api.Property;
import ac.il.technion.twc.api.PropertyFactory;
import ac.il.technion.twc.api.TwitterDataCenter;
import ac.il.technion.twc.api.TwitterQuery;
import ac.il.technion.twc.api.TwitterQueryFactory;
import ac.il.technion.twc.api.core.TwitterSystemBuilder;
import ac.il.technion.twc.api.tweet.BaseTweet;
import ac.il.technion.twc.api.tweet.ID;
import ac.il.technion.twc.api.tweet.Retweet;
import ac.il.technion.twc.api.tweet.Tweet;
import ac.il.technion.twc.api.tweet.parser.TweetFormat;
import ac.il.technion.twc.api.tweet.parser.TweetParser;

/**
 * @author Ophir De Jager
 * 
 */
public class AdvancedExample {

	private static final Path dir = Paths.get("AdvancedUsageTest");

	/**
	 * @throws IOException
	 */
	@Before
	public void setup() throws IOException {
		if (Files.exists(dir) && Files.isDirectory(dir))
			FileUtils.deleteDirectory(dir.toFile());
	}

	/**
	 * @throws IOException
	 */
	@After
	public void tearDown() throws IOException {
		if (Files.exists(dir) && Files.isDirectory(dir))
			FileUtils.deleteDirectory(dir.toFile());
	}

	/**
	 * Test Method for {@link TwitterDataCenter}, {@link TwitterSystemBuilder}.
	 */
	@Test
	public final void sharedPropertyBetweenQueriesShouldBeCalculatedOnlyOnce() {
		// Add 1 property and 2 queries that use that property
		final TwitterDataCenter dataCenter = new TwitterSystemBuilder(dir)
				.addProperty(SharedProperty.class,
						new SharedProperty.UselessPropertyFactory())
				.registerQuery(Query1.class, new Query1.UselessQueryFactory())
				.registerQuery(Query2.class).build();
		// Evaluate both queries (also calculates the property)
		dataCenter.evaluateQueries();
		// The property was evaluated only once and is shared between queries
		assertEquals("query 1, property instance: 1",
				dataCenter.getService(Query1.class).query());
		assertEquals("query 2, property instance: 1",
				dataCenter.getService(Query2.class).query());
		// cleanup
		dataCenter.clear();
	}

	/**
	 * Test Method for {@link TweetParser}.
	 * 
	 * @throws ParseException
	 */
	@Test
	public final void usingTweetParser() throws ParseException {
		final String myTweetFormat = "this is my tweet format";
		final Tweet t = new BaseTweet(new Date(123456789), new ID(
				"this is my tweet"));
		final TweetParser parser = new TweetParser(new TweetFormat() {

			@Override
			public Tweet parse(final String tweet) throws ParseException {
				if (myTweetFormat.equals(tweet))
					return t;
				throw new ParseException("", 0);
			}

		});

		assertEquals(t, parser.parse(myTweetFormat).get(0));
	}

	@SuppressWarnings("javadoc")
	public static class SharedProperty implements Property {

		public static class UselessPropertyFactory implements
				PropertyFactory<SharedProperty> {

			@Override
			public SharedProperty get(final List<BaseTweet> baseTweets,
					final List<Retweet> retweets) {
				return new SharedProperty(++numInstansiations);
			}

		}

		public static int numInstansiations = 0;
		private final int number;

		public SharedProperty(final int number_) {
			number = number_;
		}

	}

	@SuppressWarnings("javadoc")
	public static class Query1 implements TwitterQuery {

		public static class UselessQueryFactory implements
				TwitterQueryFactory<Query1> {

			public Query1 get(final SharedProperty p) {
				return new Query1(p.number);
			}

		}

		private final int number;

		public Query1(final int number_) {
			number = number_;
		}

		public String query() {
			return "query 1, property instance: " + number;
		}

	}

	@SuppressWarnings("javadoc")
	public static class Query2 implements TwitterQuery {

		private final int number;

		public Query2(final SharedProperty p) {
			number = p.number;
		}

		public String query() {
			return "query 2, property instance: " + number;
		}

	}

}
