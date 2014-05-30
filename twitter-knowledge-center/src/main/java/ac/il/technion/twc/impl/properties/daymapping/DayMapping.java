package ac.il.technion.twc.impl.properties.daymapping;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import ac.il.technion.twc.api.tweets.BaseTweet;
import ac.il.technion.twc.api.tweets.Retweet;

/**
 * A mapping between dates of tweets and the day
 * 
 * @author Ziv Ronen
 * @date 26.05.2014
 * @mail akarks@gmail.com
 * 
 * @version 2.0
 * @since 2.0
 */
public class DayMapping {

  private final Map<Long, Integer> dayByDateBase;
  private final Map<Long, Integer> dayByDateRe;

  /**
   * @param bases
   *          the base tweets in the system
   * @param res
   *          the retweets in the system
   */
  public DayMapping(final List<BaseTweet> bases, final List<Retweet> res) {
    final SortedMap<Long, Integer> baseOccurencebyTime =
        new TreeMap<Long, Integer>();
    final SortedMap<Long, Integer> reOccurenceByTime =
        new TreeMap<Long, Integer>();
    for (final BaseTweet base : bases)
      increaseCounter(base.date().getTime(), baseOccurencebyTime);
    for (final Retweet retweet : res)
      increaseCounter(retweet.date().getTime(), reOccurenceByTime);

    dayByDateBase = Collections.unmodifiableMap(baseOccurencebyTime);
    dayByDateRe = Collections.unmodifiableMap(reOccurenceByTime);
  }

  private void increaseCounter(final Long time,
      final Map<Long, Integer> countersMap) {
    countersMap.put(time, (!countersMap.containsKey(Long.valueOf(time)) ? 0
        : countersMap.get(time)) + 1);
  }

  /**
   * @return all the days of base tweets
   */
  public Set<Entry<Long, Integer>> getAllDaysBase() {
    return dayByDateBase.entrySet();
  }

  /**
   * @return all the days of retweets
   */
  public Set<Entry<Long, Integer>> getAllDaysRe() {
    return dayByDateRe.entrySet();
  }

}
