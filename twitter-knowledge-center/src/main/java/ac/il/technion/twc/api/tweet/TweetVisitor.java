package ac.il.technion.twc.api.tweet;


/**
 * Visitor design pattern for messages (for adding new behaviors)
 * 
 * @author Ziv Ronen
 * @date 07.05.2014
 * @mail akarks@gmail.com
 * 
 * @version 2.0
 * @since 2.0
 * @param <T>
 *          return value
 */
public interface TweetVisitor<T> {

  /**
   * @param t
   *          a base tweet
   * @return any needed value
   */
  T visit(BaseTweet t);

  /**
   * @param t
   *          a retweet
   * @return any needed value
   */
  T visit(Retweet t);

}
