package ac.il.technion.twc.message.visitor;

import ac.il.technion.twc.message.tweet.BaseTweet;
import ac.il.technion.twc.message.tweet.Retweet;

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
public interface MessageVisitor<T> {

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
