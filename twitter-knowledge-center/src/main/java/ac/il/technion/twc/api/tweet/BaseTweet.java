package ac.il.technion.twc.api.tweet;

import java.util.Date;
import java.util.List;

/**
 * Tweet that is not a retwwet.
 * 
 * @author Ophir De Jager
 * 
 */
public class BaseTweet extends Tweet {

  /**
   * @param date
   * @param id
   */
  public BaseTweet(final Date date, final ID id) {
    super(date, id);
  }

  /**
   * @param date
   * @param id
   * @param hashtags
   */
  public BaseTweet(final Date date, final ID id, final List<String> hashtags) {
    super(date, id, hashtags);
  }

}
