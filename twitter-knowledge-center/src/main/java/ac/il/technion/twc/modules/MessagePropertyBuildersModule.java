package ac.il.technion.twc.modules;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import ac.il.technion.twc.histogram.DayHistogramBuilder;
import ac.il.technion.twc.lifetime.LifeTimeBuilder;
import ac.il.technion.twc.message.visitor.MessagePropertyBuilder;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.name.Named;
import com.google.inject.name.Names;

public class MessagePropertyBuildersModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(DateFormat.class).annotatedWith(
				Names.named("twitter date formatter")).toInstance(
				new SimpleDateFormat("dd/MM/yyyy HH:mm:ss"));
	}

	@Provides
	List<MessagePropertyBuilder<?>> getPropertyBuilders(
			final DayHistogramBuilder dayHistogramBuilder,
			final LifeTimeBuilder lifeTimeBuilder) {
		final List<MessagePropertyBuilder<?>> $ = new ArrayList<>();
		$.add(dayHistogramBuilder);
		$.add(lifeTimeBuilder);
		return $;
	}

	@Provides
	@Named("root storage directory")
	Path storageDirectory() {
		return Paths.get("../");
	}

}
