package ac.il.technion.twc.histogram;

import java.util.Map;

public class DayHistogram {
	
	final Map<DayOfWeek, Integer> basetweets;
	final Map<DayOfWeek, Integer> retweets;

	public DayHistogram(Map<DayOfWeek, Integer> tweets, Map<DayOfWeek, Integer> retweets) {
		this.basetweets = tweets;
		this.retweets = retweets;
	}
	
	public int tweets(DayOfWeek day) {
		return basetweets.get(day) + retweets.get(day);
	}
	
	public int retweets(DayOfWeek day) {
		return retweets.get(day);
	}

}
