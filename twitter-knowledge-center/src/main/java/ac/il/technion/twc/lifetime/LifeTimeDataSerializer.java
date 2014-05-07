package ac.il.technion.twc.lifetime;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import ac.il.technion.twc.message.ID;
import ac.il.technion.twc.message.tweet.BaseTweet;
import ac.il.technion.twc.message.tweet.Retweet;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.reflect.TypeToken;

public class LifeTimeDataSerializer implements JsonSerializer<LifeTimeData>,
		JsonDeserializer<LifeTimeData> {

	@Override
	public LifeTimeData deserialize(final JsonElement json, final Type type,
			final JsonDeserializationContext context) throws JsonParseException {
		System.out.println("deserialize called");
		final JsonObject jsonObject = json.getAsJsonObject();
		final Set<BaseTweet> baseTweets = context.deserialize(
				jsonObject.get("baseTweets"), new TypeToken<Set<BaseTweet>>() {
				}.getType());
		final Set<Retweet> retweets = context.deserialize(
				jsonObject.get("retweets"), new TypeToken<Set<Retweet>>() {
				}.getType());
		final Map<String, Long> m = context.deserialize(jsonObject.get("map"),
				new TypeToken<Map<String, Long>>() {
				}.getType());
		final Map<ID, Long> map = new HashMap<>();
		for (final Entry<String, Long> e : m.entrySet())
			map.put(new ID(e.getKey()), e.getValue());
		// for (final Entry<ID, Long> e : map.entrySet())
		// System.out.println(e.getKey().id + ":" + e.getValue());
		// for (final BaseTweet t : baseTweets)
		// System.out.println(t.id().id + ", " + t.date());
		// for (final Retweet t : retweets)
		// System.out.println(t.id().id + ", " + t.date() + ", " +
		// t.originId.id);
		return new LifeTimeData(map, baseTweets, retweets);
	}

	@Override
	public JsonElement serialize(final LifeTimeData lifeTime, final Type type,
			final JsonSerializationContext context) {
		System.out.println("serialize called");
		final JsonObject $ = new JsonObject();
		$.add("baseTweets", context.serialize(lifeTime.baseTweets));
		$.add("retweets", context.serialize(lifeTime.retweets));
		final Map<String, Long> m = new HashMap<>();
		for (final Entry<ID, Long> e : lifeTime.map.entrySet())
			m.put(e.getKey().id, e.getValue());
		$.add("map", context.serialize(m));
		// System.out.println($);
		return $;
	}

}
