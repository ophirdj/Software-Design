package ac.il.technion.twc.api.core;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import ac.il.technion.twc.api.ServiceSetup;
import ac.il.technion.twc.api.TwitterDataCenterBuilder.MissingPropertitesException;
import ac.il.technion.twc.api.TwitterDataCenterBuilder.NotAPropertyException;
import ac.il.technion.twc.api.TwitterDataCenterBuilder.NotAServiceException;
import ac.il.technion.twc.api.tweet.BaseTweet;
import ac.il.technion.twc.api.tweet.Retweet;

/**
 * Utils class for checking if a service can be build and for building it
 * 
 * @author Ziv Ronen
 * @date 30.05.2014
 * @mail akarks@gmail.com
 * 
 * @version 2.0
 * @since 2.0
 */
class ServiceBuildingManager {

  private final Set<Class<?>> supportedProperties = new HashSet<>();
  private final Set<Class<?>> supportedObjects = new HashSet<>();
  private final Map<Class<?>, Object> predefinedValues = new HashMap<>();
  private final Map<Class<?>, Future<Object>> properties = new HashMap<>();
  private final ExecutorService pool = Executors.newCachedThreadPool();

  /**
   * Inform the {@link ServiceBuildingManager} that the given type is property
   * 
   * @param type
   *          The type supported by a property builder
   */
  public void addProperty(final Class<?> type) {
    supportedProperties.add(type);
    supportedObjects.add(type);
  }

  /**
   * @param type
   *          the type of the property
   * 
   * @throws NotAPropertyException
   *           if the type doesn't represent a property
   */
  public void checkProperty(final Class<?> type) {
    try {
      final Constructor<?> ctor = type.getConstructor(List.class, List.class);
      final Type[] parameters = ctor.getGenericParameterTypes();
      if (BaseTweet.class.equals(((ParameterizedType) parameters[0])
          .getActualTypeArguments()[0])
          && Retweet.class.equals(((ParameterizedType) parameters[1])
              .getActualTypeArguments()[0]) && isConcrete(type))
        // Succeed
        return;
    } catch (NoSuchMethodException | SecurityException e) {
    }
    throw new NotAPropertyException(type.getSimpleName());
  }

  /**
   * Make all the properties
   * 
   * @param bases
   *          List of base tweets
   * @param res
   *          List of Retweets
   */
  public void
      setProperties(final List<BaseTweet> bases, final List<Retweet> res) {
    final List<BaseTweet> unmodifiableBases =
        Collections.unmodifiableList(bases);
    final List<Retweet> unmodifiableRes = Collections.unmodifiableList(res);
    for (final Class<?> supportedType : supportedProperties)
      properties.put(supportedType, pool.submit(new Callable<Object>() {

        @Override
        public Object call() throws Exception {
          return supportedType.getConstructor(List.class, List.class)
              .newInstance(unmodifiableBases, unmodifiableRes);
        }
      }));
  }

  /**
   * Inform the {@link ServiceBuildingManager} that the value of the given type
   * is predefine to the given value
   * 
   * @param type
   *          The type
   * @param value
   *          Its new value
   */
  public <T, ST extends T> void addPredfineValue(final Class<T> type,
      final ST value) {
    supportedObjects.add(type);
    predefinedValues.put(type, value);
  }

  /**
   * @param type
   *          The type of the service
   * @throws MissingPropertitesException
   *           if in the dependencies' path of the service one of the class is
   *           missing and can't be uniquely instantiate
   * @throws NotAServiceException
   *           if the type do not possess precisely one constructor annotated
   *           with {@link ServiceSetup}
   */
  public void checkService(final Class<?> type)
      throws MissingPropertitesException, NotAServiceException {
    if (!hasSingleAnnotatedCtor(type))
      throw new NotAServiceException(type.getSimpleName());
    if (supportedObjects.contains(type))
      return;
    final StringBuilder missingMessageBuilder =
        new StringBuilder("The service ").append(type.getSimpleName()).append(
            " can't be register because:\n");
    boolean isMissing = false;
    // c'tor != null due to hasSingleAnnotatedCtor function
    final Constructor<?> ctor = getServiceCtor(type);
    final Type[] values = ctor.getParameterTypes();
    for (final Type parameter : values) {
      if (!(parameter instanceof Class<?>)) {
        missingMessageBuilder.append(prefix(parameter.toString())).append(
            "is not a class\n");
        continue;
      }
      final HashSet<Type> visited = new HashSet<>();
      isMissing |=
          isMissingProperty((Class<?>) parameter, visited,
              missingMessageBuilder, new StringBuilder().append("path: ")
                  .append(type.getSimpleName()));
    }
    missingMessageBuilder.append("\n");
    if (isMissing)
      throw new MissingPropertitesException(missingMessageBuilder.toString());
  }

  private String prefix(final String type) {
    return new StringBuilder("- object of type ").append(type)
        .append(" can't be created because it ").toString();
  }

  private boolean hasSingleAnnotatedCtor(final Class<?> type) {
    int count = 0;
    if (!isConcrete(type))
      return false;
    for (final Constructor<?> ctor : type.getConstructors())
      if (ctor.isAnnotationPresent(ServiceSetup.class))
        count++;
    return 1 == count;
  }

  private boolean isMissingProperty(final Class<?> type,
      final HashSet<Type> visited, final StringBuilder missingMessage,
      final StringBuilder currentPath) {
    currentPath.append("->").append(type.getSimpleName());
    if (!visited.add(type)) {
      missingMessage.append(prefix(type.getSimpleName()))
          .append("cause dependency circle. ").append(currentPath.toString())
          .append("\n");
      return true;
    }
    try {
      if (supportedObjects.contains(type))
        return false;
      if (!isConcrete(type)) {
        missingMessage.append(prefix(type.getSimpleName()))
            .append("is not a concrete class (or it is a primitive). ")
            .append(currentPath.toString()).append("\n");
        return true;
      }

      final Constructor<?> ctor = getServiceCtor(type);
      if (ctor == null) {
        missingMessage.append(prefix(type.getSimpleName()))
            .append("doesn't possess identifible requested constructor. ")
            .append(currentPath.toString()).append("\n")
            .append("\t(try to add a ")
            .append(ServiceSetup.class.getSimpleName())
            .append(" Annotation to the requested constructor.)");
        return true;
      }
      final Type[] values = ctor.getParameterTypes();
      boolean missing = false;
      for (final Type parameter : values) {
        final StringBuilder neededPath =
            new StringBuilder(currentPath.toString());
        if (!(parameter instanceof Class<?>)) {
          missingMessage.append(prefix(parameter.toString()))
              .append("is not a class. ").append(neededPath.toString())
              .append("\n");
          missing = true;
        } else if (isMissingProperty((Class<?>) parameter, visited,
            missingMessage, neededPath))
          missing = true;
      }
      return missing;
    } finally {
      visited.remove(type);
    }
  }

  private boolean isConcrete(final Class<?> type) {
    return !Modifier.isAbstract(type.getModifiers())
        && !Modifier.isInterface(type.getModifiers());
  }

  private static Constructor<?> getServiceCtor(final Class<?> type) {
    final Constructor<?>[] constructors = type.getConstructors();
    if (1 == constructors.length)
      return constructors[0];
    for (final Constructor<?> ctor : constructors)
      if (ctor.isAnnotationPresent(ServiceSetup.class))
        return ctor;
    return null;
  }

  /**
   * @param type
   *          the requested service type
   * @return An instance of the given type
   * 
   * 
   * 
   * @throws InstantiationException
   * @throws IllegalAccessException
   * @throws IllegalArgumentException
   * @throws InvocationTargetException
   */
  public Object getInstance(final Class<?> type) throws InstantiationException,
      IllegalAccessException, IllegalArgumentException,
      InvocationTargetException {
    if (properties.containsKey(type))
      try {
        return properties.get(type).get();
      } catch (final InterruptedException e) {
        // never going to happen
        throw new RuntimeException("interrupted while instantiating class");
      } catch (final ExecutionException e) {
        throw new RuntimeException(e.getCause());
      }
    final Constructor<?> ctor = getSetupCtor(type);
    try {
      return ctor.newInstance(getCtorValues(ctor));
    } catch (final InterruptedException e) {
      // never going to happen
      throw new RuntimeException("interrupted while instantiating class");
    } catch (final ExecutionException e) {
      throw new RuntimeException(e.getCause());
    }
  }

  private Constructor<?> getSetupCtor(final Class<?> type) {
    final Constructor<?>[] constructors = type.getConstructors();
    if (1 == constructors.length)
      return constructors[0];
    for (final Constructor<?> ctor : constructors)
      if (ctor.isAnnotationPresent(ServiceSetup.class))
        return ctor;
    throw new IllegalArgumentException();
  }

  private Object[] getCtorValues(final Constructor<?> ctor)
      throws InterruptedException, ExecutionException {
    final Type[] calledParameters = ctor.getParameterTypes();
    final Object[] values = new Object[calledParameters.length];
    for (int i = 0; i < calledParameters.length; i++) {
      final Class<?> neededParameterType = (Class<?>) calledParameters[i];
      if (properties.containsKey(neededParameterType))
        values[i] = properties.get(neededParameterType).get();
      else if (predefinedValues.containsKey(neededParameterType))
        values[i] = predefinedValues.get(neededParameterType);
      else {
        final Constructor<?> innerCtor = getSetupCtor(neededParameterType);
        try {
          values[i] = innerCtor.newInstance(getCtorValues(innerCtor));
        } catch (InstantiationException | IllegalAccessException
            | IllegalArgumentException | InvocationTargetException e) {
          throw new RuntimeException(e);
        }
      }
    }
    return values;
  }

}