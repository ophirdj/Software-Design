package ac.il.technion.twc.api.core;

import static junitparams.JUnitParamsRunner.$;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import ac.il.technion.twc.api.properties.OriginFinder;
import ac.il.technion.twc.api.properties.OriginFinder.NotFoundException;
import ac.il.technion.twc.api.properties.TweetsRetriever;
import ac.il.technion.twc.api.tweet.BaseTweet;
import ac.il.technion.twc.api.tweet.ID;
import ac.il.technion.twc.api.tweet.Retweet;
import ac.il.technion.twc.impl.properties.hashtags.IdHashtags;
import ac.il.technion.twc.impl.services.histogram.DayHistogram;
import ac.il.technion.twc.impl.services.histogram.HistogramFormat;
import ac.il.technion.twc.impl.services.histogram.TemporalHistogram;
import ac.il.technion.twc.impl.services.lifetime.TweetToLifeTime;
import ac.il.technion.twc.impl.services.lifetime.TweetToLifeTimeSerializer;
import ac.il.technion.twc.impl.services.tagpopularity.TagToPopularity;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

/**
 * test for {@link Storage}
 * 
 * @author Ziv Ronen
 * @date 27.05.2014
 * @mail akarks@gmail.com
 * 
 * @version 2.0
 * @since 2.0
 */
@RunWith(JUnitParamsRunner.class)
public class StorageTest {

  private static final class FutureFromCallable implements
      Answer<Future<Object>> {
    @Override
    public Future<Object> answer(final InvocationOnMock invocation)
        throws Throwable {

      @SuppressWarnings("unchecked")
      // The call always use Callable<Object>
      final Callable<Object> caller =
          (Callable<Object>) invocation.getArguments()[0];
      // can't check generic types while mocking
      @SuppressWarnings("unchecked")
      final Future<Object> $ = mock(Future.class);
      try {
        final Object retVal = caller.call();
        when($.get()).thenReturn(retVal);
      } catch (final Exception e) {
        when($.get()).thenThrow(new ExecutionException(e));
      }
      return $;
    }
  }

  private final Path testPath = Paths.get("test");
  private final Path fullTestPath = testPath.resolve(Paths.get(Storage.class
      .getSimpleName()));
  private final Storage $;

  /**
   * 
   */
  public final @Rule
  ExpectedException thrown = ExpectedException.none();
  private final FileHandler fileHandlingMock;
  private final ExecutorService threadPoolMock;
  private final Gson gson;

  /**
   * C'tor
   */
  // can't check generic types while mocking
  @SuppressWarnings("unchecked")
  public StorageTest() {
    fileHandlingMock = mock(FileHandler.class);
    threadPoolMock = mock(ExecutorService.class);
    when(threadPoolMock.submit(any(Callable.class))).thenAnswer(
        new FutureFromCallable());
    gson =
        new GsonBuilder()
            .setDateFormat("EEE MMM d HH:mm:ss Z yyyy")
            .registerTypeAdapter(TweetToLifeTime.class,
                new TypeAdapter<TweetToLifeTime>() {

                  @Override
                  public void write(final JsonWriter out,
                      final TweetToLifeTime value) throws IOException {
                    out.value(new TweetToLifeTimeSerializer()
                        .objectToString(value));
                  }

                  @Override
                  public TweetToLifeTime read(final JsonReader in)
                      throws IOException {
                    return new TweetToLifeTimeSerializer().stringToObject(in
                        .nextString());
                  }

                }).create();
    $ = new Storage(gson, testPath, fileHandlingMock, threadPoolMock);
  }

  /**
   * Test method for: {@link PersistanceStorage#load(Object)}
   */
  @SuppressWarnings("unused")
  // used by JunitParams
      private
      Object[] defualtFromType() {
    return $(
        $(DayHistogram.class, new DayHistogram(new HistogramFormat())),
        $(TweetToLifeTime.class, new TweetToLifeTime()),
        $(TemporalHistogram.class, new TemporalHistogram(new HistogramFormat())),
        $(TagToPopularity.class, new TagToPopularity()));
  }

  /**
   * @param type
   * @param defaultValue
   * @throws IOException
   */
  @Parameters(method = "defualtFromType")
  @Test
  public <T> void checkDefualtAnswer(final Class<T> type, final T defaultValue)
      throws IOException {
    when(fileHandlingMock.load(fullTestPath.resolve(type.getCanonicalName())))
        .thenThrow(new IOException());
    assertEquals(defaultValue, $.load(type, defaultValue));
  }

  /**
   * Test method for: {@link PersistanceStorage#load(Object)}
   * 
   * @throws NotFoundException
   */
  @SuppressWarnings("unused")
  // used by JunitParams
      private
      Object[] loadFromType() throws NotFoundException {
    final List<String> hashtags = Arrays.asList("YOLO", "SWAG");
    final BaseTweet bt =
        new BaseTweet(new Date(123456789), new ID("base"), hashtags);
    final Retweet rt =
        new Retweet(new Date(987654321), new ID("retweet"), new ID("base"));
    final OriginFinder trf = mock(OriginFinder.class);
    final TweetsRetriever tr = mock(TweetsRetriever.class);
    final IdHashtags ih = mock(IdHashtags.class);
    when(tr.getRetweets()).thenReturn(Arrays.asList(rt));
    when(trf.origin(rt)).thenReturn(bt);
    when(ih.getHashtags(new ID("base"))).thenReturn(hashtags);
    final TweetToLifeTime defaultTweetToLifeTime = new TweetToLifeTime(trf, tr);
    final TagToPopularity defaultTagToPopularity =
        new TagToPopularity(trf, tr, ih);
    return $(
        $(DayHistogram.class, new DayHistogram(new HistogramFormat()),
            new DayHistogram(mock(HistogramFormat.class))),
        $(TemporalHistogram.class,
            new TemporalHistogram(new HistogramFormat()),
            new TemporalHistogram(mock(HistogramFormat.class))),
        $(TweetToLifeTime.class, new TweetToLifeTime(), defaultTweetToLifeTime),
        $(TagToPopularity.class, new TagToPopularity(), defaultTagToPopularity));
  }

  /**
   * @param type
   * @param toStore
   * @param defaultValue
   * @throws IOException
   */
  @Parameters(method = "loadFromType")
  @Test
  public <T> void checkStoredAnswer(final Class<T> type, final T toStore,
      final T defaultValue) throws IOException {
    when(fileHandlingMock.load(fullTestPath.resolve(type.getCanonicalName())))
        .thenReturn(gson.toJson(toStore));
    assertEquals(toStore, $.load(type, defaultValue));
  }

  /**
   * Test method for: {@link Storage#clear()}
   * 
   * @throws IOException
   * @throws ExecutionException
   * @throws InterruptedException
   */
  @Test
  public void clearShouldCallFileHandlerClearWithTestPath() throws IOException,
      InterruptedException, ExecutionException {
    $.clear();
    verify(fileHandlingMock).clear(fullTestPath);

  }
}
