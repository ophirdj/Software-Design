package ac.il.technion.twc.impl.properties.daymapping;

import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

import ac.il.technion.twc.api.properties.PropertyBuilder;
import ac.il.technion.twc.api.tweets.BaseTweet;
import ac.il.technion.twc.api.tweets.Retweet;

/**
 * Builder for an ordered mapping between Dates and the corresponding day.
 * 
 * @author Ziv Ronen
 * @date 23.05.2014
 * @mail akarks@gmail.com
 * 
 * @version 2.0
 * @since 2.0
 */
// XXX: maybe it is better to use NavigableSet of the date and calculate
// DayOfWeek on usage?
public class DayMappingBuilder implements PropertyBuilder<DayMapping> {

	private final NavigableMap<Long, Integer> baseOccurencebyTime;
	private final NavigableMap<Long, Integer> reOccurenceByTime;

	/**
	 * c'tor
	 */
	public DayMappingBuilder() {
		baseOccurencebyTime = new TreeMap<Long, Integer>();
		reOccurenceByTime = new TreeMap<Long, Integer>();
	}

	@Override
	public Void visit(final BaseTweet t) {
		increaseCounter(t.date().getTime(), baseOccurencebyTime);
		return null;
	}

	@Override
	public Void visit(final Retweet t) {
		increaseCounter(t.date().getTime(), reOccurenceByTime);
		return null;
	}

	private void increaseCounter(final Long time,
			final Map<Long, Integer> countersMap) {
		countersMap.put(time, (!countersMap.containsKey(Long.valueOf(time)) ? 0
				: countersMap.get(time)) + 1);
	}

	@Override
	public void clear() {
		baseOccurencebyTime.clear();
		reOccurenceByTime.clear();
	}

	@Override
	public DayMapping getResult() {
		return new DayMapping(baseOccurencebyTime, reOccurenceByTime);
	}

}
