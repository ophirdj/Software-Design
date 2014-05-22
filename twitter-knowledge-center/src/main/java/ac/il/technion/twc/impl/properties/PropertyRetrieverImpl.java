package ac.il.technion.twc.impl.properties;

import ac.il.technion.twc.api.properties.PropertyBuilder;
import ac.il.technion.twc.api.properties.PropertyRetriever;

public class PropertyRetrieverImpl<T> implements PropertyRetriever<T> {

  private final PropertyBuilder<T> builder;

  public PropertyRetrieverImpl(final PropertyBuilder<T> builder) {
    this.builder = builder;
  }

  @Override
  public T retrieve() {
    return builder.getResult();
  }

  public static class PropertyRetrieverImplFactory implements
      PropertyRetrieverFactory {

    @Override
    public <T> PropertyRetriever<T> create(final PropertyBuilder<T> builder) {
      return new PropertyRetrieverImpl<T>(builder);
    }

  }

}
