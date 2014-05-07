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

  // for gson
  @SuppressWarnings("unused")
  private DayHistogram() {
    basetweets = null;
    retweets = null;
  }

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

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + (basetweets == null ? 0 : basetweets.hashCode());
    result = prime * result + (retweets == null ? 0 : retweets.hashCode());
    return result;
  }

  @Override
  public boolean equals(final Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    final DayHistogram other = (DayHistogram) obj;
    if (basetweets == null) {
      if (other.basetweets != null)
        return false;
    } else if (!basetweets.equals(other.basetweets))
      return false;
    if (retweets == null) {
      if (other.retweets != null)
        return false;
    } else if (!retweets.equals(other.retweets))
      return false;
    return true;
  }

}
