package ac.il.technion.twc.impl.properties.hashtags;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ac.il.technion.twc.api.Property;
import ac.il.technion.twc.api.tweet.BaseTweet;
import ac.il.technion.twc.api.tweet.ID;
import ac.il.technion.twc.api.tweet.Retweet;

/**
 * Retrieve the hashtags of a base tweet from its id
 * 
 * @author Ziv Ronen
 * @date 26.05.2014
 * @mail akarks@gmail.com
 */
public class IdHashtags implements Property {

  private final Map<ID, List<String>> HashtagById;

  /**
   * @param bases
   * @param res
   */
  public IdHashtags(final List<BaseTweet> bases, final List<Retweet> res) {
    final Map<ID, List<String>> tagsById = new HashMap<ID, List<String>>();
    for (final Retweet retweet : res)
      if (retweet.originId != null && retweet.originHashtags != null)
        tagsById.put(retweet.originId, retweet.originHashtags);
    for (final BaseTweet tweet : bases)
      if (tweet.hashtags != null)
        tagsById.put(tweet.id, tweet.hashtags);
    HashtagById = Collections.unmodifiableMap(tagsById);
  }

  /**
   * @param id
   * @return All the hashtags of an id if exist or empty list otherwise
   */
  public List<String> getHashtags(final ID id) {
    return HashtagById.containsKey(id) ? Collections
        .unmodifiableList(HashtagById.get(id)) : Collections
        .<String> emptyList();
  }

}
