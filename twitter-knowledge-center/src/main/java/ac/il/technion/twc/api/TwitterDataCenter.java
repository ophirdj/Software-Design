package ac.il.technion.twc.api;

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
 * @version 2.0
 * @since 2.0
 */
public interface TwitterDataCenter {

  /**
   * Unchecked wrapper for checked exceptions.
   * 
   * @author Ophir De Jager
   * 
   */
  public static class OperationFailedException extends RuntimeException {

    private static final long serialVersionUID = 2095741218824580067L;
    private final Exception cause;

    /**
     * @param e
     */
    public OperationFailedException(final Exception e) {
      cause = e;
    }

    @Override
    public Exception getCause() {
      return cause;
    }

  }

  /**
   * Add tweets to the system, compute all properties and services on all the
   * tweets so far, then store the services in a persistent storage.
   * 
   * @param tweets
   *          new tweets
   * @throws OperationFailedException
   *           If storing system state as failed
   */
  void importData(Collection<? extends Tweet> tweets)
      throws OperationFailedException;

  /**
   * evaluate all registered queries
   */
  void evaluateQueries();

  /**
   * Clear persistent storage.
   * 
   * @throws OperationFailedException
   *           If clearing persistent storage failed.
   */
  void clear() throws OperationFailedException;

  /**
   * 
   * @param type
   * @return The service from the given type
   * @throws IllegalArgumentException
   *           If a service that isn't registered was requested
   */
  <T> T getService(Class<T> type) throws IllegalArgumentException;

}
