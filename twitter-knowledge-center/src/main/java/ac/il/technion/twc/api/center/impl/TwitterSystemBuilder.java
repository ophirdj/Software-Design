package ac.il.technion.twc.api.center.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executors;

import ac.il.technion.twc.api.center.TwitterServicesCenter;
import ac.il.technion.twc.api.center.TwitterServicesCenterBuilder;
import ac.il.technion.twc.api.properties.PropertyBuilder;

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
  private final ServiceBuildingManager serviceBuilder =
      new ServiceBuildingManager();

  private final Set<Object> services = new HashSet<>();
  private final List<Serializer> serializers = new ArrayList<>();

  @Override
  public <T> TwitterServicesCenterBuilder addProperty(final Class<T> type) {
    serviceBuilder.checkProperty(type);
    serviceBuilder.addProperty(type);
    return this;
  }

  @Override
  public TwitterServicesCenterBuilder addSerializer(
      final Serializer serialzerToAdd) {
    serializers.add(serialzerToAdd);
    return this;
  }

  @Override
  public <T> TwitterServicesCenterBuilder registerService(final T service)
      throws NotAServiceException, MissingPropertitesException {
    serviceBuilder.checkService(service.getClass());
    services.add(service);
    return this;
  }

  @Override
  public <T, ST extends T> TwitterServicesCenterBuilder setDefaultValue(
      final Class<T> type, final ST value) {
    serviceBuilder.addPredfineValue(type, value);
    return this;
  }

  @Override
  public TwitterServicesCenter getResult() {
    return new TwitterSystemHandler(builders, services, serviceBuilder,
        new StorageFactory(serializers).buildStorage(),
        Executors.newFixedThreadPool(1 + services.size()));
  }

}
