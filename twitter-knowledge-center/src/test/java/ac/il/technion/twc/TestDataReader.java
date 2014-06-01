package ac.il.technion.twc;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * Get the string for json tweets.
 * 
 * @author Ziv Ronen
 * @date 28.05.2014
 * @mail akarks@gmail.com
 * 
 */
public class TestDataReader {
  private static final String BUNDLE_NAME = "ac.il.technion.twc.testdata"; //$NON-NLS-1$

  private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle
      .getBundle(BUNDLE_NAME);

  private TestDataReader() {
  }

  /**
   * @param key
   * @return the string named by that key
   */
  public static String getString(final String key) {
    try {
      return RESOURCE_BUNDLE.getString(key);
    } catch (final MissingResourceException e) {
      return '!' + key + '!';
    }
  }
}
