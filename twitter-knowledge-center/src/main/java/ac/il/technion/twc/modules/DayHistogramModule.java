package ac.il.technion.twc.modules;

import java.util.EnumMap;

import ac.il.technion.twc.histogram.DayHistogram;
import ac.il.technion.twc.histogram.DayHistogramBuilder;
import ac.il.technion.twc.histogram.DayHistogramCache;
import ac.il.technion.twc.histogram.DayOfWeek;
import ac.il.technion.twc.storage.StorageHandler;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.name.Named;

public class DayHistogramModule extends AbstractModule {
	
	private final StorageHandler<DayHistogram> dayHistogramStorageHandler = new StorageHandler<>();

	@Override
	protected void configure() {
		//nothing to do here...
	}
	
	@Provides
	StorageHandler<DayHistogram> dayHistogramStorage() {
		return dayHistogramStorageHandler;
	}

	@Provides
	DayHistogramCache dayHistogramCache(
			DayHistogramBuilder dayHistogramBuilder,
			@Named("default") DayHistogram emptyDayHistogram) {
		return new DayHistogramCache(
				dayHistogramBuilder.loadResult(emptyDayHistogram));
	}

	@Provides
	@Named("default")
	DayHistogram defaultDayHistogram() {
		EnumMap<DayOfWeek, Integer> tweets = new EnumMap<>(DayOfWeek.class);
		EnumMap<DayOfWeek, Integer> retweets = new EnumMap<>(DayOfWeek.class);
		for (DayOfWeek day : DayOfWeek.values()) {
			tweets.put(day, 0);
			retweets.put(day, 0);
		}
		return new DayHistogram(tweets, retweets);
	}

}
