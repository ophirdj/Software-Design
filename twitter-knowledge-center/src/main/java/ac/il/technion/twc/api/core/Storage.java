package ac.il.technion.twc.api.core;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import com.google.gson.Gson;

/**
 * Basic implementation for storing and retriving data
 * 
 * @author Ziv Ronen
 * @date 23.05.2014
 * @mail akarks@gmail.com
 * 
 * @version 2.0
 * @since 2.0
 */
class Storage {

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
    storePath = storageLocation.resolve(Paths.get(getClass().getSimpleName()));
  }

  /**
   * Store a given object. Can only store one object from each type.
   * 
   * @param service
   *          The object to store.
   * @throws IOException
   */
  public <T> void store(final T service) throws IOException {
    retriverByType.remove(service.getClass());
    fileHandling.store(objectPath(service.getClass()),
        serializer.toJson(service, service.getClass()));
  }

  /**
   * @param defualt
   *          default return value.
   * 
   * @return The last stored object of the given type or default if no such
   *         value exists.
   */
  public <T> T load(final T defualt) {
    @SuppressWarnings("unchecked")
    final Class<T> type = (Class<T>) defualt.getClass();
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
      if (e.getCause() instanceof IOException)
        return defualt;
      else
        throw new RuntimeException(e.getCause());
    }
  }

  /**
   * Prepare enable the storage to prepare the given types for feature load.
   * note: Except from performance, The program should behave the same with or
   * without call to that method.
   * 
   * @param types
   *          The types of the objects we want to load
   */
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

  /**
   * Remove all data that was stored by instance of the same group. The group is
   * implementation depended.
   * 
   * @throws IOException
   */
  public void clear() throws IOException {
    fileHandling.clear(storePath);
    retriverByType.clear();
  }

  private <T> Path objectPath(final Class<T> serviceClass) {
    return storePath.resolve(serviceClass.getCanonicalName());
  }

}
