package ac.il.technion.twc.histogram;

import static org.junit.Assert.assertEquals;

import java.util.GregorianCalendar;

import org.junit.Test;

/**
 * Tests for {@link DayOfWeek}
 * 
 * @author Ophir De Jager
 * 
 */
public class DayOfWeekTest {

	/**
	 * Test method for {@link DayOfWeek#fromDate(java.util.Date)}
	 */
	@Test
	public final void fromDateSouldBeSunday() {
		assertEquals(DayOfWeek.SUNDAY,
				DayOfWeek.fromDate(new GregorianCalendar(2014, 4, 4).getTime()));
	}

	/**
	 * Test method for {@link DayOfWeek#fromDate(java.util.Date)}
	 */
	@Test
	public final void fromDateSouldBeMonday() {
		assertEquals(DayOfWeek.MONDAY,
				DayOfWeek.fromDate(new GregorianCalendar(2014, 4, 5).getTime()));
	}

	/**
	 * Test method for {@link DayOfWeek#fromDate(java.util.Date)}
	 */
	@Test
	public final void fromDateSouldBeTuesday() {
		assertEquals(DayOfWeek.TUESDAY,
				DayOfWeek.fromDate(new GregorianCalendar(2014, 4, 6).getTime()));
	}

	/**
	 * Test method for {@link DayOfWeek#fromDate(java.util.Date)}
	 */
	@Test
	public final void fromDateSouldBeWednesday() {
		assertEquals(DayOfWeek.WEDNESDAY,
				DayOfWeek.fromDate(new GregorianCalendar(2014, 4, 7).getTime()));
	}

	/**
	 * Test method for {@link DayOfWeek#fromDate(java.util.Date)}
	 */
	@Test
	public final void fromDateSouldBeThursday() {
		assertEquals(DayOfWeek.THURSDAY,
				DayOfWeek.fromDate(new GregorianCalendar(2014, 4, 1).getTime()));
	}

	/**
	 * Test method for {@link DayOfWeek#fromDate(java.util.Date)}
	 */
	@Test
	public final void fromDateSouldBeFriday() {
		assertEquals(DayOfWeek.FRIDAY,
				DayOfWeek.fromDate(new GregorianCalendar(2014, 4, 2).getTime()));
	}

	/**
	 * Test method for {@link DayOfWeek#fromDate(java.util.Date)}
	 */
	@Test
	public final void fromDateSouldBeSaturday() {
		assertEquals(DayOfWeek.SATURDAY,
				DayOfWeek.fromDate(new GregorianCalendar(2014, 4, 3).getTime()));
	}

}
