package ac.il.technion.twc.impl.system;

import java.util.Collection;
import java.util.List;

import ac.il.technion.twc.api.PersistanceStorage;
import ac.il.technion.twc.api.TwitterServicesCenter;
import ac.il.technion.twc.api.properties.PropertyBuilder;
import ac.il.technion.twc.message.tweet.Tweet;

public class TwitterSystemHandler implements TwitterServicesCenter {

  private final List<PropertyBuilder<?>> builders;
  private final PersistanceStorage storage;

  public TwitterSystemHandler(final List<PropertyBuilder<?>> builders,
      final PersistanceStorage storage) {
    this.builders = builders;
    this.storage = storage;
  }

  @Override
  public void importData(final Collection<Tweet> importedTweets) {
    final List<Tweet> tweets =
        storage.load(Tweets.class, new Tweets()).getTweets();
    tweets.addAll(importedTweets);
    // TODO: parallelize.
    for (final PropertyBuilder<?> builder : builders) {
      builder.clear();
      for (final Tweet tweet : tweets)
        tweet.accept(builder);
    }
    storage.store(new Tweets(tweets));
    // TODO: join.
  }

  @Override
  public void clearSystem() {
    for (final PropertyBuilder<?> builder : builders)
      builder.clear();
    storage.clearAll();
  }

}
