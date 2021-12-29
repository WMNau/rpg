package nau.mike.rpg.engine.rendering;

import lombok.Getter;
import nau.mike.rpg.engine.utils.FileUtil;
import org.lwjgl.system.MemoryStack;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.Optional;

import static org.lwjgl.opengl.GL11C.*;
import static org.lwjgl.opengl.GL13C.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13C.glActiveTexture;
import static org.lwjgl.stb.STBImage.stbi_image_free;
import static org.lwjgl.stb.STBImage.stbi_load;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;

@SuppressWarnings("unused")
@Getter
public class Texture {

  private final int id;
  private final int width;
  private final int height;

  public Texture(final Texture texture) {
    this.id = texture.id;
    this.width = texture.width;
    this.height = texture.height;
  }

  public Texture(final String fileName, final String ext) {
    this.id = bindTexture();

    generateTextureParameters();

    try (final MemoryStack stack = stackPush()) {
      final IntBuffer w = stack.mallocInt(1);
      final IntBuffer h = stack.mallocInt(1);
      final IntBuffer c = stack.mallocInt(1);

      final String path = FileUtil.getPath("textures", fileName, ext);

      final ByteBuffer buffer =
          Optional.ofNullable(stbi_load(path, w, h, c, 4))
              .orElseThrow(
                  () -> {
                    final String errorMessage =
                        String.format("Image /textures/%s could not be found", fileName);
                    throw new IllegalStateException(errorMessage);
                  });

      final int nrChannel = c.get();
      final int internalFormat = nrChannel == 4 || nrChannel == 3 ? GL_RGBA : GL_RGB;
      this.width = w.get();
      this.height = h.get();

      storeTextureImage(internalFormat, buffer);
      stbi_image_free(buffer);
    }
    unbind();
  }

  public Texture(final int width, final int height) {
    this.width = width;
    this.height = height;
    this.id = bindTexture();
    generateTextureParameters();
    storeTextureImage(GL_RGB, null);
    unbind();
  }

  public void bind(int activeIndex) {
    glActiveTexture(GL_TEXTURE0 + activeIndex);
    glBindTexture(GL_TEXTURE_2D, id);
  }

  public void bind() {
    bind(0);
  }

  public void unbind() {
    glBindTexture(GL_TEXTURE_2D, 0);
  }

  public void clean() {
    glDeleteTextures(id);
  }

  private int bindTexture() {
    final int texture = glGenTextures();
    glBindTexture(GL_TEXTURE_2D, texture);
    return texture;
  }

  private void generateTextureParameters() {
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
  }

  private void storeTextureImage(final int internalFormat, final ByteBuffer buffer) {
    if (buffer == null) {
      glTexImage2D(
          GL_TEXTURE_2D,
          0,
          internalFormat,
          width,
          height,
          0,
          internalFormat,
          GL_UNSIGNED_BYTE,
          NULL);
    } else {
      glTexImage2D(
          GL_TEXTURE_2D,
          0,
          internalFormat,
          width,
          height,
          0,
          internalFormat,
          GL_UNSIGNED_BYTE,
          buffer);
    }
  }
}
