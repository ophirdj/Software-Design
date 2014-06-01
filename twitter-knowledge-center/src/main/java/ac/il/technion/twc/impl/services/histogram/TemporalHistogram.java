package ac.il.technion.twc.impl.services.histogram;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import ac.il.technion.twc.FuntionalityTester;
import ac.il.technion.twc.api.QuerySetup;
import ac.il.technion.twc.api.TwitterQuery;
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
 */
public class TemporalHistogram implements TwitterQuery {

  private static class HistogramRetriver {
    private final List<Long> times;
    private final List<Integer> amounts;
    private final List<Long> timesHistograms;
    private final List<int[]> amountsHistograms;

    public HistogramRetriver(final List<Long> timesBase,
        final List<Integer> occuarenceBase) {
      times = timesBase;
      amounts = occuarenceBase;
      timesHistograms =
          new ArrayList<>(1 + timesBase.size() / datesPerHistogram);
      amountsHistograms =
          new ArrayList<>(1 + timesBase.size() / datesPerHistogram);
      initHistogram();
    }

    private void initHistogram() {
      final int[] histogram = new int[7];
      timesHistograms.add(0L);
      amountsHistograms.add(new int[7]);
      for (int j = 0; j < times.size(); j++) {
        histogram[DayOfWeek.fromDate(new Date(times.get(j))).ordinal()] +=
            amounts.get(j);
        if ((j + 1) % datesPerHistogram == 0) {
          timesHistograms.add(times.get(j));
          amountsHistograms.add(Arrays.copyOf(histogram, histogram.length));
        }
      }
    }

    public int[] retrive(final Date from, final Date to) {
      final int[] $ = new int[DayOfWeek.values().length];
      final int[] toHistogram = findHistogramAtTime(to.getTime());
      // from.getTime() - 1 so we want remove the value in form
      final int[] fromHistogram = findHistogramAtTime(from.getTime() - 1);
      for (int i = 0; i < $.length; i++)
        $[i] = toHistogram[i] - fromHistogram[i];
      return $;
    }

    /**
     * 
     * @param time
     * @return the histogram up to the given time (inclusive)
     */
    private int[] findHistogramAtTime(final long time) {
      final int indexPreCalculeted = binarySearch(timesHistograms, time, true);
      // IncompleteHistogram is the histogram up to
      // histogramTime[indexPreCalculeted] (inclusive)
      final int[] incompleteHistogram =
          amountsHistograms.get(indexPreCalculeted);
      final int[] $ =
          Arrays.copyOf(incompleteHistogram, incompleteHistogram.length);
      // histogramTime[indexPreCalculeted] + 1 so we want count
      // histogramTime[indexPreCalculeted] twice.
      final int indexCompletion =
          binarySearch(times, timesHistograms.get(indexPreCalculeted) + 1,
              false);
      for (int i = indexCompletion; i < times.size(); i++) {
        if (times.get(i) > time)
          break;
        $[DayOfWeek.fromDate(new Date(times.get(i))).ordinal()] +=
            amounts.get(i);
      }
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
    private int binarySearch(final List<Long> array, final long search,
        final boolean before) {
      int first = 0;
      int last = array.size() - 1;
      while (first <= last) {
        final int guess = (first + last) / 2;
        if (array.get(guess) == search)
          return guess;
        if (array.get(guess) > search)
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
      result = prime * result + (amounts == null ? 0 : amounts.hashCode());
      result =
          prime * result
              + (amountsHistograms == null ? 0 : amountsHistograms.hashCode());
      result = prime * result + (times == null ? 0 : times.hashCode());
      result =
          prime * result
              + (timesHistograms == null ? 0 : timesHistograms.hashCode());
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
      final HistogramRetriver other = (HistogramRetriver) obj;

      if (amounts == null) {
        if (other.amounts != null)
          return false;
      } else if (!amounts.equals(other.amounts))
        return false;
      if (amountsHistograms == null) {
        if (other.amountsHistograms != null)
          return false;
      } else if (amountsHistograms.size() != other.amountsHistograms.size())
        return false;
      else
        for (int i = 0; i < amountsHistograms.size(); i++)
          if (!Arrays.equals(amountsHistograms.get(i),
              other.amountsHistograms.get(i)))
            return false;
      if (times == null) {
        if (other.times != null)
          return false;
      } else if (!times.equals(other.times))
        return false;
      if (timesHistograms == null) {
        if (other.timesHistograms != null)
          return false;
      } else if (!timesHistograms.equals(other.timesHistograms))
        return false;
      return true;
    }

  }

  private final HistogramRetriver baseHistogram;
  private final HistogramRetriver reHistogram;

  private final HistogramFormat format;

  /**
   * We will create one histogram for every datesPerHistogram dates.
   * 
   * Help to reduce time for setup.
   */
  private static final int datesPerHistogram = 10;

  /**
   * Create an empty histogram
   * 
   * @param format
   *          the format for the return values
   */
  public TemporalHistogram(final HistogramFormat format) {
    baseHistogram =
        new HistogramRetriver(Collections.<Long> emptyList(),
            Collections.<Integer> emptyList());
    reHistogram =
        new HistogramRetriver(Collections.<Long> emptyList(),
            Collections.<Integer> emptyList());
    this.format = format;
  }

  /**
   * @param dayMapProperty
   *          the property from which the service is builded
   * @param format
   *          the format for the return values
   */
  @QuerySetup
  public TemporalHistogram(final DayMapping dayMapProperty,
      final HistogramFormat format) {
    this.format = format;
    baseHistogram = createHistogram(dayMapProperty.getAllDaysBase());
    reHistogram = createHistogram(dayMapProperty.getAllDaysRe());
  }

  private HistogramRetriver createHistogram(
      final Set<Entry<Long, Integer>> allDays) {
    final ArrayList<Long> times = new ArrayList<>(allDays.size());
    final ArrayList<Integer> amount = new ArrayList<>(allDays.size());

    for (final Entry<Long, Integer> entry : allDays) {
      times.add(entry.getKey());
      amount.add(entry.getValue());
    }
    return new HistogramRetriver(times, amount);
  }

  /**
   * @param from
   *          The beginning of the time
   * @param to
   *          The end of the time
   * @return histogram of the tweets in the given time
   */
  public String[] get(final Date from, final Date to) {
    if (from.after(to))
      return format.formatHistogram(new int[7], new int[7]);
    return format.formatHistogram(baseHistogram.retrive(from, to),
        reHistogram.retrive(from, to));
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result =
        prime * result + (baseHistogram == null ? 0 : baseHistogram.hashCode());
    result = prime * result + (format == null ? 0 : format.hashCode());
    result =
        prime * result + (reHistogram == null ? 0 : reHistogram.hashCode());
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
    if (baseHistogram == null) {
      if (other.baseHistogram != null)
        return false;
    } else if (!baseHistogram.equals(other.baseHistogram))
      return false;
    if (format == null) {
      if (other.format != null)
        return false;
    } else if (!format.equals(other.format))
      return false;
    if (reHistogram == null) {
      if (other.reHistogram != null)
        return false;
    } else if (!reHistogram.equals(other.reHistogram))
      return false;
    return true;
  }

}
