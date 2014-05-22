package ac.il.technion.twc.impl.properties;

import ac.il.technion.twc.api.properties.PropertyBuilder;
import ac.il.technion.twc.api.properties.PropertyRetriever;

public interface PropertyRetrieverFactory {

  <T> PropertyRetriever<T> create(PropertyBuilder<T> builder);
}
