package ac.il.technion.twc.storage;

import com.google.inject.Inject;

public class StorageHandler<T> {
	
	private T t;

	@Inject
	public StorageHandler() {
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * Store data to data file (overwriting it if exists).
	 * @param toStore Data to be stored.
	 */
	public void store(T toStore) {
		// TODO Auto-generated method stub
		t = toStore;
	}
	
	/**
	 * Load stored data from data file.
	 * @param defaultReturnValue Value to be returned if no data was stored.
	 * @return Stored data.
	 */
	public T load(T defaultReturnValue){
		// TODO Auto-generated method stub
		return (t != null) ? t : defaultReturnValue;
	}

}
