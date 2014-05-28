package ac.il.technion.twc.api.tweets;

import static junitparams.JUnitParamsRunner.$;
import static org.junit.Assert.assertEquals;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Tests for {@link ID}
 * 
 * @author Ziv Ronen
 * @date 28.05.2014
 * @mail akarks@gmail.com
 * 
 * @version 2.0
 * @since 2.0
 */
@RunWith(JUnitParamsRunner.class)
public class IDTest {

  @SuppressWarnings("unused")
  // used by JunitParams
      private
      Object[] testHistogram() {
    return $($(true, "32586876843"), $(true, "ahsb78465GHKGHJ"), $(true, "A"),
        $(false, ""), $(false, "{id=50984}"), $(false, "43258 "));

  }

  /**
   * @param isID
   *          is the string should be id
   * @param id
   *          the given string
   */
  @Parameters(method = "testHistogram")
  @Test
  public void runHistogramTest(final boolean isID, final String id) {
    assertEquals(isID, ID.isID(id));
  }

}
