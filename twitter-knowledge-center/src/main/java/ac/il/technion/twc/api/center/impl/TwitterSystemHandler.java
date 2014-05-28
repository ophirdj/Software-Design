package ac.il.technion.twc.api.center.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import ac.il.technion.twc.api.center.TwitterServicesCenter;
import ac.il.technion.twc.api.properties.PropertyBuilder;
import ac.il.technion.twc.api.storage.PersistanceStorage;
import ac.il.technion.twc.api.tweets.BaseTweet;
import ac.il.technion.twc.api.tweets.Retweet;
import ac.il.technion.twc.api.tweets.Tweet;

/**
 * Used for keeping the tweets data and build the properties
 * 
 * @author Ziv Ronen
 * @date 22.05.2014
 * @mail akarks@gmail.com
 * 
 * @version 2.0
 * @since 2.0
 */
public class TwitterSystemHandler implements TwitterServicesCenter {

  private final List<PropertyBuilder<?>> builders;
  private final PersistanceStorage storage;

  private final ExecutorService threadPool;

  /**
   * @param builders
   * @param storage
   * @param threadPool
   */
  public TwitterSystemHandler(final List<PropertyBuilder<?>> builders,
      final PersistanceStorage storage, final ExecutorService threadPool) {
    this.builders = builders;
    this.storage = storage;
    this.threadPool = threadPool;
  }

  @Override
  public void importData(final Collection<Tweet> importedTweets)
      throws IOException {
    final Tweets storedTweets = storage.load(Tweets.class, new Tweets());
    final List<Tweet> tweets = new ArrayList<>();
    tweets.addAll(importedTweets);
    tweets.addAll(storedTweets.getBaseTweets());
    tweets.addAll(storedTweets.getRetweets());

    final List<Callable<Void>> buildingTasks = new ArrayList<>();
    for (final PropertyBuilder<?> builder : builders)
      buildingTasks.add(new Callable<Void>() {

        @Override
        public Void call() throws Exception {
          builder.clear();
          for (final Tweet tweet : tweets)
            tweet.accept(builder);
          return null;
        }
      });
    try {
      final List<Future<Void>> barriers = threadPool.invokeAll(buildingTasks);
      for (final Future<Void> task : barriers)
        task.get();
      storage.store(new Tweets(tweets));
    } catch (final InterruptedException e) {
      // TODO not sure what to do here
      e.printStackTrace();
    } catch (final ExecutionException e) {
      // TODO not sure what to do here
      e.printStackTrace();
    }
  }

  @Override
  public void clearSystem() throws IOException {
    for (final PropertyBuilder<?> builder : builders)
      builder.clear();
    storage.clear();
  }

  /**
   * Wrap class for list of tweets.
   * 
   * @author Ziv Ronen
   * @date 22.05.2014
   * @mail akarks@gmail.com
   * 
   * @version 2.0
   * @since 2.0
   */
  static class Tweets {
    private List<BaseTweet> baseTweets;
    private List<Retweet> retweets;

    /**
     * @param tweets
     *          get the tweets and split them to base tweets and retweets
     */
    public Tweets(final List<Tweet> tweets) {
      baseTweets = new ArrayList<>();
      retweets = new ArrayList<>();
      for (final Tweet tweet : tweets)
        if (tweet instanceof BaseTweet)
          baseTweets.add((BaseTweet) tweet);
        else
          retweets.add((Retweet) tweet);
    }

    /**
     * 
     */
    public Tweets() {
      baseTweets = new ArrayList<>();
      retweets = new ArrayList<>();
    }

    /**
     * @return the base tweets
     */
    public List<BaseTweet> getBaseTweets() {
      return baseTweets;
    }

    /**
     * set the base tweets
     * 
     * @param baseTweets
     *          new value
     */
    public void setBaseTweets(final List<BaseTweet> baseTweets) {
      this.baseTweets = baseTweets;
    }

    /**
     * @return the retweets
     */
    public List<Retweet> getRetweets() {
      return retweets;
    }

    /**
     * set the retweets
     * 
     * @param retweets
     *          new value
     */
    public void setRetweets(final List<Retweet> retweets) {
      this.retweets = retweets;
    }

    @Override
    public int hashCode() {
      final int prime = 31;
      int result = 1;
      result =
          prime * result + (baseTweets == null ? 0 : baseTweets.hashCode());
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
      final Tweets other = (Tweets) obj;
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

}
