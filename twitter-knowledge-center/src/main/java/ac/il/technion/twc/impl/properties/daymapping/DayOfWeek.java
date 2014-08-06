package ac.il.technion.twc.impl.properties.daymapping;

import java.util.Date;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

/**
 * enum for each day in a week
 * 
 * @author Ziv Ronen
 * @date 07.05.2014
 * @mail akarks@gmail.com
 */
public enum DayOfWeek {

  /**
   * represent Sunday
   */
  SUNDAY,

  /**
   * represent Monday
   */
  MONDAY,

  /**
   * represent Tuesday
   */
  TUESDAY,

  /**
   * represent Wednesday
   */
  WEDNESDAY,

  /**
   * represent Thursday
   */
  THURSDAY,

  /**
   * represent Friday
   */
  FRIDAY,

  /**
   * represent Saturday
   */
  SATURDAY

  ;

  /**
   * @param date
   *          an date
   * @return A day in the week corresponding to this date
   */
  public static DayOfWeek fromDate(final Date date) {
    return DayOfWeek.values()[new DateTime(date.getTime(), DateTimeZone.UTC).getDayOfWeek() % 7];
  }

}
