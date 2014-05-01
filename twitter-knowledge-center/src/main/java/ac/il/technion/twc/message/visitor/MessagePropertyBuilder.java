package ac.il.technion.twc.message.visitor;

import ac.il.technion.twc.storage.StorageHandler;

import com.google.inject.Inject;

public abstract class MessagePropertyBuilder<T> implements MessageVisitor<Void> {

	private StorageHandler<T> storageHandler;

	@Inject
	public MessagePropertyBuilder(StorageHandler<T> storageHandler) {
		this.storageHandler = storageHandler;
	}

	protected abstract T getResult();

	public final void saveResult() {
		storageHandler.store(getResult());
	}

	public final T loadResult(T defaultReturnValue) {
		return storageHandler.load(defaultReturnValue);
	}

}
