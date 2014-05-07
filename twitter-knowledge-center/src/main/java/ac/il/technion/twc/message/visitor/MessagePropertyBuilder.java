package ac.il.technion.twc.message.visitor;

import java.io.IOException;

import ac.il.technion.twc.storage.StorageHandler;

import com.google.inject.Inject;

public abstract class MessagePropertyBuilder<T> implements MessageVisitor<Void> {

	private final StorageHandler<T> storageHandler;

	@Inject
	public MessagePropertyBuilder(final StorageHandler<T> storageHandler) {
		this.storageHandler = storageHandler;
	}

	protected abstract T getResult();

	public final void saveResult() throws IOException {
		storageHandler.store(getResult());
	}

	public final T loadResult(final T defaultReturnValue) {
		return storageHandler.load(defaultReturnValue);
	}

}
