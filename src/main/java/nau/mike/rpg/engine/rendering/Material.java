package nau.mike.rpg.engine.rendering;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.joml.Vector3f;

@AllArgsConstructor
@Data
public class Material {

  private Texture texture;
  private Vector3f color;
  private float specularIntensity;
  private float specularPower;

  public Material(final Texture texture) {
    this(texture, new Vector3f(1.0f));
  }

  public Material(final Texture texture, final Vector3f color) {
    this(texture, color, 2.0f, 32.0f);
  }

  public void clean() {
    texture.clean();
  }
}
