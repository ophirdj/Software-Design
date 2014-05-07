package ac.il.technion.twc.lifetime;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
	public final void visitBaseTweetLifeTimeShouldBe0() {
		initBuilder(emptyMap);
		final ID id = new ID("trolololol");
		final BaseTweet t = new BaseTweet(
				new GregorianCalendar(2014, 4, 1).getTime(), id);
		underTest.visit(t);
		assertEquals(Long.valueOf(0), underTest.getResult().map.get(id));
	}
	
	@Test
	public final void baseTweetThenOneRetweetLifeTimeShouldBe24Hours() {
		initBuilder(emptyMap);
		final ID baseId = new ID("trolololol");
		final BaseTweet base = new BaseTweet(
				new GregorianCalendar(2014, 4, 1).getTime(), baseId);
		final ID reId = new ID("lolololol");
		Retweet re = new Retweet(new GregorianCalendar(2014, 4, 2).getTime(), reId, baseId);
		underTest.visit(base);
		underTest.visit(re);
		assertEquals(Long.valueOf(24 * 60 * 60 * 1000), underTest.getResult().map.get(baseId));
	}
	
	@Test
	public final void baseTweetThenOneRetweetThenLaterNotRelatedRetweetLifeTimeShouldBe24Hours() {
		initBuilder(emptyMap);
		final ID baseId = new ID("trolololol");
		final BaseTweet base = new BaseTweet(
				new GregorianCalendar(2014, 4, 1).getTime(), baseId);
		final ID reId = new ID("lolololol");
		Retweet re = new Retweet(new GregorianCalendar(2014, 4, 2).getTime(), reId, baseId);
		Retweet notRelated = new Retweet(new GregorianCalendar(2014, 5, 2).getTime(), reId, new ID("not base ID"));
		underTest.visit(base);
		underTest.visit(re);
		assertEquals(Long.valueOf(24 * 60 * 60 * 1000), underTest.getResult().map.get(baseId));
	}

	@Test
	public final void visitBaseTweet() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void visitRetweet() {
		fail("Not yet implemented"); // TODO
	}

}
