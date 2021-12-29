package nau.mike.rpg.engine;

import lombok.extern.slf4j.Slf4j;
import nau.mike.rpg.engine.input.Keyboard;
import nau.mike.rpg.engine.input.Keys;
import nau.mike.rpg.engine.utils.TimeUtil;

@Slf4j
public class Application {

  private boolean running;

  private final Window window;
  private final IGame game;

  public Application(final String title, final int width, final int height, final IGame game) {
    this.running = false;
    this.window = new Window(title, width, height);
    this.window.init();
    this.game = game;
  }

  public void init() {
    game.init();
  }

  private void input() {
    if (Keyboard.pressed(Keys.ESCAPE)) {
      window.close();
    }
    game.input();
  }

  private void update() {
    game.update();
  }

  private void render() {
    game.render();
  }

  private void clean() {
    game.clean();
  }

  private void run() {
    while (!window.shouldClose()) {
      TimeUtil.start();
      while (TimeUtil.shouldUpdate()) {
        input();
        update();
        TimeUtil.update();
      }
      window.startFrame();
      render();
      window.endFrame();
      TimeUtil.render();
      if (TimeUtil.shouldReset()) {
        window.debugMessage(TimeUtil.statistics());
        TimeUtil.reset();
      }
    }
    stop();
  }

  public void start() {
    if (!running) {
      running = true;
      run();
    }
  }

  private void stop() {
    clean();
    window.close();
    window.clean();
    running = false;
  }
}
