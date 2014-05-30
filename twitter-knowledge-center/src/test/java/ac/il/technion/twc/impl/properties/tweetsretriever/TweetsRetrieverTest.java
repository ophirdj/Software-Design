package ac.il.technion.twc.impl.properties.tweetsretriever;

import static org.junit.Assert.assertEquals;

import java.util.Date;
import java.util.List;

import org.junit.Test;

import ac.il.technion.twc.api.tweets.BaseTweet;
import ac.il.technion.twc.api.tweets.ID;
import ac.il.technion.twc.api.tweets.Retweet;

/**
 * Test for {@link TweetsRetriever} and {@link TweetsRetrieverBuilder}
 * 
 * @author Ziv Ronen
 * @date 28.05.2014
 * @mail akarks@gmail.com
 * 
 * @version 2.0
 * @since 2.0
 */
public class TweetsRetrieverTest {

	private final TweetsRetrieverBuilder $ = new TweetsRetrieverBuilder();

	@Test
	public final void getBaseTweetsShouldReturnBaseTweet() {
		final BaseTweet bt = new BaseTweet(new Date(123456789), new ID(
				"base tweet ID"));
		$.visit(bt);
		assertEquals(bt, $.getResult().getBaseTweets().get(0));
	}

	@Test
	public final void getRetweetsShouldReturnRetweet() {
		final Retweet rt = new Retweet(new Date(123456789),
				new ID("retweet ID"), new ID("base tweet ID"));
		$.visit(rt);
		assertEquals(rt, $.getResult().getRetweets().get(0));
	}

	@Test
	public final void getRetweetsShouldReturnNoRetweets() {
		final BaseTweet bt = new BaseTweet(new Date(123456789), new ID(
				"base tweet ID"));
		$.visit(bt);
		assertEquals(0, $.getResult().getRetweets().size());
	}

	@Test
	public final void getBaseTweetsShouldReturnNoBaseTweets() {
		final Retweet rt = new Retweet(new Date(123456789),
				new ID("retweet ID"), new ID("base tweet ID"));
		$.visit(rt);
		assertEquals(0, $.getResult().getBaseTweets().size());
	}

	@Test
	public final void getBaseTweetsShouldReturnBaseTweetsByReceiveOrder() {
		final BaseTweet bt1 = new BaseTweet(new Date(123456789), new ID(
				"base tweet 1 ID"));
		final BaseTweet bt2 = new BaseTweet(new Date(123456789), new ID(
				"base tweet 2 ID"));
		$.visit(bt1);
		$.visit(bt2);
		final List<BaseTweet> tweets = $.getResult().getBaseTweets();
		assertEquals(bt1, tweets.get(0));
		assertEquals(bt2, tweets.get(1));
	}

	@Test
	public final void getRetweetsShouldReturnRetweetsByReceiveOrder() {
		final Retweet rt1 = new Retweet(new Date(123456789), new ID(
				"retweet 1 ID"), new ID("base tweet ID"));
		final Retweet rt2 = new Retweet(new Date(123456789), new ID(
				"retweet 2 ID"), new ID("base tweet ID"));
		$.visit(rt1);
		$.visit(rt2);
		final List<Retweet> tweets = $.getResult().getRetweets();
		assertEquals(rt1, tweets.get(0));
		assertEquals(rt2, tweets.get(1));
	}

	@Test
	public final void getBaseTweetsShouldReturnBaseTweetAndGetRetweetsShouldReturnRetweet() {
		final BaseTweet bt = new BaseTweet(new Date(123456789), new ID(
				"base tweet ID"));
		final Retweet rt = new Retweet(new Date(123456789),
				new ID("retweet ID"), new ID("base tweet ID"));
		$.visit(bt);
		$.visit(rt);
		final TweetsRetriever tr = $.getResult();
		assertEquals(bt, tr.getBaseTweets().get(0));
		assertEquals(rt, tr.getRetweets().get(0));
	}

}
