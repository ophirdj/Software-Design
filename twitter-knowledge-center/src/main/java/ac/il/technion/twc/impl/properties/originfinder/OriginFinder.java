package ac.il.technion.twc.impl.properties.originfinder;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ac.il.technion.twc.api.tweet.BaseTweet;
import ac.il.technion.twc.api.tweet.ID;
import ac.il.technion.twc.api.tweet.Retweet;
import ac.il.technion.twc.api.tweet.Tweet;

/**
 * <br>
 * Finds the ancestor tweet (that isn't a retweet) of a tweet.</br>For example,
 * say we have 3 tweets: <code>A</code>, <code>B</code>, and <code>C</code>. <br>
 * <code>A</code> is a simple tweet, <code>B</code> is a retweet of
 * <code>A</code>, and <code>C</code> is a retweet of <code>B</code>.</br>Then:
 * <code>origin(A)</code> = <code>origin(B)</code> = <code>origin(C)</code> =
 * <code>A</code>.
 * 
 * @author Ziv Ronen
 * @date 07.05.2014
 * @mail akarks@gmail.com
 * 
 * @version 2.0
 * @since 2.0
 * 
 */
public class OriginFinder {

	private final Map<ID, ID> relation;
	private final Map<ID, BaseTweet> baseTweets;

	/**
	 * @param baseTweets
	 * @param retweets
	 */
	public OriginFinder(final List<BaseTweet> baseTweets,
			final List<Retweet> retweets) {
		relation = new HashMap<>();
		final Map<ID, BaseTweet> buildingBaseTweetMap = new HashMap<ID, BaseTweet>();
		for (final BaseTweet baseTweet : baseTweets)
			buildingBaseTweetMap.put(baseTweet.id(), baseTweet);
		for (final Retweet retweet : retweets)
			relation.put(retweet.id(), retweet.originId);
		this.baseTweets = Collections.unmodifiableMap(buildingBaseTweetMap);
		;
	}

	/**
	 * @param tweet
	 *            A tweet
	 * @return The ancestor tweet (that isn't a retweet) of given tweet.
	 * @throws NotFoundException
	 *             If no ancestor tweet that isn't a retweet exists.
	 */
	public BaseTweet origin(final Tweet tweet) throws NotFoundException {
		final ID rootId = findRootAux(tweet.id());
		if (!baseTweets.containsKey(rootId))
			throw new NotFoundException();
		return baseTweets.get(rootId);
	}

	private ID findRootAux(final ID currentId) {
		if (!relation.containsKey(currentId))
			return currentId;
		final ID rootId = findRootAux(relation.get(currentId));
		relation.put(currentId, rootId);
		return rootId;
	}

	/**
	 * @author Ziv Ronen
	 * @date 07.05.2014
	 * @mail akarks@gmail.com
	 * 
	 * @version 2.0
	 * @since 2.0
	 * 
	 *        An exception represent missing root in findRoot calling
	 */
	public static class NotFoundException extends Exception {

		/**
		 * 
		 */
		public static final long serialVersionUID = 1957959540233599796L;

	}
}
