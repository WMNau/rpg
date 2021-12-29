package nau.mike.rpg.engine.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nau.mike.rpg.engine.rendering.Mesh;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

@SuppressWarnings("unused")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Slf4j
public class FileUtil {

  public static Mesh loadMesh(final String directory, final String fileName) {
    final String filePath =
        "".equals(directory) ? fileName : String.format("%s/%s", directory, fileName);
    return new Mesh(ObjLoader.load(filePath));
  }

  public static String getShaderSource(final String shaderFileName)
      throws URISyntaxException, IOException {
    return getFileSource("shaders", shaderFileName, "glsl");
  }

  public static BufferedImage getBufferedImage(
      final String directory, final String fileName, final String ext) {
    final String path = buildFilePath(directory, fileName, ext);
    try {
      final InputStream resource = FileUtil.class.getResourceAsStream(path);
      if (null == resource) {
        throw new IOException();
      }
      return ImageIO.read(resource);
    } catch (final IOException e) {
      throw new IllegalStateException("Could not load image from " + path, e);
    }
  }

  public static String getFileSource(
      final String directory, final String fileName, final String ext)
      throws URISyntaxException, IOException {
    final String path = buildFilePath(directory, fileName, ext);
    log.debug("Loading file {}", path);
    final URL url = getUrl(path);

    return Optional.of(new String(Files.readAllBytes(Paths.get(url.toURI()))))
        .orElseThrow(() -> new IllegalStateException(String.format("Error loading file %s", path)));
  }

  public static String buildFilePath(
      final String directory, final String fileName, final String ext) {
    return String.format("/%s/%s.%s", directory, fileName, ext);
  }

  public static URL getUrl(final String directory, final String fileName, final String ext) {
    final String path = getPath(directory, fileName, ext);
    return getUrl(path);
  }

  public static String getPath(final String directory, final String fileName, final String ext) {
    final String path = buildFilePath(directory, fileName, ext);
    final URL url = getUrl(path);
    return url.getPath();
  }

  public static URL getUrl(final String path) {
    log.debug("Getting URL for file {}", path);
    return Optional.ofNullable(FileUtil.class.getResource(path))
        .orElseThrow(() -> new IllegalStateException(String.format("File %s was not found", path)));
  }
}
