package ac.il.technion.twc.impl.properties.daymapping;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * A mapping between dates of tweets and the day
 * 
 * @author Ziv Ronen
 * @date 26.05.2014
 * @mail akarks@gmail.com
 * 
 * @version 2.0
 * @since 2.0
 */
public class DayMapping {

  private final Map<Long, Integer> dayByDateBase;
  private final Map<Long, Integer> dayByDateRe;

  /**
   * @param dayByDateBase
   * @param dayByDateRe
   */
  public DayMapping(final Map<Long, Integer> dayByDateBase,
      final Map<Long, Integer> dayByDateRe) {
    this.dayByDateBase = dayByDateBase;
    this.dayByDateRe = dayByDateRe;
  }

  /**
   * @return all the days of base tweets
   */
  public Set<Entry<Long, Integer>> getAllDaysBase() {
    return dayByDateBase.entrySet();
  }

  /**
   * @return all the days of retweets
   */
  public Set<Entry<Long, Integer>> getAllDaysRe() {
    return dayByDateRe.entrySet();
  }

}
