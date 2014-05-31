package ac.il.technion.twc.api;

import java.util.List;

import ac.il.technion.twc.api.core.Serializer;
import ac.il.technion.twc.api.tweet.BaseTweet;
import ac.il.technion.twc.api.tweet.Retweet;

/**
 * Build our main API.
 * 
 * @author Ziv Ronen
 * @date 22.05.2014
 * @mail akarks@gmail.com
 * 
 * @version 2.0
 * @since 2.0
 */
// TODO: find Shorter name
public interface TwitterDataCenterBuilder {

  /**
   * Add a property to the system. All properties will be calculated when
   * {@link TwitterDataCenter#importData(java.util.Collection)} is called.
   * 
   * 
   * @param propertyType
   *          The type of the property. see {@link Property} for details.
   * @return this {@link TwitterDataCenterBuilder}.
   */
  <T extends Property> TwitterDataCenterBuilder addProperty(
      final Class<T> propertyType);

  /**
   * Add a property to the system. All properties will be calculated when
   * {@link TwitterDataCenter#importData(java.util.Collection)} is called.
   * 
   * 
   * @param propertyType
   *          The type of the property. see {@link Property} for details.
   * @param propertyFactory
   *          factory for the given property
   * @return this {@link TwitterDataCenterBuilder}.
   */
  <T extends Property, S extends T> TwitterDataCenterBuilder addProperty(
      final Class<T> propertyType, PropertyFactory<S> propertyFactory);

  /**
   * 
   * @param queryType
   *          The type of the query to be register. see {@link TwitterQuery} for
   *          details.
   * 
   * @return this {@link TwitterDataCenterBuilder}.
   * @throws NotAServiceException
   *           if the object is not a legal service
   * @throws MissingPropertitesException
   *           if any of the property required for the service can't be
   *           obtained.
   */
  <T extends TwitterQuery> TwitterDataCenterBuilder registerQuery(
      Class<T> queryType) throws NotAServiceException,
      MissingPropertitesException;

  /**
   * 
   * @param queryType
   *          The type of the query to be register. see {@link TwitterQuery} for
   *          details.
   * @param queryFactory
   *          factory for the given query. see {@link TwitterQueryFactory} for
   *          details.
   * 
   * @return this {@link TwitterDataCenterBuilder}.
   * @throws NotAServiceException
   *           if the object is not a legal service
   * @throws MissingPropertitesException
   *           if any of the property required for the service can't be
   *           obtained.
   */
  <T extends TwitterQuery, S extends T> TwitterDataCenterBuilder registerQuery(
      Class<T> queryType, TwitterQueryFactory<S> queryFactory)
      throws NotAServiceException, MissingPropertitesException;

  /**
   * @param serialzersToAdd
   *          a {@link Serializer} for any class that can't be automatically
   *          serialize. When an object of the given type is stored, we will use
   *          the given serializer.
   * @return a reference to this object.
   */
  TwitterDataCenterBuilder addSerializer(Serializer serialzersToAdd);

  /**
   * @return A {@link TwitterDataCenter}.
   */
  TwitterDataCenter build();

  /**
   * Indicate that the object doesn't have a single construct annotated with
   * {@link ServiceSetup} .
   * 
   * @author Ziv Ronen
   * @date 29.05.2014
   * @mail akarks@gmail.com
   * 
   * @version 2.0
   * @since 2.0
   */
  public static class NotAServiceException extends RuntimeException {

    /**
     * @param simpleName
     *          The name of the service
     */
    public NotAServiceException(final String simpleName) {
      super(simpleName + " is not a service");
    }

    private static final long serialVersionUID = 7808573286722982627L;
  }

  /**
   * Indicate that the type doesn't have accessible constructor that get (
   * {@link List}<{@link BaseTweet}> , {@link List}<{@link Retweet}>)
   * 
   * @author Ziv Ronen
   * @date 30.05.2014
   * @mail akarks@gmail.com
   * 
   * @version 2.0
   * @since 2.0
   */
  public static class NotAPropertyException extends RuntimeException {

    private static final long serialVersionUID = 7277301716229432516L;

    /**
     * @param simpleName
     *          The name of the service
     */
    public NotAPropertyException(final String simpleName) {
      super(simpleName + " is not a property");
    }
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

}
