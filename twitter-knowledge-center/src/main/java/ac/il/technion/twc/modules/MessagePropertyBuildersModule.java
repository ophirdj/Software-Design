package ac.il.technion.twc.modules;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import ac.il.technion.twc.histogram.DayHistogramBuilder;
import ac.il.technion.twc.lifetime.LifeTimeBuilder;
import ac.il.technion.twc.lifetime.LifeTimeData;
import ac.il.technion.twc.lifetime.LifeTimeDataSerializer;
import ac.il.technion.twc.message.visitor.MessagePropertyBuilder;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.name.Named;
import com.google.inject.name.Names;

/**
 * @author Ziv Ronen
 * @date 07.05.2014
 * @mail akarks@gmail.com
 * 
 * @version 2.0
 * @since 2.0
 * 
 *        Guice module for global property building info
 */
public class MessagePropertyBuildersModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(DateFormat.class).annotatedWith(
				Names.named("twitter date formatter")).toInstance(
				new SimpleDateFormat("dd/MM/yyyy HH:mm:ss"));
	}

	/**
	 * @param dayHistogramBuilder
	 * @param lifeTimeBuilder
	 * @return All the builders for properties to be calculated
	 */
	@Provides
	List<MessagePropertyBuilder<?>> getPropertyBuilders(
			final DayHistogramBuilder dayHistogramBuilder,
			final LifeTimeBuilder lifeTimeBuilder) {
		final List<MessagePropertyBuilder<?>> $ = new ArrayList<>();
		$.add(dayHistogramBuilder);
		$.add(lifeTimeBuilder);
		return $;
	}

	/**
	 * @return Path for default storage directory of message properties.
	 */
	@Provides
	@Named("storage directory")
	Path storageDirectory() {
		return Paths.get("cache");
	}

	@Provides
	@Named("property serializer")
	Gson propertySerializer() {
		return new GsonBuilder().registerTypeAdapter(LifeTimeData.class,
				new LifeTimeDataSerializer()).create();
	}

}
