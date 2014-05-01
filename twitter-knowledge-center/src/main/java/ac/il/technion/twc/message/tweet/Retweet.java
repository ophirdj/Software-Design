package ac.il.technion.twc.message.tweet;

import java.util.Date;

import ac.il.technion.twc.message.ID;
import ac.il.technion.twc.message.visitor.MessageVisitor;

public class Retweet extends Tweet {
	
	public final ID originId;

	public Retweet(Date date, ID id, ID originId) {
		super(date, id);
		this.originId = originId;
	}

	public <T> T accept(MessageVisitor<T> visitor) {
		return visitor.visit(this);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
				+ ((originId == null) ? 0 : originId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!super.equals(obj)) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Retweet other = (Retweet) obj;
		if (originId == null) {
			if (other.originId != null) {
				return false;
			}
		} else if (!originId.equals(other.originId)) {
			return false;
		}
		return true;
	}
	
	

}
