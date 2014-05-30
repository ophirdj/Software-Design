package ac.il.technion.twc.api.center;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.List;

import ac.il.technion.twc.api.center.impl.Serializer;
import ac.il.technion.twc.api.tweets.BaseTweet;
import ac.il.technion.twc.api.tweets.Retweet;

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
public interface TwitterServicesCenterBuilder {

  /**
   * Register a property builder to the system and return a way two get the
   * property later on. Property is any object that have a constructor that
   * require ({@link List}<{@link BaseTweet}> , {@link List}<{@link Retweet}>)
   * 
   * @param propertyType
   *          The type of the property
   * @return a reference to this object.
   */
  <T> TwitterServicesCenterBuilder addProperty(final Class<T> propertyType);

  /**
   * Register a property builder to the system and return a way two get the
   * property later on.
   * 
   * @param type
   *          The type of the property
   * @param value
   *          The value to be return
   * @return a reference to this object.
   */
  <T, S extends T> TwitterServicesCenterBuilder setDefaultValue(Class<T> type,
      S value);

  /**
   * Service can be register if it has precisely one constructor annotated with
   * {@link ServiceSetup}.
   * 
   * <br>
   * In addition, any type the constructor required must be createable. That
   * mean the type must not be a primitive and also be either (check in the
   * following order): <br>
   * - a property for which a property builder was register <br>
   * - Class for which predefine value was register <br>
   * - fulfill All the following: <br>
   * (1) be a concrete class <br>
   * (2) have either a single public constructor or one public constructor
   * annotated with {@link ServiceSetup} <br>
   * (3) any type required by the constructor in (2) should also be createable
   * 
   * @param defaultServiceValue
   *          an object with a method that annotated with service. This if the
   *          default value of the service, to be returned if for any reason
   *          loading the service failed.
   * @return a reference to this object.
   * @throws NotAServiceException
   *           if the object is not a legal service
   * @throws MissingPropertitesException
   *           if any of the property required for the service can't be
   *           obtained.
   */
  <T> TwitterServicesCenterBuilder registerService(T defaultServiceValue)
      throws NotAServiceException, MissingPropertitesException;

  /**
   * @param serialzersToAdd
   *          a {@link Serializer} for any class that can't be automatically
   *          serialize. When an object of the given type is stored, we will use
   *          the given serializer.
   * @return a reference to this object.
   */
  TwitterServicesCenterBuilder addSerializer(Serializer serialzersToAdd);

  /**
   * @return The builded center
   */
  TwitterServicesCenter getResult();

  /**
   * Used to annotate the setup method of service. Any service should have
   * precisely one constructor with this annotation.
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

    /**
     * 
     */
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

    /**
     * 
     */
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

    /**
     * 
     */
    private static final long serialVersionUID = -2955615443853602756L;
  }

}
