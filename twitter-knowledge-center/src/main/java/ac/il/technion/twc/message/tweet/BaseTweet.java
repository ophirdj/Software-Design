package ac.il.technion.twc.message.tweet;

import java.util.Date;

import ac.il.technion.twc.message.ID;
import ac.il.technion.twc.message.visitor.MessageVisitor;

public class BaseTweet extends Tweet {
	
	public BaseTweet(Date date, ID id) {
		super(date, id);
	}

	public <T> T accept(MessageVisitor<T> visitor) {
		return visitor.visit(this);
	}


}
