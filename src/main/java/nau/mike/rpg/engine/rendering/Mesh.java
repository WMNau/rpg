package nau.mike.rpg.engine.rendering;

import lombok.Data;
import nau.mike.rpg.engine.utils.MathUtil;
import org.lwjgl.opengl.GL30;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL15C.*;
import static org.lwjgl.opengl.GL20C.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30C.*;

@SuppressWarnings("unused")
@Data
public class Mesh {

  private final int vao;
  private final List<Integer> vbos;
  private final int vertexCount;
  private int attributes;

  public Mesh(final Mesh mesh) {
    this.vbos = mesh.vbos;
    this.attributes = mesh.attributes;
    this.vao = mesh.vao;
    this.vertexCount = mesh.vertexCount;
  }

  public Mesh(final Vertex vertices) {
    this.vbos = new ArrayList<>();
    this.attributes = 0;
    this.vao = glGenVertexArrays();
    glBindVertexArray(vao);
    bindIndicesBuffer(vertices.getIndices());
    storeDataInAttributeList(vertices.getPositions(), 3);
    storeDataInAttributeList(vertices.getNormals(), 3);
    storeDataInAttributeList(vertices.getTextureCoordinates(), 2);
    this.vertexCount = vertices.getIndices().length;
    glBindVertexArray(0);
  }

  public void draw() {
    startDraw();
    glDrawElements(GL_TRIANGLES, vertexCount, GL_UNSIGNED_INT, 0);
    endDraw();
  }

  public void drawArrays() {
    startDraw();
    glDrawArrays(GL_TRIANGLES, 0, vertexCount);
    endDraw();
  }

  public void clean() {
    glBindVertexArray(0);
    vbos.forEach(GL30::glDeleteBuffers);
    glDeleteVertexArrays(vao);
  }

  private void startDraw() {
    glBindVertexArray(vao);
    enableVertexAttributes(true);
  }

  private void endDraw() {
    enableVertexAttributes(false);
    glBindVertexArray(0);
  }

  private void bindIndicesBuffer(final int[] data) {
    final int vbo = glGenBuffers();
    glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, vbo);
    vbos.add(vbo);
    final IntBuffer buffer = MathUtil.buffer(data);
    glBufferData(GL_ELEMENT_ARRAY_BUFFER, buffer, GL_STATIC_DRAW);
  }

  private void storeDataInAttributeList(final float[] data, final int size) {
    final int vbo = glGenBuffers();
    glBindBuffer(GL_ARRAY_BUFFER, vbo);
    vbos.add(vbo);
    final FloatBuffer buffer = MathUtil.buffer(data);
    glBufferData(GL_ARRAY_BUFFER, buffer, GL_STATIC_DRAW);
    glVertexAttribPointer(attributes++, size, GL_FLOAT, false, 0, 0);
    glBindBuffer(GL_ARRAY_BUFFER, 0);
  }

  private void enableVertexAttributes(final boolean enable) {
    for (int i = 0; i < attributes; i++) {
      if (enable) {
        glEnableVertexAttribArray(i);
      } else {
        glDisableVertexAttribArray(i);
      }
    }
  }
}
