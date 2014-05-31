package acceptance;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ac.il.technion.twc.api.Property;
import ac.il.technion.twc.api.PropertyFactory;
import ac.il.technion.twc.api.ServiceSetup;
import ac.il.technion.twc.api.TwitterDataCenter;
import ac.il.technion.twc.api.TwitterDataCenterBuilder;
import ac.il.technion.twc.api.TwitterQuery;
import ac.il.technion.twc.api.TwitterQueryFactory;
import ac.il.technion.twc.api.core.TwitterSystemBuilder;
import ac.il.technion.twc.api.tweet.BaseTweet;
import ac.il.technion.twc.api.tweet.ID;
import ac.il.technion.twc.api.tweet.Retweet;
import ac.il.technion.twc.api.tweet.Tweet;

/**
 * A simple demonstration of the API's core capabilities.
 * 
 * @author Ophir De Jager
 * 
 */
public class SimpleUsageTest {

  @SuppressWarnings("javadoc")
  public static final class MyQueryFactory implements
      TwitterQueryFactory<MyQuery> {
    public MyQuery get(final MyProperty p) {
      return new MyQuery(p, 0);
    }
  }

  @SuppressWarnings("javadoc")
  public static class MyProperty implements Property {

    public final int numBase;
    public final int numRe;

    public MyProperty(final List<BaseTweet> baseTweets,
        final List<Retweet> retweets) {
      numBase = baseTweets.size();
      numRe = retweets.size();
    }

    public MyProperty(final List<BaseTweet> baseTweets,
        final List<Retweet> retweets, final int n) {
      numBase = baseTweets.size();
      numRe = retweets.size();
    }

  }

  @SuppressWarnings("javadoc")
  public static class MyQuery implements TwitterQuery {

    public final int numBase;
    public final int numRe;

    @ServiceSetup
    // Need this to tell TwitterDataCenter to use this constructor when
    // creating the query
    public MyQuery(final MyProperty p) {
      numBase = p.numBase;
      numRe = p.numRe;
    }

    public MyQuery(final MyProperty p, final int m) {
      numBase = p.numBase;
      numRe = p.numRe;
    }

  }

  private List<? extends Tweet> getSomeTweets() {
    return Arrays.asList(new BaseTweet(new Date(11111), new ID("base 1")),
        new BaseTweet(new Date(22222), new ID("base 2")), new Retweet(new Date(
            33333), new ID("retweet"), new ID("tweet")));
  }

  private int getNumRetweets(final List<? extends Tweet> tweets) {
    int numReActual = 0;
    for (final Tweet t : tweets)
      if (t instanceof Retweet)
        ++numReActual;
    return numReActual;
  }

  /**
   * @throws IOException
   */
  @Before
  public void setup() throws IOException {
    FileUtils.cleanDirectory(Paths.get("system", "Storage").toFile());
  }

  /**
   * @throws IOException
   */
  @After
  public void tearDown() throws IOException {
    FileUtils.cleanDirectory(Paths.get("system", "Storage").toFile());
  }

  /**
   * Test method for {@link TwitterDataCenterBuilder} and
   * {@link TwitterDataCenter}
   */
  @Test
  public final void simpleUsageTest() {
    // Create a builder for the data center
    final TwitterDataCenterBuilder builder = new TwitterSystemBuilder();
    // Add wanted properties and queries
    builder.addProperty(MyProperty.class).registerQuery(MyQuery.class);
    // Create the data center
    final TwitterDataCenter dataCenter = builder.build();

    // Let's get some tweets and count how many are retweets
    final List<? extends Tweet> tweets = getSomeTweets();
    final int numReActual = getNumRetweets(tweets);

    // Import the tweets
    dataCenter.importData(tweets);
    // Evaluate the queries
    dataCenter.loadServices();
    // Now we can ask the queries
    final MyQuery q = dataCenter.getService(MyQuery.class);
    assertEquals(numReActual, q.numRe);
    assertEquals(tweets.size() - numReActual, q.numBase);

    // cleanup of persistent storage
    dataCenter.clear();
  }

  /**
   * Test method for {@link TwitterDataCenterBuilder} and
   * {@link TwitterDataCenter}
   */
  @Test
  public final void simpleUsageTestWithoutTweets() {
    // Add wanted properties and queries and create the data center
    final TwitterDataCenter dataCenter =
        new TwitterSystemBuilder().addProperty(MyProperty.class)
            .registerQuery(MyQuery.class).build();
    // Evaluate the queries
    dataCenter.loadServices();
    // Now we can ask the queries
    final MyQuery q = dataCenter.getService(MyQuery.class);
    assertEquals(0, q.numRe);
    assertEquals(0, q.numBase);
  }

  /**
   * Test method for {@link TwitterDataCenterBuilder} and
   * {@link TwitterDataCenter}
   */
  @Test
  public final void simpleUsageTestWithFactories() {
    // Create a builder for the data center
    final TwitterDataCenterBuilder builder = new TwitterSystemBuilder();
    // Add wanted properties using factories
    builder.addProperty(MyProperty.class, new PropertyFactory<MyProperty>() {

      @Override
      public MyProperty get(final List<BaseTweet> baseTweets,
          final List<Retweet> retweets) {
        return new MyProperty(baseTweets, retweets, 0);
      }
    });
    // Add wanted queries using factories
    builder.registerQuery(MyQuery.class, new MyQueryFactory());

    // Create the data center
    final TwitterDataCenter dataCenter = builder.build();

    // Let's get some tweets and count how many are retweets
    final List<? extends Tweet> tweets = getSomeTweets();
    final int numReActual = getNumRetweets(tweets);

    // Import the tweets
    dataCenter.importData(tweets);
    // Evaluate the queries
    dataCenter.loadServices();
    // Now we can ask the queries
    final MyQuery q = dataCenter.getService(MyQuery.class);
    assertEquals(numReActual, q.numRe);
    assertEquals(tweets.size() - numReActual, q.numBase);

    // cleanup of persistent storage
    dataCenter.clear();
  }

}
