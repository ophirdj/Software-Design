package ac.il.technion.twc.impl.services.histogram;

import java.util.Date;
import java.util.TreeMap;

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

  private final DayMapping dayByDate;
  private final HistogramFormat format;

  /**
   * @param format
   */
  private TemporalHistogram(final HistogramFormat format) {
    dayByDate =
        new DayMapping(new TreeMap<Date, DayOfWeek>(),
            new TreeMap<Date, DayOfWeek>());
    this.format = format;
  }

  /**
   * @param dayMapProperty
   * @param format
   */
  public TemporalHistogram(final DayMapping dayMapProperty,
      final HistogramFormat format) {
    dayByDate = dayMapProperty;
    this.format = format;
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
    return format.buildHistogram(dayByDate.getAllDaysBase(from, to),
        dayByDate.getAllDaysRe(from, to));
  }

  /**
   * @return An empty {@link TemporalHistogram}
   */
  public static TemporalHistogram empty() {
    return new TemporalHistogram(new HistogramFormat());
  }
}
