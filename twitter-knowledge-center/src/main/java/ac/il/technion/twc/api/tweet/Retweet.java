package ac.il.technion.twc.api.tweet;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Ophir De Jager
 * 
 */
public class Retweet extends Tweet {

  /**
   * ID of tweet that is being retweeted.
   */
  public final ID prevId;
  /**
   * ID of original (base) tweet that is being retweeted.
   */
  public final ID originId;
  /**
   * The hashtags of the original (base) tweet
   */
  public final List<String> originHashtags;

  /**
   * @param date
   *          Time when tweet was published.
   * @param id
   *          Unique ID for this tweet.
   * @param prevId
   *          ID of original tweet that is being retweeted.
   */
  public Retweet(final Date date, final ID id, final ID prevId) {
    this(date, id, prevId, new ArrayList<String>(), null, null);
  }

  /**
   * @param date
   *          Time when tweet was published.
   * @param id
   *          Unique ID for this tweet.
   * @param prevId
   *          ID of original tweet that is being retweeted.
   * @param hashtags
   *          The hastags of the tweet
   * @param originId
   *          The id of the original tweet
   * @param originHashtags
   *          The hastags of the original tweet
   */
  public Retweet(final Date date, final ID id, final ID prevId,
      final List<String> hashtags, final ID originId,
      final List<String> originHashtags) {
    super(date, id, hashtags);
    this.prevId = prevId;
    this.originId = originId;
    this.originHashtags = originHashtags;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = super.hashCode();
    result =
        prime * result
            + (originHashtags == null ? 0 : originHashtags.hashCode());
    result = prime * result + (originId == null ? 0 : originId.hashCode());
    result = prime * result + (prevId == null ? 0 : prevId.hashCode());
    return result;
  }

  @Override
  public boolean equals(final Object obj) {
    if (this == obj)
      return true;
    if (!super.equals(obj))
      return false;
    if (getClass() != obj.getClass())
      return false;
    final Retweet other = (Retweet) obj;
    if (originHashtags == null) {
      if (other.originHashtags != null)
        return false;
    } else if (!originHashtags.equals(other.originHashtags))
      return false;
    if (originId == null) {
      if (other.originId != null)
        return false;
    } else if (!originId.equals(other.originId))
      return false;
    if (prevId == null) {
      if (other.prevId != null)
        return false;
    } else if (!prevId.equals(other.prevId))
      return false;
    return true;
  }

}
