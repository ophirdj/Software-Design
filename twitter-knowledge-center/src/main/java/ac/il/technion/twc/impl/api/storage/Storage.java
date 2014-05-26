package ac.il.technion.twc.impl.api.storage;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import org.apache.commons.io.FileUtils;

import ac.il.technion.twc.api.storage.PersistanceStorage;

import com.google.gson.Gson;

/**
 * Basic implementation of {@link PersistanceStorage}
 * 
 * @author Ziv Ronen
 * @date 23.05.2014
 * @mail akarks@gmail.com
 * 
 * @version 2.0
 * @since 2.0
 */
public class Storage implements PersistanceStorage {

  private final Gson serializer;
  private final FileHandler fileHandling;
  private final Map<Class<?>, Future<Object>> retriverByType;
  private final ExecutorService threadPool;
  private final Path storePath;

  /**
   * @param gson
   * @param fileHandling
   * @param threadPool
   * 
   * @param storageLocation
   *          The file directory containing the files. All the instance with the
   *          same path have the same group.
   */
  public Storage(final Gson gson, final Path storageLocation,
      final FileHandler fileHandling, final ExecutorService threadPool) {
    serializer = gson;
    this.fileHandling = fileHandling;
    this.threadPool = threadPool;
    retriverByType = new HashMap<Class<?>, Future<Object>>();
    storePath =
        Paths.get(getClass().getCanonicalName()).resolve(storageLocation);
  }

  @Override
  public <T> void store(final T service) throws IOException {
    retriverByType.remove(service.getClass());
    fileHandling.store(objectPath(service.getClass()),
        serializer.toJson(service, service.getClass()));
  }

  @Override
  public <T, ST extends T> T load(final Class<T> type, final ST defualt) {
    if (!retriverByType.containsKey(type))
      prepareSingle(type);
    final Future<Object> value = retriverByType.get(type);
    try {
      // It will be wise to limit the time for such operation
      return type.cast(value.get());
    } catch (final InterruptedException e) {
      // TODO not sure what to do here
      throw new RuntimeException(e);
    } catch (final ExecutionException e) {
      // TODO: need to handle differently
      throw new RuntimeException(e);
    }
  }

  @Override
  public void prepare(final Class<?>... types) {
    for (final Class<?> type : types)
      try {
        prepareSingle(type);
      } catch (final Exception e) {
        // prepare should not do anything if failed
      }
  }

  private void prepareSingle(final Class<?> type) {
    retriverByType.put(type, threadPool.submit(new Callable<Object>() {

      @Override
      public Object call() throws Exception {
        return serializer.fromJson(fileHandling.load(objectPath(type)), type);
      }
    }));
  }

  @Override
  public void clear() throws IOException {
    FileUtils.cleanDirectory(storePath.toFile());
    retriverByType.clear();
  }

  private <T> Path objectPath(final Class<T> serviceClass) {
    return storePath.resolve(serviceClass.getCanonicalName());
  }

}
