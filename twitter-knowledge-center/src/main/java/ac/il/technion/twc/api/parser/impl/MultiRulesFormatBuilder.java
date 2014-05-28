package ac.il.technion.twc.api.parser.impl;

import java.util.ArrayList;
import java.util.List;

import ac.il.technion.twc.api.parser.ParserFormat;

/**
 * Builder for {@link MultiRulesFormat}
 * 
 * @author Ziv Ronen
 * @date 22.05.2014
 * @mail akarks@gmail.com
 * 
 * @version 2.0
 * @since 2.0
 */
public class MultiRulesFormatBuilder {

  private final List<ParserFormat> rules = new ArrayList<>();

  /**
   * @param rule
   *          a new rule for the formatter
   * @return the builder
   */
  public MultiRulesFormatBuilder addRule(final ParserFormat rule) {
    rules.add(rule);
    return this;
  }

  /**
   * @return a new {@link MultiRulesFormat}, which parse using the rules by the
   *         order {@link MultiRulesFormatBuilder#addRule(ParserFormat)} was
   *         called.
   */
  public ParserFormat getResult() {
    return new MultiRulesFormat(rules);
  }

}
