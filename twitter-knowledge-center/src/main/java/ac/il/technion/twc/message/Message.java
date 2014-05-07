package ac.il.technion.twc.message;

import ac.il.technion.twc.message.visitor.MessageVisitor;


public interface Message {
	
	<T> T accept(MessageVisitor<T> visitor);
	
	ID id();

}
