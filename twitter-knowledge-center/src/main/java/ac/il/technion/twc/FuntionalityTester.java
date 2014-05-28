package ac.il.technion.twc;

import java.io.IOException;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.concurrent.Executors;

import ac.il.technion.twc.api.TwitterServicesCenter;
import ac.il.technion.twc.api.TwitterSystemBuilder;
import ac.il.technion.twc.api.parser.MultiFormatsParserBuilder;
import ac.il.technion.twc.api.parser.TweetsParser;
import ac.il.technion.twc.api.properties.PropertyRetriever;
import ac.il.technion.twc.api.properties.PropertyRetrieverImpl;
import ac.il.technion.twc.api.storage.FileHandler;
import ac.il.technion.twc.api.storage.PersistanceStorage;
import ac.il.technion.twc.api.storage.Storage;
import ac.il.technion.twc.api.tweets.ID;
import ac.il.technion.twc.impl.parsers.csFormat.CSFormatUtils;
import ac.il.technion.twc.impl.parsers.jsonFormat.JsonTweetFormat;
import ac.il.technion.twc.impl.properties.daymapping.DayMapping;
import ac.il.technion.twc.impl.properties.daymapping.DaysMappingBuilder;
import ac.il.technion.twc.impl.properties.hashtags.IdHashtags;
import ac.il.technion.twc.impl.properties.hashtags.IdHashtagsBuilder;
import ac.il.technion.twc.impl.properties.rootfinder.TransitiveRootFinder;
import ac.il.technion.twc.impl.properties.rootfinder.TransitivityBuilder;
import ac.il.technion.twc.impl.properties.tweetsretriever.TweetsRetriever;
import ac.il.technion.twc.impl.properties.tweetsretriever.TweetsRetrieverBuilder;
import ac.il.technion.twc.impl.services.histogram.DayHistogram;
import ac.il.technion.twc.impl.services.histogram.HistogramFormat;
import ac.il.technion.twc.impl.services.histogram.TemporalHistogram;
import ac.il.technion.twc.impl.services.lifetime.TweetToLifeTime;
import ac.il.technion.twc.impl.services.lifetime.TweetToLifeTimeSerializer;
import ac.il.technion.twc.impl.services.tagpopularity.TagToPopularity;

import com.google.gson.GsonBuilder;

/**
 * This class is meant to act as a wrapper to test your functionality. You
 * should implement all its methods and not change any of their signatures. You
 * can also implement an argumentless constructor if you wish, and any number of
 * new public methods
 * 
 * @author Gal Lalouche
 */
public class FuntionalityTester {

  private final TwitterServicesCenter serviceCenter;
  private final PersistanceStorage storage;
  private final TweetsParser parser;

  // TODO: need to find another way.
  private final DateFormat dateFormat = CSFormatUtils.getDateFormat();

  private final PropertyRetriever<DayMapping> dayMappingRetriever;
  private final PropertyRetriever<TransitiveRootFinder> rootFinderRetriever;
  private final PropertyRetriever<IdHashtags> idHashtagsRetriever;
  private final PropertyRetriever<TweetsRetriever> tweetsRetriever;

  private DayHistogram histogramService;
  private TemporalHistogram temporalHistogramService;
  private TweetToLifeTime lifeTimeService;
  private TagToPopularity tagPopularityService;

  /**
   * 
   */
  public FuntionalityTester() {
    final TwitterSystemBuilder systemBuilder =
        new TwitterSystemBuilder(
            new PropertyRetrieverImpl.PropertyRetrieverImplFactory(),
            new Storage(new GsonBuilder().create(), Paths.get("system"),
                new FileHandler(), Executors.newFixedThreadPool(5)),
            Executors.newCachedThreadPool());
    dayMappingRetriever =
        systemBuilder.registerBuilder(new DaysMappingBuilder());
    rootFinderRetriever =
        systemBuilder.registerBuilder(new TransitivityBuilder());
    idHashtagsRetriever =
        systemBuilder.registerBuilder(new IdHashtagsBuilder());
    tweetsRetriever =
        systemBuilder.registerBuilder(new TweetsRetrieverBuilder());

    serviceCenter = systemBuilder.getResult();
    storage =
        new Storage(new GsonBuilder()
            .setDateFormat("EEE MMM d HH:mm:ss Z yyyy")
            .registerTypeAdapter(TweetToLifeTime.class,
                new TweetToLifeTimeSerializer()).create(),
            Paths.get("services"), new FileHandler(),
            Executors.newFixedThreadPool(4));

    parser =
        new MultiFormatsParserBuilder()
            .addFormat(CSFormatUtils.getTweetFormatBuilder().getResult())
            .addFormat(new JsonTweetFormat()).getResult();
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
    final DayMapping dayMap = dayMappingRetriever.retrieve();
    final TransitiveRootFinder rootFinder = rootFinderRetriever.retrieve();
    final IdHashtags idHashtags = idHashtagsRetriever.retrieve();
    final TweetsRetriever tweets = tweetsRetriever.retrieve();

    storage.store(new DayHistogram(dayMap, new HistogramFormat()));
    storage.store(new TemporalHistogram(dayMap, new HistogramFormat()));
    storage.store(new TweetToLifeTime(rootFinder, tweets));
    storage.store(new TagToPopularity(rootFinder, tweets, idHashtags));
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
    storage.prepare(DayHistogram.class, TemporalHistogram.class,
        TagToPopularity.class, TweetToLifeTime.class);
    histogramService = storage.load(DayHistogram.class, DayHistogram.empty());
    temporalHistogramService =
        storage.load(TemporalHistogram.class, TemporalHistogram.empty());
    tagPopularityService =
        storage.load(TagToPopularity.class, TagToPopularity.empty());
    lifeTimeService =
        storage.load(TweetToLifeTime.class, TweetToLifeTime.empty());
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
    return lifeTimeService.getLifeTimeById(new ID(tweetId));
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
    return histogramService.get();
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
    return tagPopularityService.getPopularityByHashtag(hashtag).toString();
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
    return temporalHistogramService.get(dateFormat.parse(t1),
        dateFormat.parse(t2));
  }

  /**
   * Cleans up all persistent data from the system; this method will be called
   * before every test, to ensure that all tests are independent.
   */
  public void cleanPersistentData() {
    try {
      serviceCenter.clearSystem();
      storage.clear();
    } catch (final IOException e) {
      // TODO Not sure what to do here.
      throw new RuntimeException(e);
    }

  }
}