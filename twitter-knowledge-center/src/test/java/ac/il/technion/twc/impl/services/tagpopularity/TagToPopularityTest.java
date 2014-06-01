package ac.il.technion.twc.impl.services.tagpopularity;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.junit.Test;

import ac.il.technion.twc.api.properties.OriginFinder;
import ac.il.technion.twc.api.properties.OriginFinder.NotFoundException;
import ac.il.technion.twc.api.properties.TweetsRetriever;
import ac.il.technion.twc.api.tweet.BaseTweet;
import ac.il.technion.twc.api.tweet.ID;
import ac.il.technion.twc.api.tweet.Retweet;
import ac.il.technion.twc.impl.properties.hashtags.IdHashtags;

/**
 * Test for {@link TagToPopularity}
 * 
 * @author Ziv Ronen
 * @date 28.05.2014
 * @mail akarks@gmail.com
 */
public class TagToPopularityTest {

  private final OriginFinder trf = mock(OriginFinder.class);
  private final TweetsRetriever tr = mock(TweetsRetriever.class);
  private final IdHashtags ih = mock(IdHashtags.class);

  /**
   * Test method for {@link TagToPopularity#TagToPopularity()}
   */
  @Test
  public void emptyLifeTimeShouldReturnAllZerosWithDefaultConstructor() {
    assertEquals(0, new TagToPopularity().getPopularityByHashtag("SWAG"));
  }

  /**
   * Test method for {@link TagToPopularity#TagToPopularity()}
   */
  @Test
  public final void
      emptyLifeTimeShouldReturnAllZerosAlsoWithAnnotatedConstructor() {
    when(tr.getRetweets()).thenReturn(Collections.<Retweet> emptyList());
    assertEquals(0,
        new TagToPopularity(trf, tr, ih).getPopularityByHashtag("SWAG"));
  }

  /**
   * Test method for {@link TagToPopularity#TagToPopularity()}
   * 
   * @throws NotFoundException
   */
  @Test
  public final void popularityOfHashagsOfBaseTweetWithSingleRetweetShouldBe1()
      throws NotFoundException {
    final List<String> hashtags = Arrays.asList("YOLO", "SWAG", "KILLYOURSELF");
    final BaseTweet bt =
        new BaseTweet(new Date(123456789), new ID("base"), hashtags);
    final Retweet rt =
        new Retweet(new Date(987654321), new ID("retweet"), new ID("base"));
    when(tr.getRetweets()).thenReturn(Arrays.asList(rt));
    when(trf.origin(rt)).thenReturn(bt);
    final TagToPopularity $ = new TagToPopularity(trf, tr, ih);
    for (final String hashtag : hashtags)
      assertEquals(1, $.getPopularityByHashtag(hashtag));
  }

  /**
   * Test method for {@link TagToPopularity#TagToPopularity()}
   * 
   * @throws NotFoundException
   */
  @Test
  public final
      void
      popularityOfHashagWithNoBaseTweetsShouldBe0EvenIfThereAreOtherHashtagsWithPositivePopularity()
          throws NotFoundException {
    final List<String> hashtags = Arrays.asList("YOLO", "SWAG", "KILLYOURSELF");
    final BaseTweet bt =
        new BaseTweet(new Date(123456789), new ID("base"), hashtags);
    final Retweet rt =
        new Retweet(new Date(987654321), new ID("retweet"), new ID("base"));
    when(tr.getRetweets()).thenReturn(Arrays.asList(rt));
    when(trf.origin(rt)).thenReturn(bt);
    final TagToPopularity $ = new TagToPopularity(trf, tr, ih);
    assertEquals(0, $.getPopularityByHashtag("OMFG"));
  }

  /**
   * Test method for {@link TagToPopularity#TagToPopularity()}
   * 
   * @throws NotFoundException
   */
  @Test
  public final void
      popularityOfHashagsOfBaseTweetWithRetweetsShouldBeNumberOfRetweets()
          throws NotFoundException {
    final List<String> hashtags = Arrays.asList("YOLO", "SWAG", "KILLYOURSELF");
    final BaseTweet bt =
        new BaseTweet(new Date(123456789), new ID("base"), hashtags);
    final Retweet rt1 =
        new Retweet(new Date(987654321), new ID("retweet 1"), new ID("base"));
    final Retweet rt2 =
        new Retweet(new Date(987654321), new ID("retweet 2"), new ID("base"));
    when(tr.getRetweets()).thenReturn(Arrays.asList(rt1, rt2));
    when(trf.origin(rt1)).thenReturn(bt);
    when(trf.origin(rt2)).thenReturn(bt);
    final TagToPopularity $ = new TagToPopularity(trf, tr, ih);
    for (final String hashtag : hashtags)
      assertEquals(2, $.getPopularityByHashtag(hashtag));
  }

  /**
   * Test method for {@link TagToPopularity#TagToPopularity()}
   * 
   * @throws NotFoundException
   */
  @Test
  public final
      void
      popularityOfHashagsOfDifferentBaseTweetsWithSameHashtagsShouldBeNumberOfRetweetsOfBothBaseTweets()
          throws NotFoundException {
    final List<String> hashtags = Arrays.asList("YOLO", "SWAG", "KILLYOURSELF");
    final BaseTweet bt1 =
        new BaseTweet(new Date(123456789), new ID("base 1"), hashtags);
    final BaseTweet bt2 =
        new BaseTweet(new Date(123456789), new ID("base 2"), Arrays.asList(
            "YOLO", "SWAG", "SWAG", "KILLYOURSELF"));
    final Retweet rt1 =
        new Retweet(new Date(987654321), new ID("retweet 1"), new ID("base 1"));
    final Retweet rt2 =
        new Retweet(new Date(987654321), new ID("retweet 2"), new ID("base 2"));
    when(tr.getRetweets()).thenReturn(Arrays.asList(rt1, rt2));
    when(trf.origin(rt1)).thenReturn(bt1);
    when(trf.origin(rt2)).thenReturn(bt2);
    final TagToPopularity $ = new TagToPopularity(trf, tr, ih);
    for (final String hashtag : hashtags)
      assertEquals(2, $.getPopularityByHashtag(hashtag));
  }

}
