package ac.il.technion.twc.impl.api.parser;

import java.text.ParseException;
import java.util.List;

import ac.il.technion.twc.api.tweets.Tweet;

/**
 * Format that have multiple rules. Useful for adding new kinds of tweets with
 * the same format
 * 
 * @author Ziv Ronen
 * @date 22.05.2014
 * @mail akarks@gmail.com
 * 
 * @version 2.0
 * @since 2.0
 */
public class MultiRulesFormat implements ParserFormat {

  private final List<ParserFormat> rules;

  /**
   * @param rules
   */
  public MultiRulesFormat(final List<ParserFormat> rules) {
    this.rules = rules;
  }

  @Override
  public Tweet parse(final String line) throws ParseException {
    for (final ParserFormat rule : rules)
      if (rule.isFromFormat(line))
        return rule.parse(line);
    throw new ParseException(line, 0);
  }

  @Override
  public boolean isFromFormat(final String line) {
    for (final ParserFormat rule : rules)
      if (rule.isFromFormat(line))
        return true;
    return false;
  }

}
