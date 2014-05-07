package ac.il.technion.twc.lifetime;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Date;
import java.util.GregorianCalendar;

import org.junit.Test;

import ac.il.technion.twc.God;
import ac.il.technion.twc.message.ID;
import ac.il.technion.twc.message.tweet.BaseTweet;
import ac.il.technion.twc.message.tweet.Retweet;
import ac.il.technion.twc.storage.StorageHandler;

import com.google.inject.Key;
import com.google.inject.name.Names;

public class LifeTimeBuilderTest {

	private LifeTimeBuilder underTest;
	private final StorageHandler<LifeTimeMap> storageHandler;
	private final LifeTimeMap emptyMap;

	public LifeTimeBuilderTest() {
		storageHandler = mock(StorageHandler.class);
		emptyMap = God.injector.getInstance(Key.get(LifeTimeMap.class,
				Names.named("default")));
	}

	private void initBuilder(final LifeTimeMap storedMap) {
		when(storageHandler.load(emptyMap)).thenReturn(storedMap);
		underTest = new LifeTimeBuilder(storageHandler, emptyMap);
	}

	@Test
	public final void constructorShouldCallStorageHandlerToLoadMap() {
		initBuilder(emptyMap);
		verify(storageHandler).load(emptyMap);
		assertNotNull(underTest);
	}

	@Test
	public final void constructorShouldUseEmptyMapReturnedByStorageHandler() {
		initBuilder(emptyMap);
		verify(storageHandler).load(emptyMap);
		assertEquals(emptyMap, underTest.getResult());
	}

	@Test
	public final void constructorShouldUseHistogramReturnedByStorageHandler() {
		final LifeTimeMap storedMap = God.injector.getInstance(Key.get(
				LifeTimeMap.class, Names.named("default")));
		// TODO: modify stored map
		initBuilder(storedMap);
		verify(storageHandler).load(emptyMap);
		assertEquals(storedMap, underTest.getResult());
	}

	@Test
	// return value not defined by course requirements
	public final void visitBaseTweetLifeTimeShouldBe0() {
		initBuilder(emptyMap);
		final ID id = new ID("trolololol");
		underTest.visit(new BaseTweet(new GregorianCalendar(2014, 4, 1, 0, 0)
				.getTime(), id));
		assertEquals(Long.valueOf(0), underTest.getResult().get(id));
	}

	@Test
	public final void retweetSouldExtendLifTimeOfBaseTweet() {
		initBuilder(emptyMap);
		final ID baseId = new ID("base");
		final ID reId = new ID("retweet");
		underTest.visit(new BaseTweet(new GregorianCalendar(2014, 4, 1, 3, 00)
				.getTime(), baseId));
		underTest.visit(new Retweet(new GregorianCalendar(2014, 4, 1, 4, 30)
				.getTime(), reId, baseId));
		assertEquals(Long.valueOf(90 * 60 * 1000),
				underTest.getResult().get(baseId));
	}

	@Test
	public final void notRelatedRetweetsShouldntChangeLifeTimeOfBaseTweetLifeTimeShouldRemain24Hours() {
		initBuilder(emptyMap);
		final ID baseId = new ID("base");
		final ID reId = new ID("retweet");
		underTest.visit(new BaseTweet(new GregorianCalendar(2014, 4, 1)
				.getTime(), baseId));
		underTest.visit(new Retweet(
				new GregorianCalendar(2014, 4, 2).getTime(), reId, baseId));
		underTest.visit(new Retweet(
				new GregorianCalendar(2014, 5, 2).getTime(), new ID(
						"unrelated retweet"), new ID("not base")));
		assertEquals(Long.valueOf(24 * 60 * 60 * 1000), underTest.getResult()
				.get(baseId));
	}

	@Test
	public final void retweetOfRetweetShouldExtendLifeTimeOfBaseTweet() {
		initBuilder(emptyMap);
		final ID baseId = new ID("base");
		final ID reId = new ID("retweet");
		underTest.visit(new BaseTweet(new GregorianCalendar(2014, 4, 1)
				.getTime(), baseId));
		underTest.visit(new Retweet(
				new GregorianCalendar(2014, 4, 2).getTime(), reId, baseId));
		underTest.visit(new Retweet(
				new GregorianCalendar(2014, 4, 3).getTime(), new ID(
						"another retweet"), reId));
		assertEquals(Long.valueOf(48 * 60 * 60 * 1000), underTest.getResult()
				.get(baseId));
	}

	@Test
	public final void retweetOfRetweetShouldExtendLifeTimeOfBaseTweetNoMatterOrderOfRetweets() {
		initBuilder(emptyMap);
		final ID baseId = new ID("base");
		final ID reId = new ID("retweet");
		underTest.visit(new BaseTweet(new GregorianCalendar(2014, 4, 1)
				.getTime(), baseId));
		underTest.visit(new Retweet(
				new GregorianCalendar(2014, 4, 3).getTime(), new ID(
						"another retweet"), reId));
		underTest.visit(new Retweet(
				new GregorianCalendar(2014, 4, 2).getTime(), reId, baseId));
		assertEquals(Long.valueOf(48 * 60 * 60 * 1000), underTest.getResult()
				.get(baseId));
	}

	@Test
	public final void retweetOfRetweetShouldExtendLifeTimeOfBaseTweetEvenIfRetweetsAreVisitingBeforeBaseTweet() {
		initBuilder(emptyMap);
		final ID baseId = new ID("base");
		final ID reId = new ID("retweet");
		underTest.visit(new Retweet(
				new GregorianCalendar(2014, 4, 3).getTime(), new ID(
						"another retweet"), reId));
		underTest.visit(new Retweet(
				new GregorianCalendar(2014, 4, 2).getTime(), reId, baseId));
		underTest.visit(new BaseTweet(new GregorianCalendar(2014, 4, 1)
				.getTime(), baseId));
		assertEquals(Long.valueOf(48 * 60 * 60 * 1000), underTest.getResult()
				.get(baseId));
	}

	@Test
	public final void baseTweetShouldNotExtendLifeTimeOfAnotherBaseTweet() {
		initBuilder(emptyMap);
		final ID base1Id = new ID("base 1");
		final ID base2Id = new ID("base 2");
		underTest.visit(new BaseTweet(new GregorianCalendar(2014, 4, 1)
				.getTime(), base1Id));
		underTest.visit(new BaseTweet(new GregorianCalendar(2014, 4, 3)
				.getTime(), base2Id));
		underTest.visit(new Retweet(
				new GregorianCalendar(2014, 4, 2).getTime(), new ID("retweet"),
				base1Id));
		assertEquals(Long.valueOf(48 * 60 * 60 * 1000), underTest.getResult()
				.get(base1Id));
	}

	@Test
	public final void lifeTimeShouldBeDeterminedByTheChronologicallyLatestRetweet() {
		initBuilder(emptyMap);
		final ID baseId = new ID("base");
		underTest.visit(new BaseTweet(new GregorianCalendar(2014, 4, 1, 10, 00)
				.getTime(), baseId));
		underTest.visit(new Retweet(new GregorianCalendar(2014, 4, 1, 10, 30)
				.getTime(), new ID("retweet 1"), baseId));
		underTest.visit(new Retweet(new GregorianCalendar(2014, 4, 1, 14, 00)
				.getTime(), new ID("retweet 2"), baseId));
		underTest.visit(new Retweet(new GregorianCalendar(2014, 4, 1, 12, 59)
				.getTime(), new ID("retweet 3"), baseId));
		assertEquals(Long.valueOf(4 * 60 * 60 * 1000),
				underTest.getResult().map.get(baseId));
	}

	@Test
	public final void lifeTimeShouldBeAccurate() {
		initBuilder(emptyMap);
		final ID baseId = new ID("base");
		final long baseTime = 123456789;
		final long interval = 111111111;
		underTest.visit(new BaseTweet(new Date(123456), baseId));
		underTest.visit(new Retweet(new Date(baseTime + interval), new ID(
				"retweet 1"), baseId));
		assertEquals(Long.valueOf(interval), underTest.getResult().get(baseId));
	}

}
