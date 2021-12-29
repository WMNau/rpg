package nau.mike.rpg.engine.shaders;

import nau.mike.rpg.engine.Window;
import nau.mike.rpg.engine.rendering.Material;
import nau.mike.rpg.engine.rendering.Transform;
import nau.mike.rpg.engine.utils.FileUtil;
import nau.mike.rpg.engine.utils.MathUtil;
import org.joml.*;
import org.lwjgl.opengl.GL20;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.FloatBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.lwjgl.opengl.GL20C.*;
import static org.lwjgl.opengl.GL32C.GL_GEOMETRY_SHADER;

@SuppressWarnings("unused")
public abstract class Shader {

  private final int program;
  private final int vertex;
  private final int fragment;
  private final Optional<Integer> geometry;

  private final Map<String, Integer> uniformMap;

  protected Shader(final String fileName, final boolean hasGeometry) {
    this.program = glCreateProgram();
    this.vertex = createShader(fileName + ".vs", GL_VERTEX_SHADER);
    this.fragment = createShader(fileName + ".fs", GL_FRAGMENT_SHADER);
    this.geometry =
        hasGeometry
            ? Optional.of(createShader(fileName + ".gs", GL_GEOMETRY_SHADER))
            : Optional.empty();
    bindAllAttributes();
    link();
    this.uniformMap = new HashMap<>();
  }

  protected abstract void bindAllAttributes();

  protected void bindAttributeLocation(final int position, final String name) {
    glBindAttribLocation(program, position, name);
  }

  protected void setUniform(final String name, final int x) {
    glUniform1i(uniformLocation(name), x);
  }

  protected void setUniform(final String name, final int x, final int y) {
    glUniform2i(uniformLocation(name), x, y);
  }

  protected void setUniform(final String name, final int x, final int y, final int z) {
    glUniform3i(uniformLocation(name), x, y, z);
  }

  protected void setUniform(final String name, final int x, final int y, final int z, final int w) {
    glUniform4i(uniformLocation(name), x, y, z, w);
  }

  protected void setUniform(final String name, final Vector2i vector) {
    setUniform(name, vector.x, vector.y);
  }

  protected void setUniform(final String name, final Vector3i vector) {
    setUniform(name, vector.x, vector.y, vector.z);
  }

  protected void setUniform(final String name, final Vector4i vector) {
    setUniform(name, vector.x, vector.y, vector.z, vector.w);
  }

  protected void setUniform(final String name, final boolean value) {
    setUniform(name, value ? 1 : 0);
  }

  protected void setUniform(final String name, final float x) {
    glUniform1f(uniformLocation(name), x);
  }

  protected void setUniform(final String name, final float x, final float y) {
    glUniform2f(uniformLocation(name), x, y);
  }

  protected void setUniform(final String name, final float x, final float y, final float z) {
    glUniform3f(uniformLocation(name), x, y, z);
  }

  protected void setUniform(
      final String name, final float x, final float y, final float z, final float w) {
    glUniform4f(uniformLocation(name), x, y, z, w);
  }

  protected void setUniform(final String name, final Vector2f vector) {
    setUniform(name, vector.x, vector.y);
  }

  protected void setUniform(final String name, final Vector3f vector) {
    setUniform(name, vector.x, vector.y, vector.z);
  }

  protected void setUniform(final String name, final Vector4f vector) {
    setUniform(name, vector.x, vector.y, vector.z, vector.w);
  }

  protected void setUniform(final String name, final Matrix3f matrix) {
    final FloatBuffer buffer = MathUtil.buffer(matrix);
    glUniformMatrix3fv(uniformLocation(name), false, buffer);
  }

  protected void setUniform(final String name, final Matrix4f matrix) {
    final FloatBuffer buffer = MathUtil.buffer(matrix);
    glUniformMatrix4fv(uniformLocation(name), false, buffer);
  }

  public void start() {
    glUseProgram(program);
  }

  public void loadModelProjectionMatrix(final Transform transform) {
    setUniform("uModelMatrix", transform.getModelMatrix());
    setUniform("uProjectionMatrix", Window.getProjectionMatrix());
  }

  public void loadViewMatrix(final Matrix4f matrix) {
    setUniform("uViewMatrix", matrix);
  }

  public void loadMaterial(final Material material) {
    if (null != material.getTexture()) {
      material.getTexture().bind();
    }
    setUniform("uBaseColor", material.getColor());
    setUniform("uSpecularIntensity", material.getSpecularIntensity());
    setUniform("uSpecularPower", material.getSpecularPower());
  }

  public void stop() {
    glUseProgram(0);
  }

  public void clean() {
    stop();
    glDetachShader(program, vertex);
    glDetachShader(program, fragment);
    geometry.ifPresent(id -> glDetachShader(program, id));
    glDeleteProgram(program);
  }

  private int uniformLocation(final String name) {
    if (null == uniformMap.get(name)) {
      final int location = glGetUniformLocation(program, name);
      uniformMap.put(name, location);
      return location;
    }
    return uniformMap.get(name);
  }

  private int createShader(final String fileName, final int type) {
    final int shader = glCreateShader(type);
    try {
      final String source = FileUtil.getShaderSource(fileName);
      glShaderSource(shader, source);
      glCompileShader(shader);
      final int success = glGetShaderi(shader, GL_COMPILE_STATUS);
      if (GL_FALSE == success) {
        final String infoLog = glGetShaderInfoLog(shader);
        throw new IllegalStateException("Could not create shader\n" + infoLog);
      }
      glAttachShader(program, shader);
      return shader;
    } catch (URISyntaxException | IOException e) {
      throw new IllegalStateException(e.getLocalizedMessage(), e);
    }
  }

  private void link() {
    glLinkProgram(program);
    final int success = glGetProgrami(program, GL_LINK_STATUS);
    if (GL_FALSE == success) {
      final String infoLog = glGetProgramInfoLog(program);
      throw new IllegalStateException("Could not link program\n" + infoLog);
    }
    glDeleteShader(vertex);
    glDeleteShader(fragment);
    geometry.ifPresent(GL20::glDeleteShader);
  }
}
