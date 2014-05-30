package ac.il.technion.twc.api.center;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import ac.il.technion.twc.api.center.impl.TwitterSystemBuilder.MissingPropertitesException;
import ac.il.technion.twc.api.properties.PropertyBuilder;

/**
 * Build our main API
 * 
 * @author Ziv Ronen
 * @date 22.05.2014
 * @mail akarks@gmail.com
 * 
 * @version 2.0
 * @since 2.0
 */
// TODO: find Shorter name
public interface TwitterServicesCenterBuilder {

  /**
   * Register a property builder to the system and return a way two get the
   * property later on.
   * 
   * @param type
   * 
   * @param builder
   *          a builder of the property
   * @return object used to retrieve that value when required
   */
  <T> TwitterServicesCenterBuilder registerBuilder(final Class<T> type,
      PropertyBuilder<T> builder);

  /**
   * @param service
   *          an object with a method that annotated with service
   * @return the builder
   * @throws NotAServiceException
   *           if the object is not a legal service
   * @throws MissingPropertitesException
   */
  <T> TwitterServicesCenterBuilder registerService(T service)
      throws NotAServiceException, MissingPropertitesException;

  /**
   * @return The builded center
   */
  TwitterServicesCenter getResult();

  /**
   * Used to annotate the setup method of service. Any service should have
   * precisely one method with this annotation
   * 
   * @author Ziv Ronen
   * @date 29.05.2014
   * @mail akarks@gmail.com
   * 
   * @version 2.0
   * @since 2.0
   */
  @Target(ElementType.CONSTRUCTOR)
  @Retention(RetentionPolicy.RUNTIME)
  public @interface ServiceSetup {

  }

  /**
   * Indicate that the object doesn't have any method with {@link ServiceSetup}
   * annotation
   * 
   * @author Ziv Ronen
   * @date 29.05.2014
   * @mail akarks@gmail.com
   * 
   * @version 2.0
   * @since 2.0
   */
  public static class NotAServiceException extends Exception {

    public NotAServiceException(final String simpleName) {
      super(simpleName);
    }

    /**
     * 
     */
    private static final long serialVersionUID = 7808573286722982627L;

  }

}
