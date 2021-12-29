package nau.mike.rpg.engine.input;

import lombok.Getter;
import org.joml.Vector2f;
import org.lwjgl.glfw.GLFWCursorPosCallbackI;

import static org.lwjgl.glfw.GLFW.glfwSetCursorPosCallback;

@SuppressWarnings("unused")
public class MousePosition implements GLFWCursorPosCallbackI {

  @Getter private static final Vector2f position = new Vector2f(0.0f);
  private static final Vector2f lastPosition = new Vector2f(0.0f);

  /**
   * Constructor
   *
   * @param window - instance of GlfwWindow
   */
  public MousePosition(final long window) {
    glfwSetCursorPosCallback(window, this);
  }

  @Override
  public void invoke(long window, double xPos, double yPos) {
    lastPosition.x = position.x;
    lastPosition.y = position.y;
    position.x = (float) xPos;
    position.y = (float) yPos;
  }

  /**
   * Helper function to get the x position.
   *
   * @return float - position.x
   */
  public static float getX() {
    return position.x;
  }

  /**
   * Helper function to get the y position.
   *
   * @return float - position.y
   */
  public static float getY() {
    return position.y;
  }

  /**
   * Helper function to get the deltaX position.
   *
   * @return float - (position.x - lastPosition.x)
   */
  public static float getDx() {
    return position.x - lastPosition.x;
  }

  /**
   * Helper function to get the deltaY position.
   *
   * @return float - (position.y - lastPosition.y)
   */
  public static float getDy() {
    return position.y - lastPosition.y;
  }

  /**
   * Helper function to assist in debugging
   *
   * @return String position
   */
  public static String positionString() {
    return String.format("Mouse Position: ( %f, %f)", getX(), getY());
  }

  /**
   * Helper function to assist in debugging
   *
   * @return String last position
   */
  public static String lastPositionString() {
    return String.format("Last Mouse Position( %f, %f)", getDx(), getDy());
  }
}
