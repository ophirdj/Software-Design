package ac.il.technion.twc.histogram;

import ac.il.technion.twc.message.tweet.BaseTweet;
import ac.il.technion.twc.message.tweet.Retweet;
import ac.il.technion.twc.message.visitor.MessagePropertyBuilder;
import ac.il.technion.twc.storage.StorageHandler;

import com.google.inject.Inject;
import com.google.inject.name.Named;

/**
 * 
 * @author Ziv Ronen
 * @date 07.05.2014
 * @mail akarks@gmail.com
 * 
 * @version 2.0
 * @since 2.0
 * 
 *        handling histogram property index building, storing and loading
 */
public class DayHistogramBuilder extends MessagePropertyBuilder<DayHistogram> {

	/**
	 * @param storageHandler
	 * @param defaultHistogram
	 */
	@Inject
	public DayHistogramBuilder(
			final StorageHandler<DayHistogram> storageHandler,
			@Named("default") final DayHistogram defaultHistogram) {
		super(storageHandler, defaultHistogram);
	}

	@Override
	public Void visit(final BaseTweet t) {
		final DayOfWeek day = DayOfWeek.fromDate(t.date());
		data.basetweets.put(day, data.basetweets.get(day) + 1);
		return null;
	}

	@Override
	public Void visit(final Retweet t) {
		final DayOfWeek day = DayOfWeek.fromDate(t.date());
		data.retweets.put(day, data.retweets.get(day) + 1);
		return null;
	}

	@Override
	protected DayHistogram getResult() {
		return data.prepareStringRepresentation();
	}

}
