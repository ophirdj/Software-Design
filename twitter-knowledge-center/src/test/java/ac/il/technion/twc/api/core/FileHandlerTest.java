package ac.il.technion.twc.api.core;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.commons.io.FileUtils;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * Test for {@link FileHandler}
 * 
 * @author Ziv Ronen
 * @date 07.05.2014
 * @mail akarks@gmail.com
 */
public class FileHandlerTest {

  private static final Path testDirectory = Paths.get("test");
  private static final Path testPath = testDirectory.resolve("cache");
  private final FileHandler $;

  /**
   * 
   */
  public final @Rule
  ExpectedException thrown = ExpectedException.none();

  /**
   * C'tor
   */
  public FileHandlerTest() {
    $ = new FileHandler();
  }

  /**
   * workspace init
   * 
   * @throws Exception
   */
  @BeforeClass
  public static void setUpClass() throws Exception {
    Files.createDirectory(testDirectory);
  }

  /**
   * workspace clean
   * 
   * @throws Exception
   */
  @AfterClass
  public static void tearDownClass() throws Exception {
    FileUtils.deleteDirectory(testDirectory.toFile());
  }

  /**
   * 
   * @throws Exception
   */
  @Before
  public void setup() throws Exception {
    FileUtils.cleanDirectory(testDirectory.toFile());
  }

  /**
   * Test method for: {@link FileHandler#load(Path)}
   * 
   * @throws IOException
   */
  @Test
  public void loadWithoutStoreShouldThrowIOException() throws IOException {
    thrown.expect(IOException.class);
    $.load(testPath);
  }

  /**
   * Test method for: {@link FileHandler#load(Path)},
   * {@link FileHandler#store(Path,String)}
   * 
   * @throws IOException
   */
  @Test
  public void loadEmptyStringShouldReturnEmptyString() throws IOException {
    StoredStringShouldBeEqualLoadedString("");
  }

  /**
   * Test method for: {@link FileHandler#load(Path)},
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
    $.store(testPath, s);
    assertEquals(s, $.load(testPath));
  }

  /**
   * Test method for: {@link FileHandler#load(Path)},
   * {@link FileHandler#store(Path,String)}, {@link FileHandler#clear(Path)}
   * 
   * @throws IOException
   */
  @Test
  public void ClearShouldRemoveStoredFileInSamePath() throws IOException {
    $.store(testPath, "abc");
    $.clear(testDirectory);
    thrown.expect(IOException.class);
    $.load(testPath);
  }

  /**
   * Test method for: {@link FileHandler#load(Path)},
   * {@link FileHandler#store(Path,String)}, {@link FileHandler#clear(Path)}
   * 
   * @throws IOException
   */
  @Test
  public void ClearShouldRemoveFirstStoredFiles() throws IOException {
    $.store(testDirectory.resolve("first"), "abc");
    $.store(testDirectory.resolve("second"), "def");
    $.clear(testDirectory);
    thrown.expect(IOException.class);
    $.load(testDirectory.resolve("first"));
  }

  /**
   * Test method for: {@link FileHandler#load(Path)},
   * {@link FileHandler#store(Path,String)}, {@link FileHandler#clear(Path)}
   * 
   * @throws IOException
   */
  @Test
  public void ClearShouldRemoveSecondStoredFiles() throws IOException {
    $.store(testDirectory.resolve("first"), "abc");
    $.store(testDirectory.resolve("second"), "def");
    $.clear(testDirectory);
    thrown.expect(IOException.class);
    $.load(testDirectory.resolve("second"));
  }

  /**
   * Test method for: {@link FileHandler#load(Path)},
   * {@link FileHandler#store(Path,String)}, {@link FileHandler#clear(Path)}
   * 
   * @throws IOException
   */
  @Test
  public void ClearShouldntRemoveFilesStoredInDifferentDirectory()
      throws IOException {
    $.store(testDirectory.resolve("test_first").resolve("cache"), "abc");
    $.store(testDirectory.resolve("test_second").resolve("cache"), "def");
    $.clear(testDirectory.resolve("test_first"));
    assertEquals("def", $.load(Paths.get("test", "test_second", "cache")));
  }

}
