package ac.il.technion.twc.impl.system;

import java.util.ArrayList;
import java.util.List;

import ac.il.technion.twc.api.PersistanceStorage;
import ac.il.technion.twc.api.TwitterServicesCenter;
import ac.il.technion.twc.api.TwitterServicesCenterBuilder;
import ac.il.technion.twc.api.properties.PropertyBuilder;
import ac.il.technion.twc.api.properties.PropertyRetriever;
import ac.il.technion.twc.impl.properties.PropertyRetrieverFactory;

public class TwitterSystemBuilder implements TwitterServicesCenterBuilder {

  private final List<PropertyBuilder<?>> builders = new ArrayList<>();
  private final PropertyRetrieverFactory factory;
  private final PersistanceStorage storage;

  public TwitterSystemBuilder(final PropertyRetrieverFactory factory,
      final PersistanceStorage storage) {
    this.factory = factory;
    this.storage = storage;
  }

  @Override
  public <T> PropertyRetriever<T> registerBuilder(
      final PropertyBuilder<T> builder) {
    builders.add(builder);
    return factory.create(builder);
  }

  @Override
  public TwitterServicesCenter getResult() {
    return new TwitterSystemHandler(builders, storage);
  }
}
