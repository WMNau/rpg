#version 400 core

in vec3 position;
in vec3 normal;
in vec2 textureCoordinates;

uniform mat4 uModelMatrix;
uniform mat4 uViewMatrix;
uniform mat4 uProjectionMatrix;

out vec3 vSurfaceNormal;
out vec2 vTextureCoordinates;
out vec3 vWorldPosition;

void main()
{
    vec4 worldPosition = uModelMatrix * vec4(position, 1.0f);
    gl_Position = uProjectionMatrix * uViewMatrix * worldPosition;

    vSurfaceNormal = (vec4(normal, 0.0f) * worldPosition).xyz;
    vTextureCoordinates = textureCoordinates;
    vWorldPosition = worldPosition.xyz;
}
