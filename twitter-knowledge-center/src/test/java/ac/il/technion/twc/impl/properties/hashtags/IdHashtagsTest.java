package ac.il.technion.twc.impl.properties.hashtags;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.junit.Test;

import ac.il.technion.twc.api.tweets.BaseTweet;
import ac.il.technion.twc.api.tweets.ID;

/**
 * Tests for {@link IdHashtags} and {@link IdHashtagsBuilder}
 * 
 * @author Ziv Ronen
 * @date 28.05.2014
 * @mail akarks@gmail.com
 * 
 * @version 2.0
 * @since 2.0
 */
public class IdHashtagsTest {

	private final IdHashtagsBuilder $ = new IdHashtagsBuilder();

	@Test
	public final void noTweetsIdHashtagsShouldReturnEmptyList() {
		assertTrue($.getResult().getHashtags(new ID("doesn't exist")).isEmpty());
	}

	@Test
	public final void baseTweetWithHashtagsIdHashtagsShouldReturnHashtags() {
		final ID id = new ID("base tweet ID");
		final List<String> hashtags = Arrays.asList("YOLO", "SWAG",
				"KILLYOURSELF");
		final BaseTweet bt = new BaseTweet(new Date(123456789), id, hashtags);
		$.visit(bt);
		assertEquals(hashtags, $.getResult().getHashtags(id));
	}

	@Test
	public final void baseTweetWithoutHashtagsIdHashtagsShouldReturnEmptyList() {
		final ID id = new ID("base tweet ID");
		final BaseTweet bt = new BaseTweet(new Date(123456789), id);
		$.visit(bt);
		assertTrue($.getResult().getHashtags(id).isEmpty());
	}

	@Test
	public final void tweetsShouldNotInfluenceHashtagsOfOtherTweets() {
		final ID id = new ID("base tweet ID");
		final List<String> hashtags = Arrays.asList("YOLO", "SWAG",
				"KILLYOURSELF");
		final BaseTweet bt = new BaseTweet(new Date(123456789), id, hashtags);
		final ID otherId = new ID("other tweet ID");
		final List<String> otherHashtags = Arrays
				.asList("OMFG", "FUCKDAPOLICE");
		final BaseTweet another = new BaseTweet(new Date(123456789), otherId,
				otherHashtags);
		$.visit(bt);
		$.visit(another);
		final IdHashtags ih = $.getResult();
		assertEquals(hashtags, ih.getHashtags(id));
		assertEquals(otherHashtags, ih.getHashtags(otherId));
	}

}
