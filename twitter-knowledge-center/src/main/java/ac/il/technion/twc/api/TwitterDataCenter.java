package ac.il.technion.twc.api;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Collection;

import ac.il.technion.twc.api.tweet.Tweet;

/**
 * A manager that is responsible for getting tweets, computing properties over
 * these tweets and services over these properties, and managing storage
 * operations for those services
 * 
 * @author Ziv Ronen
 * @date 22.05.2014
 * @mail akarks@gmail.com
 * 
 */
public interface TwitterDataCenter {

  /**
   * Unchecked wrapper for checked exceptions. Wrap exceptions that was caused
   * by failure in the underlying system java run upon.
   * 
   * For instance, {@link IOException}
   * 
   * @author Ophir De Jager
   * 
   */
  public static class SystemOperationFailedException extends RuntimeException {

    private static final long serialVersionUID = 2095741218824580067L;

    /**
     * @param e
     *          the cause of the exception
     */
    public SystemOperationFailedException(final Exception e) {
      super(e);
    }
  }

  /**
   * Unchecked wrapper for checked exceptions. Wrap exceptions that was caused
   * by failure in our creating process. Usually, they can be solved by making
   * your properties and queries pojos.
   * 
   * 
   * For instance, {@link IllegalAccessError}
   * 
   * @author Ophir De Jager
   * 
   */
  public static class CreatingOperationFailedException extends RuntimeException {

    private static final long serialVersionUID = 2095741218824580067L;
    private final Type type;

    /**
     * @param e
     *          the cause of the exception
     * @param type
     *          the type that cause the exception
     */
    public CreatingOperationFailedException(final Exception e, final Type type) {
      super(e);
      this.type = type;
    }

    /**
     * @return The type that cause the exception
     */
    public Type getCausingType() {
      return type;
    }

    @Override
    public String toString() {
      return "Caused in type :" + type.toString() + ". Exception detail: "
          + getCause().toString();
    }
  }

  /**
   * Add tweets to the system, compute all properties and services on all the
   * tweets so far, then store the services in a persistent storage.
   * 
   * @param tweets
   *          new tweets
   * @throws SystemOperationFailedException
   *           If storing system state as failed
   */
  void importData(Collection<? extends Tweet> tweets)
      throws SystemOperationFailedException;

  /**
   * evaluate all registered queries
   */
  void evaluateQueries();

  /**
   * Clear persistent storage.
   * 
   * @throws SystemOperationFailedException
   *           If clearing persistent storage failed.
   */
  void clear() throws SystemOperationFailedException;

  /**
   * 
   * @param type
   * @return The service from the given type
   * @throws IllegalArgumentException
   *           If a service that isn't registered was requested
   */
  <T> T getQuery(Class<T> type) throws IllegalArgumentException;

}
