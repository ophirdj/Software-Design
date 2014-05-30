package ac.il.technion.twc.api.center.impl;

import java.util.ArrayList;
import java.util.Arrays;
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
  private final ServiceBuildingManager serviceBuilding =
      new ServiceBuildingManager();

  private final Set<Object> services = new HashSet<>();
  private final List<Serializer> serializers = new ArrayList<>();

  @Override
  public <T> TwitterServicesCenterBuilder registerBuilder(final Class<T> type,
      final PropertyBuilder<T> builder) {
    serviceBuilding.addProperty(type);
    builders.add(builder);
    return this;
  }

  @Override
  public TwitterServicesCenterBuilder addSerializers(
      final Serializer... serialzersToAdd) {
    serializers.addAll(Arrays.asList(serialzersToAdd));
    return this;
  }

  @Override
  public <T> TwitterServicesCenterBuilder registerService(final T service)
      throws NotAServiceException, MissingPropertitesException {
    serviceBuilding.checkService(service.getClass());
    services.add(service);
    return this;
  }

  @Override
  public <T, ST extends T> TwitterServicesCenterBuilder registerTypeValue(
      final Class<T> type, final ST value) {
    serviceBuilding.addPredfineValue(type, value);
    return this;
  }

  @Override
  public TwitterServicesCenter getResult() {
    return new TwitterSystemHandler(builders, services, serviceBuilding,
        new StorageFactory(serializers).buildStorage(),
        Executors.newFixedThreadPool(services.size()));
  }

}
