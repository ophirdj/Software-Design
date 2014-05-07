package ac.il.technion.twc.histogram;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public enum DayOfWeek {

	SUNDAY,

	MONDAY,

	TUESDAY,

	WEDNESDAY,

	THURSDAY,

	FRIDAY,

	SATUREDAY

	;

	public static DayOfWeek fromDate(Date date) {
		Calendar c = new GregorianCalendar();
		c.setTime(date);
		return DayOfWeek.values()[c.get(Calendar.DAY_OF_WEEK) - 1];
	}

}
