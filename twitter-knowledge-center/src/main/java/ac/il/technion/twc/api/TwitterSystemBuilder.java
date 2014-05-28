package ac.il.technion.twc.api;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;

import ac.il.technion.twc.api.properties.PropertyBuilder;
import ac.il.technion.twc.api.properties.PropertyRetriever;
import ac.il.technion.twc.api.properties.PropertyRetrieverFactory;
import ac.il.technion.twc.api.storage.PersistanceStorage;

/**
 * Builder for {@link TwitterSystemHandler}
 * 
 * @author Ziv Ronen
 * @date 22.05.2014
 * @mail akarks@gmail.com
 * 
 * @version 2.0
 * @since 2.0
 */
public class TwitterSystemBuilder implements TwitterServicesCenterBuilder {

  private final List<PropertyBuilder<?>> builders = new ArrayList<>();
  private final PropertyRetrieverFactory factory;
  private final PersistanceStorage storage;
  private final ExecutorService threadPool;

  /**
   * @param threadPool
   * @param factory
   *          used for creating the PropertyRetriever when
   *          {@link TwitterSystemBuilder#registerBuilder} is called.
   * @param storage
   *          Will be used to store data in the created element. All the values
   *          build by this builder will use the same storage.
   */
  public TwitterSystemBuilder(final PropertyRetrieverFactory factory,
      final PersistanceStorage storage, final ExecutorService threadPool) {
    this.factory = factory;
    this.storage = storage;
    this.threadPool = threadPool;
  }

  @Override
  public <T> PropertyRetriever<T> registerBuilder(
      final PropertyBuilder<T> builder) {
    builders.add(builder);
    return factory.create(builder);
  }

  @Override
  public TwitterServicesCenter getResult() {
    return new TwitterSystemHandler(builders, storage, threadPool);
  }
}
