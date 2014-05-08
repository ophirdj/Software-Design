package ac.il.technion.twc.histogram;

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

	private final DayHistogram histogram;

	/**
	 * @param histogram
	 *            The histogram for keeping
	 */
	@Inject
	public DayHistogramCache(final DayHistogram histogram) {
		this.histogram = histogram;
	}

	/**
	 * @return An array of strings, each string in the format of
	 *         ("<number of tweets (including retweets),number of retweets only>"
	 *         ), for example: ["100,10","250,20",...,"587,0"]. The 0th index of
	 *         the array is Sunday.
	 */
	public String[] getDailyHistogram() {
		return histogram.getStringRepresentation();
	}

}
