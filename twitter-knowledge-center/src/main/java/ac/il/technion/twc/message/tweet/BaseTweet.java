package ac.il.technion.twc.message.tweet;

import java.util.Date;

import ac.il.technion.twc.message.ID;
import ac.il.technion.twc.message.visitor.MessageVisitor;

/**
 * Tweet that is not a retwwet.
 * 
 * @author Ophir De Jager
 * 
 */
public class BaseTweet extends Tweet {

	/**
	 * @param date
	 * @param id
	 */
	public BaseTweet(final Date date, final ID id) {
		super(date, id);
	}

	@Override
	public <T> T accept(final MessageVisitor<T> visitor) {
		return visitor.visit(this);
	}

}
