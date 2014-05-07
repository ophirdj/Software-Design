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

public class DayHistogramModule extends AbstractModule {

	@Override
	protected void configure() {
		// nothing to do here...
	}

	@Singleton
	@Provides
	StorageHandler<DayHistogram> dayHistogramStorage() {
		return new StorageHandler<>(new Gson(), null);
	}

	@Provides
	DayHistogramCache dayHistogramCache(
			final DayHistogramBuilder dayHistogramBuilder,
			@Named("default") final DayHistogram emptyDayHistogram) {
		return new DayHistogramCache(
				dayHistogramBuilder.loadResult(emptyDayHistogram));
	}

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
