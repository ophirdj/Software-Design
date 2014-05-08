package ac.il.technion.twc.modules;

import java.nio.file.Path;
import java.util.HashSet;

import ac.il.technion.twc.lifetime.LifeTimeBuilder;
import ac.il.technion.twc.lifetime.LifeTimeCache;
import ac.il.technion.twc.lifetime.LifeTimeData;
import ac.il.technion.twc.lifetime.TransitiveRootFinder;
import ac.il.technion.twc.message.tweet.BaseTweet;
import ac.il.technion.twc.message.tweet.Retweet;
import ac.il.technion.twc.message.visitor.PropertyBuilder;
import ac.il.technion.twc.storage.FileHandler;
import ac.il.technion.twc.storage.StorageHandler;

import com.google.gson.Gson;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.TypeLiteral;
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
		bind(new TypeLiteral<PropertyBuilder<LifeTimeData, LifeTimeCache>>() {
		}).to(LifeTimeBuilder.class);
	}

	/**
	 * @param storageDir
	 *            Root storage directory for properties.
	 * @param gson
	 *            serializer for the property
	 * @return A handler for life time property storage
	 */
	@Provides
	StorageHandler<LifeTimeData> lifeTimeDataStorage(
			@Named("storage directory") final Path storageDir,
			@Named("serializer") final Gson gson) {
		return new StorageHandler<>(gson, storageDir.resolve("life_time_data"),
				new FileHandler());
	}

	/**
	 * @param storageDir
	 *            Root storage directory for properties.
	 * @param gson
	 *            serializer for the property
	 * @return A handler for life time property storage
	 */
	@Provides
	StorageHandler<LifeTimeCache> lifeTimeCacheStorage(
			@Named("storage directory") final Path storageDir,
			@Named("serializer") final Gson gson) {
		return new StorageHandler<>(gson,
				storageDir.resolve("life_time_cache"), new FileHandler());
	}

	/**
	 * @return an empty data for life time property (first usage)
	 */
	@Provides
	@Named("default")
	LifeTimeData defaultLifeTime() {
		return new LifeTimeData(new HashSet<BaseTweet>(),
				new HashSet<Retweet>());
	}

	/**
	 * @return an empty data for life time property (first usage)
	 */
	@Provides
	@Named("default")
	LifeTimeCache defaultLifeTimeMap(
			@Named("default") final LifeTimeData defaultLifeTime,
			final TransitiveRootFinder baseTweetFinder) {
		return new LifeTimeCache(defaultLifeTime, baseTweetFinder);
	}

}
