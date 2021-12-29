#version 400 core

in vec3 vSurfaceNormal;
in vec2 vTextureCoordinates;

uniform vec3 uBaseColor;
uniform sampler2D sampler;

out vec4 fColor;

void main()
{
    vec4 textureColor = texture(sampler, vTextureCoordinates);

    if(vec4(0.0f, 0.0f, 0.0f, 0.0f) == textureColor)
    {
        fColor = vec4(uBaseColor, 1.0f);
    }
    else
    {
        fColor = textureColor * vec4(uBaseColor, 1.0f);
    }
}
