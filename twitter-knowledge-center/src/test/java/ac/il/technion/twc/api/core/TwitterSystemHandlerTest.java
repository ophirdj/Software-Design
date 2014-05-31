package ac.il.technion.twc.api.core;

import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.junit.Test;

import ac.il.technion.twc.api.core.ServiceBuildingManager;
import ac.il.technion.twc.api.core.Storage;
import ac.il.technion.twc.api.core.TwitterSystemBuilder;
import ac.il.technion.twc.api.core.TwitterSystemHandler;
import ac.il.technion.twc.api.core.TwitterSystemHandler.Tweets;
import ac.il.technion.twc.api.tweet.BaseTweet;
import ac.il.technion.twc.api.tweet.ID;
import ac.il.technion.twc.api.tweet.Retweet;
import ac.il.technion.twc.api.tweet.Tweet;

/**
 * Test class for {@link TwitterSystemHandler} and {@link TwitterSystemBuilder}
 * 
 * @author Ziv Ronen
 * @date 28.05.2014
 * @mail akarks@gmail.com
 * 
 * @version 2.0
 * @since 2.0
 */
public class TwitterSystemHandlerTest {

	private final Set<Object> services;
	private final Storage storageMock;
	private final ServiceBuildingManager serviceBuilder;

	private final TwitterSystemHandler $;

	/**
   * 
   */
	@SuppressWarnings("unchecked")
	public TwitterSystemHandlerTest() {
		storageMock = mock(Storage.class);
		services = mock(Set.class);
		serviceBuilder = mock(ServiceBuildingManager.class);
		$ = new TwitterSystemHandler(services, serviceBuilder, storageMock);
	}

	/**
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 * @throws IOException
	 */
	@Test
	public final void importDataShouldCallStorageLoadWithEachService()
			throws InstantiationException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException, IOException {
		final Tweets mockOldTweets = mock(Tweets.class);
		final BaseTweet baseTweet = new BaseTweet(new Date(1L), new ID("2763"));
		final Retweet retweet = new Retweet(new Date(2L), new ID("4256"),
				new ID("2763"));
		final List<Tweet> mockNewTweets = Arrays.asList(baseTweet, retweet);
		when(storageMock.load(new Tweets())).thenReturn(mockOldTweets);
		final BaseTweet baseTweet2 = new BaseTweet(new Date(3756L), new ID(
				"356876"));
		final Retweet retweet2 = new Retweet(new Date(3865L),
				new ID("46464774"), new ID("2354362"));
		when(mockOldTweets.getBaseTweets()).thenReturn(
				Arrays.asList(baseTweet2));
		when(mockOldTweets.getRetweets()).thenReturn(Arrays.asList(retweet2));

		final List<Object> os = new ArrayList<>();
		final Integer defaultInteger = new Integer(0);
		final Long defaultLong = new Long(0);
		os.add(defaultInteger);
		os.add(defaultLong);

		when(services.iterator()).thenReturn(os.iterator());
		final Integer integerValue = new Integer(5);
		when(serviceBuilder.getInstance(Integer.class))
				.thenReturn(integerValue);
		final Long longValue = new Long(5L);
		when(serviceBuilder.getInstance(Long.class)).thenReturn(longValue);
		$.importData(mockNewTweets);
		verify(storageMock).store(
				new Tweets(Arrays.asList(baseTweet, baseTweet2, retweet,
						retweet2)));
		verify(serviceBuilder).setProperties(
				Arrays.asList(baseTweet, baseTweet2),
				Arrays.asList(retweet, retweet2));
		verify(storageMock).store(integerValue);
		verify(storageMock).store(longValue);
	}

	/**
	 * 
	 */
	@Test
	public final void loadServicesShouldCallStorageLoadWithEachService() {
		final Object o1 = mock(Object.class);
		final Object o2 = mock(Object.class);
		final Object o3 = mock(Object.class);
		when(services.iterator()).thenReturn(
				Arrays.asList(o1, o2, o3).iterator());
		$.loadServices();
		verify(storageMock).load(o1);
		verify(storageMock).load(o2);
		verify(storageMock).load(o3);
	}

	/**
	 */
	@Test
	public final void getServiceShouldReturnLoadedValue() {
		final Integer o1 = new Integer(1);
		final Long o2 = new Long(1L);
		final List<Object> os = new ArrayList<>();
		final Integer defaultInteger = new Integer(0);
		final Long defaultLong = new Long(0);
		os.add(defaultInteger);
		os.add(defaultLong);
		when(services.iterator()).thenReturn(os.iterator());
		when(storageMock.load(defaultInteger)).thenReturn(o1);
		when(storageMock.load(defaultLong)).thenReturn(o2);
		$.loadServices();
		assertSame(o1, $.getService(Integer.class));
		assertSame(o2, $.getService(Long.class));
	}

	/**
	 * @throws IllegalArgumentException
	 */
	@Test(expected = IllegalArgumentException.class)
	public final void getServiceShouldThrowIfNoServiceWasRegistered()
			throws IllegalArgumentException {
		when(services.iterator()).thenReturn(Collections.emptyIterator());
		$.loadServices();
		$.getService(Byte.class);
	}

	/**
	 * @throws IOException
	 */
	@Test
	public final void clearSystemShouldCallStorageClear() throws IOException {
		$.clear();
		verify(storageMock).clear();
	}

}
