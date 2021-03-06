package ac.il.technion.twc.impl.properties.daymapping;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import ac.il.technion.twc.api.Property;
import ac.il.technion.twc.api.tweet.BaseTweet;
import ac.il.technion.twc.api.tweet.Retweet;

/**
 * A mapping between dates of tweets and the amount of tweets in that date.<br>
 * map both base tweets and retweet separately<br>
 * <br>
 * For example, say we have 3 base tweets: <code>A</code>, <code>B</code>, and
 * <code>C</code> and one retweet <code>D</code>. <br>
 * <code>A</code> was tweeted at T1, <br>
 * <code>B</code> and <code>C</code> were tweeted at T2 and <br>
 * D was tweeted at T3<br>
 * Then {@link DayMapping#getAllDaysBase()} will return two entries:
 * <code>(T1,1)</code> and <code>(T2,2)</code> <br>
 * and {@link DayMapping#getAllDaysRe()} will return one entry,
 * <code>(T3,1)</code>
 * 
 * 
 * @author Ziv Ronen
 * @date 26.05.2014
 * @mail akarks@gmail.com
 * 
 */
public class DayMapping implements Property {

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
