package ac.il.technion.twc.impl.services.histogram;

import ac.il.technion.twc.impl.properties.daymapping.DayOfWeek;

/**
 * Build the histogram from a collection of tweets
 * 
 * @author Ziv Ronen
 * @date 26.05.2014
 * @mail akarks@gmail.com
 * 
 * @version 2.0
 * @since 2.0
 */
public class HistogramFormat {

	/**
	 * @param histogramBase
	 *            histogram of base tweets
	 * @param histogramRe
	 *            histogram of retweets
	 * @return the histogram as should be returned to the client
	 */
	public String[] formatHistogram(final int[] histogramBase,
			final int[] histogramRe) {

		final String[] $ = new String[DayOfWeek.values().length];
		for (final DayOfWeek day : DayOfWeek.values())
			$[day.ordinal()] = new StringBuilder()
					.append(histogramBase[day.ordinal()]
							+ histogramRe[day.ordinal()]).append(',')
					.append(histogramRe[day.ordinal()]).toString();
		return $;
	}

	@Override
	public int hashCode() {
		return super.hashCode();
	}

	@Override
	public boolean equals(final Object obj) {
		return obj != null && obj.getClass() == this.getClass();
	}

}
