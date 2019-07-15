package com.bn.Sample11_7;

import static com.bn.Sample11_7.Constant.*;
import android.opengl.GLES30;
//存储球运动过程中物理信息的对象所属类
public class BallForControl   
{	
	public static final float TIME_SPAN=0.05f;//物理模拟的单位时间间隔
	public static final float G=0.8f;//重力加速度
	 
	BallTextureByVertex btv;//用于绘制的篮球的纹理球
	float startY;//每轮起始点位置
	float timeLive=0;//此周期存活时长
	float currentY=0;//当前Y位置
	float vy=0;//每轮初始y轴方向速度
	
	public BallForControl(BallTextureByVertex btv,float startYIn)
	{
		//初始化速度与起始位置
		this.btv=btv;
		this.startY=startYIn;		
		currentY=startYIn;
		new Thread()
		{//开启一个线程运动篮球
			public void run()
			{
				while(true)
				{
					//此轮运动时间增加
					timeLive+=TIME_SPAN;
					//根据此轮起始Y坐标、此轮运动时间、此轮起始速度计算当前位置
					float tempCurrY=startY-0.5f*G*timeLive*timeLive+vy*timeLive;
					
					
					if(tempCurrY<=FLOOR_Y)
					{//若当前位置低于地面则碰到地面反弹
						//反弹后起始高度为0
						startY=FLOOR_Y;		
						//反弹后起始速度
						vy=-(vy-G*timeLive)*0.8f;
						//反弹后此轮运动时间清0
						timeLive=0;
						//若速度小于阈值则停止运动
						if(vy<0.35f)
						{
							currentY=FLOOR_Y;
							break;
						}
					}
					else
					{//若没有碰到地面则正常运动
						currentY=tempCurrY;
					}
					
					try
					{
						Thread.sleep(20);
					}
					catch(Exception e)
					{
						e.printStackTrace();
					}
				}
			}
		}.start();
	}
	
	public void drawSelf(int texId)
	{//绘制物体自身	
		MatrixState.pushMatrix();
		MatrixState.translate(0, UNIT_SIZE*BALL_SCALE+currentY, 0);
		btv.drawSelf(texId);		//绘制篮球			
		MatrixState.popMatrix();
	}
	
	public void drawSelfMirror(int texId)
	{//绘制 镜像体		
		GLES30.glFrontFace(GLES30.GL_CW);//顺时针卷绕
		MatrixState.pushMatrix();	
		MatrixState.scale(1, -1, 1);
		MatrixState.translate(0, UNIT_SIZE*BALL_SCALE+currentY-2*FLOOR_Y, 0);
		btv.drawSelf(texId);		//绘制篮球
		MatrixState.popMatrix();
		GLES30.glFrontFace(GLES30.GL_CCW);//逆时针卷绕
	}
}
