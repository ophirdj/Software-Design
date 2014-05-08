package ac.il.technion.twc.message.visitor;

import java.io.IOException;

import ac.il.technion.twc.message.tweet.BaseTweet;
import ac.il.technion.twc.message.tweet.Retweet;
import ac.il.technion.twc.storage.StorageHandler;

import com.google.inject.Inject;
import com.google.inject.name.Named;

/**
 * This class is responsible for the management of all persistent storage and
 * retrieval of property data, which will be provided by subclasses. A subclass
 * is in fact a builder of the relevant property data.
 * 
 * @author Ziv Ronen
 * @date 07.05.2014
 * @mail akarks@gmail.com
 * 
 * @version 2.0
 * @since 2.0
 * @param <T>
 * @param <S>
 */
public class MessagePropertyBuilder<T, S> implements MessageVisitor<Void> {

	private final StorageHandler<T> dataStorageHandler;

	private final StorageHandler<S> cacheStorageHandler;

	private final PropertyBuilder<T, S> builder;

	/**
	 * @param dataStorageHandler
	 *            Handler for storing and loading builder state.
	 * @param cacheStorageHandler
	 *            Handler for storing and loading cache data.
	 * @param builder
	 *            A property builder.
	 * @param defaultState
	 *            State of builder should loading fail.
	 */
	@Inject
	public MessagePropertyBuilder(final StorageHandler<T> dataStorageHandler,
			final StorageHandler<S> cacheStorageHandler,
			final PropertyBuilder<T, S> builder,
			@Named("default") final T defaultState) {
		this.dataStorageHandler = dataStorageHandler;
		this.cacheStorageHandler = cacheStorageHandler;
		this.builder = builder;
		builder.initializeFromState(dataStorageHandler.load(defaultState));
	}

	/**
	 * save the result to a file
	 * 
	 * @throws IOException
	 *             If storing failed for any reason
	 */
	public final void saveResult() throws IOException {
		dataStorageHandler.store(builder.getState());
		cacheStorageHandler.store(builder.getResultCache());
	}

	/**
	 * Load the stored cache or the supplied <code>defaultReturnValue</code> if
	 * load fails.
	 * 
	 * @param defaultCache
	 *            Cache to be returned should the load fail.
	 * 
	 * @return The stored cache or the supplied <code>defaultReturnValue</code>
	 *         if load fails.
	 */
	public final S loadCache(final S defaultCache) {
		return cacheStorageHandler.load(defaultCache);
	}

	@Override
	public Void visit(final BaseTweet t) {
		return builder.visit(t);
	}

	@Override
	public Void visit(final Retweet t) {
		return builder.visit(t);
	}

}
