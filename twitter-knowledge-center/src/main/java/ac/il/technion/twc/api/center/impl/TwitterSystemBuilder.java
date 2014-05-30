package ac.il.technion.twc.api.center.impl;

import java.lang.reflect.Constructor;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;

import ac.il.technion.twc.api.center.TwitterServicesCenter;
import ac.il.technion.twc.api.center.TwitterServicesCenterBuilder;
import ac.il.technion.twc.api.properties.PropertyBuilder;
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
  private final Set<Class<?>> supportedProperties = new HashSet<>();

  private final Set<Object> services = new HashSet<>();
  private final PersistanceStorage storage;
  private final ExecutorService threadPool;

  /**
   * @param threadPool
   * @param storage
   *          Will be used to store data in the created element. All the values
   *          build by this builder will use the same storage.
   */
  public TwitterSystemBuilder(final PersistanceStorage storage,
      final ExecutorService threadPool) {
    this.storage = storage;
    this.threadPool = threadPool;
  }

  @Override
  public <T> TwitterServicesCenterBuilder registerBuilder(final Class<T> type,
      final PropertyBuilder<T> builder) {
    supportedProperties.add(type);
    builders.add(builder);
    return this;
  }

  @Override
  public TwitterServicesCenter getResult() {
    return new TwitterSystemHandler(builders, services, storage, threadPool);
  }

  @Override
  public <T> TwitterServicesCenterBuilder registerService(final T service)
      throws NotAServiceException, MissingPropertitesException {
    ServiceBuildingManager
        .checkService(service.getClass(), supportedProperties);
    services.add(service);
    return this;
  }

  /**
   * If a service require a property that is not register
   * 
   * @author Ziv Ronen
   * @date 29.05.2014
   * @mail akarks@gmail.com
   * 
   * @version 2.0
   * @since 2.0
   */
  public static class MissingPropertitesException extends Exception {

    public MissingPropertitesException(final String string) {
      super(string);
    }

    /**
     * 
     */
    private static final long serialVersionUID = -2955615443853602756L;

  }
}
