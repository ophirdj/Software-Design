package ac.il.technion.twc.impl.properties;

import java.util.HashMap;
import java.util.Map;

import ac.il.technion.twc.api.properties.PropertyBuilder;
import ac.il.technion.twc.api.tweets.BaseTweet;
import ac.il.technion.twc.api.tweets.ID;
import ac.il.technion.twc.api.tweets.Retweet;

/**
 * a {@link PropertyBuilder} for {@link TransitiveRootFinder}
 * 
 * @author Ziv Ronen
 * @date 23.05.2014
 * @mail akarks@gmail.com
 * 
 * @version 2.0
 * @since 2.0
 */
public class TransitivityBuilder implements
    PropertyBuilder<TransitiveRootFinder> {

  private final Map<ID, ID> relation = new HashMap<ID, ID>();
  private final Map<ID, BaseTweet> baseTweets = new HashMap<ID, BaseTweet>();

  @Override
  public Void visit(final BaseTweet t) {
    baseTweets.put(t.id(), t);
    return null;
  }

  @Override
  public Void visit(final Retweet t) {
    relation.put(t.id(), t.originId);
    return null;
  }

  @Override
  public void clear() {
    relation.clear();
    baseTweets.clear();
  }

  // TODO: create new instance
  @Override
  public TransitiveRootFinder getResult() {
    return new TransitiveRootFinder(relation, baseTweets);
  }

}
