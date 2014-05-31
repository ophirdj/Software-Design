package acceptance;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import ac.il.technion.twc.api.Property;
import ac.il.technion.twc.api.PropertyFactory;
import ac.il.technion.twc.api.tweet.BaseTweet;
import ac.il.technion.twc.api.tweet.Retweet;

public class AdvancedUsageTest {
	
	public static class UselessProperty implements Property {
		
		public static class UselessPropertyFactory implements PropertyFactory<UselessProperty> {

			@Override
			public UselessProperty get(List<BaseTweet> baseTweets,
					List<Retweet> retweets) {
				return new UselessProperty();
			}
			
		}
		
	}

	@Test
	public final void test() {
		fail("Not yet implemented"); // TODO
	}

}
