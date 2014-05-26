package ac.il.technion.twc.impl.services.histogram;

import java.util.Collection;

import ac.il.technion.twc.impl.properties.DayOfWeek;

/**
 * Build the histogram from a collection of tweets
 * 
 * @author Ziv Ronen
 * @date 26.05.2014
 * @mail akarks@gmail.com
 * 
 * @version 2.0
 * @since 2.0
 */
public class HistogramFormat {

  // TODO: improve code quality.
  /**
   * @param baseTweets
   *          all the days of base tweets
   * @param retweets
   *          all the days of retweets
   * @return the histogram
   */
  public String[] buildHistogram(final Collection<DayOfWeek> baseTweets,
      final Collection<DayOfWeek> retweets) {
    final int[] histogram = new int[DayOfWeek.values().length];
    for (int i = 0; i < histogram.length; i++)
      histogram[i] = 0;
    final int[] histogramRe = new int[DayOfWeek.values().length];
    for (int i = 0; i < histogramRe.length; i++)
      histogramRe[i] = 0;
    for (final DayOfWeek day : baseTweets)
      histogram[day.ordinal()]++;
    for (final DayOfWeek day : retweets) {
      histogram[day.ordinal()]++;
      histogramRe[day.ordinal()]++;
    }

    final String[] $ = new String[DayOfWeek.values().length];
    for (final DayOfWeek day : DayOfWeek.values())
      $[day.ordinal()] =
          new StringBuilder().append(histogram[day.ordinal()]).append(',')
              .append(histogramRe[day.ordinal()]).toString();
    return $;
  }

}
