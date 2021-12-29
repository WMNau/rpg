#version 400 core

const int MAX_POINT_LIGHTS = 4;
const int MAX_SPOT_LIGHTS = 4;

struct BaseLight
{
    vec3 color;
    float intensity;
};

struct DirectionalLight
{
    BaseLight base;
    vec3 direction;
};

struct Attenuation
{
    float constant;
    float linear;
    float exponent;
};

struct PointLight
{
    BaseLight base;
    Attenuation attenuation;
    vec3 position;
    float range;
};

struct SpotLight
{
    PointLight pointLight;
    vec3 direction;
    float cutoff;
};

in vec3 vSurfaceNormal;
in vec2 vTextureCoordinates;
in vec3 vWorldPosition;

uniform vec3 uBaseColor;
uniform vec3 uEyePosition;
uniform vec3 uAmbientLight;
uniform sampler2D sampler;

uniform float uSpecularIntensity;
uniform float uSpecularPower;

uniform DirectionalLight uDirectionalLight;
uniform PointLight uPointLights[MAX_POINT_LIGHTS];
uniform SpotLight uSpotLights[MAX_SPOT_LIGHTS];

out vec4 fColor;

vec4 calcLight(BaseLight base, vec3 direction, vec3 normal)
{
    float diffuseFactor = dot(normal, -direction);
    vec4 diffuseColor = vec4(0.0f);
    vec4 specularColor = vec4(0.0f);
    if (diffuseFactor > 0.0f)
    {
        diffuseColor = vec4(base.color, 1.0f) * base.intensity * diffuseFactor;

        vec3 directionToEye = normalize(uEyePosition - vWorldPosition);
        vec3 reflectDirection = normalize(reflect(direction, normal));

        float specularFactor = dot(directionToEye, reflectDirection);
        specularFactor = pow(specularFactor, uSpecularPower);

        if (specularFactor > 0)
        {
            specularColor = vec4(base.color, 1.0) * uSpecularIntensity * specularFactor;
        }
    }
    return diffuseColor + specularColor;
}

vec4 calcDirectionalLight(DirectionalLight directionalLight, vec3 normal)
{
    vec3 direction = normalize(directionalLight.direction);
    return calcLight(directionalLight.base, direction, normal); // direction was negative
}

vec4 calcPointLight(PointLight pointLight, vec3 normal)
{
    vec3 lightDirection = vWorldPosition - pointLight.position;
    float distanceToPoint = length(lightDirection);
    if (distanceToPoint > pointLight.range)
    {
        return vec4(0.0f);
    }

    lightDirection = normalize(lightDirection);

    vec4 color = calcLight(pointLight.base, lightDirection, normal);

    float attenuation = pointLight.attenuation.constant
    + pointLight.attenuation.linear * distanceToPoint
    + pointLight.attenuation.exponent * distanceToPoint * distanceToPoint + 0.001f;

    return color / attenuation;
}

vec4 calcSpotLight(SpotLight spotLight, vec3 normal)
{
    vec3 direction = normalize(spotLight.direction);
    vec3 lightDirection = normalize(vWorldPosition - spotLight.pointLight.position);
    float spotFactor = dot(lightDirection, direction);

    vec4 color = vec4(0.0f);
    if (spotFactor > spotLight.cutoff)
    {
        color = calcPointLight(spotLight.pointLight, normal) * (1.0f - (1.0f - spotFactor) / (1.0f - spotLight.cutoff));
    }
    return color;
}

void main()
{
    vec4 totalLight = vec4(uAmbientLight, 1.0f);
    vec4 textureColor = texture(sampler, vTextureCoordinates);
    vec4 color = vec4(uBaseColor, 1.0f);
    vec3 unitNormal = normalize(vSurfaceNormal);

    if (vec4(0.0f, 0.0f, 0.0f, 0.0f) != textureColor)
    {
        color = mix(textureColor, color, 0.1f);
    }

    totalLight += calcDirectionalLight(uDirectionalLight, unitNormal);

    for (int i = 0; i < MAX_POINT_LIGHTS; i++)
    {
        if (uPointLights[i].base.intensity > 0.0f)
        {
            totalLight += calcPointLight(uPointLights[i], unitNormal);
        }
    }

    for (int i = 0; i < MAX_SPOT_LIGHTS; i++)
    {
        if (uSpotLights[i].pointLight.base.intensity > 0.0f)
        {
            totalLight += calcSpotLight(uSpotLights[i], unitNormal);
        }
    }

    fColor = color * totalLight;
}
