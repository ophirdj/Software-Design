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
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import ac.il.technion.twc.api.TwitterQuery;
import ac.il.technion.twc.api.core.ServiceBuildingManager.UserMethodInvokationException;
import ac.il.technion.twc.api.core.TwitterDataCenterimpl.BuildFailedException;
import ac.il.technion.twc.api.core.TwitterDataCenterimpl.Tweets;
import ac.il.technion.twc.api.tweet.BaseTweet;
import ac.il.technion.twc.api.tweet.ID;
import ac.il.technion.twc.api.tweet.Retweet;
import ac.il.technion.twc.api.tweet.Tweet;

/**
 * Test class for {@link TwitterDataCenterimpl} and
 * {@link TwitterDataCenterBuilder}
 * 
 * @author Ziv Ronen
 * @date 28.05.2014
 * @mail akarks@gmail.com
 * 
 */
public class TwitterDataCenterImplTest {

  /**
   * 
   */
  public final @Rule
  ExpectedException thrown = ExpectedException.none();

  private final Set<Class<? extends TwitterQuery>> services;
  private final Storage storageMock;
  private final ServiceBuildingManager serviceBuilder;

  private final TwitterDataCenterimpl $;

  /**
   * 
   */
  @SuppressWarnings("unchecked")
  public TwitterDataCenterImplTest() {
    storageMock = mock(Storage.class);
    services = mock(Set.class);
    serviceBuilder = mock(ServiceBuildingManager.class);
    $ = new TwitterDataCenterimpl(services, serviceBuilder, storageMock);
  }

  /**
   * 
   */
  @Test
  public final void ctorShouldSetPropertiesWithEmptyLists() {
    final ServiceBuildingManager mock = mock(ServiceBuildingManager.class);
    new TwitterDataCenterimpl(services, mock, storageMock);
    verify(mock).setProperties(new ArrayList<BaseTweet>(),
        new ArrayList<Retweet>());
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
    final ServiceOne o2 = new ServiceOne();
    final Class<ServiceOne> c = ServiceOne.class;
    final List<Class<? extends TwitterQuery>> os =
        Arrays.<Class<? extends TwitterQuery>> asList(c);
    when(services.iterator()).thenReturn(os.iterator());
    when(serviceBuilder.getInstance(c)).thenReturn(o1);
    when(storageMock.load(c, o1)).thenReturn(o2);
    $.evaluateQueries();
    assertSame(o2, $.getQuery(c));
  }

  /**
   * @throws InvocationTargetException
   * @throws IllegalArgumentException
   * @throws IllegalAccessException
   * @throws InstantiationException
   */
  @Test
  public final void getServiceWithoutEvaluteShouldReturnLoadedValue()
      throws InstantiationException, IllegalAccessException,
      IllegalArgumentException, InvocationTargetException {
    final ServiceOne o1 = new ServiceOne();
    final ServiceOne o2 = new ServiceOne();
    final Class<ServiceOne> c = ServiceOne.class;
    when(services.contains(c)).thenReturn(true);
    when(serviceBuilder.getInstance(c)).thenReturn(o1);
    when(storageMock.load(c, o1)).thenReturn(o2);
    assertSame(o2, $.getQuery(c));
  }

  /**
   * @throws InvocationTargetException
   * @throws IllegalArgumentException
   * @throws IllegalAccessException
   * @throws InstantiationException
   */
  @Test
  public final void
      storeQueryThatCauseExceptionShouldThrowFailedToBuildException()
          throws InstantiationException, IllegalAccessException,
          IllegalArgumentException, InvocationTargetException {

    final Class<? extends TwitterQuery> c = ServiceOne.class;

    final Throwable mockThrow = mock(Throwable.class);
    final UserMethodInvokationException userMethodInvokationException =
        new UserMethodInvokationException(mockThrow);
    when(services.iterator()).thenReturn(
        Arrays.<Class<? extends TwitterQuery>> asList(c).iterator());
    when(serviceBuilder.getInstance(c))
        .thenThrow(userMethodInvokationException);
    when(storageMock.load(Tweets.class, new Tweets())).thenReturn(new Tweets());
    thrown.expect(BuildFailedException.class);
    thrown.expectMessage(storingPrefix());
    thrown.expectMessage(buildingFailedMessages(ServiceOne.class, mockThrow));
    $.importData(Collections.<Tweet> emptyList());
  }

  /**
   * @throws InvocationTargetException
   * @throws IllegalArgumentException
   * @throws IllegalAccessException
   * @throws InstantiationException
   */
  @Test
  public final void
      storeQueryThatCause2ExceptionsShouldThrowFailedToBuildException()
          throws InstantiationException, IllegalAccessException,
          IllegalArgumentException, InvocationTargetException {

    final Class<? extends TwitterQuery> c1 = ServiceOne.class;
    final Class<? extends TwitterQuery> c2 = ServiceTwo.class;
    final Throwable mockThrow = mock(Throwable.class);
    final Throwable mockThrow2 = mock(Throwable.class);
    when(services.iterator()).thenReturn(
        Arrays.<Class<? extends TwitterQuery>> asList(c1, c2).iterator());
    final UserMethodInvokationException userMethodInvokationException1 =
        new UserMethodInvokationException(mockThrow);
    when(serviceBuilder.getInstance(c1)).thenThrow(
        userMethodInvokationException1);
    final UserMethodInvokationException userMethodInvokationException2 =
        new UserMethodInvokationException(mockThrow2);
    when(serviceBuilder.getInstance(c2)).thenThrow(
        userMethodInvokationException2);
    when(storageMock.load(Tweets.class, new Tweets())).thenReturn(new Tweets());
    thrown.expect(BuildFailedException.class);
    thrown.expectMessage(storingPrefix());
    thrown.expectMessage(buildingFailedMessages(ServiceOne.class, mockThrow));
    thrown.expectMessage(buildingFailedMessages(ServiceTwo.class, mockThrow2));
    $.importData(Collections.<Tweet> emptyList());
  }

  /**
   * @throws InvocationTargetException
   * @throws IllegalArgumentException
   * @throws IllegalAccessException
   * @throws InstantiationException
   */
  @Test
  public final void
      evaluateQueriesThatCauseExceptionShouldThrowFailedToBuildException()
          throws InstantiationException, IllegalAccessException,
          IllegalArgumentException, InvocationTargetException {

    final Class<? extends TwitterQuery> c = ServiceOne.class;

    final Throwable mockThrow = mock(Throwable.class);
    final UserMethodInvokationException userMethodInvokationException =
        new UserMethodInvokationException(mockThrow);
    when(services.iterator()).thenReturn(
        Arrays.<Class<? extends TwitterQuery>> asList(c).iterator());
    when(serviceBuilder.getInstance(c))
        .thenThrow(userMethodInvokationException);
    thrown.expect(BuildFailedException.class);
    thrown.expectMessage(loadingPrefix());
    thrown.expectMessage(buildingFailedMessages(ServiceOne.class, mockThrow));
    $.evaluateQueries();
  }

  /**
   * @throws InvocationTargetException
   * @throws IllegalArgumentException
   * @throws IllegalAccessException
   * @throws InstantiationException
   */
  @Test
  public final void
      evaluateQueriesThatCause2ExceptionsShouldThrowFailedToBuildException()
          throws InstantiationException, IllegalAccessException,
          IllegalArgumentException, InvocationTargetException {

    final Class<? extends TwitterQuery> c1 = ServiceOne.class;
    final Class<? extends TwitterQuery> c2 = ServiceTwo.class;
    final Throwable mockThrow = mock(Throwable.class);
    final Throwable mockThrow2 = mock(Throwable.class);
    when(services.iterator()).thenReturn(
        Arrays.<Class<? extends TwitterQuery>> asList(c1, c2).iterator());
    final UserMethodInvokationException userMethodInvokationException1 =
        new UserMethodInvokationException(mockThrow);
    when(serviceBuilder.getInstance(c1)).thenThrow(
        userMethodInvokationException1);
    final UserMethodInvokationException userMethodInvokationException2 =
        new UserMethodInvokationException(mockThrow2);
    when(serviceBuilder.getInstance(c2)).thenThrow(
        userMethodInvokationException2);
    thrown.expect(BuildFailedException.class);
    thrown.expectMessage(loadingPrefix());
    thrown.expectMessage(buildingFailedMessages(ServiceOne.class, mockThrow));
    thrown.expectMessage(buildingFailedMessages(ServiceTwo.class, mockThrow2));
    $.evaluateQueries();
  }

  /**
   * @throws InstantiationException
   * @throws IllegalAccessException
   * @throws IllegalArgumentException
   * @throws InvocationTargetException
   */
  @Test
  public final void
      getQueryThatCauseExceptionsShouldThrowFailedToBuildException()
          throws InstantiationException, IllegalAccessException,
          IllegalArgumentException, InvocationTargetException {

    final Class<? extends TwitterQuery> c1 = ServiceOne.class;
    final Throwable mockThrow = mock(Throwable.class);
    when(services.contains(c1)).thenReturn(true);
    final UserMethodInvokationException userMethodInvokationException1 =
        new UserMethodInvokationException(mockThrow);
    when(serviceBuilder.getInstance(c1)).thenThrow(
        userMethodInvokationException1);
    thrown.expect(BuildFailedException.class);
    thrown.expectMessage(loadingPrefix());
    thrown.expectMessage(buildingFailedMessages(ServiceOne.class, mockThrow));
    $.getQuery(c1);
  }

  private String buildingFailedMessages(final Class<?> type,
      final Throwable cause) {
    return "\t- class " + type.getSimpleName()
        + " can't be build because building it cause " + cause + "\n";
  }

  private String storingPrefix() {
    return "Storing the following queries failed:\n";
  }

  private String loadingPrefix() {
    return "Failed to load and couldn't build default";
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
    $.evaluateQueries();
    $.getQuery(Byte.class);
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
