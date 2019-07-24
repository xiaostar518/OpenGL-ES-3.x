#version 300 es
in vec3 Position;
in vec4 SourceColor;
out vec4 DestinationColor;
uniform mat4 Modelview;

void main(void)
{
    DestinationColor = SourceColor;
    gl_Position = Modelview * vec4(Position,1);
}






