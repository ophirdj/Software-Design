package ac.il.technion.twc.impl.parser;

import java.util.ArrayList;
import java.util.List;

public class ParserImplBuilder {

  List<ParserFormat> formats = new ArrayList<>();

  ParserImplBuilder addFormat(final ParserFormat format) {
    formats.add(format);
    return this;
  }

  ParserImpl getResult() {
    return new ParserImpl(formats);
  }
}
