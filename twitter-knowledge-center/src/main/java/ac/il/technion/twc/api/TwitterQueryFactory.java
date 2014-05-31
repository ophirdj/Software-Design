package ac.il.technion.twc.api;

/**
 * 
 * A factory for a {@link TwitterQuery}. Must have a single method named get
 * with only properties.
 * 
 * @author Ziv Ronen
 * @date 31.05.2014
 * @mail akarks@gmail.com
 * @param <T>
 * 
 */
public interface TwitterQueryFactory<T> {

  /**
   * Indicate that the {@link TwitterQueryFactory} doesn't have a single get
   * method with only properties.
   * 
   * @author Ziv Ronen
   * @date 31.05.2014
   * @mail akarks@gmail.com
   */
  public class NotAQueryFactory extends RuntimeException {

    /**
     * @param simpleName
     *          The name of the factory type
     */
    public NotAQueryFactory(final String simpleName) {
      super(simpleName + " is not a legal "
          + TwitterQueryFactory.class.getSimpleName());
    }

    /**
     * @param simpleName
     *          The name of the factory type
     * @param cause
     *          The reason it is not a legal query factory
     */
    public NotAQueryFactory(final String simpleName, final String cause) {
      super(simpleName + " is not a legal "
          + TwitterQueryFactory.class.getSimpleName() + " because " + cause);
    }

    /**
     * 
     */
    private static final long serialVersionUID = 7754875378545313265L;

  }
}
