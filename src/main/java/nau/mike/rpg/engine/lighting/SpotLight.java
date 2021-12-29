package nau.mike.rpg.engine.lighting;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.joml.Vector3f;

@AllArgsConstructor
@Data
public class SpotLight {

  private PointLight pointLight;
  private Vector3f direction;
  private float cutoff;
}
