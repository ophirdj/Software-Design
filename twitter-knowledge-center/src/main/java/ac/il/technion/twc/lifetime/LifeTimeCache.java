package ac.il.technion.twc.lifetime;

import ac.il.technion.twc.message.ID;

public class LifeTimeCache {
	
	private LifeTimeData map;

	public LifeTimeCache(LifeTimeData map) {
		this.map = map;
	}
	
	public String getLifetimeOfTweets(ID id) {
		return map.get(id).toString();
	}

}
