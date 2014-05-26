package ac.il.technion.twc.impl.properties;

import java.util.ArrayList;
import java.util.List;

import ac.il.technion.twc.api.properties.PropertyBuilder;
import ac.il.technion.twc.api.tweets.BaseTweet;
import ac.il.technion.twc.api.tweets.Retweet;

/**
 * A {@link PropertyBuilder} for {@link TweetsRetriever}
 * 
 * @author Ziv Ronen
 * @date 26.05.2014
 * @mail akarks@gmail.com
 * 
 * @version 2.0
 * @since 2.0
 */
public class TweetsRetrieverBuilder implements PropertyBuilder<TweetsRetriever> {

  private final List<BaseTweet> baseTweets = new ArrayList<>();
  private final List<Retweet> retweets = new ArrayList<>();;

  @Override
  public Void visit(final BaseTweet t) {
    baseTweets.add(t);
    return null;
  }

  @Override
  public Void visit(final Retweet t) {
    retweets.add(t);
    return null;
  }

  @Override
  public void clear() {
    baseTweets.clear();
    retweets.clear();
  }

  @Override
  public TweetsRetriever getResult() {
    return new TweetsRetriever(baseTweets, retweets);
  }

}
