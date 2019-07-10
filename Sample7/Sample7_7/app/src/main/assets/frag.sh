#version 300 es
precision mediump float;//给出浮点默认精度
uniform sampler2D sTexture;//纹理内容数据
out vec4 fragColor; 	//输出片元的颜色
void main() 
{  
   	//从内建变量获取纹理坐标
   vec2 texCoor=gl_PointCoord;
   //进行纹理采样
   fragColor = texture(sTexture,texCoor);
}