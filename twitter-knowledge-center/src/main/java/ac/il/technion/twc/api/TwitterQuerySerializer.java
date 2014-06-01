package ac.il.technion.twc.api;

/**
 * User defined serialzer for a type
 * 
 * @author Ziv Ronen
 * @date 30.05.2014
 * @mail akarks@gmail.com
 * 
 * @param <T>
 *          The type of the serialized object
 */
public interface TwitterQuerySerializer<T> {

  /**
   * @param t
   *          object of type T
   * @return A string representing the object
   */
  String objectToString(T t);

  /**
   * 
   * @param s
   *          A string representing the object
   * @return object of type T
   */
  T stringToObject(String s);

  /**
   * @return the type that can be serialize. should be T
   */
  Class<T> getType();
}
