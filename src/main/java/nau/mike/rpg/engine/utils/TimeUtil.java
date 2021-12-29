package nau.mike.rpg.engine.utils;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TimeUtil {

  private static final float fps = 60.0f;
  private static final float NS = 1_000_000_000.0f / fps;

  private static long lastTime = System.nanoTime();
  private static long timer = System.currentTimeMillis();

  @Getter private static float delta = 0.0f;

  private static int updates = 0;
  private static int frames = 0;

  public static void start() {
    final long now = System.nanoTime();
    delta += (now - lastTime) / NS;
    lastTime = now;
  }

  public static boolean shouldUpdate() {
    return delta >= 1.0f;
  }

  public static void update() {
    delta--;
    updates++;
  }

  public static void render() {
    frames++;
  }

  public static boolean shouldReset() {
    return (System.currentTimeMillis() - timer) > 1000L;
  }

  public static void reset() {
    timer += 1000L;
    updates = 0;
    frames = 0;
  }

  public static String statistics() {
    return String.format("UPS: %d, FPS: %d", updates, frames);
  }
}
