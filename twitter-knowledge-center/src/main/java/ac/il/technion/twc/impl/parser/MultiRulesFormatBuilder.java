package ac.il.technion.twc.impl.parser;

import java.util.ArrayList;
import java.util.List;

public class MultiRulesFormatBuilder {

  private final List<ParserFormat> rules = new ArrayList<>();

  public MultiRulesFormatBuilder addRule(final ParserFormat format) {
    rules.add(format);
    return this;
  }

  public ParserFormat getResult() {
    return new MultiRulesFormat(rules);
  }

}
