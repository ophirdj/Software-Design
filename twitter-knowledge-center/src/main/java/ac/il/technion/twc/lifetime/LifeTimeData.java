package ac.il.technion.twc.lifetime;

import java.util.Map;
import java.util.Set;

import ac.il.technion.twc.message.ID;
import ac.il.technion.twc.message.tweet.BaseTweet;
import ac.il.technion.twc.message.tweet.Retweet;

/**
 * Returns the life time for each base tweet.
 * 
 * @author Ziv Ronen
 * @date 07.05.2014
 * @mail akarks@gmail.com
 * 
 * @version 2.0
 * @since 2.0
 * 
 */
public class LifeTimeData {

	/**
	 * Thrown when trying to calculate life time of a tweet that has no
	 * well-defined life time (either because it isn't a base tweet, it doesn't
	 * exist, or ot has no retweets).
	 * 
	 * @author Ophir De Jager
	 * 
	 */
	public static final class UndefinedTimeException extends Exception {
		private static final long serialVersionUID = -4180968758763049887L;
	}

	/**
	 * Collection of the base tweets in the system
	 */
	final Set<BaseTweet> baseTweets;

	/**
	 * Collection of the retweets in the system
	 */
	final Set<Retweet> retweets;

	/**
	 * A mapping between tweet id and the time until its final retweet
	 */
	final Map<ID, Long> map;

	// for Gson
	@SuppressWarnings("unused")
	private LifeTimeData() {
		map = null;
		baseTweets = null;
		retweets = null;
	}

	/**
	 * @param map
	 *            mapping between tweet id and the time until its final retweet
	 * @param baseTweets
	 *            Collection of the base tweets in the system
	 * @param retweets
	 *            Collection of the retweets in the system
	 */
	public LifeTimeData(final Map<ID, Long> map,
			final Set<BaseTweet> baseTweets, final Set<Retweet> retweets) {
		this.map = map;
		this.baseTweets = baseTweets;
		this.retweets = retweets;
	}

	/**
	 * 
	 * @param id
	 *            an id of a tweet
	 * @return The time between the tweet with the given id and its final
	 *         retweet
	 * @throws UndefinedTimeException
	 *             If life time of tweet is undefined.
	 */
	public Long get(final ID id) throws UndefinedTimeException {
		if (!map.containsKey(id))
			throw new UndefinedTimeException();
		return map.get(id);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ (baseTweets == null ? 0 : baseTweets.hashCode());
		result = prime * result + (map == null ? 0 : map.hashCode());
		result = prime * result + (retweets == null ? 0 : retweets.hashCode());
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final LifeTimeData other = (LifeTimeData) obj;
		if (baseTweets == null) {
			if (other.baseTweets != null)
				return false;
		} else if (!baseTweets.equals(other.baseTweets))
			return false;
		if (map == null) {
			if (other.map != null)
				return false;
		} else if (!map.equals(other.map))
			return false;
		if (retweets == null) {
			if (other.retweets != null)
				return false;
		} else if (!retweets.equals(other.retweets))
			return false;
		return true;
	}

}
