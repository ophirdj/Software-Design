package ac.il.technion.twc.impl.services.histogram;

import java.util.Date;

import ac.il.technion.twc.impl.properties.DayMapping;

public class TemporalHistogram {

  private final DayMapping dayByDate;
  private final HistogramFormat format;

  public TemporalHistogram(final DayMapping dayMapProperty,
      final HistogramFormat format) {
    dayByDate = dayMapProperty;
    this.format = format;
  }

  // XXX: this is a simple solution, we will discuss faster one later on
  public String[] get(final Date from, final Date to) {
    return format.buildHistogram(dayByDate.getAllDaysBase(from, to),
        dayByDate.getAllDaysRe(from, to));
  }
}
