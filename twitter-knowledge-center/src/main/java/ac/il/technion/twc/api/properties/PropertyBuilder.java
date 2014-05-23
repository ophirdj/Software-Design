package ac.il.technion.twc.api.properties;

import ac.il.technion.twc.api.MessageVisitor;
import ac.il.technion.twc.api.tweets.BaseTweet;
import ac.il.technion.twc.api.tweets.Retweet;

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
public interface PropertyBuilder<T> extends MessageVisitor<Void> {

  /**
   * Note: must not throw any exception!
   */
  @Override
  Void visit(BaseTweet t);

  /**
   * Note: must not throw any exception!
   */
  @Override
  Void visit(Retweet t);

  /**
   * return the builder to basic state
   */
  void clear();

  /**
   * @return An object representing the property.
   */
  T getResult();
}
