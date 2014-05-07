package ac.il.technion.twc.lifetime;

import java.util.HashMap;
import java.util.Map;

import ac.il.technion.twc.message.ID;
import ac.il.technion.twc.message.tweet.BaseTweet;
import ac.il.technion.twc.message.tweet.Retweet;
import ac.il.technion.twc.message.tweet.Tweet;

public class TransitiveRootFinder {

  Map<ID, ID> relation = new HashMap<>();
  Map<ID, BaseTweet> baseTweets = new HashMap<>();

  void addTweet(final Retweet retweet) {
    relation.put(retweet.getId(), retweet.originId);
  }

  void addTweet(final BaseTweet tweet) {
    baseTweets.put(tweet.getId(), tweet);
  }

  public BaseTweet findRoot(final Tweet tweet) throws NoRootFoundException {
    final ID rootId = findRootAux(tweet.getId());
    if (!baseTweets.containsKey(rootId))
      throw new NoRootFoundException();
    return baseTweets.get(rootId);
  }

  private ID findRootAux(final ID currentId) {
    if (!relation.containsKey(currentId))
      return currentId;
    final ID rootId = findRootAux(relation.get(currentId));
    relation.put(currentId, rootId);
    return rootId;
  }

  public static class NoRootFoundException extends Exception {

  }
}
