#version 300 es
uniform mat4 uMVPMatrix; //总变换矩阵
in vec3 aPosition;  //顶点位置
in vec3 aNormal;    //顶点法向量

void main()     
{ 
	vec3 position=aPosition;//获取此顶点位置
	//将法向量转化进视空间
	vec4 ydskj=uMVPMatrix*vec4(0,0,0,1);
	vec4 fxldskj=uMVPMatrix*vec4(aNormal.xyz,1.0);
	vec2 skjNormal=fxldskj.xy-ydskj.xy;
	skjNormal=normalize(skjNormal); 
	vec4 finalPosition=uMVPMatrix * vec4(position.xyz,1);
	finalPosition=finalPosition/finalPosition.w;
   	gl_Position =finalPosition+vec4(skjNormal.xy,1.0,1.0)*0.01;//根据总变换矩阵计算此次绘制此顶点位置  
}                    