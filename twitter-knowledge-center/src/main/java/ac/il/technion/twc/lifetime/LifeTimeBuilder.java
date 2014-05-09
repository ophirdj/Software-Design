package ac.il.technion.twc.lifetime;

import ac.il.technion.twc.message.tweet.BaseTweet;
import ac.il.technion.twc.message.tweet.Retweet;
import ac.il.technion.twc.message.visitor.PropertyBuilder;

import com.google.inject.Inject;

/**
 * Builder for {@link LifeTimeData}.
 * 
 * 
 * @author Ophir De Jager
 * 
 */
public class LifeTimeBuilder implements
    PropertyBuilder<LifeTimeData, LifeTimeCache> {

  /**
   * Ongoing data for life time building
   */
  LifeTimeData metaData;
  private final TransitiveRootFinder baseTweetFinder;

  /**
   * @param rootFinder
   *          Finds base tweet for each tweet.
   */
  @Inject
  public LifeTimeBuilder(final TransitiveRootFinder rootFinder) {
    baseTweetFinder = rootFinder;
  }

  @Override
  public void initializeFromState(final LifeTimeData state) {
    metaData = state;
    baseTweetFinder.addBaseTweets(metaData.baseTweets);
    baseTweetFinder.addRetweets(metaData.retweets);
  }

  @Override
  public Void visit(final BaseTweet t) {
    metaData.baseTweets.add(t);
    baseTweetFinder.addTweet(t);
    return null;
  }

  @Override
  public Void visit(final Retweet t) {
    metaData.retweets.add(t);
    baseTweetFinder.addTweet(t);
    return null;
  }

  @Override
  public LifeTimeData getState() {
    return metaData;
  }

  @Override
  public LifeTimeCache getResultCache() {
    return new LifeTimeCache(metaData, baseTweetFinder);
  }
}
