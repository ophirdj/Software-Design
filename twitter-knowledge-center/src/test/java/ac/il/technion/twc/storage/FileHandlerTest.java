package ac.il.technion.twc.storage;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import ac.il.technion.twc.impl.storage.FileHandler;

/**
 * Test for FileHandler class
 * 
 * @author Ziv Ronen
 * @date 07.05.2014
 * @mail akarks@gmail.com
 * 
 * @version 2.0
 * @since 2.0
 */
public class FileHandlerTest {

  private final Path testPath = Paths.get("test/cache");
  private final FileHandler underTest;

  /**
   * 
   */
  public final @Rule
  ExpectedException thrown = ExpectedException.none();

  /**
   * C'tor
   */
  public FileHandlerTest() {
    underTest = new FileHandler();
  }

  /**
   * workspace init
   * 
   * @throws Exception
   */
  @BeforeClass
  public static void setUpClass() throws Exception {
    Files.createDirectory(Paths.get("test"));
  }

  /**
   * workspace clean
   * 
   * @throws Exception
   */
  @AfterClass
  public static void tearDownClass() throws Exception {
    Files.deleteIfExists(Paths.get("test"));
  }

  /**
   * remove added files
   * 
   * @throws Exception
   */
  @After
  public void tearDown() throws Exception {
    Files.deleteIfExists(testPath);
  }

  /**
   * Test method for: {@link FileHandler#load(Path)}
   * 
   * @throws IOException
   */
  @Test
  public void loadWithoutStoreShouldThrowIOException() throws IOException {
    thrown.expect(IOException.class);
    underTest.load(testPath);

  }

  /**
   * Test method for: {@link FileHandler#load(Path)}
   * {@link FileHandler#store(Path,String)}
   * 
   * @throws IOException
   */
  @Test
  public void loadEmptyStringShouldReturnEmptyString() throws IOException {
    StoredStringShouldBeEqualLoadedString("");
  }

  /**
   * Test method for: {@link FileHandler#load(Path)}
   * {@link FileHandler#store(Path,String)}
   * 
   * @throws IOException
   */
  @Test
  public void loadStringShouldReturnEmptyString() throws IOException {
    StoredStringShouldBeEqualLoadedString("abcd");
  }

  private void StoredStringShouldBeEqualLoadedString(final String s)
      throws IOException {
    underTest.store(testPath, s);
    assertEquals(s, underTest.load(testPath));
  }

}
