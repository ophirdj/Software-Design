package ac.il.technion.twc.api.core;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import ac.il.technion.twc.api.Property;
import ac.il.technion.twc.api.PropertyFactory;
import ac.il.technion.twc.api.QuerySetup;
import ac.il.technion.twc.api.TwitterDataCenterBuilder.MissingPropertitesException;
import ac.il.technion.twc.api.TwitterQuery;
import ac.il.technion.twc.api.TwitterQueryFactory;
import ac.il.technion.twc.api.TwitterQueryFactory.NotAQueryFactoryException;
import ac.il.technion.twc.api.tweet.BaseTweet;
import ac.il.technion.twc.api.tweet.Retweet;

/**
 * Utils class for checking if a service can be build and for building it
 * 
 * @author Ziv Ronen
 * @date 30.05.2014
 * @mail akarks@gmail.com
 * 
 */
class ServiceBuildingManager {

  private final Map<Class<?>, PropertyFactory<?>> supportedProperties =
      new HashMap<>();
  private final Map<Class<?>, TwitterQueryFactory<?>> supportedQueries =
      new HashMap<>();

  private final Set<Class<?>> supportedObjects = new HashSet<>();
  private final Map<Class<?>, Future<Object>> properties = new HashMap<>();
  private final ExecutorService pool = Executors.newCachedThreadPool();

  /**
   * Inform the {@link ServiceBuildingManager} that the given type is property
   * 
   * @param type
   *          the type of the property
   * 
   * @throws Property.NotAPropertyException
   *           if the type doesn't represent a property
   */
  public <T extends Property> void addProperty(final Class<T> type) {
    try {
      final Constructor<T> ctor = type.getConstructor(List.class, List.class);
      final Type[] parameters = ctor.getGenericParameterTypes();
      if (BaseTweet.class.equals(((ParameterizedType) parameters[0])
          .getActualTypeArguments()[0])
          && Retweet.class.equals(((ParameterizedType) parameters[1])
              .getActualTypeArguments()[0]) && isConcrete(type)) {
        supportedObjects.add(type);
        supportedProperties.put(type, new PropertyFactory<T>() {
          @Override
          public T get(final List<BaseTweet> baseTweets,
              final List<Retweet> retweets) {
            try {
              return ctor.newInstance(baseTweets, retweets);
            } catch (InstantiationException | IllegalAccessException
                | IllegalArgumentException e) {
              // TODO warp with our exception
              throw new RuntimeException(e);
            } catch (final InvocationTargetException e) {
              throw new RuntimeException(e.getCause());
            }
          }
        });
        return;
      }
    } catch (NoSuchMethodException | SecurityException e) {
    }
    throw new Property.NotAPropertyException(type.getSimpleName());
  }

  /**
   * Inform the {@link ServiceBuildingManager} that the given type is property
   * 
   * @param type
   *          the type of the property
   * @param factory
   *          factory for the property
   * 
   * @throws Property.NotAPropertyException
   *           if the type doesn't represent a property
   */
  public <T extends Property, S extends T> void addProperty(
      final Class<T> type, final PropertyFactory<S> factory) {
    supportedProperties.put(type, factory);
    supportedObjects.add(type);
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
    for (final Entry<Class<?>, PropertyFactory<?>> entry : supportedProperties
        .entrySet())
      properties.put(entry.getKey(), pool.submit(new Callable<Object>() {

        @Override
        public Object call() throws Exception {
          return entry.getValue().get(unmodifiableBases, unmodifiableRes);
        }
      }));
  }

  /**
   * @param type
   *          The type of the service
   * @param factory
   *          a factory for the type
   */
  public <T extends TwitterQuery, S extends T> void addQuery(
      final Class<T> type, final TwitterQueryFactory<S> factory) {
    Method chosen = null;
    boolean usableFactory = true;
    boolean missingProperty = false;
    final StringBuilder missing = new StringBuilder();
    final StringBuilder notQueryFactoryCause = new StringBuilder(":\n");
    for (final Method m : factory.getClass().getMethods()) {
      if (!"get".equals(m.getName()))
        continue;
      if (chosen != null)
        throw new NotAQueryFactoryException(factory.getClass().getSimpleName(),
            "have multiple get function.");
      final Class<?>[] paramters = m.getParameterTypes();
      chosen = m;
      for (final Class<?> paramterType : paramters) {
        if (!Property.class.isAssignableFrom(paramterType)) {
          notQueryFactoryCause.append("\t-The parameter "
              + paramterType.getSimpleName() + " is not a property.\n");
          usableFactory = false;
        }
        if (!supportedProperties.containsKey(paramterType)) {
          missing.append("\t- " + paramterType.getSimpleName() + "\n");
          missingProperty = true;
        }
      }
    }
    if (chosen == null)
      throw new NotAQueryFactoryException(factory.getClass().getSimpleName(),
          "have no get method.");

    if (!usableFactory)
      throw new NotAQueryFactoryException(factory.getClass().getSimpleName(),
          notQueryFactoryCause.toString());
    if (missingProperty)
      throw new MissingPropertitesException("The factory "
          + factory.getClass().getSimpleName() + " missing the properties: \n"
          + missing.toString());
    supportedQueries.put(type, factory);
  }

  /**
   * @param type
   *          The type of the service
   * @throws MissingPropertitesException
   *           if in the dependencies' path of the service one of the class is
   *           missing and can't be uniquely instantiate
   * @throws TwitterQuery.NotAQueryException
   *           if the type do not possess precisely one constructor annotated
   *           with {@link QuerySetup}
   */
  public <T extends TwitterQuery> void addQuery(final Class<T> type)
      throws MissingPropertitesException, TwitterQuery.NotAQueryException {
    if (!hasSingleAnnotatedCtor(type))
      throw new TwitterQuery.NotAQueryException(type.getSimpleName());
    if (supportedObjects.contains(type))
      return;
    final StringBuilder missingMessageBuilder =
        new StringBuilder("The service ").append(type.getSimpleName()).append(
            " can't be register because:\n");
    boolean isMissing = false;
    // c'tor != null due to hasSingleAnnotatedCtor function
    final Constructor<T> ctor = getServiceCtor(type);
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
    supportedQueries.put(type, new TwitterQueryFactory<T>() {

      // read using reflection
      @SuppressWarnings("unused")
      public T get() {
        try {
          return ctor.newInstance(getCtorValues(ctor));
        } catch (final InterruptedException e) {
          throw new RuntimeException("interrupted while instantiating class");
        } catch (InstantiationException | IllegalAccessException e) {
          // TODO wrap with our class
          throw new RuntimeException(e);
        } catch (ExecutionException | InvocationTargetException e) {
          throw new RuntimeException(e.getCause());
        }
      }

    });
  }

  private String prefix(final String type) {
    return new StringBuilder("- object of type ").append(type)
        .append(" can't be created because it ").toString();
  }

  private boolean hasSingleAnnotatedCtor(final Class<?> type) {
    if (!isConcrete(type))
      return false;
    final Constructor<?>[] constructors = type.getConstructors();
    if (constructors.length == 1)
      return true;
    int count = 0;
    for (final Constructor<?> ctor : constructors)
      if (ctor.isAnnotationPresent(QuerySetup.class))
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
            .append(QuerySetup.class.getSimpleName())
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

  private static <T> Constructor<T> getServiceCtor(final Class<T> type) {
    @SuppressWarnings("unchecked")
    final Constructor<T>[] constructors =
        (Constructor<T>[]) type.getConstructors();
    if (1 == constructors.length)
      return constructors[0];
    for (final Constructor<T> ctor : constructors)
      if (ctor.isAnnotationPresent(QuerySetup.class))
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
   */
  public Object getInstance(final Class<?> type) {
    if (supportedQueries.containsKey(type)) {
      final TwitterQueryFactory<?> factory = supportedQueries.get(type);
      for (final Method m : factory.getClass().getMethods())
        if ("get".equals(m.getName()))
          try {
            return m.invoke(factory, getValues(m));
          } catch (final IllegalAccessException e) {
            // TODO wrap with our class
            throw new RuntimeException(e);
          } catch (final InvocationTargetException e) {
            throw new RuntimeException(e.getCause());
          }
    }
    throw new IllegalArgumentException("service wanted but not register");
  }

  private Object[] getValues(final Method m) {
    final Class<?>[] parameters = m.getParameterTypes();
    final Object[] $ = new Object[parameters.length];
    for (int i = 0; i < $.length; i++)
      try {
        $[i] = properties.get(parameters[i]).get();
      } catch (final InterruptedException e) {
        throw new RuntimeException("interrupted while instantiating class");
      } catch (final ExecutionException e) {
        throw new RuntimeException(e);
      }
    return $;
  }

  private Constructor<?> getSetupCtor(final Class<?> type) {
    final Constructor<?>[] constructors = type.getConstructors();
    if (1 == constructors.length)
      return constructors[0];
    for (final Constructor<?> ctor : constructors)
      if (ctor.isAnnotationPresent(QuerySetup.class))
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
      else {
        final Constructor<?> innerCtor = getSetupCtor(neededParameterType);
        try {
          values[i] = innerCtor.newInstance(getCtorValues(innerCtor));
        } catch (InstantiationException | IllegalAccessException e) {
          // TODO wrap with our class
          throw new RuntimeException(e);
        } catch (final InvocationTargetException e) {
          throw new RuntimeException(e.getCause());
        }
      }
    }
    return values;
  }

}
