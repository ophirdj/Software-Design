package ac.il.technion.twc.impl.services.tagpopularity;

import java.util.HashMap;
import java.util.Map;

import ac.il.technion.twc.FuntionalityTester;
import ac.il.technion.twc.api.ServiceSetup;
import ac.il.technion.twc.api.tweet.BaseTweet;
import ac.il.technion.twc.api.tweet.Retweet;
import ac.il.technion.twc.impl.properties.hashtags.IdHashtags;
import ac.il.technion.twc.impl.properties.originfinder.OriginFinder;
import ac.il.technion.twc.impl.properties.originfinder.OriginFinder.NotFoundException;
import ac.il.technion.twc.impl.properties.tweetsretriever.TweetsRetriever;

/**
 * Service that answer {@link FuntionalityTester#getHashtagPopularity(String)}
 * query
 * 
 * @author Ziv Ronen
 * @date 26.05.2014
 * @mail akarks@gmail.com
 * 
 * @version 2.0
 * @since 2.0
 */
public class TagToPopularity {

	private final Map<String, Integer> popularityFromHashtag = new HashMap<String, Integer>();

	/**
	 * Empty TagToPopularity
	 */
	public TagToPopularity() {

	}

	/**
	 * @param baseTweetFinder
	 * @param tweets
	 * @param hashtags
	 */
	@ServiceSetup
	public TagToPopularity(final OriginFinder baseTweetFinder,
			final TweetsRetriever tweets, final IdHashtags hashtags) {
		for (final Retweet retweet : tweets.getRetweets())
			try {
				final BaseTweet base = baseTweetFinder.origin(retweet);
				for (final String tag : base.hashtags())
					popularityFromHashtag.put(tag,
							getPopularityByHashtag(tag) + 1);
			} catch (final NotFoundException e) {
				continue;
			}
	}

	/**
	 * @param hashtag
	 * @return The amount of retweets to base tweets with given hashtag.
	 */
	public int getPopularityByHashtag(final String hashtag) {
		if (!popularityFromHashtag.containsKey(hashtag))
			return 0;
		return popularityFromHashtag.get(hashtag).intValue();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ (popularityFromHashtag == null ? 0 : popularityFromHashtag
						.hashCode());
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
		final TagToPopularity other = (TagToPopularity) obj;
		if (popularityFromHashtag == null) {
			if (other.popularityFromHashtag != null)
				return false;
		} else if (!popularityFromHashtag.equals(other.popularityFromHashtag))
			return false;
		return true;
	}

}
