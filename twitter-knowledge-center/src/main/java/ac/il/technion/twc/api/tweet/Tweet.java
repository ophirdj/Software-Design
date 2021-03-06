package ac.il.technion.twc.api.tweet;

import java.util.Date;
import java.util.List;

/**
 * 
 * @author Ophir De Jager
 * 
 */
public abstract class Tweet {

  private final Long date;
  /**
   * Message's ID.
   */
  public final ID id;
  /**
   * The hastags in the tweet text
   */
  public final List<String> hashtags;

  /**
   * @param date
   *          Time when tweet was published.
   * @param id
   *          Unique ID for this tweet.
   */
  public Tweet(final Date date, final ID id) {
    this(date, id, null);
  }

  /**
   * @param date
   *          Time when tweet was published.
   * @param id
   *          Unique ID for this tweet.
   * @param hashtags
   *          any hashtag the tweet has
   */
  public Tweet(final Date date, final ID id, final List<String> hashtags) {
    this.date = date.getTime();
    this.id = id;
    this.hashtags = hashtags;
  }

  /**
   * @return Time when tweet was published.
   */
  public Date date() {
    return new Date(date);
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + (date == null ? 0 : date.hashCode());
    result = prime * result + (hashtags == null ? 0 : hashtags.hashCode());
    result = prime * result + (id == null ? 0 : id.hashCode());
    return result;
  }

  @Override
  public boolean equals(final Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    final Tweet other = (Tweet) obj;
    if (date == null) {
      if (other.date != null)
        return false;
    } else if (!date.equals(other.date))
      return false;
    if (hashtags == null) {
      if (other.hashtags != null)
        return false;
    } else if (!hashtags.equals(other.hashtags))
      return false;
    if (id == null) {
      if (other.id != null)
        return false;
    } else if (!id.equals(other.id))
      return false;
    return true;
  }

  @Override
  public String toString() {
    return "Tweet [date=" + date + ", id=" + id + ", hashtags=" + hashtags
        + "]";
  }

}
