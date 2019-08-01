#version 300 es
precision mediump float;	
uniform sampler2D sTexture;		//纹理内容数据
in vec4 ambient;			//接收从顶点着色器传递过来的环境光参数
in vec4 diffuse;				//接收从顶点着色器传递过来的散射光参数
in vec4 specular;			//接收从顶点着色器传递过来的镜面光参数
in vec4 vPosition;  			//接收从顶点着色器传递过来的片元位置
uniform highp mat4 uMVPMatrixGY; //光源位置处虚拟摄像机观察及投影组合矩阵 
out vec4 fragColor;
void main()
{
   //将片元的位置投影到光源处虚拟摄像机的近平面上
   vec4 gytyPosition=uMVPMatrixGY * vec4(vPosition.xyz,1);
   gytyPosition=gytyPosition/gytyPosition.w;	//进行透视除法   
   float s=gytyPosition.s+0.5;				//将投影后的坐标换算为纹理坐标
   float t=gytyPosition.t+0.5;    
   vec4 finalcolor=vec4(0.8,0.8,0.8,1.0); 		   //物体本身的颜色
   vec4 colorA=finalcolor*ambient+finalcolor*specular+finalcolor*diffuse;//光照下颜色
   vec4 colorB=vec4(0.1,0.1,0.1,0.0);//阴影下的颜色
   if(s>=0.0&&s<=1.0&&t>=0.0&&t<=1.0)
   {	   
        //若纹理坐标在合法范围内则考虑投影贴图  
		vec4 projColor=texture(sTexture, vec2(s,t)); //对投影纹理图进行采样
		float a=step(0.9999,projColor.r);								//如果r<0.9999，则a=0，否则a=1
        float b=step(0.0001,projColor.r); 								//如果r<0.0001，则a=b，否则b=1
        float c=1.0-sign(a);  											//如果a>0,则c=1.如果a=0，则c=0            	
		fragColor =a*colorA+(1.0-b)*colorB+b*c*mix(					//计算最终片元颜色
		colorB,colorA,smoothstep(0.0,1.0,projColor.r));
   }
   else
   {
         fragColor = colorA;
   }
}     
