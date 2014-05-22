package ac.il.technion.twc.example;

import ac.il.technion.twc.api.properties.PropertyBuilder;
import ac.il.technion.twc.message.tweet.BaseTweet;
import ac.il.technion.twc.message.tweet.Retweet;

public class Buidler1 implements PropertyBuilder<MyProperty> {

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
  public MyProperty getResult() {
    // TODO Auto-generated method stub
    return null;
  }

}
