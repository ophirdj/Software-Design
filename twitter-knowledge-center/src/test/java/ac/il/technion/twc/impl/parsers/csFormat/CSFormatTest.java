package ac.il.technion.twc.impl.parsers.csFormat;

import static junitparams.JUnitParamsRunner.$;
import static org.junit.Assert.assertEquals;

import java.text.ParseException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;

import ac.il.technion.twc.api.tweet.BaseTweet;
import ac.il.technion.twc.api.tweet.ID;
import ac.il.technion.twc.api.tweet.Retweet;
import ac.il.technion.twc.api.tweet.Tweet;
import ac.il.technion.twc.api.tweet.parser.TweetFormat;
import ac.il.technion.twc.impl.parser.CommaSeparatedTweetFormat;

/**
 * Tests for comma separated format.
 * 
 * @author Ophir De Jager
 * 
 */
@RunWith(JUnitParamsRunner.class)
public class CSFormatTest {

  /**
   * Rule for verifying correct error messages of thrown exceptions.
   */
  public final @Rule
  ExpectedException thrown = ExpectedException.none();

  private final TweetFormat $ = new CommaSeparatedTweetFormat();

  @SuppressWarnings("unused")
  // used by JunitParams
      private
      Object[] correctTweets() {
    final Calendar gc = new GregorianCalendar();
    gc.setTimeZone(TimeZone.getTimeZone("UTC"));
    gc.setTimeInMillis(0L);
    gc.set(2013, 4, 15, 13, 8, 7);
    final BaseTweet baseOne =
        new BaseTweet(gc.getTime(), new ID("334611141902872576"));
    gc.set(2013, 4, 15, 0, 0, 0);
    final BaseTweet baseTwo =
        new BaseTweet(gc.getTime(), new ID("33461114190"));
    gc.set(2013, 4, 15, 5, 23, 7);
    final Retweet retweet =
        new Retweet(gc.getTime(), new ID("334611141890285568"), new ID(
            "334611004342280192"));
    return $(
        $(baseOne, "15/05/2013 13:08:07, 334611141902872576"),
        $(retweet,
            "15/05/2013 05:23:07, 334611141890285568, 334611004342280192"),
        $(baseTwo, "15/05/2013 00:00:00, 33461114190"));
  }

  /**
   * Test method for {@link TweetFormat#parse(String)}.
   * 
   * @param expected
   * @param tweetStr
   * @throws ParseException
   */
  @Parameters(method = "correctTweets")
  @Test
  public void parseCorrect(final Tweet expected, final String tweetStr)
      throws ParseException {
    assertEquals(expected, $.parse(tweetStr));
  }

  @SuppressWarnings("unused")
  // used by JunitParams
      private
      Object[] badTweets() {
    return $(
        $((String) null),
        $("15/05/2013 13:08:07"),
        $("15/05/2013 13:08:07, 334611141902872576, 334611141902872577, 334611141902872578"),
        $("334611141902872576, 334611141902872577"),
        $("15/05/2013 13:08:07, 334611141902872577, 1321321321, "),
        $(", 334611141902872577"), $("334611141902872576, "));
  }

  /**
   * Test method for {@link TweetFormat#parse(String)}.
   * 
   * @param tweetStr
   * @throws ParseException
   */
  @Parameters(method = "badTweets")
  @Test
  public void parseBad(final String tweetStr) throws ParseException {
    thrown.expect(ParseException.class);
    thrown.expectMessage("Bad tweet format: " + tweetStr);
    $.parse(tweetStr);
  }

}
