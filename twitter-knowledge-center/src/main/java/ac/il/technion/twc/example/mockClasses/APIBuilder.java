package ac.il.technion.twc.example.mockClasses;

import ac.il.technion.twc.api.TwitterServicesCenter;
import ac.il.technion.twc.api.TwitterServicesCenterBuilder;
import ac.il.technion.twc.api.properties.PropertyBuilder;
import ac.il.technion.twc.api.properties.PropertyRetriever;

public class APIBuilder implements TwitterServicesCenterBuilder {

  @Override
  public <T> PropertyRetriever<T> registerBuilder(
      final PropertyBuilder<T> builder) {
    return null;
  }

  @Override
  public TwitterServicesCenter getResult() {
    return null;
  }

}
