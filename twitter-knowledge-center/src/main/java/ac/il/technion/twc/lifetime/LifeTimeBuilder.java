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

	private final LifeTimeMap map;
	private final TransitiveRootFinder baseTweetFinder;

	@Inject
	public LifeTimeBuilder(final StorageHandler<LifeTimeMap> storageHandler,
			final TransitiveRootFinder rootFinder,
			@Named("default") final LifeTimeMap defaultMap) {
		super(storageHandler);
		map = storageHandler.load(defaultMap);
		baseTweetFinder = rootFinder;
	}

	@Override
	public Void visit(final BaseTweet t) {
		baseTweetFinder.addTweet(t);
		return null;
	}

	@Override
	public Void visit(final Retweet t) {
		baseTweetFinder.addTweet(t);
		return null;
	}

	@Override
	protected LifeTimeMap getResult() {
		// TODO Auto-generated method stub
		return null;
	}

}
