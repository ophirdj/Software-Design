package ac.il.technion.twc.impl.parser;

import java.text.ParseException;
import java.util.List;

import ac.il.technion.twc.message.tweet.Tweet;

public class MultiRulesFormat implements ParserFormat {

  private final List<ParserFormat> rules;

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
