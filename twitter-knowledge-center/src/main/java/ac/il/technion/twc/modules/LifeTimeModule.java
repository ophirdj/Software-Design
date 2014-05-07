package ac.il.technion.twc.modules;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.HashSet;

import ac.il.technion.twc.lifetime.LifeTimeBuilder;
import ac.il.technion.twc.lifetime.LifeTimeCache;
import ac.il.technion.twc.lifetime.LifeTimeData;
import ac.il.technion.twc.message.ID;
import ac.il.technion.twc.message.tweet.BaseTweet;
import ac.il.technion.twc.message.tweet.Retweet;
import ac.il.technion.twc.storage.StorageHandler;

import com.google.gson.Gson;
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

	@Override
	protected void configure() {
		// nothing to do here...
	}

	/**
	 * @param storageDir
	 *            Root storage directory for properties.
	 * @return A handler for life time property storage
	 */
	@Provides
	StorageHandler<LifeTimeData> lifeTimeStorage(
			@Named("storage directory") final Path storageDir) {
		return new StorageHandler<>(new Gson(), storageDir.resolve("life_time"));
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
		return new LifeTimeData(new HashMap<ID, Long>(),
				new HashSet<BaseTweet>(), new HashSet<Retweet>());
	}

}
