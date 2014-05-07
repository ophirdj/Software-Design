package ac.il.technion.twc.histogram;

import ac.il.technion.twc.message.tweet.BaseTweet;
import ac.il.technion.twc.message.tweet.Retweet;
import ac.il.technion.twc.message.visitor.MessagePropertyBuilder;
import ac.il.technion.twc.storage.StorageHandler;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;

@Singleton
public class DayHistogramBuilder extends MessagePropertyBuilder<DayHistogram> {

	private final DayHistogram histogram;

	@Inject
	public DayHistogramBuilder(
			final StorageHandler<DayHistogram> storageHandler,
			@Named("default") final DayHistogram defaultHistogram) {
		super(storageHandler);
		histogram = storageHandler.load(defaultHistogram);
	}

	public Void visit(BaseTweet t) {
		DayOfWeek day = DayOfWeek.fromDate(t.date());
		histogram.basetweets.put(day, histogram.basetweets.get(day) + 1);
		return null;
	}

	public Void visit(Retweet t) {
		DayOfWeek day = DayOfWeek.fromDate(t.date());
		histogram.retweets.put(day, histogram.retweets.get(day) + 1);
		return null;
	}

	protected DayHistogram getResult() {
		return histogram;
	}

}
