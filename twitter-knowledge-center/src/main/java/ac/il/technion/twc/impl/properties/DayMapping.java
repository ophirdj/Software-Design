package ac.il.technion.twc.impl.properties;

import java.util.Collection;
import java.util.Date;
import java.util.NavigableMap;

public class DayMapping {

  private final NavigableMap<Date, DayOfWeek> dayByDateBase;
  private final NavigableMap<Date, DayOfWeek> dayByDateRe;

  public DayMapping(final NavigableMap<Date, DayOfWeek> dayByDateBase,
      final NavigableMap<Date, DayOfWeek> dayByDateRe) {
    this.dayByDateBase = dayByDateBase;
    this.dayByDateRe = dayByDateRe;
  }

  public Collection<DayOfWeek> getAllDaysBase() {
    return dayByDateBase.values();
  }

  public Collection<DayOfWeek> getAllDaysBase(final Date from, final Date to) {
    return dayByDateBase.subMap(from, true, to, true).values();
  }

  public Collection<DayOfWeek> getAllDaysRe() {
    return dayByDateRe.values();
  }

  public Collection<DayOfWeek> getAllDaysRe(final Date from, final Date to) {
    return dayByDateRe.subMap(from, true, to, true).values();
  }
}
