package ac.il.technion.twc.example.mockClasses;

import java.util.Collection;

import ac.il.technion.twc.api.parsers.TweetsParser;
import ac.il.technion.twc.api.tweets.Tweet;

public class MyParser implements TweetsParser {

  @Override
  public Collection<Tweet> parse(final String... lines) {
    return null;
  }

}
