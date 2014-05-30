package ac.il.technion.twc.api.center.impl;

import java.lang.reflect.Type;

/**
 * User defined serialzer
 * 
 * @author Ziv Ronen
 * @date 30.05.2014
 * @mail akarks@gmail.com
 * 
 * @version 2.0
 * @since 2.0
 */
public interface Serializer {

  /**
   * @param t
   *          object of type t
   * @return A string representing the object
   */
  String objectToString(Object t);

  /**
   * 
   * @param s
   *          A string representing the object
   * @return object of type t
   */
  Object stringToObject(String s);

  /**
   * @return the type that can be serialize. Should be T.
   */
  Type getType();
}
