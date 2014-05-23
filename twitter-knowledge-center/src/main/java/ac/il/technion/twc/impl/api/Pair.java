package ac.il.technion.twc.impl.api;

/**
 * Stored two elements
 * 
 * @author Ziv Ronen
 * @date 22.05.2014
 * @mail akarks@gmail.com
 * 
 * @version 2.0
 * @since 2.0
 * @param <T1>
 * @param <T2>
 */
public class Pair<T1, T2> {
  /**
   * The first element
   */
  public final T1 first;
  /**
   * The second element
   */
  public final T2 second;

  /**
   * @param t1
   *          value for the first element
   * @param t2
   *          value for the second element
   */
  public Pair(final T1 t1, final T2 t2) {
    first = t1;
    second = t2;
  }
}
