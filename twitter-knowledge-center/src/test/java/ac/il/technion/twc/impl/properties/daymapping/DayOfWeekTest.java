package ac.il.technion.twc.impl.properties.daymapping;

import static junitparams.JUnitParamsRunner.$;
import static org.junit.Assert.assertEquals;

import java.util.Date;
import java.util.GregorianCalendar;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Tests for {@link DayOfWeek}
 * 
 * @author Ophir De Jager
 */
@RunWith(JUnitParamsRunner.class)
public class DayOfWeekTest {

  @SuppressWarnings("unused")
  // used by JunitParams
      private
      Object[] getDates() {
    return $(
        $(DayOfWeek.SATURDAY, new GregorianCalendar(2014, 4, 4).getTime()),
        $(DayOfWeek.SUNDAY, new GregorianCalendar(2014, 4, 5).getTime()),
        $(DayOfWeek.MONDAY, new GregorianCalendar(2014, 4, 6).getTime()),
        $(DayOfWeek.TUESDAY, new GregorianCalendar(2014, 4, 7).getTime()),
        $(DayOfWeek.WEDNESDAY, new GregorianCalendar(2014, 4, 1).getTime()),
        $(DayOfWeek.THURSDAY, new GregorianCalendar(2014, 4, 2).getTime()),
        $(DayOfWeek.FRIDAY, new GregorianCalendar(2014, 4, 3).getTime()),
        $(DayOfWeek.WEDNESDAY, new GregorianCalendar(2014, 4, 8).getTime()),
        $(DayOfWeek.THURSDAY, new GregorianCalendar(2014, 4, 9).getTime()),
        $(DayOfWeek.FRIDAY, new GregorianCalendar(2014, 4, 10).getTime()),
        $(DayOfWeek.SATURDAY, new GregorianCalendar(2014, 4, 11).getTime()),
        $(DayOfWeek.SUNDAY, new GregorianCalendar(2014, 4, 4, 20, 30).getTime()));
  }

  /**
   * Test method for {@link DayOfWeek#fromDate(java.util.Date)}
   * 
   * @param day
   *          Day of week of given date
   * @param date
   *          A date.
   */
  @Parameters(method = "getDates")
  @Test
  public void fromDateShouldReturnCorrectDay(final DayOfWeek day,
      final Date date) {
    assertEquals(day, DayOfWeek.fromDate(date));
  }

}
