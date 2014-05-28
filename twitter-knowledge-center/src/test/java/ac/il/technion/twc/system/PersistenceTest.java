package ac.il.technion.twc.system;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Test;

import ac.il.technion.twc.FuntionalityTester;

/**
 * Test that data is stored persistently.
 * 
 * @author Ophir De Jager
 * 
 */
public class PersistenceTest {

  private static final String[] lines = new String[] {
      "04/04/2014 12:00:00, iddqd", "05/04/2014 12:00:00, idkfa, iddqd",
      "06/04/2014 13:00:00, 593393706" };

  private FuntionalityTester $;

  /**
   * Cleanup.
   * 
   * @throws Exception
   */
  @After
  public void tearDown() throws Exception {
    $.cleanPersistentData();
  }

  /**
   * Test method for {@link FuntionalityTester#setupIndex()}
   * 
   * @throws Exception
   */
  @Test
  public final void
      shouldReturnCorrectResultsAfterShutdownBetweenImportDataAndSetupIndex()
          throws Exception {
    new FuntionalityTester().importData(lines);
    // system shutdown... now back online
    $ = new FuntionalityTester();
    $.setupIndex();
    assertEquals("86400000", $.getLifetimeOfTweets("iddqd"));
    assertArrayEquals(new String[] { "1,0", "0,0", "0,0", "0,0", "0,0", "1,0",
        "1,1" }, $.getDailyHistogram());
  }

  /**
   * Test method for: {@link FuntionalityTester#setupIndex()},
   * {@link FuntionalityTester#importData(String[])}
   * 
   * @throws Exception
   */
  @Test
  public final
      void
      shouldBeAbleToImportMoreTweetsAfterShutdownBetweenImportDataAndSetupIndex()
          throws Exception {
    new FuntionalityTester().importData(lines);
    // system shutdown... now back online
    $ = new FuntionalityTester();
    $.setupIndex();
    $.importData(new String[] { "06/04/2014 11:00:00, 40624256" });
    $.setupIndex();
    assertArrayEquals(new String[] { "2,0", "0,0", "0,0", "0,0", "0,0", "1,0",
        "1,1" }, $.getDailyHistogram());
  }

}
