package ac.il.technion.twc.api.core;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.concurrent.Executors;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

/**
 * Factory for storage. Mainly to make sure that no one will create it by
 * himself
 * 
 * @author Ziv Ronen
 * @date 30.05.2014
 * @mail akarks@gmail.com
 * 
 * @version 2.0
 * @since 2.0
 */
class StorageFactory {

  private final Gson gson;

  /**
   * @param serializers
   *          a serializer for any class that can't be automatically serialize.
   */
  @SuppressWarnings("rawtypes")
  public StorageFactory(final Collection<Serializer> serializers) {
    final GsonBuilder gsonBuilder = new GsonBuilder();
    for (final Serializer serializer : serializers)
      gsonBuilder.registerTypeAdapter(serializer.getType(), new TypeAdapter() {

        @Override
        public void write(final JsonWriter out, final Object value)
            throws IOException {
          out.value(serializer.objectToString(value));
        }

        @Override
        public Object read(final JsonReader in) throws IOException {
          return serializer.stringToObject(in.nextString());
        }

      });
    gson = gsonBuilder.create();
  }

  /**
   * @return Storage for {@link TwitterSystemHandler}
   */
  Storage buildStorage() {
    return new Storage(gson, Paths.get("system"), new FileHandler(),
        Executors.newCachedThreadPool());

  }
}
