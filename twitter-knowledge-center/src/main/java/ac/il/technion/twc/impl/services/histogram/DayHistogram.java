package ac.il.technion.twc.impl.services.histogram;

import java.util.ArrayList;

import ac.il.technion.twc.FuntionalityTester;
import ac.il.technion.twc.impl.properties.DayMapping;
import ac.il.technion.twc.impl.properties.DayOfWeek;

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
        format.buildHistogram(new ArrayList<DayOfWeek>(),
            new ArrayList<DayOfWeek>());
  }

  /**
   * 
   * @param dayMapProperty
   * @param format
   */
  public DayHistogram(final DayMapping dayMapProperty,
      final HistogramFormat format) {
    answer =
        format.buildHistogram(dayMapProperty.getAllDaysBase(),
            dayMapProperty.getAllDaysRe());
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
}
