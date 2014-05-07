package ac.il.technion.twc.lifetime;

import ac.il.technion.twc.lifetime.LifeTimeData.UndefinedTimeException;
import ac.il.technion.twc.message.ID;

/**
 * 
 * 
 * @author Ophir De Jager
 * 
 */
public class LifeTimeCache {

	private final LifeTimeData lifeTime;

	/**
	 * C'tor.
	 * 
	 * @param lifeTime
	 */
	public LifeTimeCache(final LifeTimeData lifeTime) {
		this.lifeTime = lifeTime;
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
		return lifeTime.get(id).toString();
	}

}
