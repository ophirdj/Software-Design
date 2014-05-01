package ac.il.technion.twc.lifetime;

import ac.il.technion.twc.message.tweet.BaseTweet;
import ac.il.technion.twc.message.tweet.Retweet;
import ac.il.technion.twc.message.visitor.MessagePropertyBuilder;
import ac.il.technion.twc.storage.StorageHandler;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;

@Singleton
public class LifeTimeBuilder extends MessagePropertyBuilder<LifeTimeMap> {

	@Inject
	public LifeTimeBuilder(StorageHandler<LifeTimeMap> storageHandler,
			@Named("default") final LifeTimeMap defaultMap) {
		super(storageHandler);
	}

	public Void visit(BaseTweet t) {
		// TODO Auto-generated method stub
		return null;
	}

	public Void visit(Retweet t) {
		// TODO Auto-generated method stub
		return null;
	}

	protected LifeTimeMap getResult() {
		// TODO Auto-generated method stub
		return null;
	}

}
