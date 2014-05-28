package ac.il.technion.twc.api.parser.impl;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import ac.il.technion.twc.api.parser.ParserFormat;
import ac.il.technion.twc.api.parser.TweetsParser;
import ac.il.technion.twc.api.tweets.Tweet;

/**
 * A parser that support multiple formats
 * 
 * @author Ziv Ronen
 * @date 22.05.2014
 * @mail akarks@gmail.com
 * 
 * @version 2.0
 * @since 2.0
 */
// TODO: find shorter name
public class MultiFormatsParser implements TweetsParser {

  private final List<ParserFormat> formats;

  /**
   * @param formats
   */
  public MultiFormatsParser(final List<ParserFormat> formats) {
    this.formats = formats;
  }

  @Override
  public Collection<Tweet> parse(final String... lines) throws ParseException {
    final RunningFormat possibleFormats = new RunningFormat(formats);
    for (final String line : lines)
      possibleFormats.addTweet(line);
    return possibleFormats.getTweets();
  }

  private static class RunningFormat {

    private final List<Pair<ParserFormat, List<Tweet>>> formats =
        new ArrayList<>();

    public RunningFormat(final List<ParserFormat> formats) {
      for (final ParserFormat parserFormat : formats)
        this.formats.add(new Pair<ParserFormat, List<Tweet>>(parserFormat,
            new ArrayList<Tweet>()));
    }

    public Collection<Tweet> getTweets() throws ParseException {
      if (formats.isEmpty())
        throw new ParseException("No uniform parser", 0);
      return formats.get(0).second;
    }

    public void addTweet(final String line) {
      for (final Iterator<Pair<ParserFormat, List<Tweet>>> iterator =
          formats.iterator(); iterator.hasNext();) {
        final Pair<ParserFormat, List<Tweet>> parserFormat = iterator.next();
        if (!parserFormat.first.isFromFormat(line)) {
          iterator.remove();
          continue;
        }
        try {
          parserFormat.second.add(parserFormat.first.parse(line));
        } catch (final ParseException e) {
          // Unreachable
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
