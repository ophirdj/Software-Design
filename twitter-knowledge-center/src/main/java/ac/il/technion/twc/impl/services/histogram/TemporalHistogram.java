package ac.il.technion.twc.impl.services.histogram;

import java.util.Date;
import java.util.Map.Entry;

import ac.il.technion.twc.FuntionalityTester;
import ac.il.technion.twc.impl.properties.daymapping.DayMapping;
import ac.il.technion.twc.impl.properties.daymapping.DayOfWeek;

/**
 * Service that answer
 * {@link FuntionalityTester#getTemporalHistogram(String, String)} query.
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

  private final long[] timesRe;
  private final int[] occuarenceRe;

  private final HistogramFormat format;

  /**
   * @param format
   */
  private TemporalHistogram(final HistogramFormat format) {
    timesBase = new long[0];
    occuarenceBase = new int[0];
    timesRe = new long[0];
    occuarenceRe = new int[0];
    this.format = format;
  }

  /**
   * @param dayMapProperty
   * @param format
   */
  public TemporalHistogram(final DayMapping dayMapProperty,
      final HistogramFormat format) {
    this.format = format;

    timesBase = new long[dayMapProperty.getAllDaysBase().size()];
    occuarenceBase = new int[dayMapProperty.getAllDaysBase().size()];
    initArrays(dayMapProperty.getAllDaysBase(), timesBase, occuarenceBase);

    timesRe = new long[dayMapProperty.getAllDaysRe().size()];
    occuarenceRe = new int[dayMapProperty.getAllDaysRe().size()];
    initArrays(dayMapProperty.getAllDaysRe(), timesRe, occuarenceRe);
  }

  private void initArrays(final Iterable<Entry<Long, Integer>> data,
      final long[] times, final int[] amount) {
    int i = 0;
    for (final Entry<Long, Integer> entry : data) {
      times[i] = entry.getKey();
      amount[i] = entry.getValue();
      i++;
    }
  }

  // XXX: this is a simple solution, we will discuss faster one later on
  /**
   * @param from
   *          The beginning of the time
   * @param to
   *          The end of the time
   * @return histogram of the tweets in the given time
   */
  public String[] get(final Date from, final Date to) {
    return format.formatHistogram(getBaseHistogramBetween(from, to),
        getReHistogramBetween(from, to));
  }

  private int[] getReHistogramBetween(final Date from, final Date to) {
    return getHistogramBetween(from, to, timesRe, occuarenceRe);
  }

  private int[] getBaseHistogramBetween(final Date from, final Date to) {
    return getHistogramBetween(from, to, timesBase, occuarenceBase);
  }

  private int[] getHistogramBetween(final Date from, final Date to,
      final long[] times, final int[] amounts) {
    final int[] $ = new int[DayOfWeek.values().length];
    final int index = binarySearch(times, from.getTime());

    for (int i = index; i < times.length; i++) {
      if (times[i] > to.getTime())
        break;
      $[DayOfWeek.fromDate(new Date(times[i])).ordinal()] += amounts[i];
    }
    return $;
  }

  /**
   * 
   * @param array
   * @param search
   * @return the index of first value in array that is bigger or equal to search
   */
  private int binarySearch(final long[] array, final long search) {
    int first = 0;
    int last = array.length - 1;
    while (first <= last) {
      final int guess = (first + last) / 2;
      if (array[guess] == search)
        return guess;

      if (array[guess] < search)
        first = guess + 1;
      else
        last = guess - 1;
    }
    return first;
  }

  /**
   * @return An empty {@link TemporalHistogram}
   */
  public static TemporalHistogram empty() {
    return new TemporalHistogram(new HistogramFormat());
  }
}
