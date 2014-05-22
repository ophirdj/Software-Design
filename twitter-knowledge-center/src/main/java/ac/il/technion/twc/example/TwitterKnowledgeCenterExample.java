package ac.il.technion.twc.example;

import ac.il.technion.twc.TwitterKnowledgeCenter;
import ac.il.technion.twc.api.PersistanceStorage;
import ac.il.technion.twc.api.TwitterServicesCenter;
import ac.il.technion.twc.api.TwitterServicesCenterBuilder;
import ac.il.technion.twc.api.parsers.TweetsParser;
import ac.il.technion.twc.api.properties.PropertyBuilder;
import ac.il.technion.twc.api.properties.PropertyRetriever;
import ac.il.technion.twc.api.services.ServiceBuilder;
import ac.il.technion.twc.example.mockClasses.APIBuilder;
import ac.il.technion.twc.example.mockClasses.Buidler1;
import ac.il.technion.twc.example.mockClasses.Buidler2;
import ac.il.technion.twc.example.mockClasses.MyOtherProperty;
import ac.il.technion.twc.example.mockClasses.MyOtherService;
import ac.il.technion.twc.example.mockClasses.MyOtherServiceBuilder;
import ac.il.technion.twc.example.mockClasses.MyParser;
import ac.il.technion.twc.example.mockClasses.MyProperty;
import ac.il.technion.twc.example.mockClasses.MyService;
import ac.il.technion.twc.example.mockClasses.MyServiceBuilder;

/**
 * This class is meant to act as a wrapper to test your functionality. You
 * should implement all its methods and not change any of their signatures. You
 * can also implement an argumentless constructor if you wish.
 * 
 * @author Gal Lalouche
 */
public class TwitterKnowledgeCenterExample {

  private PersistanceStorage storage;
  private final TwitterServicesCenter api;
  private MyService s1;
  private MyOtherService s2;
  private final PropertyRetriever<MyProperty> pr1;
  private final PropertyRetriever<MyOtherProperty> pr2;

  /**
   * C'tor.
   */
  public TwitterKnowledgeCenterExample() {
    final TwitterServicesCenterBuilder apiBuilder = new APIBuilder();
    final PropertyBuilder<MyProperty> b1 = new Buidler1();
    final PropertyBuilder<MyOtherProperty> b2 = new Buidler2();
    pr1 = apiBuilder.registerBuilder(b1);
    pr2 = apiBuilder.registerBuilder(b2);
    api = apiBuilder.getResult();
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
    final TweetsParser parser = new MyParser();
    api.importData(parser.parse(lines));
    final MyProperty p1 = pr1.retrieve();
    final MyOtherProperty p2 = pr2.retrieve();
    final ServiceBuilder sb1 = new MyServiceBuilder(p1, p2);
    final ServiceBuilder sb2 = new MyOtherServiceBuilder(p1);
    storage.store(sb1.getService());
    storage.store(sb2.getService());
  }

  /**
   * Loads the index, allowing for queries on the data that was imported using
   * {@link TwitterKnowledgeCenter#importData(String[])}. setupIndex will be
   * called before any queries can be run on the system
   * 
   * @throws Exception
   *           If for any reason, loading the index failed
   */
  public void setupIndex() throws Exception {
    s1 = storage.load(MyService.class, null);
    s2 = storage.load(MyOtherService.class, null);
  }

  /**
   * Gets the lifetime of the tweet, in milliseconds.
   * 
   * @param tweetId
   *          The tweet's identifier
   * @return A string, counting the number of milliseconds between the tweet's
   *         publication and its last retweet
   */
  public String getLifetimeOfTweets(final String tweetId) {
    return s1.get(tweetId);
  }

  /**
   * Gets the weekly histogram of all tweet data
   * 
   * @return An array of strings, each string in the format of
   *         ("<number of tweets (including retweets), number of retweets only>"
   *         ), for example: ["100, 10","250,20",...,"587,0"]. The 0th index of
   *         the array is Sunday.
   * @throws Exception
   *           If it is not possible to complete the operation
   */
  public String[] getDailyHistogram() throws Exception {
    return s2.get();
  }

  /**
   * Cleans up all persistent data from the system; this method will be called
   * before every test, to ensure that all tests are independent.
   */
  public void cleanPersistentData() {

    /*
     * If the api use PersistanceStorage of the same group as storage, the first
     * call will not be needed in our implementation. However, this can't be
     * assumed in general.
     */
    api.clearSystem(); // clears builders.
    storage.clearAll(); // clears any persistent data.
  }

}
