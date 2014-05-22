package ac.il.technion.twc.message.visitor;

import ac.il.technion.twc.api.MessageVisitor;
import ac.il.technion.twc.message.Message;

/**
 * Builder of a specific property of a {@link Message}.
 * 
 * @author Ophir De Jager
 * 
 * @param <T>
 *            Builder state (can be used to store/restore builder).
 * @param <S>
 *            Property cache that can be questioned (final result).
 */
public interface MessagePropertyBuilder<T, S> extends MessageVisitor<Void> {

	/**
	 * Initialize the builder's state to be the given state (used to load from
	 * persistent storage).
	 * 
	 * @param state
	 *            An object representing a possible state of the builder.
	 */
	void initializeFromState(T state);

	/**
	 * @return An object representing the builder's state (used for storage).
	 */
	T getState();

	/**
	 * @return The cache data (can be then questioned by external user).
	 */
	S getResultCache();

}
