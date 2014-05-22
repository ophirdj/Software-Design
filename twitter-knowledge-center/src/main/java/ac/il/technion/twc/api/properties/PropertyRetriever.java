package ac.il.technion.twc.api.properties;

/**
 * Retrieve a property of the system.
 * 
 * @author Ziv Ronen
 * @date 22.05.2014
 * @mail akarks@gmail.com
 * 
 * @version 2.0
 * @since 2.0
 * @param <T>
 *          The type of the property
 */
public interface PropertyRetriever<T> {

  /**
   * @return The current value of the property. The property should be immutable
   *         since it represent the value after the system is builded
   */
  T retrieve();
}
