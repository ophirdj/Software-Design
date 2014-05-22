package ac.il.technion.twc.example.mockClasses;

import ac.il.technion.twc.api.TwitterServicesCenter;
import ac.il.technion.twc.api.TwitterServicesCenterBuilder;
import ac.il.technion.twc.api.properties.PropertyBuilder;
import ac.il.technion.twc.api.properties.PropertyRetriever;

public class APIBuilder implements TwitterServicesCenterBuilder {

  @Override
  public <T> PropertyRetriever<T> registerBuilder(PropertyBuilder<T> builder) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public TwitterServicesCenter getResult() {
    // TODO Auto-generated method stub
    return null;
  }

}
