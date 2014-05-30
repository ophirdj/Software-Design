package ac.il.technion.twc.api.center.impl;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import ac.il.technion.twc.api.center.TwitterServicesCenterBuilder.MissingPropertitesException;
import ac.il.technion.twc.api.center.TwitterServicesCenterBuilder.NotAServiceException;
import ac.il.technion.twc.api.center.TwitterServicesCenterBuilder.ServiceSetup;
import ac.il.technion.twc.api.properties.PropertyBuilder;

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
  private final Map<Class<?>, Object> predefinedValues = new HashMap<>();

  /**
   * Inform the {@link ServiceBuildingManager} that the given type is supported
   * by a {@link PropertyBuilder}
   * 
   * @param type
   *          The type supported by a property builder
   */
  public void addProperty(final Class<?> type) {
    supportedProperties.add(type);
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
    supportedProperties.add(type);
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
    if (supportedProperties.contains(type))
      return;
    final StringBuilder missingMessageBuilder =
        new StringBuilder("The service can't be register because:\n");
    boolean isMissing = false;
    // c'tor != null due to hasSingleAnnotatedCtor function
    final Constructor<?> ctor = getServiceCtor(type);
    final Type[] values = ctor.getParameterTypes();
    for (final Type parameter : values) {
      if (!(parameter instanceof Class<?>)) {
        missingMessageBuilder.append("- property ").append(parameter)
            .append(" needed for service ").append(type.getSimpleName())
            .append(" is not a class\n");
        continue;
      }
      final HashSet<Type> visited = new HashSet<>();
      isMissing =
          isMissingProperty((Class<?>) parameter, visited,
              missingMessageBuilder, new StringBuilder().append("path: ")
                  .append(type.getSimpleName()));
    }
    if (isMissing)
      throw new MissingPropertitesException(missingMessageBuilder.toString());
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
    if (!visited.add(type)) {
      missingMessage.append("- found circle.  property ")
          .append(type.getSimpleName()).append(currentPath.toString())
          .append("\n");
      return true;
    }
    try {
      if (supportedProperties.contains(type))
        return false;
      if (!isConcrete(type)) {
        missingMessage.append("- property ").append(type.getSimpleName())
            .append(currentPath.toString()).append(" is not concrete\n");
        return true;
      }
      final Constructor<?> ctor = getServiceCtor(type);
      if (ctor == null) {
        missingMessage.append("- property ").append(type.getSimpleName())
            .append(" is missing. ").append(currentPath.toString());
        return true;
      }
      final Type[] values = ctor.getParameterTypes();
      boolean missing = false;
      for (final Type parameter : values) {
        final StringBuilder neededPath =
            new StringBuilder(currentPath.toString());
        if (!(parameter instanceof Class<?>)) {
          missingMessage.append("- property ").append(parameter)
              .append(" is not a class. ").append(neededPath.toString())
              .append("\n");
          missing = true;
        } else if (isMissingProperty((Class<?>) parameter, visited,
            missingMessage, neededPath.append("->")
                .append(type.getSimpleName())))
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
   * @param properties
   *          A map between a property and object supporting it
   * @return An instance of the given type
   * 
   * 
   * 
   * @throws InstantiationException
   * @throws IllegalAccessException
   * @throws IllegalArgumentException
   * @throws InvocationTargetException
   */
  public Object getInstance(final Class<?> type,
      final Map<Class<?>, Object> properties) throws InstantiationException,
      IllegalAccessException, IllegalArgumentException,
      InvocationTargetException {
    if (properties.containsKey(type))
      return properties.get(type);
    final Constructor<?> ctor = getSetupCtor(type);
    return ctor.newInstance(getCtorValues(properties, ctor));

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

  private Object[] getCtorValues(final Map<Class<?>, Object> properties,
      final Constructor<?> ctor) {
    final Type[] calledParameters = ctor.getGenericParameterTypes();
    final Object[] values = new Object[calledParameters.length];
    for (int i = 0; i < calledParameters.length; i++) {
      final Class<?> neededParameterType = (Class<?>) calledParameters[i];
      if (properties.containsKey(neededParameterType))
        values[i] = properties.get(neededParameterType);
      else if (predefinedValues.containsKey(neededParameterType))
        values[i] = predefinedValues.get(neededParameterType);
      else {
        final Constructor<?> innerCtor = getSetupCtor(neededParameterType);
        try {
          values[i] =
              innerCtor.newInstance(getCtorValues(properties, innerCtor));
        } catch (InstantiationException | IllegalAccessException
            | IllegalArgumentException | InvocationTargetException e) {
          // shouldn't happen
          e.printStackTrace();
        }
      }
    }
    return values;
  }

}
