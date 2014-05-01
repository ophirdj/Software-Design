package ac.il.technion.twc.lifetime;

import ac.il.technion.twc.message.ID;

public class LifeTimeCache {
	
	private LifeTimeMap map;

	public LifeTimeCache(LifeTimeMap map) {
		this.map = map;
	}
	
	public String getLifetimeOfTweets(ID id) {
		return map.get(id).toString();
	}

}
