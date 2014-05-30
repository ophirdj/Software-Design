package ac.il.technion.twc.impl.properties.hashtags;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.junit.Test;

import ac.il.technion.twc.api.tweets.BaseTweet;
import ac.il.technion.twc.api.tweets.ID;
import ac.il.technion.twc.api.tweets.Retweet;

/**
 * Tests for {@link IdHashtags}
 * 
 * @author Ziv Ronen
 * @date 28.05.2014
 * @mail akarks@gmail.com
 * 
 * @version 2.0
 * @since 2.0
 */
public class IdHashtagsTest {

  private static final ArrayList<Retweet> EMPTY_RES = new ArrayList<Retweet>();
  private static final ArrayList<BaseTweet> EMPTY_BASES =
      new ArrayList<BaseTweet>();

  /**
   * Test method for {@link IdHashtags#getHashtags(ID)}
   */
  @Test
  public final void noTweetsIdHashtagsShouldReturnEmptyList() {
    assertTrue(new IdHashtags(EMPTY_BASES, EMPTY_RES).getHashtags(
        new ID("doesn't exist")).isEmpty());
  }

  /**
   * Test method for {@link IdHashtags#getHashtags(ID)}
   */
  @Test
  public final void baseTweetWithHashtagsIdHashtagsShouldReturnHashtags() {
    final ID id = new ID("base tweet ID");
    final List<String> hashtags = Arrays.asList("YOLO", "SWAG", "KILLYOURSELF");
    assertEquals(
        hashtags,
        new IdHashtags(Arrays.asList(new BaseTweet(new Date(123456789), id,
            hashtags)), EMPTY_RES).getHashtags(id));
  }

  /**
   * Test method for {@link IdHashtags#getHashtags(ID)}
   */
  @Test
  public final void baseTweetWithoutHashtagsIdHashtagsShouldReturnEmptyList() {
    final ID id = new ID("base tweet ID");
    assertTrue(new IdHashtags(Arrays.asList(new BaseTweet(new Date(123456789),
        id)), EMPTY_RES).getHashtags(id).isEmpty());
  }

  /**
   * Test method for {@link IdHashtags#getHashtags(ID)}
   */
  @Test
  public final void tweetsShouldNotInfluenceHashtagsOfOtherTweets() {
    final ID id = new ID("base tweet ID");
    final List<String> hashtags = Arrays.asList("YOLO", "SWAG", "KILLYOURSELF");
    final ID otherId = new ID("other tweet ID");
    final List<String> otherHashtags = Arrays.asList("OMFG", "FUCKDAPOLICE");
    final List<BaseTweet> bts =
        Arrays.asList(new BaseTweet(new Date(123456789), id, hashtags),
            new BaseTweet(new Date(123456789), otherId, otherHashtags));
    final IdHashtags ih = new IdHashtags(bts, EMPTY_RES);
    assertEquals(hashtags, ih.getHashtags(id));
    assertEquals(otherHashtags, ih.getHashtags(otherId));
  }

}
