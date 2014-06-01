package ac.il.technion.twc.api.parser.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.text.ParseException;
import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import ac.il.technion.twc.api.tweet.Tweet;
import ac.il.technion.twc.api.tweet.parser.TweetFormat;
import ac.il.technion.twc.api.tweet.parser.TweetParser;

/**
 * Tests for {@link TweetParser}
 * 
 * @author Ziv Ronen
 * @date 28.05.2014
 * @mail akarks@gmail.com
 * 
 */
public class MultiFormatsParserTest {

  /**
	 * 
	 */
  @Rule
  public ExpectedException thrown = ExpectedException.none();

  private final TweetFormat format1 = mock(TweetFormat.class);
  private final TweetFormat format2 = mock(TweetFormat.class);
  private final TweetParser $;

  /**
   * 
   */
  public MultiFormatsParserTest() {
    $ = new TweetParser(format1, format2);
  }

  /**
   * Test method for: <br>
   * - {@link TweetParser#TweetParser(TweetFormat...)}
   */
  @Test
  public final void parserBuildSucceeded() {
    assertNotNull($);
  }

  /**
   * Test method for: <br>
   * - {@link TweetParser#parse(String...)}
   * 
   * @throws ParseException
   */
  @Test
  public final void oneValidParserShouldReturnParsedTweet()
      throws ParseException {
    final String tweetString = "this is my tweet!";
    final Tweet tweet = mock(Tweet.class);
    when(format1.parse(tweetString)).thenReturn(tweet);
    when(format2.parse(tweetString)).thenThrow(new ParseException("", 0));
    final List<Tweet> tweets = $.parse(tweetString);
    verify(format1).parse(tweetString);
    assertEquals(1, tweets.size());
    assertSame(tweet, tweets.get(0));
  }

  /**
   * Test method for: <br>
   * - {@link TweetParser#parse(String...)}
   * 
   * @throws ParseException
   */
  @Test
  public final void noValidParserShouldThrowException() throws ParseException {
    when(format1.parse(anyString())).thenThrow(new ParseException("", 0));
    when(format2.parse(anyString())).thenThrow(new ParseException("", 0));
    thrown.expect(ParseException.class);
    thrown.expectMessage("No matching parser");
    $.parse("tweet in wrong format");
  }

  /**
   * Test method for: <br>
   * - {@link TweetParser#parse(String...)}
   * 
   * @throws ParseException
   */
  @Test
  public final void
      tooManyValidParsersAfterParsingEndsShouldThrowAmbiguityException()
          throws ParseException {
    when(format1.parse(anyString())).thenReturn(mock(Tweet.class));
    when(format2.parse(anyString())).thenReturn(mock(Tweet.class));
    thrown.expect(ParseException.class);
    thrown.expectMessage("Parsing ambiguity");
    $.parse("tweet in right format for both parsers");
  }

  /**
   * Test method for: <br>
   * - {@link TweetParser#parse(String...)}
   * 
   * @throws ParseException
   */
  @Test
  public final void parsingNoTweetsShouldReturnAnEmptyList()
      throws ParseException {
    assertEquals(0, $.parse().size());
  }

}
