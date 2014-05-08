package ac.il.technion.twc.modules;

import java.nio.file.Path;
import java.util.EnumMap;

import ac.il.technion.twc.histogram.DayHistogram;
import ac.il.technion.twc.histogram.DayHistogramBuilder;
import ac.il.technion.twc.histogram.DayHistogramCache;
import ac.il.technion.twc.histogram.DayOfWeek;
import ac.il.technion.twc.message.visitor.PropertyBuilder;
import ac.il.technion.twc.storage.FileHandler;
import ac.il.technion.twc.storage.StorageHandler;

import com.google.gson.Gson;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.TypeLiteral;
import com.google.inject.name.Named;

/**
 * 
 * @author Ziv Ronen
 * @date 07.05.2014
 * @mail akarks@gmail.com
 * 
 * @version 2.0
 * @since 2.0
 * 
 *        Guice module for day histogram
 */
public class DayHistogramModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(
				new TypeLiteral<PropertyBuilder<DayHistogram, DayHistogramCache>>() {
				}).to(DayHistogramBuilder.class);
	}

	/**
	 * @param storageDir
	 *            Root storage directory for properties.
	 * @param gson
	 *            serializer for the property
	 * @return storage handler for day histogram
	 */
	@Singleton
	@Provides
	StorageHandler<DayHistogram> dayHistogramStorage(
			@Named("storage directory") final Path storageDir,
			@Named("serializer") final Gson gson) {
		return new StorageHandler<>(gson, storageDir.resolve("day_histogram"),
				new FileHandler());
	}

	/**
	 * @param storageDir
	 *            Root storage directory for properties.
	 * @param gson
	 *            serializer for the property
	 * @return storage handler for day histogram
	 */
	@Singleton
	@Provides
	StorageHandler<DayHistogramCache> dayHistogramCacheStorage(
			@Named("storage directory") final Path storageDir,
			@Named("serializer") final Gson gson) {
		return new StorageHandler<>(gson,
				storageDir.resolve("day_histogram_cache"), new FileHandler());
	}

	/**
	 * @return empty histogram (always 0 tweets)
	 */
	@Provides
	@Named("default")
	DayHistogram defaultDayHistogram() {
		final EnumMap<DayOfWeek, Integer> tweets = new EnumMap<>(
				DayOfWeek.class);
		final EnumMap<DayOfWeek, Integer> retweets = new EnumMap<>(
				DayOfWeek.class);
		for (final DayOfWeek day : DayOfWeek.values()) {
			tweets.put(day, 0);
			retweets.put(day, 0);
		}
		return new DayHistogram(tweets, retweets);
	}

	/**
	 * @return empty histogram (always 0 tweets)
	 */
	@Provides
	@Named("default")
	DayHistogramCache defaultCache(
			@Named("default") final DayHistogram defaultDayHistogram) {
		final EnumMap<DayOfWeek, Integer> tweets = new EnumMap<>(
				DayOfWeek.class);
		final EnumMap<DayOfWeek, Integer> retweets = new EnumMap<>(
				DayOfWeek.class);
		for (final DayOfWeek day : DayOfWeek.values()) {
			tweets.put(day, 0);
			retweets.put(day, 0);
		}
		return new DayHistogramCache(defaultDayHistogram);
	}

}
