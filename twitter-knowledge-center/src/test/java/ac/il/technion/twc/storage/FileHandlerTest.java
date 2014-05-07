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

public class FileHandlerTest {

  private final Path testPath = Paths.get("test/cache");
  private final FileHandler underTest;

  public final @Rule
  ExpectedException thrown = ExpectedException.none();

  public FileHandlerTest() {
    underTest = new FileHandler();
  }

  @BeforeClass
  public static void setUpClass() throws Exception {
    Files.createDirectory(Paths.get("test"));
  }

  @AfterClass
  public static void tearDownClass() throws Exception {
    Files.deleteIfExists(Paths.get("test"));
  }

  @After
  public void tearDown() throws Exception {
    Files.deleteIfExists(testPath);
  }

  @Test
  public void loadWithoutStoreShouldThrowIOException() throws IOException {
    thrown.expect(IOException.class);
    underTest.load(testPath);

  }

  @Test
  public void loadEmptyStringShouldReturnEmptyString() throws IOException {
    StoredStringShouldBeEqualLoadedString("");
  }

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
