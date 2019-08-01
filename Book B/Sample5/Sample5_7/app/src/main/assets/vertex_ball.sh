#version 300 es
uniform mat4 uMVPMatrix; //总变换矩阵
in vec3 aPosition;  //顶点位置
in vec3 aNormal;    //顶点法向量
out vec3 vNormal;//用于传递给片元着色器的顶点法向量
out vec3 vPosition;//用于传递给片元着色器的顶点位置

void main()     
{ 	
  	vNormal=aNormal;//将顶点法向量传递给片元着色器
  	vPosition=aPosition;//将视线向量传递给片元着色器
	gl_Position=uMVPMatrix*vec4(aPosition,1);//根据总变换矩阵计算此次绘制此顶点位置
}