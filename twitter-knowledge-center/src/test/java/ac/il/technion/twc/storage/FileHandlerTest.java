package ac.il.technion.twc.storage;

import static org.junit.Assert.fail;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class FileHandlerTest {

  private final Path testPath = Paths.get("test/cache");

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
  public void test() {
    fail("Not yet implemented");
  }

}
