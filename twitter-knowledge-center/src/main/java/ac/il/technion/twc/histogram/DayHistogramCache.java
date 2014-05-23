package ac.il.technion.twc.histogram;

import ac.il.technion.twc.impl.properties.DayOfWeek;

import com.google.inject.Inject;

/**
 * @author Ziv Ronen
 * @date 07.05.2014
 * @mail akarks@gmail.com
 * 
 * @version 2.0
 * @since 2.0
 * 
 *        loaded day histogram property data
 */
public class DayHistogramCache {

	private final String[] stringRepresentation;

	/**
	 * For Gson.
	 */
	public DayHistogramCache() {
		stringRepresentation = null;
	}

	/**
	 * @param histogram
	 *            The histogram for keeping
	 */
	@Inject
	public DayHistogramCache(final DayHistogram histogram) {
		stringRepresentation = new String[DayOfWeek.values().length];
		for (final DayOfWeek day : DayOfWeek.values())
			stringRepresentation[day.ordinal()] = new StringBuilder()
					.append(histogram.tweets(day)).append(',')
					.append(histogram.retweets(day)).toString();
	}

	/**
	 * @return An array of strings, each string in the format of
	 *         ("<number of tweets (including retweets),number of retweets only>"
	 *         ), for example: ["100,10","250,20",...,"587,0"]. The 0th index of
	 *         the array is Sunday.
	 */
	public String[] getDailyHistogram() {
		return stringRepresentation;
	}

}
