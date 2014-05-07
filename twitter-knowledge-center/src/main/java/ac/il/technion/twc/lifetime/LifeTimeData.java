package ac.il.technion.twc.lifetime;

import java.util.Map;
import java.util.Set;

import ac.il.technion.twc.message.ID;
import ac.il.technion.twc.message.tweet.BaseTweet;
import ac.il.technion.twc.message.tweet.Retweet;

public class LifeTimeData {

	final Set<BaseTweet> baseTweets;

	final Set<Retweet> retweets;

	final Map<ID, Long> map;

	public LifeTimeData(final Map<ID, Long> map,
			final Set<BaseTweet> baseTweets, final Set<Retweet> retweets) {
		this.map = map;
		this.baseTweets = baseTweets;
		this.retweets = retweets;
	}

	public Long get(final ID id) {
		return map.get(id);
	}

}
