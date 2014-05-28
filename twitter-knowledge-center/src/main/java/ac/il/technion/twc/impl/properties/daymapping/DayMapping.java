package ac.il.technion.twc.impl.properties.daymapping;

import java.util.Collection;
import java.util.Date;
import java.util.NavigableMap;

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

  private final NavigableMap<Date, DayOfWeek> dayByDateBase;
  private final NavigableMap<Date, DayOfWeek> dayByDateRe;

  /**
   * @param dayByDateBase
   * @param dayByDateRe
   */
  public DayMapping(final NavigableMap<Date, DayOfWeek> dayByDateBase,
      final NavigableMap<Date, DayOfWeek> dayByDateRe) {
    this.dayByDateBase = dayByDateBase;
    this.dayByDateRe = dayByDateRe;
  }

  /**
   * @return all the days of base tweets
   */
  public Collection<DayOfWeek> getAllDaysBase() {
    return dayByDateBase.values();
  }

  /**
   * @param from
   *          The beginning of the time
   * @param to
   *          The end of the time
   * @return all the days of base tweets that happened in the given time
   */
  public Collection<DayOfWeek> getAllDaysBase(final Date from, final Date to) {
    return dayByDateBase.subMap(from, true, to, true).values();
  }

  /**
   * @return all the days of retweets
   */
  public Collection<DayOfWeek> getAllDaysRe() {
    return dayByDateRe.values();
  }

  /**
   * @param from
   *          The beginning of the time
   * @param to
   *          The end of the time
   * @return all the days of retweets that happened in the given time
   */
  public Collection<DayOfWeek> getAllDaysRe(final Date from, final Date to) {
    return dayByDateRe.subMap(from, true, to, true).values();
  }
}
