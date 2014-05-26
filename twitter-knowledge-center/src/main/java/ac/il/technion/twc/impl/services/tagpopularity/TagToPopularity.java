package ac.il.technion.twc.impl.services.tagpopularity;

import java.util.HashMap;
import java.util.Map;

import ac.il.technion.twc.FuntionalityTester;
import ac.il.technion.twc.api.tweets.BaseTweet;
import ac.il.technion.twc.api.tweets.Retweet;
import ac.il.technion.twc.impl.properties.IdHashtags;
import ac.il.technion.twc.impl.properties.TransitiveRootFinder;
import ac.il.technion.twc.impl.properties.TransitiveRootFinder.NoRootFoundException;
import ac.il.technion.twc.impl.properties.TweetsRetriever;

/**
 * Service that answer {@link FuntionalityTester#getHashtagPopularity(String)}
 * query
 * 
 * @author Ziv Ronen
 * @date 26.05.2014
 * @mail akarks@gmail.com
 * 
 * @version 2.0
 * @since 2.0
 */
public class TagToPopularity {

  private final Map<String, Integer> popularityFromHashtag =
      new HashMap<String, Integer>();

  private TagToPopularity() {

  }

  /**
   * @param baseTweetFinder
   * @param tweets
   * @param hashtags
   */
  public TagToPopularity(final TransitiveRootFinder baseTweetFinder,
      final TweetsRetriever tweets, final IdHashtags hashtags) {
    for (final Retweet retweet : tweets.getRetweets())
      try {
        final BaseTweet base = baseTweetFinder.findRoot(retweet);
        for (final String tag : base.hashtags())
          popularityFromHashtag.put(tag, getPopularityByHashtag(tag) + 1);
      } catch (final NoRootFoundException e) {
        continue;
      }

  }

  /**
   * @param hashtag
   * @return The amount of retweets to base tweets with given hashtag.
   */
  public Integer getPopularityByHashtag(final String hashtag) {
    if (!popularityFromHashtag.containsKey(hashtag))
      return 0;
    return popularityFromHashtag.get(hashtag);
  }

  /**
   * @return An empty {@link TagToPopularity}
   */
  public static TagToPopularity empty() {
    return new TagToPopularity();
  }
}
