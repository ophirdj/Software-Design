package acceptance;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ac.il.technion.twc.TestUtils;
import ac.il.technion.twc.api.QuerySetup;
import ac.il.technion.twc.api.TwitterDataCenter;
import ac.il.technion.twc.api.TwitterQuery;
import ac.il.technion.twc.api.TwitterQuerySerializer;
import ac.il.technion.twc.api.core.TwitterDataCenterBuilder;
import ac.il.technion.twc.api.properties.OriginFinder;
import ac.il.technion.twc.api.properties.OriginFinder.NotFoundException;
import ac.il.technion.twc.api.properties.TweetsRetriever;
import ac.il.technion.twc.api.tweet.BaseTweet;
import ac.il.technion.twc.api.tweet.ID;
import ac.il.technion.twc.api.tweet.Retweet;
import ac.il.technion.twc.api.tweet.Tweet;
import ac.il.technion.twc.api.tweet.parser.TweetParser;
import ac.il.technion.twc.impl.parser.JsonTweetFormat;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.reflect.TypeToken;

/**
 * Some more advanced usage of the API.
 * 
 * @author Ophir De Jager
 * 
 */
public class RealWorldExample {

  private static final Path dir = Paths.get("RealWorldUsageTest");

  /**
   * @throws IOException
   */
  @Before
  public void setup() throws IOException {
    if (Files.exists(dir) && Files.isDirectory(dir))
      FileUtils.deleteDirectory(dir.toFile());
  }

  /**
   * @throws IOException
   */
  @After
  public void tearDown() throws IOException {
    if (Files.exists(dir) && Files.isDirectory(dir))
      FileUtils.deleteDirectory(dir.toFile());
  }

  /**
   * @throws ParseException
   */
  @Test
  public final void usingPredefinedPropertiesAndCustomSerializerForQuery()
      throws ParseException {
    // Add predefined properties, register query, and add a custom query
    // serializer
    final TwitterDataCenter dataCenter =
        new TwitterDataCenterBuilder(dir.resolve("usingPredefined"))
            .addProperty(TweetsRetriever.class).addProperty(OriginFinder.class)
            .registerQuery(RetweetCounter.class)
            .addSerializer(new RetweetCounterSerializer()).build();
    // Create a parser for Json format (using JsonTweetFormat from the impl
    // package)
    final TweetParser parser = new TweetParser(new JsonTweetFormat());
    // Import 10 base tweets, where each tweet has 10 retweets, and each of
    // them has 10 retweets (so every base tweet has a total of 110
    // retweets).
    final int numBaseTweets = 10;
    final List<Tweet> tweets =
        parser.parse(TestUtils.generateTweets(numBaseTweets, 10, 2, 0));
    dataCenter.importData(tweets);
    // Prepare queries
    dataCenter.evaluateQueries();
    // Get our query
    final RetweetCounter counter = dataCenter.getQuery(RetweetCounter.class);

    // validate that, indeed, each base tweet has 110 retweets as predicted
    for (int baseTweet = 0; baseTweet < numBaseTweets; ++baseTweet)
      assertEquals(10 + 10 * 10,
          counter.getNumRetweets(new ID("base" + baseTweet)));

    // clear persistent storage
    dataCenter.clear();

    dataCenter.evaluateQueries();
    // Get our query
    final RetweetCounter counterEmpty =
        dataCenter.getQuery(RetweetCounter.class);

    // validate that, indeed, the base tweets are registered no more
    for (int baseTweet = 0; baseTweet < numBaseTweets; ++baseTweet)
      try {
        System.out.println(counterEmpty.getNumRetweets(new ID("base"
            + baseTweet)));
        fail("didn't clear the data");
      } catch (final IllegalArgumentException e) {
        assertEquals("No base tweet with this ID!", e.getMessage());
      }

    // import partial list
    dataCenter.importData(tweets.subList(0, 6));

    // create another TwitterDataCenter to simulate shutdown and restore
    final TwitterDataCenter simulateShutdown =
        new TwitterDataCenterBuilder(dir.resolve("usingPredefined"))
            .addProperty(TweetsRetriever.class).addProperty(OriginFinder.class)
            .registerQuery(RetweetCounter.class)
            .addSerializer(new RetweetCounterSerializer()).build();

    // upload the rest of the tweet
    simulateShutdown.importData(tweets.subList(6, tweets.size()));

    // evaluate the query
    simulateShutdown.evaluateQueries();

    // Get our query
    final RetweetCounter afterShutdownCounter =
        simulateShutdown.getQuery(RetweetCounter.class);

    // validate that, indeed, each base tweet has 110 retweets as predicted
    for (int baseTweet = 0; baseTweet < numBaseTweets; ++baseTweet)
      assertEquals(10 + 10 * 10,
          afterShutdownCounter.getNumRetweets(new ID("base" + baseTweet)));

    // clear persistent storage
    simulateShutdown.clear();
  }

  /**
   * Counts how many retweets each base tweet has
   * 
   * @author Ophir De Jager
   * 
   */
  public static class RetweetCounter implements TwitterQuery {

    private final Map<ID, Integer> followersFromID;

    /**
     * @param retriever
     * @param finder
     */
    @QuerySetup
    public RetweetCounter(final TweetsRetriever retriever,
        final OriginFinder finder) {
      followersFromID = new HashMap<ID, Integer>();
      for (final BaseTweet base : retriever.getBaseTweets())
        followersFromID.put(base.id, 0);
      for (final Retweet retweet : retriever.getRetweets())
        try {
          final ID origin = finder.origin(retweet).id;
          followersFromID.put(origin, followersFromID.get(origin) + 1);
        } catch (final NotFoundException e) {
          continue;
        }
    }

    /**
     * Constructor for de-serializer to use
     * 
     * @param followersFromID
     */
    RetweetCounter(final Map<ID, Integer> followersFromID) {
      this.followersFromID = followersFromID;
    }

    /**
     * @param baseTweet
     *          A base tweet ID.
     * @return Number of retweets of this base tweet.
     */
    public int getNumRetweets(final ID baseTweet) {
      if (!followersFromID.containsKey(baseTweet))
        throw new IllegalArgumentException("No base tweet with this ID!");
      return followersFromID.get(baseTweet);
    }

  }

  /**
   * A {@link TwitterQuerySerializer} for {@link RetweetCounter}
   * 
   * @author Ophir De Jager
   * 
   */
  public static class RetweetCounterSerializer implements
      TwitterQuerySerializer<RetweetCounter> {

    private final Gson gson = new GsonBuilder().registerTypeAdapter(
        RetweetCounter.class, new TweetToLifeTimeJsonSerializer()).create();

    private static class TweetToLifeTimeJsonSerializer implements
        JsonSerializer<RetweetCounter>, JsonDeserializer<RetweetCounter> {
      @Override
      public RetweetCounter deserialize(final JsonElement json,
          final Type type, final JsonDeserializationContext context)
          throws JsonParseException {
        final JsonObject jsonObject = json.getAsJsonObject();
        final Map<String, Integer> m =
            context.deserialize(jsonObject.get("followersFromID"),
                new TypeToken<HashMap<String, Integer>>() {
                }.getType());
        final Map<ID, Integer> followersFromID = new HashMap<>();
        for (final Entry<String, Integer> e : m.entrySet())
          followersFromID.put(new ID(e.getKey()), e.getValue());
        return new RetweetCounter(followersFromID);
      }

      @Override
      public JsonElement serialize(final RetweetCounter counter,
          final Type type, final JsonSerializationContext context) {
        final JsonObject $ = new JsonObject();
        final Map<String, Integer> followersFromID = new HashMap<>();
        for (final Entry<ID, Integer> e : counter.followersFromID.entrySet())
          followersFromID.put(e.getKey().id, e.getValue());
        $.add("followersFromID", context.serialize(followersFromID));
        return $;
      }
    }

    @Override
    public String objectToString(final RetweetCounter t) {
      return gson.toJson(t);
    }

    @Override
    public RetweetCounter stringToObject(final String s) {
      return gson.fromJson(s, RetweetCounter.class);
    }

    @Override
    public Class<RetweetCounter> getType() {
      return RetweetCounter.class;
    }

  }

}
