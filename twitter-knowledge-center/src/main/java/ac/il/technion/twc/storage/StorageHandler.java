package ac.il.technion.twc.storage;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.google.inject.Inject;

/**
 * Loads and stores objects to persistent storage.
 * 
 * @author Ophir De Jager
 * 
 * @param <T>
 *            Type of objects that are loaded / stored.
 */
public class StorageHandler<T> {

	private final Gson serializer;
	private final Path storePath;

	/**
	 * C'tor
	 * 
	 * @param gson
	 * @param storageLocation
	 */
	@Inject
	public StorageHandler(final Gson gson, final Path storageLocation) {
		serializer = gson;
		storePath = storageLocation;
	}

	/**
	 * Store data to data file (overwriting it if exists).
	 * 
	 * @param toStore
	 *            Data to be stored.
	 * @throws IOException
	 */
	public void store(final T toStore) throws IOException {
		try (BufferedWriter writer = Files.newBufferedWriter(storePath,
				Charset.defaultCharset())) {
			writer.write(serializer.toJson(toStore, new TypeToken<T>() {
			}.getType()));
		}
	}

	/**
	 * Load stored data from data file.
	 * 
	 * @param defaultReturnValue
	 *            Value to be returned if no data was stored.
	 * @return Stored data.
	 */
	public T load(final T defaultReturnValue) {
		try {
			return serializer.fromJson(
					new String(Files.readAllBytes(storePath)),
					new TypeToken<T>() {
					}.getType());
		} catch (JsonSyntaxException | IOException e) {
			return defaultReturnValue;
		}
	}

}
