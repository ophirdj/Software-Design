package ac.il.technion.twc.api;

import java.util.List;

import ac.il.technion.twc.api.core.TwitterSystemBuilder;
import ac.il.technion.twc.api.tweet.BaseTweet;
import ac.il.technion.twc.api.tweet.Retweet;

/**
 * A property for {@link TwitterSystemBuilder#addProperty(Class)} and
 * {@link TwitterSystemBuilder#addProperty(Class, PropertyFactory)}. <br>
 * <br>
 * 
 * A property is either: <br>
 * An object that has a constructor that accepts:
 * <code>({@link List}<{@link BaseTweet}>, {@link List}<{@link Retweet}>)</code><br>
 * - or - <br>
 * An object that was built using {@link PropertyFactory#get(List, List)}
 * method.
 * 
 * 
 * @author Ziv Ronen
 * @date 31.05.2014
 * @mail akarks@gmail.com
 * 
 */
public interface Property {

  /**
   * Indicate that the type doesn't have accessible constructor that get (
   * {@link List}<{@link BaseTweet}> , {@link List}<{@link Retweet}>)
   * 
   * @author Ziv Ronen
   * @date 30.05.2014
   * @mail akarks@gmail.com
   * 
   */
  public static class NotAPropertyException extends RuntimeException {

    private static final long serialVersionUID = 7277301716229432516L;

    /**
     * @param simpleName
     *          The name of the service
     */
    public NotAPropertyException(final String simpleName) {
      super(simpleName + " is not a property");
    }
  }

}
