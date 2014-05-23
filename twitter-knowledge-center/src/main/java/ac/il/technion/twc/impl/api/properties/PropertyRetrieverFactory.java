package ac.il.technion.twc.impl.api.properties;

import ac.il.technion.twc.api.properties.PropertyBuilder;
import ac.il.technion.twc.api.properties.PropertyRetriever;

/**
 * Factory for {@link PropertyRetriever} class
 * 
 * @author Ziv Ronen
 * @date 22.05.2014
 * @mail akarks@gmail.com
 * 
 * @version 2.0
 * @since 2.0
 */
public interface PropertyRetrieverFactory {

  /**
   * @param builder
   * @return {@link PropertyRetriever} for the property builder build
   */
  <T> PropertyRetriever<T> create(PropertyBuilder<T> builder);
}
