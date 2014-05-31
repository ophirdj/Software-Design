package ac.il.technion.twc.api.tweet;

import java.util.Date;

/**
 * @author Ophir De Jager
 * 
 */
public class Retweet extends Tweet {

	/**
	 * ID of original tweet that is being retweeted.
	 */
	public final ID originId;

	/**
	 * @param date
	 *            Time when tweet was published.
	 * @param id
	 *            Unique ID for this tweet.
	 * @param originId
	 *            ID of original tweet that is being retweeted.
	 */
	public Retweet(final Date date, final ID id, final ID originId) {
		super(date, id);
		this.originId = originId;
	}

	@Override
	public <T> T accept(final TweetVisitor<T> visitor) {
		return visitor.visit(this);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + (originId == null ? 0 : originId.hashCode());
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		final Retweet other = (Retweet) obj;
		if (originId == null) {
			if (other.originId != null)
				return false;
		} else if (!originId.equals(other.originId))
			return false;
		return true;
	}

}
