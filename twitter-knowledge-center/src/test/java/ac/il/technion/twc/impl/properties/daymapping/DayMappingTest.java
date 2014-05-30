package ac.il.technion.twc.impl.properties.daymapping;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Date;

import org.junit.Test;

import ac.il.technion.twc.api.tweets.BaseTweet;
import ac.il.technion.twc.api.tweets.ID;
import ac.il.technion.twc.api.tweets.Retweet;

/**
 * Tests for {@link DayMapping} and {@link DayMappingBuilder}
 * 
 * @author Ziv Ronen
 * @date 28.05.2014
 * @mail akarks@gmail.com
 * 
 * @version 2.0
 * @since 2.0
 */
public class DayMappingTest {

	private final DayMappingBuilder $ = new DayMappingBuilder();

	/**
	 * Test method for {@link DayMapping#getAllDaysBase()}
	 */
	@Test
	public final void noVisitsGetAllDaysBaseShouldHave0Entries() {
		assertTrue($.getResult().getAllDaysBase().isEmpty());
	}

	/**
	 * Test method for {@link DayMapping#getAllDaysRe()}
	 */
	@Test
	public final void noVisitsGetAllDaysReShouldHave0Entries() {
		assertTrue($.getResult().getAllDaysRe().isEmpty());
	}

	/**
	 * Test method for {@link DayMapping#getAllDaysBase()}
	 */
	@Test
	public final void visit1BaseTweetGetAllDaysBaseShouldHave1TweetAtThisDate() {
		final Date date = new Date(123456789);
		final BaseTweet bt = new BaseTweet(date, new ID("base tweet ID"));
		$.visit(bt);
		assertEquals(1, $.getResult().getAllDaysBase().iterator().next()
				.getValue().intValue());
	}

	/**
	 * Test method for {@link DayMapping#getAllDaysBase()}
	 */
	@Test
	public final void visit1BaseTweetGetAllDaysBaseShouldHaveATweetAtCorrectDate() {
		final Date date = new Date(123456789);
		final BaseTweet bt = new BaseTweet(date, new ID("base tweet ID"));
		$.visit(bt);
		assertEquals(date.getTime(), $.getResult().getAllDaysBase().iterator()
				.next().getKey().longValue());
	}

	/**
	 * Test method for {@link DayMapping#getAllDaysBase()}
	 */
	@Test
	public final void visit1BaseTweetGetAllDaysBaseShouldHave1Entry() {
		final Date date = new Date(123456789);
		final BaseTweet bt = new BaseTweet(date, new ID("base tweet ID"));
		$.visit(bt);
		assertEquals(1, $.getResult().getAllDaysBase().size());
	}

	/**
	 * Test method for {@link DayMapping#getAllDaysRe()}
	 */
	@Test
	public final void visit1RetweetGetAllDaysReShouldHave1TweetAtThisDate() {
		final Date date = new Date(123456789);
		final Retweet rt = new Retweet(date, new ID("retweet ID"), new ID(
				"base tweet ID"));
		$.visit(rt);
		assertEquals(1, $.getResult().getAllDaysRe().iterator().next()
				.getValue().intValue());
	}

	/**
	 * Test method for {@link DayMapping#getAllDaysRe()}
	 */
	@Test
	public final void visit1RetweetGetAllDaysReShouldHaveATweetAtCorrectDate() {
		final Date date = new Date(123456789);
		final Retweet rt = new Retweet(date, new ID("retweet ID"), new ID(
				"base tweet ID"));
		$.visit(rt);
		assertEquals(date.getTime(), $.getResult().getAllDaysRe().iterator()
				.next().getKey().longValue());
	}

	/**
	 * Test method for {@link DayMapping#getAllDaysRe()}
	 */
	@Test
	public final void visit1RetweetGetAllDaysReShouldHave1Entry() {
		final Date date = new Date(123456789);
		final Retweet rt = new Retweet(date, new ID("retweet ID"), new ID(
				"base tweet ID"));
		$.visit(rt);
		assertEquals(1, $.getResult().getAllDaysRe().size());
	}

	/**
	 * Test method for {@link DayMapping#getAllDaysBase()}
	 */
	@Test
	public final void visit2BaseTweetsInSameDateGetAllDaysBaseShouldHave2TweetsAtThisDate() {
		final Date date = new Date(123456789);
		final BaseTweet bt1 = new BaseTweet(date, new ID("base tweet 1 ID"));
		final BaseTweet bt2 = new BaseTweet(date, new ID("base tweet 2 ID"));
		$.visit(bt1);
		$.visit(bt2);
		assertEquals(2, $.getResult().getAllDaysBase().iterator().next()
				.getValue().intValue());
	}

	/**
	 * Test method for {@link DayMapping#getAllDaysBase()}
	 */
	@Test
	public final void visit2BaseTweetsInDifferentDatesGetAllDaysBaseShouldHave2Entries() {
		final BaseTweet bt1 = new BaseTweet(new Date(123456789), new ID(
				"base tweet 1 ID"));
		final BaseTweet bt2 = new BaseTweet(new Date(987654321), new ID(
				"base tweet 2 ID"));
		$.visit(bt1);
		$.visit(bt2);
		assertEquals(2, $.getResult().getAllDaysBase().size());
	}

	/**
	 * Test method for {@link DayMapping#getAllDaysRe()}
	 */
	@Test
	public final void visit2RetweetsInSameDateGetAllDaysReShouldHave2TweetAtThisDate() {
		final Date date = new Date(123456789);
		final Retweet rt1 = new Retweet(date, new ID("retweet 1 ID"), new ID(
				"base tweet ID"));
		final Retweet rt2 = new Retweet(date, new ID("retweet 2 ID"), new ID(
				"base tweet ID"));
		$.visit(rt1);
		$.visit(rt2);
		assertEquals(2, $.getResult().getAllDaysRe().iterator().next()
				.getValue().intValue());
	}

	/**
	 * Test method for {@link DayMapping#getAllDaysRe()}
	 */
	@Test
	public final void visit2RetweetsInDifferentDatesGetAllDaysReShouldHave2Entries() {
		final Retweet rt1 = new Retweet(new Date(123456789), new ID(
				"retweet 1 ID"), new ID("base tweet ID"));
		final Retweet rt2 = new Retweet(new Date(987654321), new ID(
				"retweet 2 ID"), new ID("base tweet ID"));
		$.visit(rt1);
		$.visit(rt2);
		assertEquals(2, $.getResult().getAllDaysRe().size());
	}

	/**
	 * Test method for {@link DayMapping#getAllDaysBase()},
	 * {@link DayMapping#getAllDaysRe()}
	 */
	@Test
	public final void visitRetweetAndBaseTweetInSameDateGetAllDaysReShouldHave1EntryAndGetAllDaysBaseShouldHave1Entry() {
		final Date date = new Date(123456789);
		final BaseTweet bt = new BaseTweet(date, new ID("base tweet 1 ID"));
		final Retweet rt = new Retweet(date, new ID("retweet ID"), new ID(
				"base tweet ID"));
		$.visit(bt);
		$.visit(rt);
		final DayMapping dm = $.getResult();
		assertEquals(1, dm.getAllDaysBase().size());
		assertEquals(1, dm.getAllDaysRe().size());
	}

}
