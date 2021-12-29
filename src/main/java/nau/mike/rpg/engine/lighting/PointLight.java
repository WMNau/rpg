package nau.mike.rpg.engine.lighting;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.joml.Vector3f;

@AllArgsConstructor
@Data
public class PointLight {

  private BaseLight baseLight;
  private Attenuation attenuation;
  private Vector3f position;
  private float range;
}
