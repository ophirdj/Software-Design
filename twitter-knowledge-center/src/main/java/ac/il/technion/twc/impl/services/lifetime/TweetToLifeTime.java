package ac.il.technion.twc.impl.services.lifetime;

import java.util.HashMap;
import java.util.Map;

import ac.il.technion.twc.FuntionalityTester;
import ac.il.technion.twc.api.center.TwitterServicesCenterBuilder.ServiceSetup;
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
  Map<ID, Long> lifeTimeFromId;

  /**
   * Build empty service
   */
  public TweetToLifeTime() {
    lifeTimeFromId = new HashMap<ID, Long>();
  }

  /**
   * @param lifeTimeFromId
   */
  TweetToLifeTime(final Map<ID, Long> lifeTimeFromId) {
    this.lifeTimeFromId = lifeTimeFromId;
  }

  /**
   * @param baseTweetFinder
   *          required property (finder for base tweet)
   * @param tweets
   *          required property (tweets in system)
   */
  @ServiceSetup
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

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result =
        prime * result
            + (lifeTimeFromId == null ? 0 : lifeTimeFromId.hashCode());
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
    final TweetToLifeTime other = (TweetToLifeTime) obj;
    if (lifeTimeFromId == null) {
      if (other.lifeTimeFromId != null)
        return false;
    } else if (!lifeTimeFromId.equals(other.lifeTimeFromId))
      return false;
    return true;
  }

}
