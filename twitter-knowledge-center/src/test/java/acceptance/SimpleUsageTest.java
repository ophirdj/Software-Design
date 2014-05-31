package acceptance;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.junit.Test;

import ac.il.technion.twc.api.Property;
import ac.il.technion.twc.api.TwitterDataCenter;
import ac.il.technion.twc.api.TwitterDataCenterBuilder;
import ac.il.technion.twc.api.TwitterQuery;
import ac.il.technion.twc.api.core.TwitterSystemBuilder;
import ac.il.technion.twc.api.tweet.BaseTweet;
import ac.il.technion.twc.api.tweet.ID;
import ac.il.technion.twc.api.tweet.Retweet;
import ac.il.technion.twc.api.tweet.Tweet;

/**
 * A simple demonstration of the API's core capabilities.
 * 
 * @author Ophir De Jager
 * 
 */
public class SimpleUsageTest {

	private class MyProperty implements Property {

		public final int numBase;
		public final int numRe;

		@SuppressWarnings("unused")
		public MyProperty(final List<BaseTweet> baseTweets,
				final List<Retweet> retweets) {
			numBase = baseTweets.size();
			numRe = retweets.size();
		}

	}

	private class MyQuery implements TwitterQuery {

		public final int numBase;
		public final int numRe;

		@SuppressWarnings("unused")
		public MyQuery(final MyProperty p) {
			numBase = p.numBase;
			numRe = p.numRe;
		}

	}

	/**
	 * Test method for {@link TwitterDataCenterBuilder} and
	 * {@link TwitterDataCenter}
	 */
	@Test
	public final void simpleUsageTest() {
		// Create a builder for the data center
		final TwitterDataCenterBuilder builder = new TwitterSystemBuilder();
		// Add wanted properties and queries
		builder.addProperty(MyProperty.class).registerQuery(MyQuery.class);
		// Create the data center
		final TwitterDataCenter dataCenter = builder.build();

		// Let's get some tweets and count how many are retweets
		final List<? extends Tweet> tweets = getSomeTweets();
		int numReActual = 0;
		for (final Tweet t : tweets)
			if (t instanceof Retweet)
				++numReActual;

		// Import the tweets
		dataCenter.importData(tweets);
		// Evaluate the queries
		dataCenter.loadServices();
		// Now we can ask the queries
		final MyQuery q = dataCenter.getService(MyQuery.class);
		assertEquals(numReActual, q.numRe);
		assertEquals(tweets.size() - numReActual, q.numBase);

		// cleanup of persistent storage
		dataCenter.clear();
	}

	private List<? extends Tweet> getSomeTweets() {
		return Arrays.asList(new BaseTweet(new Date(11111), new ID("base 1")),
				new BaseTweet(new Date(22222), new ID("base 2")), new Retweet(
						new Date(33333), new ID("retweet"), new ID("tweet")));
	}
}
