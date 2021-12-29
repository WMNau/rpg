package nau.mike.rpg.engine;

import lombok.Getter;
import nau.mike.rpg.engine.input.Keyboard;
import nau.mike.rpg.engine.input.MouseButton;
import nau.mike.rpg.engine.input.MousePosition;
import nau.mike.rpg.engine.input.MouseScroll;
import nau.mike.rpg.engine.utils.MathUtil;
import org.joml.Matrix4f;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;

import java.nio.IntBuffer;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window {

  @Getter private static Matrix4f projectionMatrix = new Matrix4f().identity();

  private static int winWidth;
  private static int winHeight;

  private final String title;

  private final GLFWErrorCallback errorCallback;

  private long glfwWindow;

  public Window(final String title, final int width, final int height) {
    this.title = title;
    Window.winWidth = width;
    Window.winHeight = height;
    this.errorCallback = GLFWErrorCallback.createPrint();
    Window.projectionMatrix =
        MathUtil.createProjectionMatrix(75.0f, (float) winWidth / winHeight, 0.01f, 1000.0f);
    if (!glfwInit()) {
      throw new IllegalStateException("Unable to initialize GLFW");
    }
  }

  public void init() {
    configureGlfwWindow();
    createGlfwWindow();

    glfwMakeContextCurrent(glfwWindow);
    glfwSwapInterval(1);
    glfwShowWindow(glfwWindow);
    GL.createCapabilities();

    initGraphics();
    setCallbacks();
  }

  public void startFrame() {
    glfwPollEvents();
    glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
  }

  public void endFrame() {
    glfwSwapBuffers(glfwWindow);
  }

  public boolean shouldClose() {
    return glfwWindowShouldClose(glfwWindow);
  }

  public void close() {
    glfwSetWindowShouldClose(glfwWindow, true);
  }

  public void clean() {
    glfwFreeCallbacks(glfwWindow);
    glfwDestroyWindow(glfwWindow);

    glfwTerminate();
    errorCallback.free();
  }

  public void debugMessage(final String message) {
    glfwSetWindowTitle(glfwWindow, String.format("%s | %s", title, message));
  }

  private void initGraphics() {
    glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
    glCullFace(GL_BACK);
    glEnable(GL_DEPTH_TEST);
    glEnable(GL_TEXTURE_2D);
  }

  private void configureGlfwWindow() {
    glfwDefaultWindowHints();
    glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
    glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
    glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
    glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 2);
    glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GLFW_TRUE);
    glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
  }

  private void createGlfwWindow() {
    glfwWindow = glfwCreateWindow(winWidth, winHeight, title, NULL, NULL);
    if (NULL == glfwWindow) {
      throw new IllegalStateException("Failed to create the GLFW window");
    }

    try (final MemoryStack stack = stackPush()) {
      final IntBuffer pWidth = stack.mallocInt(1);
      final IntBuffer pHeight = stack.mallocInt(1);

      glfwGetWindowSize(glfwWindow, pWidth, pHeight);

      final GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
      if (null == vidmode) {
        throw new IllegalStateException("No GPU was found");
      }

      glfwSetWindowPos(
          glfwWindow,
          (vidmode.width() - pWidth.get(0)) / 2,
          (vidmode.height() - pHeight.get(0)) / 2);
    }
  }

  private void setCallbacks() {
    new MouseScroll(glfwWindow);
    new MousePosition(glfwWindow);
    new MouseButton(glfwWindow);
    new Keyboard(glfwWindow);

    glfwSetFramebufferSizeCallback(glfwWindow, this::framebufferSizeCallback);
    glfwSetWindowSizeCallback(glfwWindow, this::framebufferSizeCallback);
  }

  private void framebufferSizeCallback(final long window, final int width, final int height) {
    Window.winWidth = width;
    Window.winHeight = height;
    projectionMatrix =
        MathUtil.createProjectionMatrix(75.0f, (float) width / height, 0.01f, 1000.0f);
    glViewport(0, 0, width, height);
  }
}
