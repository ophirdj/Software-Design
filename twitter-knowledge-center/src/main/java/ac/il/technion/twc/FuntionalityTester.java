package ac.il.technion.twc;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import ac.il.technion.twc.api.TwitterDataCenter;
import ac.il.technion.twc.api.core.TwitterSystemBuilder;
import ac.il.technion.twc.api.tweet.ID;
import ac.il.technion.twc.api.tweet.parser.MultiFormatsParserBuilder;
import ac.il.technion.twc.api.tweet.parser.TweetParser;
import ac.il.technion.twc.impl.parser.CommaSeparatedTweetFormat;
import ac.il.technion.twc.impl.parser.JsonTweetFormat;
import ac.il.technion.twc.impl.properties.daymapping.DayMapping;
import ac.il.technion.twc.impl.properties.hashtags.IdHashtags;
import ac.il.technion.twc.impl.properties.originfinder.OriginFinder;
import ac.il.technion.twc.impl.properties.tweetsretriever.TweetsRetriever;
import ac.il.technion.twc.impl.services.histogram.DayHistogram;
import ac.il.technion.twc.impl.services.histogram.TemporalHistogram;
import ac.il.technion.twc.impl.services.lifetime.TweetToLifeTime;
import ac.il.technion.twc.impl.services.lifetime.TweetToLifeTimeSerializer;
import ac.il.technion.twc.impl.services.tagpopularity.TagToPopularity;

/**
 * This class is meant to act as a wrapper to test your functionality. You
 * should implement all its methods and not change any of their signatures. You
 * can also implement an argumentless constructor if you wish, and any number of
 * new public methods
 * 
 * @author Gal Lalouche
 */
public class FuntionalityTester {

  private final TwitterDataCenter serviceCenter;
  private final TweetParser parser;

  private static final SimpleDateFormat temporalDateFormat =
      new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

  /**
	 * 
	 */
  public FuntionalityTester() {
    serviceCenter =
        new TwitterSystemBuilder()
            .addSerializer(new TweetToLifeTimeSerializer())
            .addProperty(DayMapping.class).addProperty(OriginFinder.class)
            .addProperty(IdHashtags.class).addProperty(TweetsRetriever.class)
            .registerQuery(DayHistogram.class)
            .registerQuery(TemporalHistogram.class)
            .registerQuery(TweetToLifeTime.class)
            .registerQuery(TagToPopularity.class).build();
    parser =
        new MultiFormatsParserBuilder().add(new CommaSeparatedTweetFormat())
            .add(new JsonTweetFormat()).build();
  }

  /**
   * Loads the data from an array of lines
   * 
   * @param lines
   *          An array of lines, each line formatted as <time (dd/MM/yyyy
   *          HH:mm:ss)>,<tweet id>[,original tweet]
   * @throws Exception
   *           If for any reason, handling the data failed
   */
  public void importData(final String[] lines) throws Exception {
    generalImportLine(lines);
  }

  private void generalImportLine(final String[] lines) throws IOException,
      ParseException {
    serviceCenter.importData(parser.parse(lines));
  }

  /**
   * Loads the data from an array of lines
   * 
   * @param lines
   *          An array of lines, each line is a JSON string
   * @throws Exception
   *           If for any reason, handling the data failed
   */
  public void importDataJson(final String[] lines) throws Exception {
    generalImportLine(lines);
  }

  /**
   * Loads the index, allowing for queries on the data that was imported using
   * {@link FuntionalityTester#importData(String[])}. setupIndex will be called
   * before any queries can be run on the system
   * 
   * @throws Exception
   *           If for any reason, loading the index failed
   */
  public void setupIndex() throws Exception {
    serviceCenter.loadServices();
  }

  /**
   * Gets the lifetime of the tweet, in milliseconds. You may assume we will ask
   * about the lifetime of a retweet, but only about the lifetime of an original
   * tweet.
   * 
   * @param tweetId
   *          The tweet's identifier
   * @return A string, counting the number of milliseconds between the tweet's
   *         publication and its last retweet (recursive)
   * @throws Exception
   *           If it is not possible to complete the operation
   */
  public String getLifetimeOfTweets(final String tweetId) throws Exception {
    return Long.toString(serviceCenter.getService(TweetToLifeTime.class)
        .getLifeTimeById(new ID(tweetId)));
  }

  /**
   * Gets the weekly histogram of all tweet and retweet data
   * 
   * @return An array of strings, each string in the format of
   *         ("<number of tweets (including retweets), number of retweets only>"
   *         ), for example: ["100, 10","250,20",...,"587,0"]. The 0th index of
   *         the array is Sunday.
   */
  public String[] getDailyHistogram() {
    return serviceCenter.getService(DayHistogram.class).get();
  }

  /**
   * Gets the number of (recursive) retweets made to all original tweets made
   * that contain a specific hashtag
   * 
   * @param hashtag
   *          The hashtag to check
   * @return A string, in the format of a number, contain the number of retweets
   */
  public String getHashtagPopularity(final String hashtag) {
    return Integer.toString(serviceCenter.getService(TagToPopularity.class)
        .getPopularityByHashtag(hashtag));
  }

  /**
   * Gets the weekly histogram of all tweet data
   * 
   * @param t1
   *          A date string in the format of <b>dd/MM/yyyy HH:mm:ss</b>; all
   *          tweets counted in the histogram should have been published
   *          <b>after<\b> t1.
   * @param t2
   *          A date string in the format of <b>dd/MM/yyyy HH:mm:ss</b>; all
   *          tweets counted in the histogram should have been published
   *          <b>before<\b> t2.
   * @return An array of strings, each string in the format of
   *         ("<number of tweets (including retweets), number of retweets only>"
   *         ), for example: ["100, 10","250,20",...,"587,0"]. The 0th index of
   *         the array is Sunday.
   * @throws Exception
   *           If it is not possible to complete the operation
   */
  public String[] getTemporalHistogram(final String t1, final String t2)
      throws Exception {
    return serviceCenter.getService(TemporalHistogram.class).get(
        temporalDateFormat.parse(t1), temporalDateFormat.parse(t2));
  }

  /**
   * Cleans up all persistent data from the system; this method will be called
   * before every test, to ensure that all tests are independent.
   */
  public void cleanPersistentData() {
    serviceCenter.clear();
  }
}
