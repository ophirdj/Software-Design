package ac.il.technion.twc.impl.parsers.jsonFormat;

import static junitparams.JUnitParamsRunner.$;
import static org.junit.Assert.assertEquals;

import java.text.ParseException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;

import ac.il.technion.twc.TestDataReader;
import ac.il.technion.twc.api.parser.ParserFormat;
import ac.il.technion.twc.api.tweets.BaseTweet;
import ac.il.technion.twc.api.tweets.ID;
import ac.il.technion.twc.api.tweets.Retweet;
import ac.il.technion.twc.api.tweets.Tweet;

/**
 * Tests for json format.
 * 
 * @author Ophir De Jager
 * 
 */
@RunWith(JUnitParamsRunner.class)
public class JsonFormatTest {

  /**
   * Rule for verifying correct error messages of thrown exceptions.
   */
  public final @Rule
  ExpectedException thrown = ExpectedException.none();

  private final ParserFormat underTest = new JsonTweetFormat();

  @SuppressWarnings("unused")
  // used by JunitParams
      private
      Object[] correctTweets() {
    final List<String> hashtags = Arrays.asList("Avi", "technion", "HARD"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    return $(
        $(new BaseTweet(buildDate(2014, 3, 6, 13, 0, 0), new ID("593393706")), //$NON-NLS-1$
            TestDataReader.getString("BaseTweet1")), //$NON-NLS-1$
        $(new Retweet(buildDate(2014, 4, 11, 10, 8, 8), new ID(
            "334611146097188865"), new ID("593393706")), //$NON-NLS-1$ //$NON-NLS-2$
            TestDataReader.getString("Retweet1")), //$NON-NLS-1$
        $(new BaseTweet(buildDate(2014, 3, 6, 13, 0, 0), new ID("593393706"), //$NON-NLS-1$
            hashtags), TestDataReader.getString("BaseTweet2"))); //$NON-NLS-1$
  }

  /**
   * Test method for {@link ParserFormat#parse(String)}.
   * 
   * @param expected
   * @param tweetStr
   * @throws ParseException
   */
  @Parameters(method = "correctTweets")
  @Test
  public void parseCorrect(final Tweet expected, final String tweetStr)
      throws ParseException {
    assertEquals(expected, underTest.parse(tweetStr));
  }

  @SuppressWarnings("unused")
  // used by JunitParams
      private
      Object[] badTweets() {
    // while TestDataReader.getString("NonJson2") return "" it behave
    // differently then $(""). If we change (in the format) line.equals("") to
    // line == "" the first fail end the second pass
    return $($((String) null), $(TestDataReader.getString("NonJson1")), $(""),
        $(TestDataReader.getString("NonJson2")),
        $(TestDataReader.getString("NoCreateDate")),
        $(TestDataReader.getString("NoId")),
        $(TestDataReader.getString("NoText")));

  }

  /**
   * Test method for {@link ParserFormat#parse(String)}.
   * 
   * @param tweetStr
   * @throws ParseException
   */
  @Parameters(method = "badTweets")
  @Test
  public void parseBad(final String tweetStr) throws ParseException {
    thrown.expect(ParseException.class);
    thrown.expectMessage("Bad tweet format: " + tweetStr); //$NON-NLS-1$
    underTest.parse(tweetStr);
  }

  private Date buildDate(final int year, final int month, final int date,
      final int hourOfDay, final int minute, final int second) {
    final Calendar gmt0 = new GregorianCalendar(TimeZone.getTimeZone("GMT")); //$NON-NLS-1$
    gmt0.setTimeInMillis(0);
    gmt0.set(year, month, date, hourOfDay, minute, second);
    return gmt0.getTime();
  }

}