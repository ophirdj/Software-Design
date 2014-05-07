package ac.il.technion.twc.lifetime;

import java.util.HashMap;
import java.util.Map;

import ac.il.technion.twc.message.tweet.BaseTweet;
import ac.il.technion.twc.message.tweet.Retweet;
import ac.il.technion.twc.message.tweet.Tweet;

public class TransitiveRootFinder {

  Map<Retweet, Tweet> relation = new HashMap<>();

  void addElement(final Retweet current, final Tweet original) {
    relation.put(current, original);
  }

  public BaseTweet findRoot(final Retweet current) throws NoRootFoundException {
    final Tweet tweet = findRootAux(current);
    if (tweet.getClass() != BaseTweet.class)
      throw new NoRootFoundException();
    return (BaseTweet) tweet;
  }

  private Tweet findRootAux(final Tweet current) {
    if (!relation.containsKey(current))
      return current;
    final Tweet baseTweet = findRootAux(relation.get(current));
    relation.put((Retweet) current, baseTweet);
    return baseTweet;
  }

  public static class NoRootFoundException extends Exception {

  }
}
