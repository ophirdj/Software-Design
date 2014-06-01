package ac.il.technion.twc.impl.parser;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import ac.il.technion.twc.api.tweet.BaseTweet;
import ac.il.technion.twc.api.tweet.ID;
import ac.il.technion.twc.api.tweet.Retweet;
import ac.il.technion.twc.api.tweet.Tweet;
import ac.il.technion.twc.api.tweet.parser.TweetFormat;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.twitter.Extractor;

/**
 * Rule for parsing a tweet from json format
 * 
 * @author Ziv Ronen
 * @date 23.05.2014
 * @mail akarks@gmail.com
 * 
 * @version 2.0
 * @since 2.0
 */
public class JsonTweetFormat implements TweetFormat {

  private final Gson gson = new GsonBuilder().registerTypeAdapter(Tweet.class,
      new TweetDeserializer()).create();

  @Override
  public Tweet parse(final String line) throws ParseException {
    if (null == line || line.equals(""))
      throw new ParseException("Bad tweet format: " + line, 0);
    try {
      return gson.fromJson(line, Tweet.class);
    } catch (final JsonParseException e) {
      throw new ParseException("Bad tweet format: " + line, 0);
    }
  }

  private static final class TweetDeserializer implements
      JsonDeserializer<Tweet> {

    @Override
    public Tweet deserialize(final JsonElement json, final Type typeOfT,
        final JsonDeserializationContext context) throws JsonParseException {
      try {
        final JsonObject object = json.getAsJsonObject();
        final ID id = new ID(object.get("id_str").getAsString());
        final List<String> hashtags =
            readHashtags(object.get("text").getAsString());
        ID prevId = null;
        final JsonElement retweetOf = object.get("in_reply_to_status_id_str");
        if (retweetOf != null && !retweetOf.isJsonNull())
          prevId = new ID(retweetOf.getAsString());
        final Date date =
            new SimpleDateFormat("EEE MMM d HH:mm:ss Z yyyy", Locale.ENGLISH)
                .parse(object.get("created_at").getAsString());

        ID originId = null;
        List<String> originHashtags = null;
        final JsonElement oldTweet = object.get("retweeted_status");
        if (oldTweet != null && !oldTweet.isJsonNull()) {
          final JsonObject oldTweetObject = oldTweet.getAsJsonObject();
          originId = new ID(oldTweetObject.get("id_str").getAsString());
          originHashtags =
              readHashtags(oldTweetObject.get("text").getAsString());
        }
        if (prevId != null)
          return new Retweet(date, id, prevId, hashtags, originId,
              originHashtags);
        return originId == null ? new BaseTweet(date, id, hashtags)
            : new Retweet(date, id, originId, hashtags, originId,
                originHashtags);
      } catch (final ParseException | NullPointerException e) {
        throw new JsonParseException(e);
      }
    }

    private List<String> readHashtags(final String text) {
      return new Extractor().extractHashtags(text);
    }
  }

}
