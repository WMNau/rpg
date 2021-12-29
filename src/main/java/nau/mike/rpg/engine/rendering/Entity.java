package nau.mike.rpg.engine.rendering;

import lombok.AllArgsConstructor;
import lombok.Getter;
import nau.mike.rpg.engine.cameras.Camera;
import nau.mike.rpg.engine.shaders.EntityShader;
import nau.mike.rpg.engine.shaders.PhongShader;

@AllArgsConstructor
@Getter
public abstract class Entity {

  protected EntityShader entityShader;
  protected PhongShader phongShader;

  protected Mesh mesh;
  protected Transform transform;
  protected Material material;

  public void update() {
    transform.setPosition(0.0f, 1.0f, 0.0f);}

  public void render(final Camera camera) {
    renderEntity(camera);
    renderLight(camera);
    mesh.draw();
    if (null != material.getTexture()) {
      material.getTexture().unbind();
    }
    phongShader.stop();
    entityShader.stop();
  }

  private void renderEntity(final Camera camera) {
    entityShader.start();
    entityShader.loadModelProjectionMatrix(transform);
    entityShader.loadViewMatrix(camera.getViewMatrix());
    entityShader.loadMaterial(material);
  }

  private void renderLight(final Camera camera) {
    phongShader.start();
    phongShader.loadModelProjectionMatrix(transform);
    phongShader.loadViewMatrix(camera.getViewMatrix());
    phongShader.loadMaterial(material);
    phongShader.loadEyePosition(camera.getPosition());
    phongShader.loadLights();
  }

  public void clean(){
    material.clean();
    mesh.clean();
    entityShader.clean();
    phongShader.clean();
  }
}
