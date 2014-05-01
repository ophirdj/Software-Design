package ac.il.technion.twc.histgram;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.GregorianCalendar;

import org.junit.*;

import com.google.inject.Key;
import com.google.inject.name.Names;

import ac.il.technion.twc.God;
import ac.il.technion.twc.message.ID;
import ac.il.technion.twc.message.tweet.BaseTweet;
import ac.il.technion.twc.message.tweet.Retweet;
import ac.il.technion.twc.storage.StorageHandler;

public class DayHistogramBuilderTest {

	private DayHistogramBuilder underTest;
	private final StorageHandler<DayHistogram> storageHandler;
	private final DayHistogram emptyHistogram;

	public DayHistogramBuilderTest() {
		storageHandler = (StorageHandler<DayHistogram>) mock(StorageHandler.class);
		emptyHistogram = God.injector.getInstance(Key.get(DayHistogram.class,
				Names.named("default")));
	}
	
	private void initBuilder(DayHistogram storedHistogram) {
		when(storageHandler.load(emptyHistogram)).thenReturn(storedHistogram);
		underTest = new DayHistogramBuilder(storageHandler,
				emptyHistogram);
	}

	@Test
	public final void constructorShouldCallStorageHandlerToLoadHistogram() {
		initBuilder(emptyHistogram);
		verify(storageHandler).load(emptyHistogram);
		assertNotNull(underTest);
	}
	
	@Test
	public final void constructorShouldUseEmptyHistogramReturnedByStorageHandler() {
		initBuilder(emptyHistogram);
		verify(storageHandler).load(emptyHistogram);
		assertEquals(emptyHistogram, underTest.getResult());
	}
	
	@Test
	public final void constructorShouldUseHistogramReturnedByStorageHandler() {
		DayHistogram storedHistogram = God.injector.getInstance(Key.get(DayHistogram.class,
				Names.named("default")));
		storedHistogram.basetweets.put(DayOfWeek.SUNDAY, 1);
		storedHistogram.retweets.put(DayOfWeek.MONDAY, 23);
		initBuilder(storedHistogram);
		verify(storageHandler).load(emptyHistogram);
		assertEquals(storedHistogram, underTest.getResult());
	}

	@Test
	public final void visitBaseTweetShouldIncreaseNumberOfTweetsBy1() {
		initBuilder(emptyHistogram);
		BaseTweet tweet = new BaseTweet(new GregorianCalendar(2014, 4, 1).getTime(), new ID("trolololol"));
		DayOfWeek day = DayOfWeek.fromDate(tweet.getDate());
		int numTweetsBefore = emptyHistogram.tweets(day);
		underTest.visit(tweet);
		assertEquals(numTweetsBefore + 1, underTest.getResult().tweets(day));
	}
	
	@Test
	public final void visitBaseTweetShouldNotChangeNumberOfRetweets() {
		initBuilder(emptyHistogram);
		BaseTweet tweet = new BaseTweet(new GregorianCalendar(2014, 4, 1).getTime(), new ID("trolololol"));
		DayOfWeek day = DayOfWeek.fromDate(tweet.getDate());
		int numRetweetsBefore = emptyHistogram.retweets(day);
		underTest.visit(tweet);
		assertEquals(numRetweetsBefore, underTest.getResult().retweets(day));
	}

	@Test
	public final void visitRetweetShouldIncreaseNumberOfTweetsBy1() {
		initBuilder(emptyHistogram);
		Retweet tweet = new Retweet(new GregorianCalendar(2014, 4, 1).getTime(), new ID("trolololol"), new ID("lolololol"));
		DayOfWeek day = DayOfWeek.fromDate(tweet.getDate());
		int numTweetsBefore = emptyHistogram.tweets(day);
		underTest.visit(tweet);
		assertEquals(numTweetsBefore + 1, underTest.getResult().tweets(day));
	}
	
	@Test
	public final void visitRetweetShouldIncreaseNumberOfRetweetsBy1() {
		initBuilder(emptyHistogram);
		Retweet tweet = new Retweet(new GregorianCalendar(2014, 4, 1).getTime(), new ID("trolololol"), new ID("lolololol"));
		DayOfWeek day = DayOfWeek.fromDate(tweet.getDate());
		int numTweetsBefore = emptyHistogram.retweets(day);
		underTest.visit(tweet);
		assertEquals(numTweetsBefore + 1, underTest.getResult().retweets(day));
	}
	
	@Test
	public final void visitBaseTweetTwiceInEqualDayShouldIncreaseNumberOfTweetsBy2() {
		initBuilder(emptyHistogram);
		BaseTweet tweet1 = new BaseTweet(new GregorianCalendar(2014, 4, 1).getTime(), new ID("trolololol"));
		BaseTweet tweet2 = new BaseTweet(new GregorianCalendar(2014, 4, 8).getTime(), new ID("lolololol"));
		DayOfWeek day = DayOfWeek.fromDate(tweet1.getDate());
		int numTweetsBefore = emptyHistogram.tweets(day);
		underTest.visit(tweet1);
		underTest.visit(tweet2);
		assertEquals(numTweetsBefore + 2, underTest.getResult().tweets(day));
	}
	
	@Test
	public final void visitRetweetTwiceInEqualDayShouldIncreaseNumberOfTweetsBy2() {
		initBuilder(emptyHistogram);
		Retweet tweet1 = new Retweet(new GregorianCalendar(2014, 4, 1).getTime(), new ID("trolololol"), new ID("lolololol"));
		Retweet tweet2 = new Retweet(new GregorianCalendar(2014, 4, 1).getTime(), new ID("lolololol"), new ID("lol"));
		DayOfWeek day = DayOfWeek.fromDate(tweet1.getDate());
		int numTweetsBefore = emptyHistogram.tweets(day);
		underTest.visit(tweet1);
		underTest.visit(tweet2);
		assertEquals(numTweetsBefore + 2, underTest.getResult().tweets(day));
	}
	
	@Test
	public final void visitRetweetTwiceInEqualDayShouldIncreaseNumberOfRetweetsBy2() {
		initBuilder(emptyHistogram);
		Retweet tweet1 = new Retweet(new GregorianCalendar(2014, 4, 1).getTime(), new ID("trolololol"), new ID("lolololol"));
		Retweet tweet2 = new Retweet(new GregorianCalendar(2014, 4, 1).getTime(), new ID("lolololol"), new ID("lol"));
		DayOfWeek day = DayOfWeek.fromDate(tweet1.getDate());
		int numTweetsBefore = emptyHistogram.retweets(day);
		underTest.visit(tweet1);
		underTest.visit(tweet2);
		assertEquals(numTweetsBefore + 2, underTest.getResult().retweets(day));
	}
	
	@Test
	public final void visitBaseTweetAndRetweetInEqualDayShouldIncreaseNumberOfTweetsBy2() {
		initBuilder(emptyHistogram);
		BaseTweet tweet1 = new BaseTweet(new GregorianCalendar(2014, 4, 1).getTime(), new ID("trolololol"));
		Retweet tweet2 = new Retweet(new GregorianCalendar(2014, 4, 1).getTime(), new ID("lolololol"), new ID("lol"));
		DayOfWeek day = DayOfWeek.fromDate(tweet1.getDate());
		int numTweetsBefore = emptyHistogram.tweets(day);
		underTest.visit(tweet1);
		underTest.visit(tweet2);
		assertEquals(numTweetsBefore + 2, underTest.getResult().tweets(day));
	}
	
	@Test
	public final void visitBaseTweetAndRetweetInEqualDayShouldIncreaseNumberOfRetweetsBy1() {
		initBuilder(emptyHistogram);
		BaseTweet tweet1 = new BaseTweet(new GregorianCalendar(2014, 4, 1).getTime(), new ID("trolololol"));
		Retweet tweet2 = new Retweet(new GregorianCalendar(2014, 4, 1).getTime(), new ID("lolololol"), new ID("lol"));
		DayOfWeek day = DayOfWeek.fromDate(tweet1.getDate());
		int numTweetsBefore = emptyHistogram.retweets(day);
		underTest.visit(tweet1);
		underTest.visit(tweet2);
		assertEquals(numTweetsBefore + 1, underTest.getResult().retweets(day));
	}
	
}
