package ac.il.technion.twc.api.tweet.parser;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import ac.il.technion.twc.api.tweet.Tweet;

/**
 * A parser that support multiple formats
 * 
 * @author Ziv Ronen
 * @date 22.05.2014
 * @mail akarks@gmail.com
 */
public class TweetParser {

  private final List<TweetFormat> formats;

  /**
   * @param formats
   */
  public TweetParser(final TweetFormat... formats) {
    this.formats = Arrays.asList(formats);
  }

  /**
   * @param tweets
   *          a string representation of tweets
   * @return a collection of tweets objects corresponding to those tweets.
   * @throws ParseException
   *           if the parser can't parse all the given tweets
   */
  public List<Tweet> parse(final String... tweets) throws ParseException {
    final RunningFormat possibleFormats = new RunningFormat(formats);
    for (final String tweet : tweets)
      possibleFormats.parse(tweet);
    return possibleFormats.getTweets();
  }

  private static class RunningFormat {

    private final List<Pair<TweetFormat, List<Tweet>>> formats =
        new ArrayList<>();

    public RunningFormat(final List<TweetFormat> formats) {
      for (final TweetFormat parserFormat : formats)
        this.formats.add(new Pair<TweetFormat, List<Tweet>>(parserFormat,
            new ArrayList<Tweet>()));
    }

    public List<Tweet> getTweets() throws ParseException {
      if (formats.isEmpty())
        throw new ParseException("No matching parser", 0);
      if (formats.size() > 1 && formats.get(0).second.size() > 0)
        throw new ParseException("Parsing ambiguity",
            formats.get(0).second.size());
      return formats.get(0).second;
    }

    public void parse(final String tweet) {
      for (final Iterator<Pair<TweetFormat, List<Tweet>>> iterator =
          formats.iterator(); iterator.hasNext();) {
        final Pair<TweetFormat, List<Tweet>> parserFormat = iterator.next();
        try {
          parserFormat.second.add(parserFormat.first.parse(tweet));
        } catch (final ParseException e) {
          iterator.remove();
          continue;
        }
      }
    }

  }

  private static class Pair<T1, T2> {
    /**
     * The first element
     */
    public final T1 first;
    /**
     * The second element
     */
    public final T2 second;

    /**
     * @param t1
     *          value for the first element
     * @param t2
     *          value for the second element
     */
    public Pair(final T1 t1, final T2 t2) {
      first = t1;
      second = t2;
    }
  }

}
