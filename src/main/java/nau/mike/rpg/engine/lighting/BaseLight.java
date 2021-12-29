package nau.mike.rpg.engine.lighting;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.joml.Vector3f;

@AllArgsConstructor
@Data
public class BaseLight {

    private Vector3f color;
    private float intensity;
}
