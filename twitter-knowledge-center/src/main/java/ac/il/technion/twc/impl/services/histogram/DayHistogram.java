package ac.il.technion.twc.impl.services.histogram;

import ac.il.technion.twc.TwitterKnowledgeCenter;
import ac.il.technion.twc.impl.properties.DayMapping;

/**
 * Service that answer {@link TwitterKnowledgeCenter#getDailyHistogram()} query.
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
   * @return The answer in the correct format
   */
  public String[] get() {
    return answer;
  }

}
