package nau.mike.rpg.engine.shaders;

import lombok.Getter;
import lombok.Setter;
import nau.mike.rpg.engine.lighting.*;
import org.joml.Vector3f;

public class PhongShader extends Shader {

  private static final int MAX_POINT_LIGHTS = 4;
  private static final int MAX_SPOT_LIGHTS = 4;

  @Getter @Setter private static Vector3f ambientLight;
  @Getter @Setter private static DirectionalLight directionalLight;
  @Getter private static PointLight[] pointLights;
  @Getter private static SpotLight[] spotLights;

  public PhongShader() {
    super("phong", false);
    PhongShader.ambientLight = new Vector3f(1.0f);
    final BaseLight baseLight = new BaseLight(new Vector3f(), 0.0f);
    PhongShader.directionalLight = new DirectionalLight(baseLight, new Vector3f());
    PhongShader.pointLights = new PointLight[] {};
    PhongShader.spotLights = new SpotLight[] {};
  }

  public static void addPointLights(final PointLight[] pointLights) {
    if (MAX_POINT_LIGHTS >= pointLights.length) {
      PhongShader.pointLights = pointLights;
    }
  }

  public static void addSpotLights(final SpotLight[] spotLights) {
    if (MAX_SPOT_LIGHTS >= spotLights.length) {
      PhongShader.spotLights = spotLights;
    }
  }

  @Override
  protected void bindAllAttributes() {
    super.bindAttributeLocation(0, "position");
    super.bindAttributeLocation(1, "normal");
    super.bindAttributeLocation(2, "textureCoordinates");
  }

  public void loadAmbientLight() {
    setUniform("uAmbientLight", PhongShader.ambientLight);
  }

  public void loadEyePosition(final Vector3f eyePosition) {
    setUniform("uEyePosition", eyePosition);
  }

  public void loadLights() {
    loadDirectionalLight();
    loadPointLights();
    loadSpotLights();
  }

  public void loadDirectionalLight() {
    final String uniform = "uDirectionalLight";
    loadBaseLight(uniform, directionalLight.getBaseLight());
    setUniform(uniform + ".direction", directionalLight.getDirection());
  }

  public void loadSpotLights() {
    final String uniform = "uSpotLights[";
    for (int i = 0; i < MAX_SPOT_LIGHTS; i++) {
      if (i == spotLights.length) {
        break;
      }
      final String uniformName = String.format("%s%d].pointLight", uniform, i);
      loadBaseLight(uniformName, spotLights[i].getPointLight().getBaseLight());
      loadAttenuation(uniformName, spotLights[i].getPointLight().getAttenuation());
      loadPointLights(uniformName, spotLights[i].getPointLight());
      setUniform(uniform + i + "].direction", spotLights[i].getDirection());
      setUniform(uniform + i + "].cutoff", spotLights[i].getCutoff());
    }
  }

  public void loadPointLights() {
    final String uniform = "uPointLights[";
    for (int i = 0; i < MAX_POINT_LIGHTS; i++) {
      if (i == pointLights.length) {
        break;
      }
      loadBaseLight(uniform + i + "]", pointLights[i].getBaseLight());
      loadAttenuation(uniform + i + "]", pointLights[i].getAttenuation());
      loadPointLights(uniform + i + "]", pointLights[i]);
    }
  }

  private void loadPointLights(final String uniformName, final PointLight pointLight) {
    setUniform(uniformName + ".position", pointLight.getPosition());
    setUniform(uniformName + ".range", pointLight.getRange());
  }

  private void loadBaseLight(final String uniformName, final BaseLight light) {
    final String uniform = uniformName + ".base";
    setUniform(uniform + ".color", light.getColor());
    setUniform(uniform + ".intensity", light.getIntensity());
  }

  private void loadAttenuation(final String uniformName, final Attenuation attenuation) {
    final String uniform = uniformName + ".attenuation";
    setUniform(uniform + ".constant", attenuation.getConstant());
    setUniform(uniform + ".linear", attenuation.getLinear());
    setUniform(uniform + ".exponent", attenuation.getExponent());
  }
}
