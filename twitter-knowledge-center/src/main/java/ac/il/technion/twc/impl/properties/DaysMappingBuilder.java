package ac.il.technion.twc.impl.properties;

import java.util.Date;
import java.util.NavigableMap;

import ac.il.technion.twc.api.properties.PropertyBuilder;
import ac.il.technion.twc.api.tweets.BaseTweet;
import ac.il.technion.twc.api.tweets.Retweet;

/**
 * Builder for an ordered mapping between Dates and the corresponding day.
 * 
 * @author Ziv Ronen
 * @date 23.05.2014
 * @mail akarks@gmail.com
 * 
 * @version 2.0
 * @since 2.0
 */
// XXX: maybe it is better to use NavigableSet of the date and calculate
// DayOfWeek on usage?
public class DaysMappingBuilder implements PropertyBuilder<DayMapping> {

  private final NavigableMap<Date, DayOfWeek> dayByDateBase;
  private final NavigableMap<Date, DayOfWeek> dayByDateRe;

  /**
   * @param dayByDateBase
   * @param dayByDateRe
   */
  public DaysMappingBuilder(final NavigableMap<Date, DayOfWeek> dayByDateBase,
      final NavigableMap<Date, DayOfWeek> dayByDateRe) {
    this.dayByDateBase = dayByDateBase;
    this.dayByDateRe = dayByDateRe;
  }

  @Override
  public Void visit(final BaseTweet t) {
    dayByDateBase.put(t.date(), DayOfWeek.fromDate(t.date()));
    return null;
  }

  @Override
  public Void visit(final Retweet t) {
    dayByDateRe.put(t.date(), DayOfWeek.fromDate(t.date()));
    return null;
  }

  @Override
  public void clear() {
    dayByDateBase.clear();
    dayByDateRe.clear();
  }

  // TODO: create new instance
  @Override
  public DayMapping getResult() {
    return new DayMapping(dayByDateBase, dayByDateRe);
  }

}
