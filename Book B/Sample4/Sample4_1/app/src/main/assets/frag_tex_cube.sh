#version 300 es
precision mediump float;
uniform samplerCube sTexture;//纹理内容数据
in vec3 vTextureCoord; //接收从顶点着色器过来的参数
out vec4 fragColor;//输出到的片元颜色
void main() {
    //通过传入的采样向量与立方图纹理调用texture方法执行采样
   fragColor=texture(sTexture, vTextureCoord);    
}   
