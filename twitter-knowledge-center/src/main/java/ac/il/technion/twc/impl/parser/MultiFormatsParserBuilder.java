package ac.il.technion.twc.impl.parser;

import java.util.ArrayList;
import java.util.List;

import ac.il.technion.twc.api.parsers.TweetsParser;

/**
 * A builder for {@link MultiFormatsParser}
 * 
 * @author Ziv Ronen
 * @date 22.05.2014
 * @mail akarks@gmail.com
 * 
 * @version 2.0
 * @since 2.0
 */
public class MultiFormatsParserBuilder {

  private final List<ParserFormat> formats = new ArrayList<>();

  /**
   * @param format
   *          format to add
   * @return the builder
   */
  MultiFormatsParserBuilder addFormat(final ParserFormat format) {
    formats.add(format);
    return this;
  }

  /**
   * @return the parser using all the formats. if several formats are suitable
   *         for all tweets, the result will be of the first added format.
   */
  TweetsParser getResult() {
    return new MultiFormatsParser(formats);
  }
}
