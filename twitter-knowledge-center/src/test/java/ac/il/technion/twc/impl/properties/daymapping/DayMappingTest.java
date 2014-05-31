package ac.il.technion.twc.impl.properties.daymapping;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import org.junit.Test;

import ac.il.technion.twc.api.tweet.BaseTweet;
import ac.il.technion.twc.api.tweet.ID;
import ac.il.technion.twc.api.tweet.Retweet;

/**
 * Tests for {@link DayMapping}
 * 
 * @author Ziv Ronen
 * @date 28.05.2014
 * @mail akarks@gmail.com
 * 
 * @version 2.0
 * @since 2.0
 */
public class DayMappingTest {

  private static final ArrayList<Retweet> EMPTY_RES = new ArrayList<Retweet>();
  private static final ArrayList<BaseTweet> EMPTY_BASES = new ArrayList<BaseTweet>();

  /**
   * Test method for {@link DayMapping#getAllDaysBase()}
   */
  @Test
  public final void noVisitsGetAllDaysBaseShouldHave0Entries() {
    assertTrue(new DayMapping(EMPTY_BASES,
        EMPTY_RES).getAllDaysBase().isEmpty());
  }

  /**
   * Test method for {@link DayMapping#getAllDaysRe()}
   */
  @Test
  public final void noVisitsGetAllDaysReShouldHave0Entries() {
    assertTrue(new DayMapping(EMPTY_BASES,
        EMPTY_RES).getAllDaysBase().isEmpty());
  }

  /**
   * Test method for {@link DayMapping#getAllDaysBase()}
   */
  @Test
  public final void visit1BaseTweetGetAllDaysBaseShouldHave1TweetAtThisDate() {
    assertEquals(
        1,
        new DayMapping(Arrays.asList(new BaseTweet(new Date(123456789), new ID(
            "base tweet ID"))), EMPTY_RES).getAllDaysBase()
            .iterator().next().getValue().intValue());
  }

  /**
   * Test method for {@link DayMapping#getAllDaysBase()}
   */
  @Test
  public final void
      visit1BaseTweetGetAllDaysBaseShouldHaveATweetAtCorrectDate() {
    final Date date = new Date(123456789);
    assertEquals(
        date.getTime(),
        new DayMapping(Arrays.asList(new BaseTweet(date,
            new ID("base tweet ID"))), EMPTY_RES)
            .getAllDaysBase().iterator().next().getKey().longValue());
  }

  /**
   * Test method for {@link DayMapping#getAllDaysBase()}
   */
  @Test
  public final void visit1BaseTweetGetAllDaysBaseShouldHave1Entry() {
    assertEquals(
        1,
        new DayMapping(Arrays.asList(new BaseTweet(new Date(123456789), new ID(
            "base tweet ID"))), EMPTY_RES).getAllDaysBase()
            .size());
  }

  /**
   * Test method for {@link DayMapping#getAllDaysRe()}
   */
  @Test
  public final void visit1RetweetGetAllDaysReShouldHave1TweetAtThisDate() {
    assertEquals(
        1,
        new DayMapping(EMPTY_BASES, Arrays
            .asList(new Retweet(new Date(123456789), new ID("retweet ID"),
                new ID("base tweet ID")))).getAllDaysRe().iterator().next()
            .getValue().intValue());
  }

  /**
   * Test method for {@link DayMapping#getAllDaysRe()}
   */
  @Test
  public final void visit1RetweetGetAllDaysReShouldHaveATweetAtCorrectDate() {
    final Date date = new Date(123456789);
    assertEquals(
        date.getTime(),
        new DayMapping(EMPTY_BASES, Arrays.asList(new Retweet(
            date, new ID("retweet ID"), new ID("base tweet ID"))))
            .getAllDaysRe().iterator().next().getKey().longValue());
  }

  /**
   * Test method for {@link DayMapping#getAllDaysRe()}
   */
  @Test
  public final void visit1RetweetGetAllDaysReShouldHave1Entry() {
    assertEquals(
        1,
        new DayMapping(EMPTY_BASES, Arrays
            .asList(new Retweet(new Date(123456789), new ID("retweet ID"),
                new ID("base tweet ID")))).getAllDaysRe().size());
  }

  /**
   * Test method for {@link DayMapping#getAllDaysBase()}
   */
  @Test
  public final void
      visit2BaseTweetsInSameDateGetAllDaysBaseShouldHave2TweetsAtThisDate() {
    final Date date = new Date(123456789);
    assertEquals(
        2,
        new DayMapping(Arrays
            .asList(new BaseTweet(date, new ID("base tweet 1 ID")),
                new BaseTweet(date, new ID("base tweet 2 ID"))),
            EMPTY_RES).getAllDaysBase().iterator().next()
            .getValue().intValue());
  }

  /**
   * Test method for {@link DayMapping#getAllDaysBase()}
   */
  @Test
  public final void
      visit2BaseTweetsInDifferentDatesGetAllDaysBaseShouldHave2Entries() {

    assertEquals(
        2,
        new DayMapping(Arrays.asList(new BaseTweet(new Date(123456789), new ID(
            "base tweet 1 ID")), new BaseTweet(new Date(987654321), new ID(
            "base tweet 2 ID"))), EMPTY_RES).getAllDaysBase()
            .size());
  }

  /**
   * Test method for {@link DayMapping#getAllDaysRe()}
   */
  @Test
  public final void
      visit2RetweetsInSameDateGetAllDaysReShouldHave2TweetAtThisDate() {
    final Date date = new Date(123456789);
    assertEquals(
        2,
        new DayMapping(EMPTY_BASES, Arrays.asList(new Retweet(
            date, new ID("retweet 1 ID"), new ID("base tweet ID")),
            new Retweet(date, new ID("retweet 2 ID"), new ID("base tweet ID"))))
            .getAllDaysRe().iterator().next().getValue().intValue());
  }

  /**
   * Test method for {@link DayMapping#getAllDaysRe()}
   */
  @Test
  public final void
      visit2RetweetsInDifferentDatesGetAllDaysReShouldHave2Entries() {
    assertEquals(
        2,
        new DayMapping(EMPTY_BASES, Arrays.asList(new Retweet(
            new Date(123456789), new ID("retweet 1 ID"),
            new ID("base tweet ID")), new Retweet(new Date(987654321), new ID(
            "retweet 2 ID"), new ID("base tweet ID")))).getAllDaysRe().size());
  }

  /**
   * Test method for {@link DayMapping#getAllDaysBase()},
   * {@link DayMapping#getAllDaysRe()}
   */
  @Test
  public final
      void
      visitRetweetAndBaseTweetInSameDateGetAllDaysReShouldHave1EntryAndGetAllDaysBaseShouldHave1Entry() {
    final Date date = new Date(123456789);
    final DayMapping dm =
        new DayMapping(Arrays.asList(new BaseTweet(date, new ID(
            "base tweet 1 ID"))), Arrays.asList(new Retweet(date, new ID(
            "retweet ID"), new ID("base tweet ID"))));
    assertEquals(1, dm.getAllDaysBase().size());
    assertEquals(1, dm.getAllDaysRe().size());
  }

}
