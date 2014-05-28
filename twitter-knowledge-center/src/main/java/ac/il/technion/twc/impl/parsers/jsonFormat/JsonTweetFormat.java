package ac.il.technion.twc.impl.parsers.jsonFormat;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import ac.il.technion.twc.api.parser.ParserFormat;
import ac.il.technion.twc.api.tweets.BaseTweet;
import ac.il.technion.twc.api.tweets.ID;
import ac.il.technion.twc.api.tweets.Retweet;
import ac.il.technion.twc.api.tweets.Tweet;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

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
public class JsonTweetFormat implements ParserFormat {

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

  @Override
  public boolean isFromFormat(final String line) {
    try {
      parse(line);
      return true;
    } catch (final ParseException e) {
      return false;
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
        final JsonElement jsonElement = object.get("in_reply_to_status_id_str");
        if (jsonElement != null && !jsonElement.isJsonNull())
          prevId = new ID(jsonElement.getAsString());
        final Date date =
            new SimpleDateFormat("EEE MMM d HH:mm:ss Z yyyy", Locale.ENGLISH)
                .parse(object.get("created_at").getAsString());
        return prevId == null ? new BaseTweet(date, id, hashtags)
            : new Retweet(date, id, prevId);
      } catch (final ParseException e) {
        // TODO not sure what to do here
        throw new JsonParseException(e);

      } catch (final NullPointerException e) {
        // TODO not sure what to do here
        // if one of the needed field (id or date) is not defined
        throw new JsonParseException(e);
      }

    }

    private List<String> readHashtags(final String text) {
      final List<String> $ = new ArrayList<>();
      for (final String word : text.split(" "))
        if (word.startsWith("#"))
          $.add(word.substring(1));
      return $;
    }
  }

}