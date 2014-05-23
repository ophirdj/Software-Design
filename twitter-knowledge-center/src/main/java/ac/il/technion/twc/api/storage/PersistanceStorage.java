package ac.il.technion.twc.api.storage;

import java.io.IOException;

/**
 * For storing and loading values.
 * 
 * note: The type should be equal to the stored object type. Inheritance is not
 * recommended. POJO are best.
 * 
 * @author Ziv Ronen
 * @date 22.05.2014
 * @mail akarks@gmail.com
 * 
 * @version 2.0
 * @since 2.0
 */
public interface PersistanceStorage {
  /**
   * Store a given object. Can only store one object from each type.
   * 
   * @param service
   *          The object to store.
   * @throws IOException
   */
  <T> void store(T service) throws IOException;

  /**
   * @param type
   *          The type of the object we want to load
   * @param defualt
   *          default return value.
   * 
   * @return The last stored object of the given type or default if no such
   *         value exists.
   */
  <T, ST extends T> T load(Class<T> type, ST defualt);

  /**
   * Prepare enable the storage to prepare the given types for feature load.
   * note: Except from performance, The program should behave the same with or
   * without call to that method.
   * 
   * @param types
   *          The types of the objects we want to load
   */
  void prepare(Class<?>... types);

  /**
   * Remove all data that was stored by instance of the same group. The group is
   * implementation depended.
   * 
   * @throws IOException
   */
  void clearAll() throws IOException;
}
