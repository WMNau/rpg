package nau.mike.rpg.engine.input;

import org.lwjgl.glfw.GLFWKeyCallbackI;

import static org.lwjgl.glfw.GLFW.*;

@SuppressWarnings("unused")
public class Keyboard implements GLFWKeyCallbackI {

  private static long window = -1;

  /**
   * Constructor
   *
   * @param window - instance of GlfwWindow
   */
  public Keyboard(long window) {
    Keyboard.window = window;
    glfwSetKeyCallback(window, this);
  }

  @Override
  public void invoke(long window, int key, int scancode, int action, int mods) {
    Keyboard.window = window;
  }

  /**
   * Helper function to know if the given button has been pressed or is being pressed.
   *
   * @param key - int
   * @return boolean - true if given key is pressed or being pressed
   */
  public static boolean pressed(int key) {
    return windowExists() && glfwGetKey(window, key) == GLFW_PRESS
        || glfwGetKey(window, key) == GLFW_REPEAT;
  }

  /**
   * Helper function to know if the given button has been released.
   *
   * @param key - int
   * @return boolean - true if given key is released
   */
  public static boolean released(int key) {
    return windowExists() && glfwGetKey(window, key) == GLFW_RELEASE;
  }

  /**
   * This is a sort of null check on the GLFW Window to make sure it exists.
   *
   * @return boolean - true if window is greater than zero
   */
  private static boolean windowExists() {
    return Keyboard.window >= 0;
  }
}
