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

	public static final class UndefinedTimeException extends Exception {
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

}
