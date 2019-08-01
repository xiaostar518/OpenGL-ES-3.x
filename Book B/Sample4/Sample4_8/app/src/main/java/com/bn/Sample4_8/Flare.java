package com.bn.Sample4_8;//声明包

import static com.bn.Sample4_8.Constant.DIS_MAX;
import static com.bn.Sample4_8.Constant.SCALE_MAX;
import static com.bn.Sample4_8.Constant.SCALE_MIN;

import java.util.ArrayList;//引用列表

public class Flare{
	
	public int[] textures;//纹理数组
	
	public ArrayList<SingleFlare> sFl=new ArrayList<SingleFlare>();//存放光晕元素的列表
	public Flare(int[] textures)
	{
		this.textures=textures;//初始化纹理数组
		initFlare();//初始化光晕元素
	}
	public void initFlare()
	{//初始化光晕元素对象的方法
		//以下创建并向列表中添加了13个镜头光晕元素对象
		sFl.add(new SingleFlare(textures[1],5.4f,-1.0f,new float[]{1.0f,1.0f,1.0f,1.0f}));
		sFl.add(new SingleFlare(textures[1],0.4f,-0.8f,new float[]{0.7f,0.5f,0.0f,0.02f}));
		sFl.add(new SingleFlare(textures[1],0.04f,-0.7f,new float[]{1.0f, 0.0f, 0.0f, 0.07f}));
		sFl.add(new SingleFlare(textures[0],0.4f,-0.5f,new float[]{1.0f, 1.0f, 0.0f, 0.05f}));
		sFl.add(new SingleFlare(textures[2],1.22f,-0.4f,new float[]{1.0f, 1.0f, 0.0f, 0.05f}));
		sFl.add(new SingleFlare(textures[0],0.4f,-0.3f,new float[]{1.0f, 0.5f, 0.0f, 1.0f}));
		sFl.add(new SingleFlare(textures[1],0.4f,-0.1f,new float[]{1.0f, 1.0f, 0.5f, 0.05f}));
		sFl.add(new SingleFlare(textures[0],0.4f,0.2f,new float[]{1.0f, 0.0f, 0.0f, 1.0f}));
		sFl.add(new SingleFlare(textures[1],0.8f,0.3f,new float[]{1.0f, 1.0f, 0.6f, 1.0f}));
		sFl.add(new SingleFlare(textures[0],0.6f,0.4f,new float[]{1.0f, 0.7f, 0.0f, 0.03f}));
		sFl.add(new SingleFlare(textures[2],0.6f,0.7f,new float[]{1.0f, 0.5f, 0.0f, 0.02f}));
		sFl.add(new SingleFlare(textures[2],1.28f,1.0f,new float[]{1.0f, 0.7f, 0.0f, 0.02f}));
		sFl.add(new SingleFlare(textures[2],3.20f,1.3f,new float[]{1.0f, 0.0f, 0.0f, 0.05f}));
	}
	
	public void update(float lx,float ly)
	{//更新光晕位置的方法
		
		float currDis=(float)Math.sqrt(lx*lx+ly*ly);//太阳到原点的距离
		float currScale=SCALE_MIN+(SCALE_MAX-SCALE_MIN)*(1-currDis/DIS_MAX);//距离比例值-用于绘制尺寸的计算
		
		for(SingleFlare ss:sFl)
		{//循环遍历所有光晕元素对象
			ss.px=-ss.distance*lx;//计算该光晕元素的绘制位置x坐标
			ss.py=-ss.distance*ly;//计算该光晕元素的绘制位置y坐标
			ss.bSize=ss.size*currScale;//计算变换后的尺寸
		}
	}
}
