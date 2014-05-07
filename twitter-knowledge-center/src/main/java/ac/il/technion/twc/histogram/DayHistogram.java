package ac.il.technion.twc.histogram;

import java.util.Map;

/**
 * @author Ziv Ronen
 * @date 07.05.2014
 * @mail akarks@gmail.com
 * 
 * @version 2.0
 * @since 2.0
 * 
 *        class that keep the histogram of tweets and retweet in each day
 */
public class DayHistogram {

  /**
   * Map between a day in the week and amount of base tweets in that day
   */
  final Map<DayOfWeek, Integer> basetweets;
  /**
   * Map between a day in the week and amount of retweets in that day
   */
  final Map<DayOfWeek, Integer> retweets;

  /**
   * Build the days histogram from those to map
   * 
   * @param tweets
   * @param retweets
   */
  public DayHistogram(final Map<DayOfWeek, Integer> tweets,
      final Map<DayOfWeek, Integer> retweets) {
    basetweets = tweets;
    this.retweets = retweets;
  }

  /**
   * return number of tweets (base + re) in a single day
   * 
   * @param day
   *          The day for checking
   * @return Number of tweets in that day
   */
  public int tweets(final DayOfWeek day) {
    return basetweets.get(day) + retweets.get(day);
  }

  /**
   * return number of retweets in a single day
   * 
   * @param day
   *          The day for checking
   * @return Number of retweets in that day
   */
  public int retweets(final DayOfWeek day) {
    return retweets.get(day);
  }

}
