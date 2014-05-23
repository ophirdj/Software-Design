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
public class DaysMappingBuilder implements
    PropertyBuilder<NavigableMap<Date, DayOfWeek>> {

  private final NavigableMap<Date, DayOfWeek> dayByDate;

  /**
   * @param map
   */
  public DaysMappingBuilder(final NavigableMap<Date, DayOfWeek> map) {
    dayByDate = map;
  }

  @Override
  public Void visit(final BaseTweet t) {
    dayByDate.put(t.date(), DayOfWeek.fromDate(t.date()));
    return null;
  }

  @Override
  public Void visit(final Retweet t) {
    dayByDate.put(t.date(), DayOfWeek.fromDate(t.date()));
    return null;
  }

  @Override
  public void clear() {
    dayByDate.clear();
  }

  // TODO: create new instance
  @Override
  public NavigableMap<Date, DayOfWeek> getResult() {
    return dayByDate;
  }

}
