package ac.il.technion.twc.storage;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileHandler {

  public void store(final Path storePath, final String s) throws IOException {
    try (BufferedWriter writer =
        Files.newBufferedWriter(storePath, Charset.defaultCharset())) {
      writer.write(s);
    }
  }

  public String load(final Path storePath) throws IOException {
    return new String(Files.readAllBytes(storePath));
  }

}
