package ac.il.technion.twc.lifetime;

import java.util.HashMap;
import java.util.Map;

import ac.il.technion.twc.lifetime.TransitiveRootFinder.NoRootFoundException;
import ac.il.technion.twc.message.ID;
import ac.il.technion.twc.message.tweet.BaseTweet;
import ac.il.technion.twc.message.tweet.Retweet;

/**
 * 
 * 
 * @author Ophir De Jager
 * 
 */
public class LifeTimeCache {

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

	final Map<ID, Long> lifeTimeFromId;

	/**
	 * For Gson.
	 */
	LifeTimeCache(final Map<ID, Long> lifeTimeFromId) {
		this.lifeTimeFromId = lifeTimeFromId;
	}

	/**
	 * C'tor.
	 * 
	 * @param lifeTime
	 * @param baseTweetFinder
	 */
	public LifeTimeCache(final LifeTimeData lifeTime,
			final TransitiveRootFinder baseTweetFinder) {
		lifeTimeFromId = new HashMap<>();
		for (final Retweet t : lifeTime.retweets)
			try {
				final BaseTweet base = baseTweetFinder.findRoot(t);
				lifeTimeFromId.put(base.id(), Math.max(t.date().getTime()
						- base.date().getTime(),
						!lifeTimeFromId.containsKey(base.id()) ? 0L
								: lifeTimeFromId.get(base.id())));
			} catch (final NoRootFoundException e) {
				continue;
			}
	}

	/**
	 * @param id
	 *            Base tweet ID.
	 * @return Life time of tweet
	 * @throws UndefinedTimeException
	 *             If life time of tweet is undefined.
	 */
	public String getLifetimeOfTweets(final ID id)
			throws UndefinedTimeException {
		if (!lifeTimeFromId.containsKey(id))
			throw new UndefinedTimeException();
		return lifeTimeFromId.get(id).toString();
	}

}
