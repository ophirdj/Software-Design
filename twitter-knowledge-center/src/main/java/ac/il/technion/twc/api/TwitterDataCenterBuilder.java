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
	 * @param propertyType
	 *            The type of the property. A property is any object that has a
	 *            constructor that accepts:
	 *            <code>({@link List}<{@link BaseTweet}>, {@link List}<{@link Retweet}>)</code>
	 *            .
	 * @return this {@link TwitterDataCenterBuilder}.
	 */
	<T> TwitterDataCenterBuilder addProperty(final Class<T> propertyType);

	/**
	 * Register a property builder to the system and return a way to get the
	 * property later on.
	 * 
	 * @param type
	 *            The type of the property.
	 * @param value
	 *            Value to be returned if property creation failed.
	 * @return this {@link TwitterDataCenterBuilder}.
	 */
	@Deprecated
	<T, S extends T> TwitterDataCenterBuilder setDefaultValue(
			Class<T> type, S value);

	/**
	 * Service can be register if it has precisely one constructor annotated
	 * with {@link ServiceSetup}.
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
	 *            an object with a method that annotated with service. This if
	 *            the default value of the service, to be returned if for any
	 *            reason loading the service failed.
	 * @return this {@link TwitterDataCenterBuilder}.
	 * @throws NotAServiceException
	 *             if the object is not a legal service
	 * @throws MissingPropertitesException
	 *             if any of the property required for the service can't be
	 *             obtained.
	 */
	<T> TwitterDataCenterBuilder registerService(T defaultServiceValue)
			throws NotAServiceException, MissingPropertitesException;

	/**
	 * @param serialzersToAdd
	 *            a {@link Serializer} for any class that can't be automatically
	 *            serialize. When an object of the given type is stored, we will
	 *            use the given serializer.
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
		 *            The name of the service
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
		 *            The name of the service
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
		 *            the message to be displayed
		 */
		public MissingPropertitesException(final String string) {
			super(string);
		}

		private static final long serialVersionUID = -2955615443853602756L;
	}

}