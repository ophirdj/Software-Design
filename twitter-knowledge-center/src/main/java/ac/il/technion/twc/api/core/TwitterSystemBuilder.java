package ac.il.technion.twc.api.core;

import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;

import ac.il.technion.twc.api.Property;
import ac.il.technion.twc.api.PropertyFactory;
import ac.il.technion.twc.api.TwitterDataCenter;
import ac.il.technion.twc.api.TwitterQuery;
import ac.il.technion.twc.api.TwitterQuery.NotAQueryException;
import ac.il.technion.twc.api.TwitterQueryFactory;
import ac.il.technion.twc.api.TwitterQuerySerializer;

/**
 * Builder for {@link TwitterSystem}
 * 
 * @author Ziv Ronen
 * @date 22.05.2014
 * @mail akarks@gmail.com
 * 
 * @version 2.0
 * @since 2.0
 */
public class TwitterSystemBuilder {

  private final ServiceBuildingManager serviceBuilder =
      new ServiceBuildingManager();
  private final Set<Class<? extends TwitterQuery>> services = new HashSet<>();
  private final StorageBuilder storageBuilder;

  /**
   * @param path
   *          The path for storing
   */
  public TwitterSystemBuilder(final Path path) {
    storageBuilder = new StorageBuilder(path);
  }

  /**
   * Add a property to the system. All properties will be calculated when
   * {@link TwitterDataCenter#importData(java.util.Collection)} is called.
   * 
   * 
   * @param propertyType
   *          The type of the property. see {@link Property} for details.
   * @return this {@link TwitterSystemBuilder}.
   */
  public <T extends Property> TwitterSystemBuilder addProperty(
      final Class<T> propertyType) {
    serviceBuilder.addProperty(propertyType);
    return this;
  }

  /**
   * Add a property to the system. All properties will be calculated when
   * {@link TwitterDataCenter#importData(java.util.Collection)} is called.
   * 
   * 
   * @param propertyType
   *          The type of the property. see {@link Property} for details.
   * @param propertyFactory
   *          factory for the given property
   * @return this {@link TwitterSystemBuilder}.
   */
  public <T extends Property, S extends T> TwitterSystemBuilder addProperty(
      final Class<T> propertyType, final PropertyFactory<S> propertyFactory) {
    serviceBuilder.addProperty(propertyType, propertyFactory);
    return this;
  }

  /**
   * @param serialzerToAdd
   *          a {@link TwitterQuerySerializer} for any class that can't be
   *          automatically serialize. When an object of the given type is
   *          stored, we will use the given serializer.
   * @return a reference to this object.
   */
  public <T> TwitterSystemBuilder addSerializer(
      final TwitterQuerySerializer<T> serialzerToAdd) {
    storageBuilder.addSerializer(serialzerToAdd);
    return this;
  }

  /**
   * 
   * @param queryType
   *          The type of the query to be register. see {@link TwitterQuery} for
   *          details.
   * 
   * @return this {@link TwitterSystemBuilder}.
   * @throws TwitterQuery.NotAQueryException
   *           if the object is not a legal service
   * @throws MissingPropertitesException
   *           if any of the property required for the service can't be
   *           obtained.
   */
  public <T extends TwitterQuery> TwitterSystemBuilder registerQuery(
      final Class<T> queryType) throws NotAQueryException,
      MissingPropertitesException {
    serviceBuilder.addQuery(queryType);
    services.add(queryType);
    return this;
  }

  /**
   * 
   * @param queryType
   *          The type of the query to be register. see {@link TwitterQuery} for
   *          details.
   * @param queryFactory
   *          factory for the given query. see {@link TwitterQueryFactory} for
   *          details.
   * 
   * @return this {@link TwitterSystemBuilder}.
   * @throws TwitterQuery.NotAQueryException
   *           if the object is not a legal service
   * @throws MissingPropertitesException
   *           if any of the property required for the service can't be
   *           obtained.
   */
  public <T extends TwitterQuery, S extends T> TwitterSystemBuilder
      registerQuery(final Class<T> queryType,
          final TwitterQueryFactory<S> queryFactory)
          throws TwitterQuery.NotAQueryException, MissingPropertitesException {
    serviceBuilder.addQuery(queryType, queryFactory);
    services.add(queryType);
    return this;
  }

  /**
   * @return A {@link TwitterDataCenter}.
   */
  public TwitterSystem build() {
    return new TwitterSystem(services, serviceBuilder,
        storageBuilder.buildStorage());
  }

  /**
   * If a service require a property that is not register
   * 
   * @author Ziv Ronen
   * @date 29.05.2014
   * @mail akarks@gmail.com
   * 
   */
  public static class MissingPropertitesException extends RuntimeException {

    /**
     * @param string
     *          the message to be displayed
     */
    public MissingPropertitesException(final String string) {
      super(string);
    }

    private static final long serialVersionUID = -2955615443853602756L;
  }

  /**
   * If a no query was register
   * 
   * @author Ziv Ronen
   * @date 29.05.2014
   * @mail akarks@gmail.com
   * 
   */
  public static class NoRegisterQueryException extends RuntimeException {

    /**
     * 
     */
    private static final long serialVersionUID = -4840175866315170447L;

  }

}
