package ac.il.technion.twc.lifetime;

import ac.il.technion.twc.lifetime.TransitiveRootFinder.NoRootFoundException;
import ac.il.technion.twc.message.tweet.BaseTweet;
import ac.il.technion.twc.message.tweet.Retweet;
import ac.il.technion.twc.message.visitor.MessagePropertyBuilder;
import ac.il.technion.twc.storage.StorageHandler;

import com.google.inject.Inject;
import com.google.inject.name.Named;

/**
 * Builder for {@link LifeTimeData}.
 * 
 * 
 * @author Ophir De Jager
 * 
 */
public class LifeTimeBuilder extends MessagePropertyBuilder<LifeTimeData> {

	private final TransitiveRootFinder baseTweetFinder;

	/**
	 * @param storageHandler
	 *            A storage handler for {@link LifeTimeData}.
	 * @param rootFinder
	 *            Finds base tweet for each tweet.
	 * @param defaultLifeTime
	 *            Default state of tweets if nothing was loadeds.
	 */
	@Inject
	public LifeTimeBuilder(final StorageHandler<LifeTimeData> storageHandler,
			final TransitiveRootFinder rootFinder,
			@Named("default") final LifeTimeData defaultLifeTime) {
		super(storageHandler, defaultLifeTime);
		baseTweetFinder = rootFinder;
		baseTweetFinder.addBaseTweets(data.baseTweets);
		baseTweetFinder.addRetweets(data.retweets);
	}

	@Override
	public Void visit(final BaseTweet t) {
		data.baseTweets.add(t);
		baseTweetFinder.addTweet(t);
		return null;
	}

	@Override
	public Void visit(final Retweet t) {
		data.retweets.add(t);
		baseTweetFinder.addTweet(t);
		return null;
	}

	@Override
	protected LifeTimeData getResult() {
		for (final Retweet t : data.retweets)
			try {
				final BaseTweet base = baseTweetFinder.findRoot(t);
				data.map.put(base.id(), Math.max(t.date().getTime()
						- base.date().getTime(), !data.map.containsKey(base
						.id()) ? 0L : data.map.get(base.id())));
			} catch (final NoRootFoundException e) {
				continue;
			}
		return data;
	}
}
