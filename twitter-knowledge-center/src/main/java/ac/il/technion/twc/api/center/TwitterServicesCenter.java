package ac.il.technion.twc.api.center;

import java.io.IOException;
import java.util.Collection;

import ac.il.technion.twc.api.tweets.Tweet;

/**
 * Our main API for supporting tweets
 * 
 * @author Ziv Ronen
 * @date 22.05.2014
 * @mail akarks@gmail.com
 * 
 * @version 2.0
 * @since 2.0
 */
public interface TwitterServicesCenter {

	/**
	 * Thrown when an error occurred during system clear.
	 * 
	 * @author Ophir De Jager
	 * 
	 */
	public static class ClearFailedException extends RuntimeException {

		private static final long serialVersionUID = 2095741218824580067L;
		private final IOException reason;

		public ClearFailedException(final IOException e) {
			reason = e;
		}

		public IOException getReason() {
			return reason;
		}

	}

	/**
	 * Add the given tweets to the system
	 * 
	 * @param parsedTweets
	 *            new tweets
	 * @throws IOException
	 *             If storing system state as failed
	 */
	// TODO: should use a different exception
	void importData(Collection<Tweet> parsedTweets) throws IOException;

	/**
	 * Clear the data from all builders.
	 * 
	 * @throws ClearFailedException
	 */
	void clearSystem() throws ClearFailedException;

	/**
	 * load all the services
	 */
	void loadServices();

	/**
	 * 
	 * @param type
	 * @return The service from the given type
	 */
	<T> T getService(Class<T> type);

}
