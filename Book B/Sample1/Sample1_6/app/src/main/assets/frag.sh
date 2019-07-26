#version 300 es
precision mediump float;
uniform sampler2D sTexture;//纹理内容数据
//接收从顶点着色器过来的参数
in vec4 ambient;
in vec4 diffuse;
in vec4 specular;
in vec2 vTextureCoord;

layout (location=0)out vec4 fragColor0;
layout (location=1)out vec4 fragColor1;
layout (location=2)out vec4 fragColor2;
layout (location=3)out vec4 fragColor3;

void main()                         
{    
   //将计算出的颜色给此片元
   vec4 finalColor=texture(sTexture, vTextureCoord);    
   //给此片元颜色值
   vec4 fragColor = finalColor*ambient+finalColor*specular+finalColor*diffuse;
   
   fragColor0=fragColor;
   fragColor1=vec4(fragColor.r,0.0,0.0,1.0);
   fragColor2=vec4(0.0,fragColor.g,0.0,1.0);
   fragColor3=vec4(0.0,0.0,fragColor.b,1.0);
}   