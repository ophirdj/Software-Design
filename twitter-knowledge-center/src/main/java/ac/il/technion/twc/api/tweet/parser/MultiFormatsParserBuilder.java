package ac.il.technion.twc.api.tweet.parser;

import java.util.ArrayList;
import java.util.List;

/**
 * A builder for {@link MultiFormatsTweetParser}
 * 
 * @author Ziv Ronen
 * @date 22.05.2014
 * @mail akarks@gmail.com
 * 
 * @version 2.0
 * @since 2.0
 */
public class MultiFormatsParserBuilder {

  private final List<ParseFormat> formats = new ArrayList<>();

  /**
   * @param format
   *          format to add
   * @return the builder
   */
  public MultiFormatsParserBuilder add(final ParseFormat format) {
    formats.add(format);
    return this;
  }

  /**
   * @return the parser using all the formats. if several formats are suitable
   *         for all tweets, the result will be of the first added format.
   */
  public TweetParser build() {
    return new MultiFormatsTweetParser(formats);
  }
}
