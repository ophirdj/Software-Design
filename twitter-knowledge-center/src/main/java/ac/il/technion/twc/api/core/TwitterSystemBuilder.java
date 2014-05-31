package ac.il.technion.twc.api.core;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ac.il.technion.twc.api.TwitterDataCenter;
import ac.il.technion.twc.api.TwitterDataCenterBuilder;

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
public class TwitterSystemBuilder implements TwitterDataCenterBuilder {

  private final ServiceBuildingManager serviceBuilder =
      new ServiceBuildingManager();

  private final Set<Object> services = new HashSet<>();
  private final List<Serializer> serializers = new ArrayList<>();

  @Override
  public <T> TwitterDataCenterBuilder addProperty(final Class<T> type) {
    serviceBuilder.checkProperty(type);
    serviceBuilder.addProperty(type);
    return this;
  }

  @Override
  public TwitterDataCenterBuilder addSerializer(
      final Serializer serialzerToAdd) {
    serializers.add(serialzerToAdd);
    return this;
  }

  @Override
  public <T> TwitterDataCenterBuilder registerService(final T service)
      throws NotAServiceException, MissingPropertitesException {
    serviceBuilder.checkService(service.getClass());
    services.add(service);
    return this;
  }

  @Override
  public <T, ST extends T> TwitterDataCenterBuilder setDefaultValue(
      final Class<T> type, final ST value) {
    serviceBuilder.addPredfineValue(type, value);
    return this;
  }

  @Override
  public TwitterDataCenter build() {
    return new TwitterSystemHandler(services, serviceBuilder,
        new StorageFactory(serializers).buildStorage());
  }

}
