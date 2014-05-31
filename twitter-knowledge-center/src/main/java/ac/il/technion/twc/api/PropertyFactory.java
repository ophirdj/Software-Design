package ac.il.technion.twc.api;

import java.util.List;

import ac.il.technion.twc.api.tweet.BaseTweet;
import ac.il.technion.twc.api.tweet.Retweet;

public interface PropertyFactory<T> {

	T get(List<BaseTweet> baseTweets, List<Retweet> retweets);

}
