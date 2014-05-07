package ac.il.technion.twc.modules;

import java.util.HashMap;
import java.util.HashSet;

import ac.il.technion.twc.lifetime.LifeTimeBuilder;
import ac.il.technion.twc.lifetime.LifeTimeCache;
import ac.il.technion.twc.lifetime.LifeTimeData;
import ac.il.technion.twc.message.ID;
import ac.il.technion.twc.message.tweet.BaseTweet;
import ac.il.technion.twc.message.tweet.Retweet;
import ac.il.technion.twc.storage.StorageHandler;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.name.Named;

public class LifeTimeModule extends AbstractModule {

	private final StorageHandler<LifeTimeData> lifeTimeStorageHandler = new StorageHandler<>();

	@Override
	protected void configure() {
		// nothing to do here...
	}

	@Provides
	StorageHandler<LifeTimeData> lifeTimeStorage() {
		return lifeTimeStorageHandler;
	}

	@Provides
	LifeTimeCache lifeTimeCache(final LifeTimeBuilder lifeTimeBuilder,
			@Named("default") final LifeTimeData defaultLifeTimeMap) {
		return new LifeTimeCache(lifeTimeBuilder.loadResult(defaultLifeTimeMap));
	}

	@Provides
	@Named("default")
	LifeTimeData defaultLifeTimeMap() {
		return new LifeTimeData(new HashMap<ID, Long>(),
				new HashSet<BaseTweet>(), new HashSet<Retweet>());
	}

}
