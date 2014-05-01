package ac.il.technion.twc.lifetime;

import java.util.Map;

import ac.il.technion.twc.message.ID;

public class LifeTimeMap {
	
	final Map<ID, Long> map;
	
	public LifeTimeMap(Map<ID, Long> map) {
		this.map = map;
	}

	public Long get(ID id) {
		return map.get(id);
	}

}
