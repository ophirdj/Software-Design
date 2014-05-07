package ac.il.technion.twc.message.tweet;

import java.util.Date;

import ac.il.technion.twc.message.ID;
import ac.il.technion.twc.message.Message;

/**
 * 
 * @author Ophir De Jager
 * 
 */
public abstract class Tweet implements Message {

	private final Date date;
	private final ID id;

	/**
	 * @param date
	 *            Time when tweet was published.
	 * @param id
	 *            Unique ID for this tweet.
	 */
	public Tweet(final Date date, final ID id) {
		this.date = date;
		this.id = id;
	}

	@Override
	public ID id() {
		return id;
	}

	/**
	 * @return Time when tweet was published.
	 */
	public Date date() {
		return date;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (date == null ? 0 : date.hashCode());
		result = prime * result + (id == null ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final Tweet other = (Tweet) obj;
		if (date == null) {
			if (other.date != null)
				return false;
		} else if (!date.equals(other.date))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
