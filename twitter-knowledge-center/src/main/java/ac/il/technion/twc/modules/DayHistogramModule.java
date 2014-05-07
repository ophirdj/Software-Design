package ac.il.technion.twc.modules;

import java.util.EnumMap;

import ac.il.technion.twc.histogram.DayHistogram;
import ac.il.technion.twc.histogram.DayHistogramBuilder;
import ac.il.technion.twc.histogram.DayHistogramCache;
import ac.il.technion.twc.histogram.DayOfWeek;
import ac.il.technion.twc.storage.StorageHandler;

import com.google.gson.Gson;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
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
		// nothing to do here...
	}

	/**
	 * @return storage handler for day histogram
	 */
	@Singleton
	@Provides
	StorageHandler<DayHistogram> dayHistogramStorage() {
		return new StorageHandler<>(new Gson(), null);
	}

	/**
	 * @param dayHistogramBuilder
	 *            create, store and load histograms
	 * @param emptyDayHistogram
	 *            default histogram (first time usage)
	 * @return Handler for histogram data requests
	 */
	@Provides
	DayHistogramCache dayHistogramCache(
			final DayHistogramBuilder dayHistogramBuilder,
			@Named("default") final DayHistogram emptyDayHistogram) {
		return new DayHistogramCache(
				dayHistogramBuilder.loadResult(emptyDayHistogram));
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

}
