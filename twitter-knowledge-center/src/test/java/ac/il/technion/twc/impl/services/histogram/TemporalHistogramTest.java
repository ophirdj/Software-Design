package ac.il.technion.twc.impl.services.histogram;

import static junitparams.JUnitParamsRunner.$;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

import org.junit.Test;
import org.junit.runner.RunWith;

import ac.il.technion.twc.impl.properties.daymapping.DayMapping;

/**
 * Tests for {@link TemporalHistogram}
 * 
 * @author Ziv Ronen
 * @date 28.05.2014
 * @mail akarks@gmail.com
 * 
 * @version 2.0
 * @since 2.0
 */
@RunWith(JUnitParamsRunner.class)
public class TemporalHistogramTest {

  private final DayMapping dayMappingMock;
  private final HistogramFormat histogramFormatMock;

  private final Date[] dates;
  private final int[] place;

  private final int[] valuesBase = new int[] { 1, 0, 4, 5, 0, 6, 7, 0, 0, 2, 1,
      3, 0, 0, 1, 10, 18, 0, 0 };
  private final int[] valuesRe = new int[] { 10, 0, 3, 2, 12, 0, 2, 0, 0, 2, 1,
      0, 7, 2, 0, 0, 0, 6, 1 };

  /**
   * C'tor
   */
  public TemporalHistogramTest() {
    dayMappingMock = mock(DayMapping.class);
    histogramFormatMock = mock(HistogramFormat.class);

    dates = new Date[19];
    dates[0] = new GregorianCalendar(2014, 3, 1, 2, 5, 3).getTime();
    dates[1] = new GregorianCalendar(2014, 3, 1, 2, 5, 4).getTime();
    dates[2] = new GregorianCalendar(2014, 3, 1, 2, 5, 5).getTime();
    dates[3] = new GregorianCalendar(2014, 3, 10, 2, 5).getTime();
    dates[4] = new GregorianCalendar(2014, 3, 10, 2, 8).getTime();
    dates[5] = new GregorianCalendar(2014, 3, 12, 7, 5).getTime();
    dates[6] = new GregorianCalendar(2014, 3, 13, 7, 5).getTime();
    dates[7] = new GregorianCalendar(2014, 5, 16, 8, 5).getTime();
    dates[8] = new GregorianCalendar(2014, 5, 20, 8, 5).getTime();
    dates[9] = new GregorianCalendar(2014, 6, 1, 2, 5).getTime();
    dates[10] = new GregorianCalendar(2014, 6, 1, 2, 5, 1).getTime();
    dates[11] = new GregorianCalendar(2014, 6, 3, 2, 5).getTime();
    dates[12] = new GregorianCalendar(2014, 6, 5, 2, 5, 1).getTime();
    dates[13] = new GregorianCalendar(2014, 7, 3, 2, 5).getTime();
    dates[14] = new GregorianCalendar(2014, 7, 3, 3, 5).getTime();
    dates[15] = new GregorianCalendar(2014, 7, 3, 4, 5).getTime();
    dates[16] = new GregorianCalendar(2014, 7, 3, 6, 5).getTime();
    dates[17] = new GregorianCalendar(2014, 7, 3, 7, 5).getTime();
    dates[18] = new GregorianCalendar(2014, 7, 8, 4, 5).getTime();

    place =
        new int[] { 2, 2, 2, 4, 4, 6, 0, 1, 5, 2, 2, 4, 6, 0, 0, 0, 0, 0, 5 };

  }

  /**
   * Test method for
   * {@link TemporalHistogram#TemporalHistogram(HistogramFormat)}
   */
  @Test
  public void emptyHistogramShouldReturnAllZeros() {
    new TemporalHistogram(histogramFormatMock).get(new Date(Long.MIN_VALUE),
        new Date(Long.MAX_VALUE));
    verify(histogramFormatMock).formatHistogram(eq(new int[7]), eq(new int[7]));
  }

  @SuppressWarnings("unused")
  // used by JunitParams
      private
      Object[] testHistogram() {

    // The place for each date:
    //
    // dates[0]=2
    // dates[1]=2
    // dates[2]=2
    // dates[3]=4
    // dates[4]=4
    // dates[5]=6
    // dates[6]=0
    // dates[7]=1
    // dates[8]=5
    // dates[9]=2
    // dates[10]=2
    // dates[11]=4
    // dates[12]=6
    // dates[13]=0
    // dates[14]=0
    // dates[15]=0
    // dates[16]=0
    // dates[17]=0
    // dates[18]=5
    return $(
        $(new int[] { 0, 0, 0, 0, 0, 0, 0 }, new int[] { 0, 0, 0, 0, 0, 0, 0 },
            new HashSet<>(), new HashSet<>(), new Date(), new Date()),

        $(new int[] { 0, 0, 0, 0, 0, 0, 0 }, new int[] { 0, 0, 0, 0, 0, 0, 0 },
            makeSet(new Date[] { dates[1] }, new int[] { 1 }), new HashSet<>(),
            dates[2], dates[3]),

        $(new int[] { 0, 0, 1, 0, 0, 0, 0 }, new int[] { 0, 0, 0, 0, 0, 0, 0 },
            makeSet(new Date[] { dates[1] }, new int[] { 1 }), new HashSet<>(),
            dates[1], dates[1]),

        $(new int[] { 0, 1, 0, 0, 0, 3, 0 }, new int[] { 0, 0, 0, 0, 0, 0, 0 },
            makeSet(new Date[] { dates[7], dates[8] }, new int[] { 1, 3 }),
            new HashSet<>(), dates[7], dates[8]),

        $(new int[] { 4, 0, 0, 0, 0, 0, 0 }, new int[] { 0, 0, 0, 0, 0, 0, 0 },
            makeSet(new Date[] { dates[15], dates[16] }, new int[] { 1, 3 }),
            new HashSet<>(), dates[15], dates[17]),

        $(new int[] { 0, 0, 0, 0, 0, 0, 0 }, new int[] { 0, 0, 0, 0, 0, 0, 0 },
            makeSet(new Date[] { dates[15], dates[16] }, new int[] { 1, 3 }),
            new HashSet<>(), new Date(dates[15].getTime() + 1L), new Date(
                dates[16].getTime() - 1L)),

        $(new int[] { 1, 0, 0, 0, 0, 0, 0 }, new int[] { 0, 0, 0, 0, 0, 0, 0 },
            makeSet(new Date[] { dates[15], dates[16] }, new int[] { 1, 3 }),
            new HashSet<>(), new Date(dates[15].getTime()),
            new Date(dates[16].getTime() - 1L)),

        $(new int[] { 0, 0, 0, 0, 0, 0, 0 }, new int[] { 3, 0, 0, 0, 0, 0, 0 },
            makeSet(new Date[] {}, new int[] {}),
            makeSet(new Date[] { dates[15], dates[16] }, new int[] { 1, 3 }),
            new Date(dates[15].getTime() + 1L), new Date(dates[16].getTime())),

        $(new int[] { 0, 0, 0, 0, 0, 0, 0 }, new int[] { 4, 0, 0, 0, 0, 0, 0 },
            makeSet(new Date[] {}, new int[] {}),
            makeSet(new Date[] { dates[15], dates[16] }, new int[] { 1, 3 }),
            new Date(dates[15].getTime()), new Date(dates[16].getTime())),

        $(new int[] { 1, 0, 0, 0, 0, 0, 0 }, new int[] { 3, 0, 0, 0, 0, 0, 0 },
            makeSet(new Date[] { dates[15] }, new int[] { 1 }),
            makeSet(new Date[] { dates[16] }, new int[] { 3 }), dates[15],
            dates[17])

    );

  }

  /**
   * @param expectedBase
   * @param expectedRe
   * @param dataBase
   * @param dataRe
   * @param from
   * @param to
   */
  @Parameters(method = "testHistogram")
  @Test
  public void runHistogramTest(final int[] expectedBase,
      final int[] expectedRe, final Set<Entry<Long, Integer>> dataBase,
      final Set<Entry<Long, Integer>> dataRe, final Date from, final Date to) {
    when(dayMappingMock.getAllDaysBase()).thenReturn(dataBase);
    when(dayMappingMock.getAllDaysRe()).thenReturn(dataRe);
    new TemporalHistogram(dayMappingMock, histogramFormatMock).get(from, to);
    verify(histogramFormatMock).formatHistogram(eq(expectedBase),
        eq(expectedRe));
  }

  @SuppressWarnings("unused")
  // used by JunitParams
      private
      Object[] testIntervals() {
    return $($(0, 18), $(1, 18), $(5, 18), $(5, 6), $(4, 4), $(5, 5), $(13, 17));
  }

  /**
   * @param from
   * @param to
   */
  @Parameters(method = "testIntervals")
  @Test
  public void runIntervalsTest(final int from, final int to) {
    final int[] expectedBase = createExpected(from, to, valuesBase);
    final int[] expectedRe = createExpected(from, to, valuesRe);
    when(dayMappingMock.getAllDaysBase())
        .thenReturn(makeSet(dates, valuesBase));
    when(dayMappingMock.getAllDaysRe()).thenReturn(makeSet(dates, valuesRe));
    new TemporalHistogram(dayMappingMock, histogramFormatMock).get(dates[from],
        dates[to]);
    verify(histogramFormatMock).formatHistogram(eq(expectedBase),
        eq(expectedRe));
  }

  private int[]
      createExpected(final int from, final int to, final int[] values) {
    final int[] $ = new int[7];
    for (int i = from; i <= to; i++)
      $[place[i]] += values[i];
    return $;
  }

  private Set<Entry<Long, Integer>> makeSet(final Date[] days,
      final int[] amounts) {
    final Map<Long, Integer> m = new TreeMap<>();
    for (int i = 0; i < days.length; i++)
      if (amounts[i] != 0)
        m.put(days[i].getTime(), amounts[i]);
    return m.entrySet();
  }
}
