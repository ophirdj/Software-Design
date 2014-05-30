package ac.il.technion.twc.api.center.impl;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;

import org.apache.commons.io.FileUtils;

/**
 * Handle writing string to file and read it back
 * 
 * @author Ophir De Jager
 * 
 */
class FileHandler {

  /**
   * @param storePath
   *          Path of storage file.
   * @param s
   *          To be saved.
   * @throws IOException
   *           If an I/O error occurs.
   */
  public void store(final Path storePath, final String s) throws IOException {
    if (!Files.exists(storePath.getParent()))
      Files.createDirectories(storePath.getParent());
    try (BufferedWriter writer =
        Files.newBufferedWriter(storePath, Charset.defaultCharset())) {
      writer.write(s);
    }
  }

  /**
   * @param storePath
   *          Path of storage file.
   * @return Contents of file as a single string.
   * @throws IOException
   *           f an I/O error occurs.
   */
  public String load(final Path storePath) throws IOException {
    return new String(Files.readAllBytes(storePath));
  }

  /**
   * @param storePath
   *          clear the given directory if exist
   * @throws IOException
   */
  public void clear(final Path storePath) throws IOException {
    if (Files.exists(storePath) && Files.isDirectory(storePath))
      FileUtils.cleanDirectory(storePath.toFile());
  }

}
