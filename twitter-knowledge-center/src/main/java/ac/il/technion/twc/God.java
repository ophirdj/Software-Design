package ac.il.technion.twc;

import ac.il.technion.twc.modules.DayHistogramModule;
import ac.il.technion.twc.modules.LifeTimeModule;
import ac.il.technion.twc.modules.MessagePropertyBuildersModule;

import com.google.inject.Guice;
import com.google.inject.Injector;

public class God {

	public static final Injector injector = Guice.createInjector(
			new MessagePropertyBuildersModule(), new DayHistogramModule(),
			new LifeTimeModule());

}
