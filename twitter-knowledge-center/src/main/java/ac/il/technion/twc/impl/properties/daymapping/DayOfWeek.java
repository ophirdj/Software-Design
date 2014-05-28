package ac.il.technion.twc.impl.properties.daymapping;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * @author Ziv Ronen
 * @date 07.05.2014
 * @mail akarks@gmail.com
 * 
 * @version 2.0
 * @since 2.0
 * 
 *        enum for each day in a week
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
    final Calendar c = new GregorianCalendar();
    c.setTime(date);
    return DayOfWeek.values()[c.get(Calendar.DAY_OF_WEEK) - 1];
  }

}
