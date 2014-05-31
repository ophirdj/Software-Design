package ac.il.technion.twc.api;

/**
 * A query for {@link TwitterDataCenterBuilder#registerQuery(Class)}.
 * 
 * 
 * {@link TwitterQuery} must have only one constructor or a constructor annotated
 * with {@link ServiceSetup}.
 * 
 * <br>
 * In addition, any type the constructor required must be createable. That mean
 * the type must not be a primitive and also be either (check in the following
 * order): <br>
 * - a property for which a property builder was register <br>
 * <b>or</b><br>
 * - fulfill All the following: <br>
 * (1) be a concrete class <br>
 * (2) have either a single public constructor or one public constructor
 * annotated with {@link ServiceSetup} <br>
 * (3) any type required by the constructor in (2) should also be createable
 * 
 * 
 * 
 * 
 * @author Ziv Ronen
 * @date 31.05.2014
 * @mail akarks@gmail.com
 * 
 */
public interface TwitterQuery {

  /**
   * Indicate that the object doesn't have a single construct annotated with
   * {@link ServiceSetup} .
   * 
   * @author Ziv Ronen
   * @date 29.05.2014
   * @mail akarks@gmail.com
   * 
   */
  public static class NotAQueryException extends RuntimeException {
  
    /**
     * @param simpleName
     *          The name of the service
     */
    public NotAQueryException(final String simpleName) {
      super(simpleName + " is not a service");
    }
  
    private static final long serialVersionUID = 7808573286722982627L;
  }

}
