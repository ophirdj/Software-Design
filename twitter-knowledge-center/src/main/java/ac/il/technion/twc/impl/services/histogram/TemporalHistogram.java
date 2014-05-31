package ac.il.technion.twc.impl.services.histogram;

import java.util.Arrays;
import java.util.Date;
import java.util.Map.Entry;
import java.util.Set;

import ac.il.technion.twc.FuntionalityTester;
import ac.il.technion.twc.api.ServiceSetup;
import ac.il.technion.twc.api.TwitterQuery;
import ac.il.technion.twc.impl.properties.daymapping.DayMapping;
import ac.il.technion.twc.impl.properties.daymapping.DayOfWeek;

/**
 * Service that answer
 * {@link FuntionalityTester#getTemporalHistogram(String, String)} query.
 * 
 * 
 * 
 * @author Ziv Ronen
 * @date 26.05.2014
 * @mail akarks@gmail.com
 * 
 * @version 2.0
 * @since 2.0
 */
public class TemporalHistogram implements TwitterQuery {

	private final long[] timesBaseHistograms;
	private final int[][] occuarenceBaseHistograms;

	private final long[] timesReHistograms;
	private final int[][] occuarenceReHistograms;

	private final HistogramFormat format;

	/**
	 * Create an empty histogram
	 * 
	 * @param format
	 *            the format for the return values
	 */
	public TemporalHistogram(final HistogramFormat format) {
		timesBaseHistograms = new long[] { 0 };
		occuarenceBaseHistograms = new int[1][7];

		timesReHistograms = new long[] { 0 };
		occuarenceReHistograms = new int[1][7];

		this.format = format;
	}

	/**
	 * @param dayMapProperty
	 *            the property from which the service is builded
	 * @param format
	 *            the format for the return values
	 */
	@ServiceSetup
	public TemporalHistogram(final DayMapping dayMapProperty,
			final HistogramFormat format) {
		this.format = format;

		final Set<Entry<Long, Integer>> allDaysBase = dayMapProperty
				.getAllDaysBase();
		timesBaseHistograms = new long[1 + allDaysBase.size()];
		occuarenceBaseHistograms = new int[1 + allDaysBase.size()][7];
		initArrays(allDaysBase, timesBaseHistograms, occuarenceBaseHistograms);

		final Set<Entry<Long, Integer>> allDaysRe = dayMapProperty
				.getAllDaysRe();
		timesReHistograms = new long[1 + allDaysRe.size()];
		occuarenceReHistograms = new int[1 + allDaysRe.size()][7];
		initArrays(allDaysRe, timesReHistograms, occuarenceReHistograms);
	}

	private void initArrays(final Iterable<Entry<Long, Integer>> data,
			final long[] timesHistogram, final int[][] occuarenceHistograms) {
		int i = 0;
		final int[] histogram = new int[7];
		occuarenceHistograms[0] = new int[7];
		timesHistogram[0] = 0;
		for (final Entry<Long, Integer> entry : data) {
			histogram[DayOfWeek.fromDate(new Date(entry.getKey())).ordinal()] += entry
					.getValue();
			i++;
			timesHistogram[i] = entry.getKey();
			occuarenceHistograms[i] = Arrays
					.copyOf(histogram, histogram.length);
		}
	}

	/**
	 * @param from
	 *            The beginning of the time
	 * @param to
	 *            The end of the time
	 * @return histogram of the tweets in the given time
	 */
	public String[] get(final Date from, final Date to) {
		return format.formatHistogram(
				getHistogramBetween(from, to, timesBaseHistograms,
						occuarenceBaseHistograms),
				getHistogramBetween(from, to, timesReHistograms,
						occuarenceReHistograms));
	}

	/**
	 * 
	 * @param from
	 * @param to
	 * @param times
	 * @param occurance
	 * @param histogramTime
	 * @param histograms
	 * @return the histogram between from and to (inclusive)
	 */
	private int[] getHistogramBetween(final Date from, final Date to,
			final long[] histogramTime, final int[][] histograms) {
		final int[] $ = new int[DayOfWeek.values().length];
		final int[] toHistogram = findHistogramAtTime(to.getTime(),
				histogramTime, histograms);
		// from.getTime() - 1 so we want remove the value in form
		final int[] fromHistogram = findHistogramAtTime(from.getTime() - 1,
				histogramTime, histograms);
		for (int i = 0; i < $.length; i++)
			$[i] = toHistogram[i] - fromHistogram[i];
		return $;
	}

	/**
	 * 
	 * @param time
	 * @return the histogram up to the given time (inclusive)
	 */
	private int[] findHistogramAtTime(final long time,
			final long[] histogramTime, final int[][] histograms) {
		final int indexPreCalculeted = binarySearch(histogramTime, time, true);
		final int[] incompleteHistogram = histograms[indexPreCalculeted];
		final int[] $ = Arrays.copyOf(incompleteHistogram,
				incompleteHistogram.length);
		return $;
	}

	/**
	 * 
	 * @param array
	 * @param search
	 * @return if before is true, the index of last value in array that is
	 *         smaller or equal to searched value. else the index of first value
	 *         in array that is bigger or equal to searched value
	 */
	private int binarySearch(final long[] array, final long search,
			final boolean before) {
		int first = 0;
		int last = array.length - 1;
		while (first <= last) {
			final int guess = (first + last) / 2;
			if (array[guess] == search)
				return guess;
			if (array[guess] > search)
				last = guess - 1;
			else
				first = guess + 1;
		}
		return before ? last : first;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (format == null ? 0 : format.hashCode());
		result = prime * result + Arrays.hashCode(occuarenceBaseHistograms);
		result = prime * result + Arrays.hashCode(occuarenceReHistograms);
		result = prime * result + Arrays.hashCode(timesBaseHistograms);
		result = prime * result + Arrays.hashCode(timesReHistograms);
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final TemporalHistogram other = (TemporalHistogram) obj;
		if (format == null) {
			if (other.format != null)
				return false;
		} else if (!format.equals(other.format))
			return false;
		if (!Arrays.deepEquals(occuarenceBaseHistograms,
				other.occuarenceBaseHistograms))
			return false;
		if (!Arrays.deepEquals(occuarenceReHistograms,
				other.occuarenceReHistograms))
			return false;
		if (!Arrays.equals(timesBaseHistograms, other.timesBaseHistograms))
			return false;
		if (!Arrays.equals(timesReHistograms, other.timesReHistograms))
			return false;
		return true;
	}

}
