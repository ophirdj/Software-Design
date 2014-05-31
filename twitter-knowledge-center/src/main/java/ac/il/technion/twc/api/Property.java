package ac.il.technion.twc.api;

import java.util.List;

import ac.il.technion.twc.api.tweet.BaseTweet;
import ac.il.technion.twc.api.tweet.Retweet;

/**
 * A property for {@link TwitterDataCenterBuilder#addProperty(Class)} and
 * {@link TwitterDataCenterBuilder#addProperty(Class, PropertyFactory)}. <br>
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

}
