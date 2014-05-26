package ac.il.technion.twc.impl.parsers.jsonFormat;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import ac.il.technion.twc.api.tweets.BaseTweet;
import ac.il.technion.twc.api.tweets.ID;
import ac.il.technion.twc.api.tweets.Retweet;
import ac.il.technion.twc.api.tweets.Tweet;
import ac.il.technion.twc.impl.api.parser.ParserFormat;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

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
public class JSONTweetFormat implements ParserFormat {

  @Override
  public Tweet parse(final String line) throws ParseException {
    final Gson g =
        new GsonBuilder().registerTypeAdapter(JsonTweet.class,
            JsonTweetAdapter()).create();
    try {
      final JsonTweet tweet = g.fromJson(line, JsonTweet.class);
      return tweet.prevId == null ? new BaseTweet(tweet.date, tweet.id,
          tweet.hashtags) : new Retweet(tweet.date, tweet.id, tweet.prevId);
    } catch (final JsonSyntaxException e) {
      throw new ParseException(line, 0);
    }
  }

  private TypeAdapter<JsonTweet> JsonTweetAdapter() {
    return new TypeAdapter<JsonTweet>() {

      @Override
      public JsonTweet read(final JsonReader reader) throws IOException {
        final JsonTweet tweet = new JsonTweet();
        reader.beginObject();
        while (reader.hasNext()) {
          final String name = reader.nextName();
          if (name.equals("id_str"))
            tweet.id = new ID(reader.nextString());
          else if (name.equals("text"))
            tweet.hashtags = readHashtags(reader.nextString());
          else if (name.equals("created_at")) {
            final String dateStr = reader.nextString();
            try {
              tweet.date =
                  new SimpleDateFormat("EEE MMM d HH:mm:ss Z yyyy",
                      Locale.ENGLISH).parse(dateStr);
            } catch (final ParseException e) {
              // TODO not sure what to do here
              e.printStackTrace();
            }
          } else if (name.equals("in_reply_to_status_id_str")) {
            if (reader.peek().equals(JsonToken.NULL))
              reader.nextNull();
            else
              tweet.prevId = new ID(reader.nextString());
          } else
            reader.skipValue();
        }
        reader.endObject();
        return tweet;
      }

      private List<String> readHashtags(final String text) {
        final List<String> $ = new ArrayList<>();
        for (final String word : text.split(" "))
          if (word.startsWith("#"))
            $.add(word.substring(1));
        return $;
      }

      @Override
      public void write(final JsonWriter arg0, final JsonTweet arg1)
          throws IOException {
        // Not needed
      }

    };

  }

  private class JsonTweet {

    protected ID prevId;
    protected Date date;
    protected List<String> hashtags = new ArrayList<>();
    protected ID id;

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

}
