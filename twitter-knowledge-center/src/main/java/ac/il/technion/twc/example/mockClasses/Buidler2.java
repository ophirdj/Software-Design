package ac.il.technion.twc.example.mockClasses;

import ac.il.technion.twc.api.properties.PropertyBuilder;
import ac.il.technion.twc.message.tweet.BaseTweet;
import ac.il.technion.twc.message.tweet.Retweet;

public class Buidler2 implements PropertyBuilder<MyOtherProperty> {

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
  public MyOtherProperty getResult() {
    // TODO Auto-generated method stub
    return null;
  }

}
