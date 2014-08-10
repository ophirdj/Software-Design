package ac.il.technion.twc.impl.services.histogram;

import static junitparams.JUnitParamsRunner.$;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Test;
import org.junit.runner.RunWith;

import ac.il.technion.twc.impl.properties.daymapping.DayMapping;
import ac.il.technion.twc.impl.properties.daymapping.DayOfWeek;

/**
 * Tests for {@link DayHistogram}
 * 
 * @author Ziv Ronen
 * @date 28.05.2014
 * @mail akarks@gmail.com
 */
@RunWith(JUnitParamsRunner.class)
public class DayHistogramTest {

	private final DayMapping dayMappingMock;
	private final HistogramFormat histogramFormatMock;

	/**
	 * C'tor
	 */
	public DayHistogramTest() {
		dayMappingMock = mock(DayMapping.class);
		histogramFormatMock = mock(HistogramFormat.class);
	}

	/**
	 * Test method for {@link DayHistogram#DayHistogram(HistogramFormat)}
	 */
	@Test
	public void emptyHistogramShouldReturnAllZeros() {
		new DayHistogram(histogramFormatMock).get();
		verify(histogramFormatMock).formatHistogram(eq(new int[7]),
				eq(new int[7]));
	}

	@SuppressWarnings("unused")
	// used by JunitParams
	private Object[] testHistogram() {
		return $(
				// Basic empty test
				$(new int[] { 0, 0, 0, 0, 0, 0, 0 }, new int[] { 0, 0, 0, 0, 0,
						0, 0 }, new HashSet<>(), new HashSet<>()),

				// One sample, base, re and both
				$(new int[] { 1, 0, 0, 0, 0, 0, 0 },
						new int[] { 0, 0, 0, 0, 0, 0, 0 },
						makeSet(new DayOfWeek[] { DayOfWeek.SUNDAY },
								new int[] { 1 }),
						makeSet(new DayOfWeek[] {}, new int[] {})),
				$(new int[] { 0, 0, 0, 0, 0, 0, 0 },
						new int[] { 0, 2, 0, 0, 0, 0, 0 },
						makeSet(new DayOfWeek[] {}, new int[] {}),
						makeSet(new DayOfWeek[] { DayOfWeek.MONDAY },
								new int[] { 2 })),
				$(new int[] { 0, 0, 1, 0, 0, 0, 0 },
						new int[] { 0, 0, 0, 3, 0, 0, 0 },
						makeSet(new DayOfWeek[] { DayOfWeek.TUESDAY },
								new int[] { 1 }),
						makeSet(new DayOfWeek[] { DayOfWeek.WEDNESDAY },
								new int[] { 3 })),

				// Multiple different days samples, base, re and both
				$(new int[] { 2, 0, 0, 0, 10, 0, 0 },
						new int[] { 0, 0, 0, 0, 0, 0, 0 },
						makeSet(new DayOfWeek[] { DayOfWeek.SUNDAY,
								DayOfWeek.THURSDAY }, new int[] { 2, 10 }),
						makeSet(new DayOfWeek[] {}, new int[] {})),
				$(new int[] { 0, 0, 0, 0, 0, 0, 0 },
						new int[] { 0, 0, 0, 0, 0, 1, 5 },
						makeSet(new DayOfWeek[] {}, new int[] {}),
						makeSet(new DayOfWeek[] { DayOfWeek.FRIDAY,
								DayOfWeek.SATURDAY }, new int[] { 1, 5 })),
				$(new int[] { 0, 0, 1, 0, 0, 4, 6 },
						new int[] { 2, 0, 0, 3, 100, 0, 0 },
						makeSet(new DayOfWeek[] { DayOfWeek.TUESDAY,
								DayOfWeek.FRIDAY, DayOfWeek.SATURDAY },
								new int[] { 1, 4, 6 }),
						makeSet(new DayOfWeek[] { DayOfWeek.WEDNESDAY,
								DayOfWeek.SUNDAY, DayOfWeek.THURSDAY },
								new int[] { 3, 2, 100 })),

				// Multiple same days samples, base, re and both
				$(new int[] { 12, 0, 0, 0, 0, 0, 0 },
						new int[] { 0, 0, 0, 0, 0, 0, 0 },
						makeSet(new DayOfWeek[] { DayOfWeek.SUNDAY,
								DayOfWeek.SUNDAY }, new int[] { 2, 10 }),
						makeSet(new DayOfWeek[] {}, new int[] {})),
				$(new int[] { 0, 0, 0, 0, 0, 0, 0 },
						new int[] { 0, 0, 0, 0, 0, 4, 5 },
						makeSet(new DayOfWeek[] {}, new int[] {}),
						makeSet(new DayOfWeek[] { DayOfWeek.FRIDAY,
								DayOfWeek.SATURDAY, DayOfWeek.FRIDAY },
								new int[] { 1, 5, 3 })),
				$(new int[] { 0, 0, 7, 0, 0, 8, 7 },
						new int[] { 0, 0, 0, 105, 0, 0, 0 },
						makeSet(new DayOfWeek[] { DayOfWeek.TUESDAY,
								DayOfWeek.FRIDAY, DayOfWeek.SATURDAY,
								DayOfWeek.TUESDAY, DayOfWeek.FRIDAY,
								DayOfWeek.SATURDAY }, new int[] { 1, 4, 6, 6,
								4, 1 }),
						makeSet(new DayOfWeek[] { DayOfWeek.WEDNESDAY,
								DayOfWeek.WEDNESDAY, DayOfWeek.WEDNESDAY },
								new int[] { 3, 2, 100 })));

	}

	/**
	 * @param expectedBase
	 * @param expectedRe
	 * @param dataBase
	 * @param dataRe
	 */
	@Parameters(method = "testHistogram")
	@Test
	public void runHistogramTest(final int[] expectedBase,
			final int[] expectedRe, final Set<Entry<Long, Integer>> dataBase,
			final Set<Entry<Long, Integer>> dataRe) {
		when(dayMappingMock.getAllDaysBase()).thenReturn(dataBase);
		when(dayMappingMock.getAllDaysRe()).thenReturn(dataRe);
		new DayHistogram(dayMappingMock, histogramFormatMock).get();
		verify(histogramFormatMock).formatHistogram(eq(expectedBase),
				eq(expectedRe));
	}

	private Set<Entry<Long, Integer>> makeSet(final DayOfWeek[] days,
			final int[] amounts) {
		final Map<Long, Integer> m = new HashMap<>();
		final Random r = new Random(0L);
		for (int i = 0; i < days.length; i++) {
			final DateTime d = new DateTime(r.nextLong(), DateTimeZone.UTC);
			m.put(new DateTime(d.getMillis()
					+ (days[i].ordinal() - d.getDayOfWeek() % 7) * 24 * 60 * 60
					* 1000L, DateTimeZone.UTC).getMillis(), amounts[i]);
		}
		return m.entrySet();
	}
}
