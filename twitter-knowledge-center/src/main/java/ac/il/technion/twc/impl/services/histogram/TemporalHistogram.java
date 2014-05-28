package ac.il.technion.twc.impl.services.histogram;

import java.util.Arrays;
import java.util.Date;
import java.util.Map.Entry;
import java.util.Set;

import ac.il.technion.twc.FuntionalityTester;
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
public class TemporalHistogram {

  private final long[] timesBase;
  private final int[] occuarenceBase;

  private final long[] timesBaseHistograms;
  private final int[][] occuarenceBaseHistograms;

  private final long[] timesRe;
  private final int[] occuarenceRe;

  private final long[] timesReHistograms;
  private final int[][] occuarenceReHistograms;

  private final HistogramFormat format;

  private static final int histogramFrequency = 5;

  /**
   * @param format
   */
  private TemporalHistogram(final HistogramFormat format) {
    timesBase = new long[0];
    occuarenceBase = new int[0];
    timesBaseHistograms = new long[] { 0 };
    occuarenceBaseHistograms = new int[1][7];

    timesRe = new long[0];
    occuarenceRe = new int[0];
    timesReHistograms = new long[] { 0 };
    occuarenceReHistograms = new int[1][7];

    this.format = format;
  }

  /**
   * @param dayMapProperty
   * @param format
   */
  public TemporalHistogram(final DayMapping dayMapProperty,
      final HistogramFormat format) {
    this.format = format;

    final Set<Entry<Long, Integer>> allDaysBase =
        dayMapProperty.getAllDaysBase();
    timesBase = new long[allDaysBase.size()];
    occuarenceBase = new int[allDaysBase.size()];
    timesBaseHistograms = new long[1 + allDaysBase.size() / histogramFrequency];
    occuarenceBaseHistograms =
        new int[1 + allDaysBase.size() / histogramFrequency][7];
    initArrays(allDaysBase, timesBase, occuarenceBase, timesBaseHistograms,
        occuarenceBaseHistograms);

    final Set<Entry<Long, Integer>> allDaysRe = dayMapProperty.getAllDaysRe();
    timesRe = new long[allDaysRe.size()];
    occuarenceRe = new int[allDaysRe.size()];
    timesReHistograms = new long[1 + allDaysRe.size() / histogramFrequency];
    occuarenceReHistograms =
        new int[1 + allDaysRe.size() / histogramFrequency][7];
    initArrays(allDaysRe, timesRe, occuarenceRe, timesReHistograms,
        occuarenceReHistograms);
  }

  private void initArrays(final Iterable<Entry<Long, Integer>> data,
      final long[] times, final int[] amount, final long[] timesHistogram,
      final int[][] occuarenceHistograms) {
    int i = 0;
    final int[] histogram = new int[7];
    occuarenceHistograms[0] = new int[7];
    timesHistogram[0] = 0;
    for (final Entry<Long, Integer> entry : data) {
      times[i] = entry.getKey();
      amount[i] = entry.getValue();
      i++;
      if (i % histogramFrequency == 0) {
        timesHistogram[i / histogramFrequency] = entry.getKey();
        occuarenceHistograms[i / histogramFrequency] =
            Arrays.copyOf(histogram, histogram.length);
      }
      histogram[DayOfWeek.fromDate(new Date(entry.getKey())).ordinal()] +=
          entry.getValue();
    }
  }

  /**
   * @param from
   *          The beginning of the time
   * @param to
   *          The end of the time
   * @return histogram of the tweets in the given time
   */
  public String[] get(final Date from, final Date to) {
    return format.formatHistogram(
        getHistogramBetween(from, to, timesBase, occuarenceBase,
            timesBaseHistograms, occuarenceBaseHistograms),
        getHistogramBetween(from, to, timesRe, occuarenceRe, timesReHistograms,
            occuarenceReHistograms));
  }

  private int[] getHistogramBetween(final Date from, final Date to,
      final long[] times, final int[] occurance, final long[] histogramTime,
      final int[][] histograms) {
    if (0 == times.length)
      return new int[7];
    return subHistograms(
        findHistogramAtTime(to, times, occurance, histogramTime, histograms),
        findHistogramAtTime(new Date(from.getTime() - 1), times, occurance,
            histogramTime, histograms));
  }

  private int[] findHistogramAtTime(final Date time, final long[] times,
      final int[] occurance, final long[] histogramTime,
      final int[][] histograms) {
    final int index = binarySearch(histogramTime, time.getTime(), true);
    final int[] histogram =
        addHistograms(
            histograms[index],
            histogramComplete(new Date(histogramTime[index]), time, times,
                occurance));
    return histogram;
  }

  private int[] histogramComplete(final Date from, final Date to,
      final long[] times, final int[] amounts) {
    final int[] $ = new int[DayOfWeek.values().length];
    final int index = binarySearch(times, from.getTime(), false);
    for (int i = index; i < times.length; i++) {
      if (times[i] > to.getTime())
        break;
      $[DayOfWeek.fromDate(new Date(times[i])).ordinal()] += amounts[i];
    }
    return $;
  }

  private int[] addHistograms(final int[] first, final int[] second) {
    final int[] $ = new int[DayOfWeek.values().length];
    for (int i = 0; i < $.length; i++)
      $[i] = first[i] + second[i];
    return $;
  }

  private int[] subHistograms(final int[] first, final int[] second) {
    final int[] $ = new int[DayOfWeek.values().length];
    for (int i = 0; i < $.length; i++)
      $[i] = first[i] - second[i];
    return $;
  }

  /**
   * 
   * @param array
   * @param search
   * @return if before is true, the index of last value in array that is smaller
   *         or equal to searched value. else the index of first value in array
   *         that is bigger or equal to searched value
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

  /**
   * @param histogramFormat
   * @return An empty {@link TemporalHistogram}
   */
  public static TemporalHistogram empty(final HistogramFormat histogramFormat) {
    return new TemporalHistogram(histogramFormat);
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + (format == null ? 0 : format.hashCode());
    result = prime * result + Arrays.hashCode(occuarenceBase);
    result = prime * result + Arrays.hashCode(occuarenceRe);
    result = prime * result + Arrays.hashCode(timesBase);
    result = prime * result + Arrays.hashCode(timesRe);
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
      if (other.format != null) {
        System.out.println("1");
        return false;
      }
    } else if (!format.equals(other.format)) {
      System.out.println("2");
      return false;
    }
    if (!Arrays.equals(occuarenceBase, other.occuarenceBase)) {
      System.out.println("3");
      return false;
    }
    if (!Arrays.deepEquals(occuarenceBaseHistograms,
        other.occuarenceBaseHistograms)) {
      System.out.println("4");
      return false;
    }
    if (!Arrays.equals(occuarenceRe, other.occuarenceRe))
      return false;
    if (!Arrays
        .deepEquals(occuarenceReHistograms, other.occuarenceReHistograms))
      return false;
    if (!Arrays.equals(timesBase, other.timesBase))
      return false;
    if (!Arrays.equals(timesBaseHistograms, other.timesBaseHistograms))
      return false;
    if (!Arrays.equals(timesRe, other.timesRe))
      return false;
    if (!Arrays.equals(timesReHistograms, other.timesReHistograms))
      return false;
    return true;
  }

}
