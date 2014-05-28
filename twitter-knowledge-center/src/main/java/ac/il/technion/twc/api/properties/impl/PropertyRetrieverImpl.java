package ac.il.technion.twc.api.properties.impl;

import ac.il.technion.twc.api.properties.PropertyBuilder;
import ac.il.technion.twc.api.properties.PropertyRetriever;
import ac.il.technion.twc.api.properties.PropertyRetrieverFactory;


/**
 * Basic implementation of {@link PropertyRetriever}
 * 
 * @author Ziv Ronen
 * @date 22.05.2014
 * @mail akarks@gmail.com
 * 
 * @version 2.0
 * @since 2.0
 * @param <T>
 */
public class PropertyRetrieverImpl<T> implements PropertyRetriever<T> {

  private final PropertyBuilder<T> builder;

  /**
   * @param builder
   */
  public PropertyRetrieverImpl(final PropertyBuilder<T> builder) {
    this.builder = builder;
  }

  @Override
  public T retrieve() {
    return builder.getResult();
  }

  /**
   * a {@link PropertyRetrieverFactory} implemention for
   * {@link PropertyRetrieverImpl}
   * 
   * @author Ziv Ronen
   * @date 22.05.2014
   * @mail akarks@gmail.com
   * 
   * @version 2.0
   * @since 2.0
   */
  public static class PropertyRetrieverImplFactory implements
      PropertyRetrieverFactory {

    @Override
    public <T> PropertyRetriever<T> create(final PropertyBuilder<T> builder) {
      return new PropertyRetrieverImpl<T>(builder);
    }

  }

}
