package ac.il.technion.twc.message.visitor;

import ac.il.technion.twc.message.tweet.BaseTweet;
import ac.il.technion.twc.message.tweet.Retweet;

public interface MessageVisitor<T> {
	
	T visit(BaseTweet t);
	
	T visit(Retweet t);
	
}
