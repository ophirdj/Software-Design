package ac.il.technion.twc.api.core;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ac.il.technion.twc.api.TwitterDataCenter;
import ac.il.technion.twc.api.tweet.BaseTweet;
import ac.il.technion.twc.api.tweet.Retweet;
import ac.il.technion.twc.api.tweet.Tweet;

/**
 * Used for keeping the tweets data and build the properties
 * 
 * @author Ziv Ronen
 * @date 22.05.2014
 * @mail akarks@gmail.com
 * 
 * @version 2.0
 * @since 2.0
 */
public class TwitterSystemHandler implements TwitterDataCenter {

	private final Map<Class<?>, Object> servicesResult = new HashMap<>();
	private final Storage storage;
	private final Set<Object> services;
	private final ServiceBuildingManager serviceBuilder;

	/**
	 * @param services
	 * @param serviceBuilder
	 * @param storage
	 */
	public TwitterSystemHandler(final Set<Object> services,
			final ServiceBuildingManager serviceBuilder, final Storage storage) {
		this.services = services;
		this.serviceBuilder = serviceBuilder;
		this.storage = storage;
	}

	@Override
	public void importData(final Collection<Tweet> importedTweets)
			throws OperationFailedException {
		final Tweets storedTweets = storage.load(new Tweets());
		final List<Tweet> tweets = new ArrayList<>();
		tweets.addAll(importedTweets);
		tweets.addAll(storedTweets.getBaseTweets());
		tweets.addAll(storedTweets.getRetweets());
		final Tweets newTweets = new Tweets(tweets);
		try {
			storage.store(newTweets);
			buildServices(newTweets);
		} catch (final IOException e) {
			throw new OperationFailedException(e);
		}
	}

	private void buildServices(final Tweets tweets) throws IOException {
		serviceBuilder.setProperties(tweets.getBaseTweets(),
				tweets.getRetweets());
		for (final Object service : services)
			try {
				storage.store(serviceBuilder.getInstance(service.getClass()));
			} catch (IllegalAccessException | IllegalArgumentException
					| InvocationTargetException | InstantiationException e) {
				// Shouldn't happen
				e.printStackTrace();
			}
	}

	@Override
	public void loadServices() {
		for (final Object service : services)
			servicesResult.put(service.getClass(), storage.load(service));
	}

	@Override
	public <T> T getService(final Class<T> type)
			throws IllegalArgumentException {
		if (!servicesResult.containsKey(type))
			throw new IllegalArgumentException("No service of type: " + type
					+ " is registered");
		return type.cast(servicesResult.get(type));
	}

	@Override
	public void clear() throws OperationFailedException {
		try {
			storage.clear();
		} catch (final IOException e) {
			throw new OperationFailedException(e);
		}
	}

	/**
	 * Wrapper class for list of tweets.
	 * 
	 * @author Ziv Ronen
	 * @date 22.05.2014
	 * @mail akarks@gmail.com
	 * 
	 * @version 2.0
	 * @since 2.0
	 */
	static class Tweets {
		private List<BaseTweet> baseTweets;
		private List<Retweet> retweets;

		/**
		 * @param tweets
		 *            get the tweets and split them to base tweets and retweets
		 */
		public Tweets(final List<Tweet> tweets) {
			baseTweets = new ArrayList<>();
			retweets = new ArrayList<>();
			for (final Tweet tweet : tweets)
				if (tweet instanceof BaseTweet)
					baseTweets.add((BaseTweet) tweet);
				else
					retweets.add((Retweet) tweet);
		}

		/**
		 * 
		 */
		public Tweets() {
			baseTweets = new ArrayList<>();
			retweets = new ArrayList<>();
		}

		/**
		 * @return the base tweets
		 */
		public List<BaseTweet> getBaseTweets() {
			return baseTweets;
		}

		/**
		 * set the base tweets
		 * 
		 * @param baseTweets
		 *            new value
		 */
		public void setBaseTweets(final List<BaseTweet> baseTweets) {
			this.baseTweets = baseTweets;
		}

		/**
		 * @return the retweets
		 */
		public List<Retweet> getRetweets() {
			return retweets;
		}

		/**
		 * set the retweets
		 * 
		 * @param retweets
		 *            new value
		 */
		public void setRetweets(final List<Retweet> retweets) {
			this.retweets = retweets;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result
					+ (baseTweets == null ? 0 : baseTweets.hashCode());
			result = prime * result
					+ (retweets == null ? 0 : retweets.hashCode());
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
			final Tweets other = (Tweets) obj;
			if (baseTweets == null) {
				if (other.baseTweets != null)
					return false;
			} else if (!baseTweets.equals(other.baseTweets))
				return false;
			if (retweets == null) {
				if (other.retweets != null)
					return false;
			} else if (!retweets.equals(other.retweets))
				return false;
			return true;
		}
	}

}