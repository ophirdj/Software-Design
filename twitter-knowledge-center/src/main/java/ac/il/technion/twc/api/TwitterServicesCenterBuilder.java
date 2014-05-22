package ac.il.technion.twc.api;

import ac.il.technion.twc.api.properties.PropertyBuilder;
import ac.il.technion.twc.api.properties.PropertyRetriever;

/**
 * Build our main API
 * 
 * @author Ziv Ronen
 * @date 22.05.2014
 * @mail akarks@gmail.com
 * 
 * @version 2.0
 * @since 2.0
 */
// TODO: find Shorter name
public interface TwitterServicesCenterBuilder {

  /**
   * Register a property builder to the system and return a way two get the
   * property later on.
   * 
   * @param builder
   *          a builder of the property
   * @return object used to retrieve that value when required
   */
  <T> PropertyRetriever<T> registerBuilder(PropertyBuilder<T> builder);

  /**
   * @return The builded center
   */
  TwitterServicesCenter getResult();
}
