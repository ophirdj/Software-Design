package testsuites;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import ac.il.technion.twc.api.core.FileHandlerTest;
import ac.il.technion.twc.api.core.ServiceBuildingManagerTest;
import ac.il.technion.twc.api.core.StorageTest;
import ac.il.technion.twc.api.core.TweetsTest;
import ac.il.technion.twc.api.core.TwitterDataCenterImplTest;
import ac.il.technion.twc.api.parser.impl.MultiFormatsParserTest;
import ac.il.technion.twc.api.tweets.IDTest;
import ac.il.technion.twc.impl.parsers.csFormat.CSFormatTest;
import ac.il.technion.twc.impl.parsers.jsonFormat.JsonFormatTest;
import ac.il.technion.twc.impl.properties.daymapping.DayMappingTest;
import ac.il.technion.twc.impl.properties.daymapping.DayOfWeekTest;
import ac.il.technion.twc.impl.properties.hashtags.IdHashtagsTest;
import ac.il.technion.twc.impl.properties.rootfinder.TransitiveRootFinderTest;
import ac.il.technion.twc.impl.properties.tweetsretriever.TweetsRetrieverTest;
import ac.il.technion.twc.impl.services.histogram.DayHistogramTest;
import ac.il.technion.twc.impl.services.histogram.TemporalHistogramTest;
import ac.il.technion.twc.impl.services.lifetime.TweetToLifeTimeTest;
import ac.il.technion.twc.impl.services.tagpopularity.TagToPopularityTest;
import ac.il.technion.twc.system.PersistenceTest;
import ac.il.technion.twc.system.SampleTest;

/**
 * @author Ophir De Jager
 * 
 */
@RunWith(Suite.class)
@SuiteClasses({ FileHandlerTest.class, ServiceBuildingManagerTest.class,
		StorageTest.class, TweetsTest.class, TwitterDataCenterImplTest.class,
		MultiFormatsParserTest.class, IDTest.class, CSFormatTest.class,
		JsonFormatTest.class, DayMappingTest.class, DayOfWeekTest.class,
		IdHashtagsTest.class, TransitiveRootFinderTest.class,
		TweetsRetrieverTest.class, DayHistogramTest.class,
		TemporalHistogramTest.class, TweetToLifeTimeTest.class,
		TagToPopularityTest.class, PersistenceTest.class, SampleTest.class })
public class UnitTests {

}
