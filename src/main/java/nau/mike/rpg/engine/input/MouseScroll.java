package nau.mike.rpg.engine.input;

import lombok.Getter;
import org.joml.Vector2f;
import org.lwjgl.glfw.GLFWScrollCallbackI;

import static org.lwjgl.glfw.GLFW.glfwSetScrollCallback;

@SuppressWarnings("unused")
public class MouseScroll implements GLFWScrollCallbackI {
  @Getter private static final Vector2f offset = new Vector2f(0.0f);

  /**
   * Constructor
   *
   * @param window - instance of GlfwWindow
   */
  public MouseScroll(final long window) {
    glfwSetScrollCallback(window, this);
  }

  @Override
  public void invoke(long window, double xOffset, double yOffset) {
    offset.x = (float) xOffset;
    offset.y = (float) yOffset;
  }

  /**
   * Helper function to get the x-axis.
   *
   * @return float - x-axis
   */
  public static float getX() {
    final float x = offset.x;
    if (x != 0.0f) {
      offset.x = 0.0f;
    }
    return offset.x;
  }

  /**
   * Helper function to get the y-axis.
   *
   * @return float - y-axis
   */
  public static float getY() {
    final float y = offset.y;
    if (y != 0.0f) {
      offset.y = 0.0f;
    }
    return y;
  }
}
