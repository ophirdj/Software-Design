package ac.il.technion.twc.impl.api;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import ac.il.technion.twc.api.TwitterServicesCenter;
import ac.il.technion.twc.api.properties.PropertyBuilder;
import ac.il.technion.twc.api.storage.PersistanceStorage;
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
    final List<Tweet> tweets =
        storage.load(Tweets.class, new Tweets()).getTweets();
    tweets.addAll(importedTweets);
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
    storage.clearAll();
  }

}
