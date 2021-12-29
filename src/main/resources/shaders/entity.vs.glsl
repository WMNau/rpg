#version 400 core

in vec3 position;
in vec3 normal;
in vec2 textureCoordinates;

uniform mat4 uModelMatrix;
uniform mat4 uViewMatrix;
uniform mat4 uProjectionMatrix;

out vec3 vSurfaceNormal;
out vec2 vTextureCoordinates;

void main()
{
    gl_Position = uProjectionMatrix * uViewMatrix * uModelMatrix * vec4(position, 1.0f);

    vSurfaceNormal = normalize(normal);
    vTextureCoordinates = textureCoordinates;
}
