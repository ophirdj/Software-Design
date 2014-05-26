package ac.il.technion.twc.lifetime;

import java.util.HashMap;
import java.util.Map;

import ac.il.technion.twc.api.tweets.BaseTweet;
import ac.il.technion.twc.api.tweets.ID;
import ac.il.technion.twc.api.tweets.Retweet;
import ac.il.technion.twc.impl.properties.TransitiveRootFinder;
import ac.il.technion.twc.impl.properties.TransitiveRootFinder.NoRootFoundException;
import ac.il.technion.twc.impl.services.lifetime.TweetToLifeTime.UndefinedTimeException;

/**
 * 
 * 
 * @author Ophir De Jager
 * 
 */
public class LifeTimeCache {

  /**
   * Mapping between id and its life time
   */
  final Map<ID, Long> lifeTimeFromId;

  /**
   * C'tor. receive and store the require life time map
   * 
   * @param lifeTimeFromId
   */
  LifeTimeCache(final Map<ID, Long> lifeTimeFromId) {
    this.lifeTimeFromId = lifeTimeFromId;
  }

  /**
   * C'tor. build and store the require life time map
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
        lifeTimeFromId.put(base.id(), Math.max(
            t.date().getTime() - base.date().getTime(),
            !lifeTimeFromId.containsKey(base.id()) ? 0L : lifeTimeFromId
                .get(base.id())));
      } catch (final NoRootFoundException e) {
        continue;
      }
  }

  /**
   * @param id
   *          Base tweet ID.
   * @return Life time of tweet
   * @throws UndefinedTimeException
   *           If life time of tweet is undefined.
   */
  public String getLifetimeOfTweets(final ID id) throws UndefinedTimeException {
    if (!lifeTimeFromId.containsKey(id))
      throw new UndefinedTimeException();
    return lifeTimeFromId.get(id).toString();
  }

}
