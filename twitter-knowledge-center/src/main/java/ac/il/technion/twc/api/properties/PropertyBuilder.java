package ac.il.technion.twc.api.properties;

import ac.il.technion.twc.message.tweet.BaseTweet;
import ac.il.technion.twc.message.tweet.Retweet;

/**
 * Build a property form the tweets
 * 
 * @author Ziv Ronen
 * @date 22.05.2014
 * @mail akarks@gmail.com
 * 
 * @version 2.0
 * @since 2.0
 * @param <T>
 *          The type of the property
 */
public interface PropertyBuilder<T> {

  /**
   * @param t
   *          a base tweet
   */
  void visit(BaseTweet t);

  /**
   * @param t
   *          a retweet
   */
  void visit(Retweet t);

  /**
   * return the builder to basic state
   */
  void clear();

  /**
   * @return An object representing the property.
   */
  T getResult();
}
