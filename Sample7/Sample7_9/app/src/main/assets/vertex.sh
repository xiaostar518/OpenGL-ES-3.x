#version 300 es
uniform mat4 uMVPMatrix; //总变换矩阵
in vec3 aPosition;  //顶点位置
out float vid;//顶点编号

void main()  
{                            		
   gl_Position = uMVPMatrix * vec4(aPosition,1); //根据总变换矩阵计算此次绘制此顶点位置
   gl_PointSize=64.0;//设置点精灵对应点的大小
   vid=float(gl_VertexID);//将顶点编号传递给片元着色器
}                      