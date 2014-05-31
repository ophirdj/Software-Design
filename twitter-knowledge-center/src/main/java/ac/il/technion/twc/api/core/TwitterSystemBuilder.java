package ac.il.technion.twc.api.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ac.il.technion.twc.api.Property;
import ac.il.technion.twc.api.PropertyFactory;
import ac.il.technion.twc.api.TwitterDataCenter;
import ac.il.technion.twc.api.TwitterDataCenterBuilder;
import ac.il.technion.twc.api.TwitterQuery;
import ac.il.technion.twc.api.TwitterQueryFactory;
import ac.il.technion.twc.api.tweet.BaseTweet;
import ac.il.technion.twc.api.tweet.Retweet;

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

  private final Set<Class<? extends TwitterQuery>> services = new HashSet<>();
  private final List<Serializer> serializers = new ArrayList<>();

  @Override
  public <T extends Property> TwitterDataCenterBuilder addProperty(
      final Class<T> type) {
    serviceBuilder.addProperty(type);
    return this;
  }

  @Override
  public <T extends Property, S extends T> TwitterDataCenterBuilder
      addProperty(final Class<T> propertyType,
          final PropertyFactory<S> propertyFactory) {
    serviceBuilder.addProperty(propertyType, propertyFactory);
    return this;
  }

  @Override
  public TwitterDataCenterBuilder
      addSerializer(final Serializer serialzerToAdd) {
    serializers.add(serialzerToAdd);
    return this;
  }

  @Override
  public <T extends TwitterQuery> TwitterDataCenterBuilder registerQuery(
      final Class<T> queryType) throws TwitterQuery.NotAQueryException,
      MissingPropertitesException {
    serviceBuilder.addQuery(queryType);
    services.add(queryType);
    return this;
  }

  @Override
  public <T extends TwitterQuery, S extends T> TwitterDataCenterBuilder
      registerQuery(final Class<T> queryType,
          final TwitterQueryFactory<S> queryFactory)
          throws TwitterQuery.NotAQueryException, MissingPropertitesException {
    serviceBuilder.addQuery(queryType, queryFactory);
    services.add(queryType);
    return this;
  }

  @Override
  public TwitterDataCenter build() {
    serviceBuilder.setProperties(Collections.<BaseTweet> emptyList(),
        Collections.<Retweet> emptyList());
    return new TwitterSystemHandler(services, serviceBuilder,
        new StorageFactory(serializers).buildStorage());
  }

}
