package ac.il.technion.twc.impl.properties.hashtags;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import ac.il.technion.twc.api.tweets.ID;

/**
 * Retrieve the hashtags of a tweet from its id
 * 
 * @author Ziv Ronen
 * @date 26.05.2014
 * @mail akarks@gmail.com
 * 
 * @version 2.0
 * @since 2.0
 */
public class IdHashtags {

	private final Map<ID, List<String>> HashtagById;

	/**
	 * @param HashtagById
	 */
	public IdHashtags(final Map<ID, List<String>> HashtagById) {
		this.HashtagById = Collections.unmodifiableMap(HashtagById);
	}

	/**
	 * @param id
	 * @return All the hashtags of an id if exist or empty list otherwise
	 */
	public List<String> getHashtags(final ID id) {
		return HashtagById.containsKey(id) ? Collections
				.unmodifiableList(HashtagById.get(id)) : Collections.<String>emptyList();
	}

}
