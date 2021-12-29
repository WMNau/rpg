package nau.mike.rpg.engine.rendering;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class Vertex {

  private float[] positions;
  private float[] normals;
  private float[] textureCoordinates;
  private int[] indices;
}
