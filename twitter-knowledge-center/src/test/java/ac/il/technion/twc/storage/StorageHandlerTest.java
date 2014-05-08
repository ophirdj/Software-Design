package ac.il.technion.twc.storage;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

import ac.il.technion.twc.TwitterKnowledgeCenter;
import ac.il.technion.twc.histogram.DayHistogram;
import ac.il.technion.twc.histogram.DayOfWeek;
import ac.il.technion.twc.lifetime.LifeTimeData;
import ac.il.technion.twc.message.ID;
import ac.il.technion.twc.message.tweet.BaseTweet;
import ac.il.technion.twc.message.tweet.Retweet;

import com.google.gson.Gson;
import com.google.inject.Key;
import com.google.inject.name.Names;

/**
 * Test for StorageHandler
 * 
 * @author Ziv Ronen
 * @date 07.05.2014
 * @mail akarks@gmail.com
 * 
 * @version 2.0
 * @since 2.0
 */
public class StorageHandlerTest {

	private final Gson gson;
	private final FileHandler fileHandlingMock;
	private final Path testPath = Paths.get("test/cache");
	private final StorageHandler<DayHistogram> underTestHistogram;
	private final StorageHandler<LifeTimeData> underTestLifeTime;

	/**
	 * C'tor
	 */
	public StorageHandlerTest() {
		fileHandlingMock = mock(FileHandler.class);
		gson = TwitterKnowledgeCenter.injector.getInstance(Key.get(Gson.class,
				Names.named("serializer")));
		underTestHistogram = new StorageHandler<>(gson, testPath,
				fileHandlingMock);
		underTestLifeTime = new StorageHandler<>(gson, testPath,
				fileHandlingMock);
	}

	/**
	 * Test method for: {@link StorageHandler#load(Object)}.
	 * 
	 * @throws IOException
	 */
	@Test
	public void histogramLoadWithoutPreviousStoreShouldReturnDefaultValue()
			throws IOException {
		when(fileHandlingMock.load(testPath)).thenThrow(new IOException());
		final DayHistogram mockHistogram = mock(DayHistogram.class);
		assertEquals(mockHistogram, underTestHistogram.load(mockHistogram));
		verify(fileHandlingMock).load(testPath);
	}

	/**
	 * Test method for: {@link StorageHandler#load(Object)},
	 * {@link StorageHandler#store(Object)}.
	 * 
	 * @throws IOException
	 */
	@Test
	public void histogramLoadWithPreviousStoreShouldReturnPreviousStore()
			throws IOException {
		final DayHistogram mockHistogramDefault = TwitterKnowledgeCenter.injector
				.getInstance(Key.get(DayHistogram.class, Names.named("default")));
		final DayHistogram returnVal = nonEnptyHistogram();
		final String json = gson.toJson(returnVal, DayHistogram.class);
		when(fileHandlingMock.load(testPath)).thenReturn(json);
		assertEquals(returnVal, underTestHistogram.load(mockHistogramDefault));
	}

	/**
	 * Test method for: {@link StorageHandler#store(Object)}.
	 * 
	 * @throws IOException
	 */
	@Test
	public void histogramStoreShouldCallFileHandlerStoreWithCorrectJsonString()
			throws IOException {
		final DayHistogram returnVal = nonEnptyHistogram();
		underTestHistogram.store(returnVal);
		verify(fileHandlingMock).store(testPath,
				gson.toJson(returnVal, DayHistogram.class));
	}

	private DayHistogram nonEnptyHistogram() {
		final Map<DayOfWeek, Integer> tweets = new EnumMap<>(DayOfWeek.class);
		final Map<DayOfWeek, Integer> retweets = new EnumMap<>(DayOfWeek.class);
		for (final DayOfWeek day : DayOfWeek.values()) {
			tweets.put(day, 0);
			retweets.put(day, 0);
		}
		tweets.put(DayOfWeek.WEDNESDAY, 1);
		final DayHistogram returnVal = new DayHistogram(tweets, retweets);
		return returnVal;
	}

	/**
	 * Test method for: {@link StorageHandler#load(Object)}.
	 * 
	 * @throws IOException
	 */
	@Test
	public void lifeTimeLoadWithoutPreviousStoreShouldReturnDefaultValue()
			throws IOException {
		when(fileHandlingMock.load(testPath)).thenThrow(new IOException());
		final LifeTimeData mockLifeTime = mock(LifeTimeData.class);
		assertEquals(mockLifeTime, underTestLifeTime.load(mockLifeTime));
		verify(fileHandlingMock).load(testPath);
	}

	/**
	 * Test method for: {@link StorageHandler#load(Object)},
	 * {@link StorageHandler#store(Object)}.
	 * 
	 * @throws IOException
	 */
	@Test
	public void lifeTimeLoadWithPreviousStoreShouldReturnPreviousStore()
			throws IOException {
		final LifeTimeData mockLifeTime = TwitterKnowledgeCenter.injector
				.getInstance(Key.get(LifeTimeData.class, Names.named("default")));
		final LifeTimeData returnVal = nonEmptyLifeTimeData();
		when(fileHandlingMock.load(testPath)).thenReturn(
				gson.toJson(returnVal, LifeTimeData.class));
		assertEquals(returnVal, underTestLifeTime.load(mockLifeTime));
	}

	/**
	 * Test method for: {@link StorageHandler#store(Object)}.
	 * 
	 * @throws IOException
	 */
	@Test
	public void lifeTimeStoreShouldCallFileHandlerStoreWithCorrectJsonString()
			throws IOException {
		final LifeTimeData returnVal = nonEmptyLifeTimeData();
		underTestLifeTime.store(returnVal);
		verify(fileHandlingMock).store(testPath,
				gson.toJson(returnVal, LifeTimeData.class));
	}

	private LifeTimeData nonEmptyLifeTimeData() {
		final Set<BaseTweet> baseTweets = new HashSet<>();
		final Set<Retweet> retweets = new HashSet<>();
		final BaseTweet baseTweet = new BaseTweet(new Date(1L), new ID("123"));
		baseTweets.add(baseTweet);
		final Retweet retweet = new Retweet(new Date(101L), new ID("456"),
				new ID("123"));
		retweets.add(retweet);

		final LifeTimeData returnVal = new LifeTimeData(baseTweets, retweets);
		return returnVal;
	}
}
