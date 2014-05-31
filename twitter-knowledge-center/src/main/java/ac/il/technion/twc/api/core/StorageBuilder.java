package ac.il.technion.twc.api.core;

import java.io.IOException;
import java.nio.file.Path;
import java.util.concurrent.Executors;

import ac.il.technion.twc.api.TwitterQuerySerializer;

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
class StorageBuilder {

  private final GsonBuilder gsonBuilder = new GsonBuilder();
  private final Path path;

  /**
   * @param path
   *          The place in which the storage should store the data
   */
  public StorageBuilder(final Path path) {
    this.path = path;
  }

  /**
   * @param serializer
   *          A {@link TwitterQuerySerializer} for type T
   */
  public <T> void addSerializer(final TwitterQuerySerializer<T> serializer) {
    gsonBuilder.registerTypeAdapter(serializer.getType(), new TypeAdapter<T>() {

      @Override
      public void write(final JsonWriter out, final T value) throws IOException {
        out.value(serializer.objectToString(value));
      }

      @Override
      public T read(final JsonReader in) throws IOException {
        return serializer.stringToObject(in.nextString());
      }

    });
  }

  /**
   * @return Storage for {@link TwitterSystemHandler}
   */
  Storage buildStorage() {
    return new Storage(gsonBuilder.create(), path, new FileHandler(),
        Executors.newCachedThreadPool());

  }
}
