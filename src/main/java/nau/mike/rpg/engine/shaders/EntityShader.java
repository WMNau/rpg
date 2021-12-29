package nau.mike.rpg.engine.shaders;

public class EntityShader extends Shader {

  public EntityShader() {
    super("entity", false);
  }

  @Override
  protected void bindAllAttributes() {
    super.bindAttributeLocation(0, "position");
    super.bindAttributeLocation(1, "normal");
    super.bindAttributeLocation(2, "textureCoordinates");
  }
}
