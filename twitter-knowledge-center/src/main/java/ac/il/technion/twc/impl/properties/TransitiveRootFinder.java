package ac.il.technion.twc.impl.properties;

import java.util.Collection;
import java.util.HashMap;
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
   * 
   */
  @Deprecated
  public TransitiveRootFinder() {
    relation = new HashMap<>();
    baseTweets = new HashMap<>();
  }

  /**
   * @param relation
   * @param baseTweets
   */
  public TransitiveRootFinder(final Map<ID, ID> relation,
      final Map<ID, BaseTweet> baseTweets) {
    this.relation = relation;
    this.baseTweets = baseTweets;
  }

  /**
   * @param retweet
   *          Add the retweet to the transitivity relation
   */
  @Deprecated
  public void addTweet(final Retweet retweet) {
    relation.put(retweet.id(), retweet.originId);
  }

  /**
   * @param tweet
   *          Add the base tweet to the transitivity relation
   */
  @Deprecated
  public void addTweet(final BaseTweet tweet) {
    baseTweets.put(tweet.id(), tweet);
  }

  /**
   * @param tweets
   *          Add all the given base tweets to the transitivity relation
   */
  @Deprecated
  public void addBaseTweets(final Collection<BaseTweet> tweets) {
    for (final BaseTweet baseTweet : tweets)
      addTweet(baseTweet);
  }

  /**
   * @param retweets
   *          Add all the given base retweets to the transitivity relation
   */
  @Deprecated
  public void addRetweets(final Collection<Retweet> retweets) {
    for (final Retweet retweet : retweets)
      addTweet(retweet);
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
