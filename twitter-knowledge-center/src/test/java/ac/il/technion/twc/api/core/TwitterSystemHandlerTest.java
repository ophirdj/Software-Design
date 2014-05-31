package ac.il.technion.twc.api.core;

import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.junit.Test;

import ac.il.technion.twc.api.TwitterQuery;
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

  private final Set<Class<? extends TwitterQuery>> services;
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
    final Retweet retweet =
        new Retweet(new Date(2L), new ID("4256"), new ID("2763"));
    final List<Tweet> mockNewTweets = Arrays.asList(baseTweet, retweet);
    when(storageMock.load(Tweets.class, new Tweets()))
        .thenReturn(mockOldTweets);
    final BaseTweet baseTweet2 =
        new BaseTweet(new Date(3756L), new ID("356876"));
    final Retweet retweet2 =
        new Retweet(new Date(3865L), new ID("46464774"), new ID("2354362"));
    when(mockOldTweets.getBaseTweets()).thenReturn(Arrays.asList(baseTweet2));
    when(mockOldTweets.getRetweets()).thenReturn(Arrays.asList(retweet2));

    final Class<ServiceOne> c1 = ServiceOne.class;
    final Class<ServiceTwo> c2 = ServiceTwo.class;
    final List<Class<? extends TwitterQuery>> os = Arrays.asList(c1, c2);
    when(services.iterator()).thenReturn(os.iterator());

    final ServiceOne o1 = new ServiceOne();
    when(serviceBuilder.getInstance(c1)).thenReturn(o1);
    final ServiceOne o2 = new ServiceOne();
    when(serviceBuilder.getInstance(c2)).thenReturn(o2);
    $.importData(mockNewTweets);
    verify(storageMock).store(Tweets.class,
        new Tweets(Arrays.asList(baseTweet, baseTweet2, retweet, retweet2)));
    verify(serviceBuilder).setProperties(Arrays.asList(baseTweet, baseTweet2),
        Arrays.asList(retweet, retweet2));
    verify(storageMock).store(c1, o1);
    verify(storageMock).store(c2, o2);
  }

  /**
   * @throws InvocationTargetException
   * @throws IllegalArgumentException
   * @throws IllegalAccessException
   * @throws InstantiationException
   */
  @Test
  public final void getServiceShouldReturnLoadedValue()
      throws InstantiationException, IllegalAccessException,
      IllegalArgumentException, InvocationTargetException {
    final ServiceOne o1 = new ServiceOne();
    final ServiceTwo o2 = new ServiceTwo();
    final ServiceOne o3 = new ServiceOne();
    final ServiceTwo o4 = new ServiceTwo();
    final Class<ServiceOne> c1 = ServiceOne.class;
    final Class<ServiceTwo> c2 = ServiceTwo.class;
    final List<Class<? extends TwitterQuery>> os = Arrays.asList(c1, c2);
    when(services.iterator()).thenReturn(os.iterator());
    when(serviceBuilder.getInstance(c1)).thenReturn(o1);
    when(serviceBuilder.getInstance(c2)).thenReturn(o2);
    when(storageMock.load(c1, o1)).thenReturn(o3);
    when(storageMock.load(c2, o2)).thenReturn(o4);
    $.loadServices();
    assertSame(o3, $.getService(c1));
    assertSame(o4, $.getService(c2));
  }

  /**
   * @throws IllegalArgumentException
   */
  @Test(expected = IllegalArgumentException.class)
  public final void getServiceShouldThrowIfNoServiceWasRegistered()
      throws IllegalArgumentException {
    final Iterator<Class<? extends TwitterQuery>> emptyIterator =
        Collections.emptyIterator();
    when(services.iterator()).thenReturn(emptyIterator);
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

  private static class ServiceOne implements TwitterQuery {
  }

  private static class ServiceTwo implements TwitterQuery {
  }

}
