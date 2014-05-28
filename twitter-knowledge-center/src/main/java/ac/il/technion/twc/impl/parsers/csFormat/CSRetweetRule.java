package ac.il.technion.twc.impl.parsers.csFormat;

import java.text.ParseException;

import ac.il.technion.twc.api.parser.ParserFormat;
import ac.il.technion.twc.api.tweets.ID;
import ac.il.technion.twc.api.tweets.Retweet;
import ac.il.technion.twc.api.tweets.Tweet;

/**
 * Rule for parsing a retweet from comma separated format
 * 
 * @author Ziv Ronen
 * @date 23.05.2014
 * @mail akarks@gmail.com
 * 
 * @version 2.0
 * @since 2.0
 */
public class CSRetweetRule implements ParserFormat {

  @Override
  public Tweet parse(final String line) throws ParseException {
    if (!isFromFormat(line))
      throw new ParseException(line, 0);
    final String[] fields = CSFormatUtils.getFields(line);
    return new Retweet(CSFormatUtils.getDateFormat().parse(fields[0]), new ID(
        fields[1]), new ID(fields[2]));
  }

  @Override
  public boolean isFromFormat(final String line) {
    final String[] fields = CSFormatUtils.getFields(line);
    return fields != null && fields.length == 3
        && CSFormatUtils.isDate(fields[0]) && CSFormatUtils.isID(fields[1])
        && CSFormatUtils.isID(fields[2]);
  }

}
