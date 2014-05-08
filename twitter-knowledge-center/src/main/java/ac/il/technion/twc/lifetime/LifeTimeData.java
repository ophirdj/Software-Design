package ac.il.technion.twc.lifetime;

import java.util.Set;

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
	 * Collection of the base tweets in the system
	 */
	final Set<BaseTweet> baseTweets;

	/**
	 * Collection of the retweets in the system
	 */
	final Set<Retweet> retweets;

	// for Gson
	@SuppressWarnings("unused")
	private LifeTimeData() {
		baseTweets = null;
		retweets = null;
	}

	/**
	 * @param baseTweets
	 *            Collection of the base tweets in the system
	 * @param retweets
	 *            Collection of the retweets in the system
	 */
	public LifeTimeData(final Set<BaseTweet> baseTweets,
			final Set<Retweet> retweets) {
		this.baseTweets = baseTweets;
		this.retweets = retweets;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ (baseTweets == null ? 0 : baseTweets.hashCode());
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
		if (retweets == null) {
			if (other.retweets != null)
				return false;
		} else if (!retweets.equals(other.retweets))
			return false;
		return true;
	}

}
