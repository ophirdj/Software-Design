package ac.il.technion.twc.api.core;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ac.il.technion.twc.api.Property;
import ac.il.technion.twc.api.PropertyFactory;
import ac.il.technion.twc.api.TwitterDataCenter;
import ac.il.technion.twc.api.TwitterDataCenterBuilder;
import ac.il.technion.twc.api.TwitterQuery;
import ac.il.technion.twc.api.TwitterQueryFactory;

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
  public <T extends Property> TwitterDataCenterBuilder addProperty(
      final Class<T> type) {
    serviceBuilder.checkProperty(type);
    serviceBuilder.addProperty(type);
    return this;
  }

  @Override
  public <T extends Property, S extends T> TwitterDataCenterBuilder
      addProperty(final Class<T> propertyType,
          final PropertyFactory<S> propertyFactory) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public TwitterDataCenterBuilder
      addSerializer(final Serializer serialzerToAdd) {
    serializers.add(serialzerToAdd);
    return this;
  }

  @Override
  public <T extends TwitterQuery> TwitterDataCenterBuilder registerQuery(
      final Class<T> queryType) throws NotAServiceException,
      MissingPropertitesException {
    serviceBuilder.checkService(queryType);
    try {
      services.add(serviceBuilder.getInstance(queryType));
    } catch (final InstantiationException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (final IllegalAccessException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (final InvocationTargetException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return this;
  }

  @Override
  public <T extends TwitterQuery, S extends T> TwitterDataCenterBuilder
      registerQuery(final Class<T> queryType,
          final TwitterQueryFactory<S> queryFactory)
          throws NotAServiceException, MissingPropertitesException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public TwitterDataCenter build() {
    return new TwitterSystemHandler(services, serviceBuilder,
        new StorageFactory(serializers).buildStorage());
  }

}
