package ac.il.technion.twc.example.mockClasses;

import ac.il.technion.twc.api.properties.PropertyBuilder;
import ac.il.technion.twc.api.tweets.BaseTweet;
import ac.il.technion.twc.api.tweets.Retweet;

public class Buidler1 implements PropertyBuilder<MyProperty> {

  @Override
  public Void visit(final BaseTweet t) {
    return null;
  }

  @Override
  public Void visit(final Retweet t) {
    return null;
  }

  @Override
  public void clear() {
    // TODO Auto-generated method stub

  }

  @Override
  public MyProperty getResult() {
    // TODO Auto-generated method stub
    return null;
  }

}
