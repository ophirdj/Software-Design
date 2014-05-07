package ac.il.technion.twc.storage;

import java.io.IOException;
import java.nio.file.Path;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.inject.Inject;

/**
 * Loads and stores objects to persistent storage.
 * 
 * @author Ophir De Jager
 * 
 * @param <T>
 *          Type of objects that are loaded / stored.
 */
public class StorageHandler<T> {

  private final Gson serializer;
  private final Path storePath;
  private final FileHandler fileHandling;

  /**
   * C'tor
   * 
   * @param gson
   * @param storageLocation
   * @param fileHandling
   */
  @Inject
  public StorageHandler(final Gson gson, final Path storageLocation,
      final FileHandler fileHandling) {
    serializer = gson;
    storePath = storageLocation;
    this.fileHandling = fileHandling;
  }

  /**
   * Store data to data file (overwriting it if exists).
   * 
   * @param toStore
   *          Data to be stored.
   * @throws IOException
   */
  public void store(final T toStore) throws IOException {
    fileHandling.store(storePath,
        serializer.toJson(toStore, toStore.getClass()));
  }

  /**
   * Load stored data from data file.
   * 
   * @param defaultReturnValue
   *          Value to be returned if no data was stored.
   * @return Stored data.
   */
  public T load(final T defaultReturnValue) {
    try {
      return (T) serializer.fromJson(fileHandling.load(storePath),
          defaultReturnValue.getClass());
    } catch (JsonSyntaxException | IOException e) {
      return defaultReturnValue;
    }
  }
}
