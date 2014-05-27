package ac.il.technion.twc.impl.api.storage;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import ac.il.technion.twc.api.storage.PersistanceStorage;
import ac.il.technion.twc.impl.services.histogram.DayHistogram;
import ac.il.technion.twc.impl.services.lifetime.TweetToLifeTime;
import ac.il.technion.twc.impl.services.lifetime.TweetToLifeTimeSerializer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

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
public class StorageTest {

  private static final class FutureFromCallable implements
      Answer<Future<Object>> {
    @Override
    public Future<Object> answer(final InvocationOnMock invocation)
        throws Throwable {
      final Callable<Object> caller =
          (Callable<Object>) invocation.getArguments()[0];
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
  private final Path fullTestPath = Paths.get(Storage.class.getCanonicalName())
      .resolve(testPath);
  private final Storage $;

  /**
   * 
   */
  public final @Rule
  ExpectedException thrown = ExpectedException.none();
  private final FileHandler fileHandlingMock;
  private final ExecutorService threadPoolMock;
  final Gson gson;

  /**
   * C'tor
   */
  public StorageTest() {
    fileHandlingMock = mock(FileHandler.class);
    threadPoolMock = mock(ExecutorService.class);
    when(threadPoolMock.submit(any(Callable.class))).thenAnswer(
        new FutureFromCallable());
    gson =
        new GsonBuilder()
            .setDateFormat("EEE MMM d HH:mm:ss Z yyyy")
            .registerTypeAdapter(TweetToLifeTime.class,
                new TweetToLifeTimeSerializer()).create();
    $ = new Storage(gson, testPath, fileHandlingMock, threadPoolMock);
  }

  /**
   * Test method for: {@link PersistanceStorage#load(Class, Object)}
   * 
   * @throws IOException
   * @throws ExecutionException
   * @throws InterruptedException
   */
  @Test
  public void histogramLoadWithoutPreviousStoreShouldReturnDefaultValue()
      throws IOException, InterruptedException, ExecutionException {
    checkDefualtAnswer(DayHistogram.class);

  }

  private <T> void checkDefualtAnswer(final Class<T> type) throws IOException {
    when(fileHandlingMock.load(fullTestPath.resolve(type.getCanonicalName())))
        .thenThrow(new IOException());
    final T defualtVal = mock(type);
    assertEquals(defualtVal, $.load(type, defualtVal));
  }

  /**
   * Test method for: {@link PersistanceStorage#load(Class, Object)}
   * 
   * @throws IOException
   * @throws ExecutionException
   * @throws InterruptedException
   */
  @Test
  public void histogramLoadReturnStoredValue() throws IOException,
      InterruptedException, ExecutionException {
    checkStoredAnswer(DayHistogram.class, DayHistogram.empty());
  }

  private <T> void checkStoredAnswer(final Class<T> type, final T toStore)
      throws IOException {
    when(fileHandlingMock.load(fullTestPath.resolve(type.getCanonicalName())))
        .thenReturn(gson.toJson(toStore));
    final T defualtVal = mock(type);
    assertEquals(toStore, $.load(type, defualtVal));
  }

  /**
   * Test method for: {@link PersistanceStorage#clear()}
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
