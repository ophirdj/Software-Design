package ac.il.technion.twc.impl.services.lifetime;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import ac.il.technion.twc.api.tweets.ID;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.reflect.TypeToken;

/**
 * Serializer for {@link LifeTimeCache} because Gson has bugs serializing it.
 * 
 * @author Ziv Ronen
 * @date 07.05.2014
 * @mail akarks@gmail.com
 * 
 * @version 2.0
 * @since 2.0
 */
public class TweetToLifeTimeSerializer implements
    JsonSerializer<TweetToLifeTime>, JsonDeserializer<TweetToLifeTime> {

  @Override
  public TweetToLifeTime deserialize(final JsonElement json, final Type type,
      final JsonDeserializationContext context) throws JsonParseException {
    // System.out.println("deserialize called");
    final JsonObject jsonObject = json.getAsJsonObject();
    final Map<String, Long> m =
        context.deserialize(jsonObject.get("lifeTimeFromId"),
            new TypeToken<HashMap<String, Long>>() {
            }.getType());
    final Map<ID, Long> lifeTimeFromId = new HashMap<>();
    for (final Entry<String, Long> e : m.entrySet())
      lifeTimeFromId.put(new ID(e.getKey()), e.getValue());
    return new TweetToLifeTime(lifeTimeFromId);
  }

  @Override
  public JsonElement serialize(final TweetToLifeTime lifeTime, final Type type,
      final JsonSerializationContext context) {
    // System.out.println("serialize called");
    final JsonObject $ = new JsonObject();
    final Map<String, Long> lifeTimeFromId = new HashMap<>();
    for (final Entry<ID, Long> e : lifeTime.lifeTimeFromId.entrySet())
      lifeTimeFromId.put(e.getKey().id, e.getValue());
    $.add("lifeTimeFromId", context.serialize(lifeTimeFromId));
    return $;
  }

}
