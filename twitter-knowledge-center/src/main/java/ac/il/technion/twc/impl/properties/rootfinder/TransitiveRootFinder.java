package ac.il.technion.twc.impl.properties.rootfinder;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ac.il.technion.twc.api.tweets.BaseTweet;
import ac.il.technion.twc.api.tweets.ID;
import ac.il.technion.twc.api.tweets.Retweet;
import ac.il.technion.twc.api.tweets.Tweet;

/**
 * handle finding for each tweet the corresponding retweet
 * 
 * @author Ziv Ronen
 * @date 07.05.2014
 * @mail akarks@gmail.com
 * 
 * @version 2.0
 * @since 2.0
 * 
 */
// TODO: when finish migrating, delete deprecated methods
public class TransitiveRootFinder {

  private final Map<ID, ID> relation;
  private final Map<ID, BaseTweet> baseTweets;

  /**
   * @param bases
   * @param res
   */
  public TransitiveRootFinder(final List<BaseTweet> bases,
      final List<Retweet> res) {
    relation = new HashMap<>();
    final Map<ID, BaseTweet> buildingBaseTweetMap =
        new HashMap<ID, BaseTweet>();
    for (final BaseTweet baseTweet : bases)
      buildingBaseTweetMap.put(baseTweet.id(), baseTweet);
    for (final Retweet retweet : res)
      relation.put(retweet.id(), retweet.originId);
    baseTweets = Collections.unmodifiableMap(buildingBaseTweetMap);
    ;
  }

  /**
   * @param tweet
   *          A tweet in the system
   * @return The base tweet this tweet is retweeting
   * @throws NoRootFoundException
   *           If the base tweet is not known
   */
  public BaseTweet findRoot(final Tweet tweet) throws NoRootFoundException {
    final ID rootId = findRootAux(tweet.id());
    if (!baseTweets.containsKey(rootId))
      throw new NoRootFoundException();
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
  public static class NoRootFoundException extends Exception {

    /**
     * 
     */
    private static final long serialVersionUID = 1957959540233599796L;

  }
}
