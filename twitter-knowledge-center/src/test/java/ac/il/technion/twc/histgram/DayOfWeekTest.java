package ac.il.technion.twc.histgram;

import static org.junit.Assert.assertEquals;

import java.util.GregorianCalendar;

import org.junit.Test;

public class DayOfWeekTest {

	@Test
	public final void fromDateSouldBeSunday() {
		assertEquals(DayOfWeek.SUNDAY,
				DayOfWeek.fromDate(new GregorianCalendar(2014, 4, 4).getTime()));
	}

	@Test
	public final void fromDateSouldBeMonday() {
		assertEquals(DayOfWeek.MONDAY,
				DayOfWeek.fromDate(new GregorianCalendar(2014, 4, 5).getTime()));
	}

	@Test
	public final void fromDateSouldBeTuesday() {
		assertEquals(DayOfWeek.TUESDAY,
				DayOfWeek.fromDate(new GregorianCalendar(2014, 4, 6).getTime()));
	}

	@Test
	public final void fromDateSouldBeWednesday() {
		assertEquals(DayOfWeek.WEDNESDAY,
				DayOfWeek.fromDate(new GregorianCalendar(2014, 4, 7).getTime()));
	}

	@Test
	public final void fromDateSouldBeThursday() {
		assertEquals(DayOfWeek.THURSDAY,
				DayOfWeek.fromDate(new GregorianCalendar(2014, 4, 1).getTime()));
	}

	@Test
	public final void fromDateSouldBeFriday() {
		assertEquals(DayOfWeek.FRIDAY,
				DayOfWeek.fromDate(new GregorianCalendar(2014, 4, 2).getTime()));
	}

	@Test
	public final void fromDateSouldBeSaturday() {
		assertEquals(DayOfWeek.SATUREDAY,
				DayOfWeek.fromDate(new GregorianCalendar(2014, 4, 3).getTime()));
	}

}
