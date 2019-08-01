#version 300 es
precision mediump float;			//设置默认精度
uniform sampler2D sTexture;		//纹理内容数据
in vec4 ambient;				//接收从顶点着色器传递过来的环境光参数
in vec4 diffuse;					//接收从顶点着色器传递过来的散射光参数
in vec4 specular;				//接收从顶点着色器传递过来的镜面光参数
in vec4 vPosition;  				//接收从顶点着色器传递过来的片元位置
out vec4 fragColor;
uniform highp mat4 uMVPMatrixGY; //光源位置处虚拟摄像机观察及投影组合矩阵
void main(){
   //将片元的位置投影到光源处虚拟摄像机的近平面上
   vec4 gytyPosition=uMVPMatrixGY * vec4(vPosition.xyz,1);
   gytyPosition=gytyPosition/gytyPosition.w;	//进行透视除法
   float s=gytyPosition.s+0.5;				//将投影后的坐标换算为纹理坐标
   float t=gytyPosition.t+0.5;
   vec4 finalColor=vec4(0.8,0.8,0.8,1.0); 		//物体本身的颜色
   if(s>=0.0&&s<=1.0&&t>=0.0&&t<=1.0){	//若纹理坐标在合法范围内则考虑投影贴图
   vec4 projColor=texture(sTexture,vec2(s,t));	//对投影纹理图进行采样
   vec4 specularTemp=projColor*specular;	//计算投影贴图对镜面光的影响
   vec4 diffuseTemp=projColor*diffuse;		//计算投影贴图对散射光的影响
   fragColor=finalColor*ambient+finalColor*specularTemp+finalColor*diffuseTemp;//计算最终片元颜色
   }else {//计算最终片元颜色
       fragColor = finalColor*ambient+finalColor*specular+finalColor*diffuse;
    }
}