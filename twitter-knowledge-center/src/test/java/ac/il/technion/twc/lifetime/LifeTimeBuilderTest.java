package ac.il.technion.twc.lifetime;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import ac.il.technion.twc.TwitterKnowledgeCenter;
import ac.il.technion.twc.lifetime.LifeTimeCache.UndefinedTimeException;
import ac.il.technion.twc.lifetime.TransitiveRootFinder.NoRootFoundException;
import ac.il.technion.twc.message.ID;
import ac.il.technion.twc.message.tweet.BaseTweet;
import ac.il.technion.twc.message.tweet.Retweet;
import ac.il.technion.twc.storage.StorageHandler;

import com.google.inject.Key;
import com.google.inject.name.Names;

/**
 * Tests for {@link LifeTimeBuilder}.
 * 
 * @author Ophir De Jager
 * 
 */
public class LifeTimeBuilderTest {

	private final LifeTimeBuilder underTest;
	private final TransitiveRootFinder rootFinder;
	private final LifeTimeData emptyLifeTime;

	/**
	 * C'tor.
	 */
	// mocking of generic type can't be checked
	@SuppressWarnings("unchecked")
	public LifeTimeBuilderTest() {
		rootFinder = mock(TransitiveRootFinder.class);
		emptyLifeTime = TwitterKnowledgeCenter.injector.getInstance(Key.get(
				LifeTimeData.class, Names.named("default")));
		underTest = new LifeTimeBuilder(rootFinder);
	}

	/**
	 * 
	 * @param numBase
	 *            Number of base tweets.
	 * @param numRetweetsForEach
	 *            Number of retweets each tweet has (if it has any retweets).
	 * @param numLevels
	 *            How many times a tweet can be recursively retweeted (i.e.
	 *            retweet of retweet).
	 * @return A {@link LifeTimeData} that has <code>numBase</code> base tweets.
	 *         Each tweet (base or retweet) has <code>numRetweetsForEach</code>
	 *         retweets, and each retweet has again
	 *         <code>numRetweetsForEach</code> retweets of its own, and so
	 *         forth. There are <code>numLevels</code> levels of retweets (i.e.
	 *         retweet of retweet) for each base tweet.
	 * @throws NoRootFoundException
	 *             Never.
	 */
	private LifeTimeData getLifeTimeData(final int numBase,
			final int numRetweetsForEach, final int numLevels)
			throws NoRootFoundException {
		final Set<BaseTweet> baseTweets = new HashSet<>();
		final Set<Retweet> retweets = new HashSet<>();

		for (int baseTweetNum = 0; baseTweetNum < numBase; ++baseTweetNum) {
			final ID baseId = new ID("base " + baseTweetNum);
			final long baseDate = 123456789L + baseTweetNum;
			final BaseTweet baseTweet = new BaseTweet(new Date(baseDate),
					baseId);
			baseTweets.add(baseTweet);
			when(rootFinder.findRoot(baseTweet)).thenReturn(baseTweet);
			for (int level = 0; level < numLevels; ++level)
				for (int retweetNum = 0; retweetNum < numRetweetsForEach; ++retweetNum) {
					final ID originId = level > 0 ? new ID("retweet of base "
							+ baseTweetNum + " level " + (level - 1)) : baseId;
					final Retweet retweet = new Retweet(new Date(baseDate + 24
							* 60 * 60 * 1000 * level + 10 * 1000 * retweetNum),
							new ID("retweet of base " + baseTweetNum
									+ " level " + level), originId);
					retweets.add(retweet);
					when(rootFinder.findRoot(retweet)).thenReturn(baseTweet);
				}
		}

		return new LifeTimeData(baseTweets, retweets);
	}

	private void initBuilder(final LifeTimeData storedLifeTime) {
		underTest.initializeFromState(storedLifeTime);
	}

	/**
	 * Test method for
	 * {@link LifeTimeBuilder#LifeTimeBuilder(StorageHandler, TransitiveRootFinder, LifeTimeData)}
	 */
	@Test
	public final void constructorShouldCallStorageHandlerToLoadMap() {
		initBuilder(emptyLifeTime);
		assertNotNull(underTest);
	}

	/**
	 * Test method for
	 * {@link LifeTimeBuilder#LifeTimeBuilder(StorageHandler, TransitiveRootFinder, LifeTimeData)}
	 */
	@Test
	public final void constructorShouldUseEmptyLifeTimeReturnedByStorageHandler() {
		initBuilder(emptyLifeTime);
		assertEquals(emptyLifeTime, underTest.getState());
	}

	/**
	 * Test method for
	 * {@link LifeTimeBuilder#LifeTimeBuilder(StorageHandler, TransitiveRootFinder, LifeTimeData)}
	 * 
	 * @throws NoRootFoundException
	 *             Never.
	 */
	@Test
	public final void constructorShouldUseLifeTimeReturnedByStorageHandler()
			throws NoRootFoundException {
		final LifeTimeData storedLifeTime = getLifeTimeData(10, 3, 2);
		initBuilder(storedLifeTime);
		assertEquals(storedLifeTime, underTest.getState());
	}

	/**
	 * Test method for {@link LifeTimeBuilder#visit(BaseTweet)},
	 * {@link LifeTimeBuilder#visit(Retweet)},
	 * {@link LifeTimeBuilder#getState()}
	 * 
	 * @throws NoRootFoundException
	 *             Never.
	 * @throws UndefinedTimeException
	 *             Shouldn't happen.
	 */
	@Test
	public final void retweetShouldExtendLifeTimeOfBaseTweet()
			throws NoRootFoundException, UndefinedTimeException {
		initBuilder(emptyLifeTime);
		final BaseTweet base = new BaseTweet(new GregorianCalendar(2014, 4, 1,
				3, 00).getTime(), new ID("base"));
		final Retweet re = new Retweet(
				new GregorianCalendar(2014, 4, 1, 4, 30).getTime(), new ID(
						"retweet"), base.id());
		when(rootFinder.findRoot(re)).thenReturn(base);
		underTest.visit(base);
		underTest.visit(re);
		assertEquals(Long.valueOf(90 * 60 * 1000).toString(), underTest
				.getResultCache().getLifetimeOfTweets(base.id()));
	}

	/**
	 * Test method for {@link LifeTimeBuilder#visit(BaseTweet)},
	 * {@link LifeTimeBuilder#visit(Retweet)},
	 * {@link LifeTimeBuilder#getState()}
	 * 
	 * @throws NoRootFoundException
	 *             Never.
	 * @throws UndefinedTimeException
	 *             Shouldn't happen.
	 */
	@Test
	public final void notRelatedRetweetsShouldntChangeLifeTimeOfBaseTweetLifeTimeShouldRemain24Hours()
			throws NoRootFoundException, UndefinedTimeException {
		initBuilder(getLifeTimeData(3, 3, 3));
		final ID reId = new ID("retweet");
		final BaseTweet base = new BaseTweet(
				new GregorianCalendar(2014, 4, 1).getTime(), new ID("base"));
		final Retweet re = new Retweet(
				new GregorianCalendar(2014, 4, 2).getTime(), reId, base.id());
		when(rootFinder.findRoot(re)).thenReturn(base);
		final Retweet noRelated = new Retweet(
				new GregorianCalendar(2014, 5, 2).getTime(), new ID(
						"unrelated retweet"), new ID("not base"));
		when(rootFinder.findRoot(noRelated)).thenThrow(
				new NoRootFoundException());
		underTest.visit(base);
		underTest.visit(re);
		underTest.visit(noRelated);
		assertEquals(Long.valueOf(24 * 60 * 60 * 1000).toString(), underTest
				.getResultCache().getLifetimeOfTweets(base.id()));
	}

	/**
	 * Test method for {@link LifeTimeBuilder#visit(BaseTweet)},
	 * {@link LifeTimeBuilder#visit(Retweet)},
	 * {@link LifeTimeBuilder#getState()}
	 * 
	 * @throws NoRootFoundException
	 *             Never.
	 * @throws UndefinedTimeException
	 *             Shouldn't happen.
	 */
	@Test
	public final void lifeTimeShouldBeAccurate() throws NoRootFoundException,
			UndefinedTimeException {
		initBuilder(emptyLifeTime);
		final long baseTime = 123456789;
		final long interval = 111111111;
		final BaseTweet base = new BaseTweet(new Date(baseTime), new ID("base"));
		final Retweet re = new Retweet(new Date(baseTime + interval), new ID(
				"retweet 1"), base.id());
		when(rootFinder.findRoot(re)).thenReturn(base);
		underTest.visit(base);
		underTest.visit(re);
		assertEquals(Long.valueOf(interval).toString(), underTest
				.getResultCache().getLifetimeOfTweets(base.id()));
	}

	/**
	 * Test method for {@link LifeTimeBuilder#visit(BaseTweet)},
	 * {@link LifeTimeBuilder#visit(Retweet)},
	 * {@link LifeTimeBuilder#getState()}
	 * 
	 * @throws NoRootFoundException
	 *             Never.
	 * @throws UndefinedTimeException
	 */
	@Test
	public final void lifeTimeShouldBeDeterminedByTheChronologicallyLatestRetweet()
			throws NoRootFoundException, UndefinedTimeException {
		initBuilder(emptyLifeTime);
		final BaseTweet base = new BaseTweet(new GregorianCalendar(2014, 4, 1,
				10, 00).getTime(), new ID("base"));
		final Retweet re1 = new Retweet(new GregorianCalendar(2014, 4, 1, 10,
				30).getTime(), new ID("retweet 1"), base.id());
		final Retweet re2 = new Retweet(new GregorianCalendar(2014, 4, 1, 14,
				00).getTime(), new ID("retweet 2"), base.id());
		final Retweet re3 = new Retweet(new GregorianCalendar(2014, 4, 1, 12,
				59).getTime(), new ID("retweet 3"), base.id());
		when(rootFinder.findRoot(re1)).thenReturn(base);
		when(rootFinder.findRoot(re2)).thenReturn(base);
		when(rootFinder.findRoot(re3)).thenReturn(base);
		underTest.visit(base);
		underTest.visit(re1);
		underTest.visit(re2);
		underTest.visit(re3);
		assertEquals(Long.valueOf(4 * 60 * 60 * 1000).toString(), underTest
				.getResultCache().getLifetimeOfTweets(base.id()));
	}

}
