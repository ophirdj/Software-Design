package ac.il.technion.twc.api.core;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import ac.il.technion.twc.api.TwitterDataCenter;
import ac.il.technion.twc.api.TwitterQuery;
import ac.il.technion.twc.api.core.ServiceBuildingManager.UserMethodInvokationException;
import ac.il.technion.twc.api.tweet.BaseTweet;
import ac.il.technion.twc.api.tweet.Retweet;
import ac.il.technion.twc.api.tweet.Tweet;

/**
 * Used for keeping the tweets data and build the properties
 * 
 * @author Ziv Ronen
 * @date 22.05.2014
 * @mail akarks@gmail.com
 * 
 */
public class TwitterDataCenterimpl implements TwitterDataCenter {

  private final Map<Class<?>, Object> servicesResult = new HashMap<>();
  private final Storage storage;
  private final Set<Class<? extends TwitterQuery>> services;
  private final ServiceBuildingManager serviceBuilder;

  /**
   * @param services
   * @param serviceBuilder
   * @param storage
   */
  public TwitterDataCenterimpl(
      final Set<Class<? extends TwitterQuery>> services,
      final ServiceBuildingManager serviceBuilder, final Storage storage) {
    serviceBuilder.setProperties(Collections.<BaseTweet> emptyList(),
        Collections.<Retweet> emptyList());
    this.services = services;
    this.serviceBuilder = serviceBuilder;
    this.storage = storage;
  }

  @Override
  public void importData(final Collection<? extends Tweet> importedTweets)
      throws SystemOperationFailedException {
    final Tweets storedTweets = storage.load(Tweets.class, new Tweets());
    final List<Tweet> tweets = new ArrayList<>();
    tweets.addAll(importedTweets);
    tweets.addAll(storedTweets.getBaseTweets());
    tweets.addAll(storedTweets.getRetweets());
    final Tweets newTweets = new Tweets(tweets);

    servicesResult.clear();

    try {
      storage.store(Tweets.class, newTweets);
      buildQueries(newTweets);
    } catch (final IOException e) {
      throw new SystemOperationFailedException(e);
    }
  }

  private void buildQueries(final Tweets tweets) throws IOException {
    serviceBuilder.setProperties(tweets.getBaseTweets(), tweets.getRetweets());
    final Map<Class<?>, Throwable> failingCauses = new HashMap<>();
    for (final Class<? extends TwitterQuery> service : services)
      try {

        storage.store(service, serviceBuilder.getInstance(service));
      } catch (final UserMethodInvokationException e) {
        failingCauses.put(service, e.getCause());
      }
    // TODO test
    if (!failingCauses.isEmpty())
      throw new BuildFailedException("Storing the following queries failed:",
          failingCauses);
  }

  @Override
  public void evaluateQueries() {
    final Map<Class<?>, Throwable> failingCauses = new HashMap<>();
    for (final Class<?> service : services)
      try {
        servicesResult.put(service,
            storage.load(service, serviceBuilder.getInstance(service)));
      } catch (final UserMethodInvokationException e) {
        final Object loadedValue = storage.load(service, null);
        if (null == loadedValue)
          failingCauses.put(service, e.getCause());
        else
          servicesResult.put(service, loadedValue);
      }
    // TODO test
    if (!failingCauses.isEmpty())
      throw new BuildFailedException(
          "Failed to load and couldn't build default", failingCauses);
  }

  @Override
  public <T> T getQuery(final Class<T> type) throws IllegalArgumentException {

    if (!servicesResult.containsKey(type)) {
      // TODO test
      if (!services.contains(type))
        throw new IllegalArgumentException("Service: " + type
            + " wanted but not registered.");
      try {
        servicesResult.put(type,
            storage.load(type, serviceBuilder.getInstance(type)));
      } catch (final UserMethodInvokationException e) {

        final Object loadedValue = storage.load(type, null);
        if (null != loadedValue)
          servicesResult.put(type, loadedValue);
        else {
          final Map<Class<?>, Throwable> failingCauses = new HashMap<>();
          failingCauses.put(type, e.getCause());
          throw new BuildFailedException(
              "Failed to load and couldn't build default", failingCauses);
        }

      }
    }
    return type.cast(servicesResult.get(type));
  }

  @Override
  public void clear() throws SystemOperationFailedException {
    try {
      serviceBuilder.setProperties(Collections.<BaseTweet> emptyList(),
          Collections.<Retweet> emptyList());
      storage.clear();
    } catch (final IOException e) {
      throw new SystemOperationFailedException(e);
    }
  }

  /**
   * Thrown when some queries can't be built
   * 
   * @author Ziv Ronen
   * @date 01.06.2014
   * @mail akarks@gmail.com
   */
  public static class BuildFailedException extends RuntimeException {

    /**
     * 
     */
    private static final long serialVersionUID = 4677937592391059926L;
    private final Map<Class<?>, Throwable> failingCauses;
    private final String message;

    /**
     * @param message
     * @param failingCauses
     */
    public BuildFailedException(final String message,
        final Map<Class<?>, Throwable> failingCauses) {
      this.failingCauses = failingCauses;
      this.message = message;
    }

    @Override
    public String getMessage() {
      final StringBuilder builder = new StringBuilder(message).append("\n");
      for (final Entry<Class<?>, Throwable> entry : failingCauses.entrySet())
        builder.append("\t- class ").append(entry.getKey().getSimpleName())
            .append(" can't be build because building it cause ")
            .append(entry.getValue()).append("\n");
      return builder.toString();
    }

  }

  /**
   * Wrapper class for list of tweets.
   * 
   * @author Ziv Ronen
   * @date 22.05.2014
   * @mail akarks@gmail.com
   * 
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
