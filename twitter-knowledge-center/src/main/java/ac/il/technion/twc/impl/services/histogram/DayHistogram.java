package ac.il.technion.twc.impl.services.histogram;

import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.Map.Entry;

import ac.il.technion.twc.FuntionalityTester;
import ac.il.technion.twc.impl.properties.daymapping.DayMapping;
import ac.il.technion.twc.impl.properties.daymapping.DayOfWeek;

/**
 * Service that answer {@link FuntionalityTester#getDailyHistogram()} query.
 * 
 * @author Ziv Ronen
 * @date 23.05.2014
 * @mail akarks@gmail.com
 * 
 * @version 2.0
 * @since 2.0
 */
public class DayHistogram {

  private final String[] answer;

  private DayHistogram(final HistogramFormat format) {
    answer =
        format.formatHistogram(new int[DayOfWeek.values().length],
            new int[DayOfWeek.values().length]);
  }

  /**
   * 
   * @param dayMapProperty
   * @param format
   */
  public DayHistogram(final DayMapping dayMapProperty,
      final HistogramFormat format) {

    answer =
        format.formatHistogram(makeHistogram(dayMapProperty.getAllDaysBase()),
            makeHistogram(dayMapProperty.getAllDaysRe()));
  }

  private int[] makeHistogram(final Collection<Entry<Long, Integer>> samples) {
    final int[] $ = new int[DayOfWeek.values().length];
    for (final Entry<Long, Integer> entry : samples)
      $[DayOfWeek.fromDate(new Date(entry.getKey())).ordinal()] +=
          entry.getValue();
    return $;
  }

  /**
   * @return histogram of all the tweets in the system
   */
  public String[] get() {
    return answer;
  }

  /**
   * @return an empty DayHistogram
   */
  public static DayHistogram empty() {
    return new DayHistogram(new HistogramFormat());
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + Arrays.hashCode(answer);
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
    final DayHistogram other = (DayHistogram) obj;
    if (!Arrays.equals(answer, other.answer))
      return false;
    return true;
  }
}
