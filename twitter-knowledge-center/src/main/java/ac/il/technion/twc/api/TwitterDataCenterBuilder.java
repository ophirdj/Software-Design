package ac.il.technion.twc.api;

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
   * @throws TwitterQuery.NotAQueryException
   *           if the object is not a legal service
   * @throws MissingPropertitesException
   *           if any of the property required for the service can't be
   *           obtained.
   */
  <T extends TwitterQuery> TwitterDataCenterBuilder registerQuery(
      Class<T> queryType) throws TwitterQuery.NotAQueryException,
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
   * @throws TwitterQuery.NotAQueryException
   *           if the object is not a legal service
   * @throws MissingPropertitesException
   *           if any of the property required for the service can't be
   *           obtained.
   */
  <T extends TwitterQuery, S extends T> TwitterDataCenterBuilder registerQuery(
      Class<T> queryType, TwitterQueryFactory<S> queryFactory)
      throws TwitterQuery.NotAQueryException, MissingPropertitesException;

  /**
   * @param serialzersToAdd
   *          a {@link TwitterQuerySerializer} for any class that can't be
   *          automatically serialize. When an object of the given type is
   *          stored, we will use the given serializer.
   * @return a reference to this object.
   */
  <T> TwitterDataCenterBuilder addSerializer(
      TwitterQuerySerializer<T> serialzersToAdd);

  /**
   * @return A {@link TwitterDataCenter}.
   */
  TwitterDataCenter build();

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
