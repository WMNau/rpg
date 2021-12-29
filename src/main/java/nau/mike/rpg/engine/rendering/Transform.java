package nau.mike.rpg.engine.rendering;

import lombok.Data;
import lombok.Getter;
import nau.mike.rpg.engine.utils.MathUtil;
import org.joml.Matrix4f;
import org.joml.Vector3f;

@Data
public class Transform {

  @Getter private Vector3f position;
  @Getter private Vector3f rotation;
  @Getter private float scale;

  public Transform() {
    this(new Vector3f(), new Vector3f(), 1.0f);
  }

  public Transform(final Vector3f position, final Vector3f rotation, final float scale) {
    this.position = position;
    this.rotation = rotation;
    this.scale = scale;
  }

  public void setPosition(final float x, final float y, final float z) {
    this.position = new Vector3f(x, y, z);
  }

  public void setRotation(final float x, final float y, final float z) {
    this.rotation = new Vector3f(x, y, z);
  }

  public Matrix4f getModelMatrix() {
    return MathUtil.createModelMatrix(position, rotation, scale);
  }
}
