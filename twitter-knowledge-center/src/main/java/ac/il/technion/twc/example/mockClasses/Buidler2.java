package ac.il.technion.twc.example.mockClasses;

import ac.il.technion.twc.api.properties.PropertyBuilder;
import ac.il.technion.twc.message.tweet.BaseTweet;
import ac.il.technion.twc.message.tweet.Retweet;

public class Buidler2 implements PropertyBuilder<MyOtherProperty> {

  @Override
  public void visit(BaseTweet t) {
    // TODO Auto-generated method stub

  }

  @Override
  public void visit(Retweet t) {
    // TODO Auto-generated method stub

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
