package ac.il.technion.twc.impl.services.lifetime;

import java.util.HashMap;
import java.util.Map;

import ac.il.technion.twc.FuntionalityTester;
import ac.il.technion.twc.api.tweets.BaseTweet;
import ac.il.technion.twc.api.tweets.ID;
import ac.il.technion.twc.api.tweets.Retweet;
import ac.il.technion.twc.impl.properties.rootfinder.TransitiveRootFinder;
import ac.il.technion.twc.impl.properties.rootfinder.TransitiveRootFinder.NoRootFoundException;
import ac.il.technion.twc.impl.properties.tweetsretriever.TweetsRetriever;

/**
 * Service that answer {@link FuntionalityTester#getLifetimeOfTweets(String)}
 * query.
 * 
 * @author Ziv Ronen
 * @date 26.05.2014
 * @mail akarks@gmail.com
 * 
 * @version 2.0
 * @since 2.0
 */
public class TweetToLifeTime {

  /**
   * Mapping between id and its life time
   */
  final Map<ID, Long> lifeTimeFromId;

  private TweetToLifeTime() {
    lifeTimeFromId = new HashMap<ID, Long>();
  }

  TweetToLifeTime(final Map<ID, Long> lifeTimeFromId) {
    this.lifeTimeFromId = lifeTimeFromId;
  }

  /**
   * @param baseTweetFinder
   * @param tweets
   */
  public TweetToLifeTime(final TransitiveRootFinder baseTweetFinder,
      final TweetsRetriever tweets) {
    lifeTimeFromId = new HashMap<ID, Long>();
    for (final Retweet retweet : tweets.getRetweets())
      try {
        final BaseTweet base = baseTweetFinder.findRoot(retweet);
        lifeTimeFromId.put(base.id(), Math.max(
            retweet.date().getTime() - base.date().getTime(),
            !lifeTimeFromId.containsKey(base.id()) ? 0L : lifeTimeFromId
                .get(base.id())));
      } catch (final NoRootFoundException e) {
        continue;
      }
  }

  /**
   * @param id
   *          The id of the tweets
   * @return The life time of the tweet
   * @throws UndefinedTimeException
   *           if the tweet is not registered or has no retweets
   */
  public String getLifeTimeById(final ID id) throws UndefinedTimeException {
    if (!lifeTimeFromId.containsKey(id))
      throw new UndefinedTimeException();
    return lifeTimeFromId.get(id).toString();
  }

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
   * @return An empty {@link TweetToLifeTime}
   */
  public static TweetToLifeTime empty() {
    return new TweetToLifeTime();
  }

}
