package ac.il.technion.twc.histogram;

import ac.il.technion.twc.message.tweet.BaseTweet;
import ac.il.technion.twc.message.tweet.Retweet;
import ac.il.technion.twc.message.visitor.PropertyBuilder;

/**
 * 
 * @author Ziv Ronen
 * @date 07.05.2014
 * @mail akarks@gmail.com
 * 
 * @version 2.0
 * @since 2.0
 * 
 *        handling histogram property index building, storing and loading
 */
public class DayHistogramBuilder implements
    PropertyBuilder<DayHistogram, DayHistogramCache> {

  /**
   * Ongoing histogram state
   */
  DayHistogram histogram;

  @Override
  public void initializeFromState(final DayHistogram state) {
    histogram = state;
  }

  @Override
  public Void visit(final BaseTweet t) {
    final DayOfWeek day = DayOfWeek.fromDate(t.date());
    histogram.basetweets.put(day, histogram.basetweets.get(day) + 1);
    return null;
  }

  @Override
  public Void visit(final Retweet t) {
    final DayOfWeek day = DayOfWeek.fromDate(t.date());
    histogram.retweets.put(day, histogram.retweets.get(day) + 1);
    return null;
  }

  @Override
  public DayHistogram getState() {
    return histogram;
  }

  @Override
  public DayHistogramCache getResultCache() {
    return new DayHistogramCache(histogram);
  }

}
