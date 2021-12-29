package nau.mike.rpg.engine.cameras;

import lombok.Data;
import nau.mike.rpg.engine.input.*;
import nau.mike.rpg.engine.utils.MathUtil;
import org.joml.Matrix4f;
import org.joml.Vector3f;

@SuppressWarnings("unused")
@Data
public class Camera {

  private static final float MOUSE_MOVEMENT_SENSITIVITY = 0.01f;
  private static final float MOVEMENT_SPEED = 0.1f;

  private Vector3f position;
  private Vector3f rotation;

  public Camera() {
    this(new Vector3f(), new Vector3f());
  }

  public Camera(final Vector3f position, final Vector3f rotation) {
    this.position = position;
    this.rotation = rotation;
  }

  public void input() {
    if (MouseButton.pressed(Buttons.BUTTON_RIGHT)) {
      rotation.x += MousePosition.getDy() * MOUSE_MOVEMENT_SENSITIVITY;
      rotation.y += MousePosition.getDx() * MOUSE_MOVEMENT_SENSITIVITY;
    }

    if (Keyboard.pressed(Keys.W)) {
      position.y += MOVEMENT_SPEED;
    } else if (Keyboard.pressed(Keys.S)) {
      position.y -= MOVEMENT_SPEED;
    }

    if (Keyboard.pressed(Keys.D)) {
      position.x += MOVEMENT_SPEED;
    } else if (Keyboard.pressed(Keys.A)) {
      position.x -= MOVEMENT_SPEED;
    }

    position.z -= MouseScroll.getY();
  }

  public void setPosition(final float x, final float y, final float z) {
    this.position = new Vector3f(x, y, z);
  }

  public void setRotation(final float x, final float y, final float z) {
    this.rotation = new Vector3f(x, y, z);
  }

  public Matrix4f getViewMatrix() {
    return MathUtil.createViewMatrix(this);
  }
}
