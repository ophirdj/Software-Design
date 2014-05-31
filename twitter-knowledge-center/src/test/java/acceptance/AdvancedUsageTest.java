package acceptance;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
import ac.il.technion.twc.api.tweet.Retweet;

/**
 * @author Ophir De Jager
 * 
 */
public class AdvancedUsageTest {

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

	@SuppressWarnings("javadoc")
	public static class UselessProperty implements Property {

		public static int numInstansiations = 0;

		public static class UselessPropertyFactory implements
				PropertyFactory<UselessProperty> {

			@Override
			public UselessProperty get(final List<BaseTweet> baseTweets,
					final List<Retweet> retweets) {
				++numInstansiations;
				return new UselessProperty();
			}

		}

	}

	@SuppressWarnings("javadoc")
	public static class UselessQuery implements TwitterQuery {

		public static class UselessQueryFactory implements
				TwitterQueryFactory<UselessQuery> {

			public UselessQuery get(final UselessProperty p) {
				return new UselessQuery();
			}

		}

		public String query() {
			return "useless query";
		}

	}

	@SuppressWarnings("javadoc")
	public static class UselessQuery2 implements TwitterQuery {

		public UselessQuery2(final UselessProperty p) {
		}

		public String query() {
			return "useless query 2";
		}

	}

	/**
	 * Test Method for {@link TwitterDataCenter}, {@link TwitterSystemBuilder}.
	 */
	@Test
	public final void sharedPropertyBetweenQueriesShouldBeCalculatedOnlyOnce() {
		// Add 1 property and 2 queries that use that property
		final TwitterDataCenter dataCenter = new TwitterSystemBuilder(dir)
				.addProperty(UselessProperty.class,
						new UselessProperty.UselessPropertyFactory())
				.registerQuery(UselessQuery.class,
						new UselessQuery.UselessQueryFactory())
				.registerQuery(UselessQuery2.class).build();
		// Evaluate both queries (also calculates the property)
		dataCenter.evaluateQueries();
		// See that queries were loaded successfully
		assertEquals("useless query", dataCenter.getService(UselessQuery.class)
				.query());
		assertEquals("useless query 2",
				dataCenter.getService(UselessQuery2.class).query());
		// The shared property was evaluated only once
		assertEquals(1, UselessProperty.numInstansiations);
		// cleanup
		dataCenter.clear();
	}

}
