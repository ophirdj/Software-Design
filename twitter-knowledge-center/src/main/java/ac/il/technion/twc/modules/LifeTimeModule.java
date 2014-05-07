package ac.il.technion.twc.modules;

import java.util.HashMap;
import java.util.HashSet;

import ac.il.technion.twc.lifetime.LifeTimeBuilder;
import ac.il.technion.twc.lifetime.LifeTimeCache;
import ac.il.technion.twc.lifetime.LifeTimeData;
import ac.il.technion.twc.message.ID;
import ac.il.technion.twc.message.tweet.BaseTweet;
import ac.il.technion.twc.message.tweet.Retweet;
import ac.il.technion.twc.storage.StorageHandler;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.name.Named;

/**
 * @author Ziv Ronen
 * @date 07.05.2014
 * @mail akarks@gmail.com
 * 
 * @version 2.0
 * @since 2.0
 * 
 *        Guice module for life time property
 */
public class LifeTimeModule extends AbstractModule {

  private final StorageHandler<LifeTimeData> lifeTimeStorageHandler =
      new StorageHandler<>();

  @Override
  protected void configure() {
    // nothing to do here...
  }

  /**
   * @return A handler for life time property storage
   */
  @Provides
  StorageHandler<LifeTimeData> lifeTimeStorage() {
    return lifeTimeStorageHandler;
  }

  /**
   * @param lifeTimeBuilder
   * @param defaultLifeTimeMap
   * @return displayer for life time property
   */
  @Provides
  LifeTimeCache lifeTimeCache(final LifeTimeBuilder lifeTimeBuilder,
      @Named("default") final LifeTimeData defaultLifeTimeMap) {
    return new LifeTimeCache(lifeTimeBuilder.loadResult(defaultLifeTimeMap));
  }

  /**
   * @return an empty data for life time property (first usage)
   */
  @Provides
  @Named("default")
  LifeTimeData defaultLifeTimeMap() {
    return new LifeTimeData(new HashMap<ID, Long>(), new HashSet<BaseTweet>(),
        new HashSet<Retweet>());
  }

}
