#version 300 es
uniform mat4 uMVPMatrix; 		//总变换矩阵
uniform mat4 uMMatrix; 			//变换矩阵
uniform vec3 uLightLocation;	//光源位置
uniform vec3 uCamera;			//摄像机位置
in vec3 aPosition;  			//顶点位置
in vec3 aNormal;    			//顶点法向量
out float vEdge;				//描边系数
out vec2 vTextureCoord;			//根据光照强度折算的纹理坐标

void pointLight(				//定位光光照计算的方法
  in vec3 normal,				//法向量
  out float diffuse,			//散射光最终强度
  out float specular,			//镜面光最终强度
  out float edge,				//描边系数
  in vec3 lightLocation,		//光源位置
  in float lightDiffuse,		//散射光强度
  in float lightSpecular		//镜面光强度
){
  vec3 normalTarget=aPosition+normal;	//计算变换后的法向量
  vec3 newNormal=(uMMatrix*vec4(normalTarget,1)).xyz-(uMMatrix*vec4(aPosition,1)).xyz;
  newNormal=normalize(newNormal); 	//对法向量规格化
  //计算从表面点到摄像机的向量
  vec3 eye= normalize(uCamera-(uMMatrix*vec4(aPosition,1)).xyz);  
  //计算描边系数
  edge = max(0.0,dot(newNormal,eye));
  //计算从表面点到光源位置的向量vp
  vec3 vp= normalize(lightLocation-(uMMatrix*vec4(aPosition,1)).xyz);  
  vp=normalize(vp);//格式化vp
  vec3 halfVector=normalize(vp+eye);	//求视线与光线的半向量    
  float shininess=50.0;				//粗糙度，越小越光滑
  float nDotViewPosition=max(0.0,dot(newNormal,vp)); 	//求法向量与vp的点积与0的最大值  
  diffuse=lightDiffuse*nDotViewPosition;				//计算散射光的最终强度
  float nDotViewHalfVector=dot(newNormal,halfVector);	//法线与半向量的点积 
  float powerFactor=max(0.0,pow(nDotViewHalfVector,shininess)); 	//镜面反射光强度因子
  specular=lightSpecular*powerFactor;    			//计算镜面光的最终强度
}

void main(){ 
   gl_Position = uMVPMatrix * vec4(aPosition,1); //根据总变换矩阵计算此次绘制此顶点位置  
   float diffuse;//散射光的最终强度
   float specular;//镜面光的最终强度
   //进行光照计算
   pointLight(normalize(aNormal),diffuse,specular,vEdge,uLightLocation,0.8,0.9);
   //将散射光的最终强度与镜面光的最终强度相加--光照强度
   float s=diffuse+specular;
   //相加后的值作为S纹理坐标
   vTextureCoord=vec2(s,0.5);
}                      