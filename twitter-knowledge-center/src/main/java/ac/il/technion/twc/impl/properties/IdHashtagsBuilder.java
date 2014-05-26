package ac.il.technion.twc.impl.properties;

import java.util.List;
import java.util.Map;

import ac.il.technion.twc.api.properties.PropertyBuilder;
import ac.il.technion.twc.api.tweets.BaseTweet;
import ac.il.technion.twc.api.tweets.ID;
import ac.il.technion.twc.api.tweets.Retweet;

/**
 * a {@link PropertyBuilder} for {@link IdHashtags}
 * 
 * @author Ziv Ronen
 * @date 26.05.2014
 * @mail akarks@gmail.com
 * 
 * @version 2.0
 * @since 2.0
 */
public class IdHashtagsBuilder implements PropertyBuilder<IdHashtags> {

  private Map<ID, List<String>> tagsById;

  @Override
  public Void visit(final BaseTweet t) {
    tagsById.put(t.id(), t.hashtags());
    return null;
  }

  @Override
  public Void visit(final Retweet t) {
    return null;
  }

  @Override
  public void clear() {
    tagsById.clear();
  }

  @Override
  public IdHashtags getResult() {
    return new IdHashtags(tagsById);
  }

}
