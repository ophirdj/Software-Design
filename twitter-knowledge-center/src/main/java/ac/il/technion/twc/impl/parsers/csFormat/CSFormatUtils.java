package ac.il.technion.twc.impl.parsers.csFormat;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;

import ac.il.technion.twc.api.parser.impl.MultiRulesFormatBuilder;
import ac.il.technion.twc.api.tweets.ID;

/**
 * Help functions for comma separated format
 * 
 * @author Ziv Ronen
 * @date 23.05.2014
 * @mail akarks@gmail.com
 * 
 * @version 2.0
 * @since 2.0
 */
public class CSFormatUtils {

  private static final SimpleDateFormat simpleDateFormat =
      new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

  private CSFormatUtils() {
    // static class
  }

  /**
   * @param line
   *          string representing a comma separated object
   * @return the corresponding tweet
   */
  public static String[] getFields(final String line) {
    final String[] f = (line + ", $").split(", ");
    return Arrays.copyOf(f, f.length - 1);
  }

  /**
   * @return Formatter for date
   */
  public static DateFormat getDateFormat() {
    return simpleDateFormat;
  }

  // XXX: Maybe return the format and not a builder for it?
  /**
   * @return a builder for cs tweet parser
   */
  public static MultiRulesFormatBuilder getTweetFormatBuilder() {
    final MultiRulesFormatBuilder $ = new MultiRulesFormatBuilder();
    $.addRule(new CSBaseTweetRule());
    $.addRule(new CSRetweetRule());
    return $;
  }

  /**
   * @param field
   * @return true if the given field represent date
   */
  public static boolean isDate(final String field) {
    try {
      simpleDateFormat.parse(field);
      return true;
    } catch (final ParseException e) {
      return false;
    }
  }

  /**
   * @param field
   * @return true if the given field represent id
   */
  public static boolean isID(final String field) {
    return ID.isID(field);
  }
}
