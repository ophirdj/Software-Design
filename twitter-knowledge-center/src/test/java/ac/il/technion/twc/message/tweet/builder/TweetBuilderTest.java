package ac.il.technion.twc.message.tweet.builder;

import static org.junit.Assert.assertEquals;

import java.text.ParseException;
import java.util.GregorianCalendar;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import ac.il.technion.twc.God;
import ac.il.technion.twc.message.ID;
import ac.il.technion.twc.message.tweet.BaseTweet;
import ac.il.technion.twc.message.tweet.Retweet;
import ac.il.technion.twc.message.tweet.Tweet;

public class TweetBuilderTest {
	
	public final @Rule ExpectedException thrown = ExpectedException.none();

	
	private final TweetBuilder underTest = God.injector.getInstance(TweetBuilder.class);
	
	@Test
	public final void parseBaseTweetSouldReturnBaseTweet() throws ParseException {
		Tweet tweet = underTest.parse("15/05/2013 13:08:07, 334611141902872576");
		assertEquals(new BaseTweet(new GregorianCalendar(2013, 4, 15, 13, 8, 7).getTime(), new ID("334611141902872576")), tweet);
	}

	@Test
	public final void parseRetweetSouldReturnRetweet() throws ParseException {
		Tweet tweet = underTest.parse("15/05/2013 05:23:07, 334611141890285568, 334611004342280192");
		assertEquals(new Retweet(new GregorianCalendar(2013, 4, 15, 5, 23, 7).getTime(), new ID("334611141890285568"), new ID("334611004342280192")), tweet);
	}
	
	@Test
	public final void parseWithNoIdShouldThrowParseException() throws ParseException {
		String tweet = "15/05/2013 13:08:07";
		thrown.expect(ParseException.class);
		thrown.expectMessage("Bad tweet format: " + tweet);
		underTest.parse(tweet);
	}
	
	@Test
	public final void parseWith3IdsShouldThrowParseException() throws ParseException {
		String tweet = "15/05/2013 13:08:07, 334611141902872576, 334611141902872577, 334611141902872578";
		thrown.expect(ParseException.class);
		thrown.expectMessage("Bad tweet format: " + tweet);
		underTest.parse(tweet);
	}
	
	@Test
	public final void parseWithNoDateShouldThrowParseException() throws ParseException {
		String tweet = "334611141902872576, 334611141902872577";
		thrown.expect(ParseException.class);
		thrown.expectMessage("Bad tweet format: " + tweet);
		underTest.parse(tweet);
	}
	
	@Test
	public final void parseTweetAtMidnight00SouldReturnTweet() throws ParseException {
		Tweet tweet = underTest.parse("15/05/2013 00:00:00, 334611141902872576");
		assertEquals(new BaseTweet(new GregorianCalendar(2013, 4, 15, 0, 0, 0).getTime(), new ID("334611141902872576")), tweet);
	}

}
