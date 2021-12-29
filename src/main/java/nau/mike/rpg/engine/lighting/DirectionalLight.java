package nau.mike.rpg.engine.lighting;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.joml.Vector3f;

@AllArgsConstructor
@Data
public class DirectionalLight {

  private BaseLight baseLight;
  private Vector3f direction;
}
