package nau.mike.rpg.engine.input;

import org.lwjgl.glfw.GLFWMouseButtonCallbackI;

import static org.lwjgl.glfw.GLFW.*;

@SuppressWarnings("unused")
public class MouseButton implements GLFWMouseButtonCallbackI {

  private static long window = -1;

  /**
   * Constructor
   *
   * @param window - instance of GlfwWindow
   */
  public MouseButton(long window) {
    MouseButton.window = window;
    glfwSetMouseButtonCallback(window, this);
  }

  @Override
  public void invoke(long window, int button, int action, int mods) {
    MouseButton.window = window;
    if (action == GLFW_PRESS) {
      glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_DISABLED);
    } else {
      glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_NORMAL);
    }
  }

  /**
   * Helper function to know if the given button has been pressed or is being pressed.
   *
   * @param button - int
   * @return boolean - true if given button is pressed or being pressed
   */
  public static boolean pressed(int button) {
    return windowExists()
        && (glfwGetMouseButton(window, button) == GLFW_PRESS
            || glfwGetMouseButton(window, button) == GLFW_REPEAT);
  }

  /**
   * Helper function to know if the given button has been released.
   *
   * @param button - int
   * @return boolean - true if given button is released
   */
  public static boolean released(int button) {
    return windowExists() && (glfwGetMouseButton(window, button) == GLFW_RELEASE);
  }

  /**
   * This is a sort of null check on the GLFW Window to make sure it exists.
   *
   * @return boolean - true if window is greater than zero
   */
  private static boolean windowExists() {
    return MouseButton.window >= 0;
  }
}
