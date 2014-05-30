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

import ac.il.technion.twc.api.parser.ParserFormat;
import ac.il.technion.twc.api.parser.TweetsParser;
import ac.il.technion.twc.api.tweets.Tweet;

/**
 * Tests for {@link MultiFormatsTweetsParser} and
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

  private final ParserFormat format1 = mock(ParserFormat.class);
  private final ParserFormat format2 = mock(ParserFormat.class);
  private final TweetsParser $;

  /**
   * 
   */
  public MultiFormatsParserTest() {
    $ = new MultiFormatsParserBuilder().add(format1).add(format2).build();
  }

  /**
   * Test method for: <br>
   * - {@link MultiFormatsParserBuilder#add(ParserFormat)} <br>
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
   * - {@link MultiFormatsTweetsParser#parse(String...)}
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
   * - {@link MultiFormatsTweetsParser#parse(String...)}
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
   * - {@link MultiFormatsTweetsParser#parse(String...)}
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
   * - {@link MultiFormatsTweetsParser#parse(String...)}
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
   * - {@link MultiFormatsTweetsParser#parse(String...)}
   * 
   * @throws ParseException
   */
  @Test
  public final void parsingNoTweetsShouldReturnAnEmptyList()
      throws ParseException {
    assertEquals(0, $.parse().size());
  }

}
