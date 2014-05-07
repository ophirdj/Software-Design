package ac.il.technion.twc.message.visitor;

import java.io.IOException;

import ac.il.technion.twc.storage.StorageHandler;

import com.google.inject.Inject;

/**
 * @author Ziv Ronen
 * @date 07.05.2014
 * @mail akarks@gmail.com
 * 
 * @version 2.0
 * @since 2.0
 * @param <T>
 * 
 *          Handler for property index building and retrieving
 */
public abstract class MessagePropertyBuilder<T> implements MessageVisitor<Void> {

  private final StorageHandler<T> storageHandler;

  /**
   * @param storageHandler
   *          handler for storing and loading data
   */
  @Inject
  public MessagePropertyBuilder(final StorageHandler<T> storageHandler) {
    this.storageHandler = storageHandler;
  }

  /**
   * @return The data to be stored
   */
  protected abstract T getResult();

  /**
   * save the result to a file
   */
  public final void saveResult() throws IOException {
    storageHandler.store(getResult());
  }

  /**
   * Load the final stored result
   * 
   * @param defaultReturnValue
   *          value to return if reading fail
   * @return The stored data or defaultReturnValue if reading failed
   */
  public final T loadResult(final T defaultReturnValue) {
    return storageHandler.load(defaultReturnValue);
  }

}
