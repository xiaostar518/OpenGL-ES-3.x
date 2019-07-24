#version 300 es   
precision mediump float;//给出默认的浮点精度
uniform sampler2D sTexture;//纹理内容数据
in float vEdge;//描边系数
in vec2 vTextureCoord;//纹理坐标
out vec4 fragColor;//输出到的片元颜色

void main()                         
{
   //从纹理中采样出颜色值            
   vec4 finalColor=texture(sTexture, vTextureCoord);
   //描边的颜色
   const vec4 edgeColor=vec4(0.0);
   //计算此片元是否进行描边的因子
   float mbFactor=step(0.2,vEdge);//vEdge>0.2--return0
   //如果不为边缘像素用纹理采样颜色，如果为边缘像素用描边颜色
   fragColor=(1.0-mbFactor)*edgeColor+mbFactor*finalColor;
}