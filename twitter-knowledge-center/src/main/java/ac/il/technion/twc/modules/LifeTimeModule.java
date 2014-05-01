package ac.il.technion.twc.modules;

import java.util.HashMap;

import ac.il.technion.twc.lifetime.LifeTimeBuilder;
import ac.il.technion.twc.lifetime.LifeTimeCache;
import ac.il.technion.twc.lifetime.LifeTimeMap;
import ac.il.technion.twc.message.ID;
import ac.il.technion.twc.storage.StorageHandler;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.name.Named;

public class LifeTimeModule extends AbstractModule {
	
	private final StorageHandler<LifeTimeMap> lifeTimeStorageHandler = new StorageHandler<>();

	@Override
	protected void configure() {
		//nothing to do here...
	}
	
	@Provides
	StorageHandler<LifeTimeMap> lifeTimeStorage() {
		return lifeTimeStorageHandler;
	}

	@Provides
	LifeTimeCache lifeTimeCache(LifeTimeBuilder lifeTimeBuilder,
			@Named("default") LifeTimeMap defaultLifeTimeMap) {
		return new LifeTimeCache(lifeTimeBuilder.loadResult(defaultLifeTimeMap));
	}

	@Provides
	@Named("default")
	LifeTimeMap defaultLifeTimeMap() {
		return new LifeTimeMap(new HashMap<ID, Long>());
	}

}
