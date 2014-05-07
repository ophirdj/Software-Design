package ac.il.technion.twc.histogram;

import com.google.inject.Inject;

public class DayHistogramCache {
	
	private final DayHistogram histogram;

	@Inject
	public DayHistogramCache(DayHistogram histogram) {
		this.histogram = histogram;
	}
	
	public String[] getDailyHistogram() {
		String[] $ = new String[DayOfWeek.values().length];
		for(DayOfWeek day: DayOfWeek.values())
			$[day.ordinal()] = "" + histogram.tweets(day) + "," + histogram.retweets(day);
		return $;
	}

}
