#version 300 es
uniform mat4 uMVPMatrix; //总变换矩阵
in vec3 aPosition;  //顶点位置
in vec2 aTexCoor;    //顶点纹理坐标
out vec2 vTextureCoord;  //用于传递给片元着色器的变量
uniform float ratio;//当前整体扭动角度因子
void main()
{       
	float pi = 3.1415926; //圆周率
	float centerX=0.0;//中心点的X坐标
	float centerY=-5.0;//中心点的Y坐标
	float currX = aPosition.x;//当前点的x坐标
	float currY = aPosition.y;//当前点的y坐标
	float spanX = currX - centerX;//当前x偏移量
	float spanY = currY - centerY;//当前y偏移量
	float currRadius = sqrt(spanX * spanX + spanY * spanY);//计算距离
	float currRadians;//当前点与x轴正方向的夹角
	if(spanX != 0.0)
	{//一般情况
		currRadians = atan(spanY , spanX);
	}
	else
	{
		currRadians = spanY > 0.0 ? pi/2.0 : 3.0*pi/2.0; 
	}
	float resultRadians = currRadians + ratio*currRadius;//计算出扭曲后的角度
	float resultX = centerX + currRadius * cos(resultRadians);//计算结果点的x坐标
	float resultY = centerY + currRadius * sin(resultRadians);//计算结果点的y坐标
	//构造结果点，并根据总变换矩阵计算此次绘制此顶点的位置
    gl_Position = uMVPMatrix * vec4(resultX,resultY,0.0,1); 
    vTextureCoord = aTexCoor;//将接收的纹理坐标传递给片元着色器
}      
                     