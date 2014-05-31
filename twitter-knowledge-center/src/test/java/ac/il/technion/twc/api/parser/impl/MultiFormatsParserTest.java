package ac.il.technion.twc.api.parser.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.text.ParseException;
import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import ac.il.technion.twc.api.tweet.Tweet;
import ac.il.technion.twc.api.tweet.parser.MultiFormatsParserBuilder;
import ac.il.technion.twc.api.tweet.parser.MultiFormatsTweetParser;
import ac.il.technion.twc.api.tweet.parser.ParseFormat;
import ac.il.technion.twc.api.tweet.parser.TweetParser;

/**
 * Tests for {@link MultiFormatsTweetParser} and
 * {@link MultiFormatsParserBuilder}
 * 
 * @author Ziv Ronen
 * @date 28.05.2014
 * @mail akarks@gmail.com
 * 
 * @version 2.0
 * @since 2.0
 */
public class MultiFormatsParserTest {

  /**
	 * 
	 */
  @Rule
  public ExpectedException thrown = ExpectedException.none();

  private final ParseFormat format1 = mock(ParseFormat.class);
  private final ParseFormat format2 = mock(ParseFormat.class);
  private final TweetParser $;

  /**
   * 
   */
  public MultiFormatsParserTest() {
    $ = new MultiFormatsParserBuilder().add(format1).add(format2).build();
  }

  /**
   * Test method for: <br>
   * - {@link MultiFormatsParserBuilder#add(ParseFormat)} <br>
   * - {@link MultiFormatsParserBuilder#build()} <br>
   * - {@link MultiFormatsParserBuilder#MultiFormatsParserBuilder()}
   * 
   */
  @Test
  public final void parserBuildSucceeded() {
    assertNotNull($);
  }

  /**
   * Test method for: <br>
   * - {@link MultiFormatsTweetParser#parse(String...)}
   * 
   * @throws ParseException
   */
  @Test
  public final void oneValidParserShouldCallValidParser() throws ParseException {
    final String tweetString = "this is my tweet!";
    final Tweet tweet = mock(Tweet.class);
    when(format1.isFromFormat(tweetString)).thenReturn(Boolean.TRUE);
    when(format1.parse(tweetString)).thenReturn(tweet);
    when(format2.isFromFormat(tweetString)).thenReturn(Boolean.FALSE);
    final List<Tweet> tweets = $.parse(tweetString);
    verify(format1).isFromFormat(tweetString);
    verify(format1).parse(tweetString);
    assertEquals(1, tweets.size());
    assertSame(tweet, tweets.get(0));
  }

  /**
   * Test method for: <br>
   * - {@link MultiFormatsTweetParser#parse(String...)}
   * 
   * @throws ParseException
   */
  @Test
  public final void oneValidParserShouldNotCallInValidParser()
      throws ParseException {
    final String tweetString = "this is my tweet!";
    when(format1.isFromFormat(tweetString)).thenReturn(Boolean.TRUE);
    when(format1.parse(tweetString)).thenReturn(mock(Tweet.class));
    when(format2.isFromFormat(tweetString)).thenReturn(Boolean.FALSE);
    $.parse(tweetString);
    verify(format2, never()).parse(anyString());
  }

  /**
   * Test method for: <br>
   * - {@link MultiFormatsTweetParser#parse(String...)}
   * 
   * @throws ParseException
   */
  @Test
  public final void noValidParserShouldThrowException() throws ParseException {
    when(format1.isFromFormat(anyString())).thenReturn(Boolean.FALSE);
    when(format2.isFromFormat(anyString())).thenReturn(Boolean.FALSE);
    thrown.expect(ParseException.class);
    thrown.expectMessage("No matching parser");
    $.parse("tweet in wrong format");
  }

  /**
   * Test method for: <br>
   * - {@link MultiFormatsTweetParser#parse(String...)}
   * 
   * @throws ParseException
   */
  @Test
  public final void
      tooManyValidParsersAfterParsingEndsShouldThrowAmbiguityException()
          throws ParseException {
    when(format1.isFromFormat(anyString())).thenReturn(Boolean.TRUE);
    when(format2.isFromFormat(anyString())).thenReturn(Boolean.TRUE);
    when(format1.parse(anyString())).thenReturn(mock(Tweet.class));
    when(format2.parse(anyString())).thenReturn(mock(Tweet.class));
    thrown.expect(ParseException.class);
    thrown.expectMessage("Parsing ambiguity");
    $.parse("tweet in right format for both parsers");
  }

  /**
   * Test method for: <br>
   * - {@link MultiFormatsTweetParser#parse(String...)}
   * 
   * @throws ParseException
   */
  @Test
  public final void parsingNoTweetsShouldReturnAnEmptyList()
      throws ParseException {
    assertEquals(0, $.parse().size());
  }

}
