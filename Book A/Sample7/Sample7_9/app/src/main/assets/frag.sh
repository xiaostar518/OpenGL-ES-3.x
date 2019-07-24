#version 300 es
precision mediump float;//给出浮点默认精度
uniform mediump sampler2DArray sTexture;//纹理内容数据
in float vid;//顶点编号
out vec4 fragColor; //传递到渲染管线的片元颜色

void main() 
{  
   //组装纹理坐标
   vec3 texCoor=vec3(gl_PointCoord.st,vid);
   //进行纹理采样
   fragColor = texture(sTexture,texCoor);
}