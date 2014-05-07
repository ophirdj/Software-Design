package ac.il.technion.twc.message;

import ac.il.technion.twc.message.visitor.MessageVisitor;

/**
 * @author Ophir De Jager
 * 
 */
public interface Message {

	/**
	 * For visitor design pattern. Implementing classes should call
	 * <code>visitor.visit(this)</code>.
	 * 
	 * @param visitor
	 * @return Return value of visitor for this message.
	 */
	<T> T accept(MessageVisitor<T> visitor);

	/**
	 * @return Message's ID.
	 */
	ID id();

}
