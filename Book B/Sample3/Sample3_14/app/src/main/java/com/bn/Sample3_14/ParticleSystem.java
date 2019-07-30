package com.bn.Sample3_14;//声明包

import static com.bn.Sample3_14.MySurfaceView.cx;
import static com.bn.Sample3_14.MySurfaceView.cz;
import static com.bn.Sample3_14.ParticleDataConstant.*;
import android.opengl.GLES30;

public class ParticleSystem implements Comparable<ParticleSystem> 
{
	public float[] startColor;//粒子起始颜色数组
	public float[] endColor;//粒子终止颜色数组
	public int srcBlend;//源混合因子
	public int dstBlend;//目标混合因子
	public int blendFunc;//混合方式
	public float maxLifeSpan;//粒子最大生命期
	public float lifeSpanStep;//粒子生命期步进
	public int sleepSpan;//粒子更新线程休眠时间间隔
	public int groupCount;//每批喷发的粒子数量
	public float sx;//基础发射点x坐标
	public float sy;//基础发射点y坐标
	float positionX;//绘制位置x坐标
	float positionZ;//绘制位置z坐标
	public float xRange;//发射点x方向的变化范围
	public float yRange;//发射点y方向的变化范围
	public float vx;//粒子发射的x方向速度
	public float vy;//粒子发射的y方向速度
	float yAngle=0;//此粒子系统的旋转角度
	ParticleForDraw fpfd;//粒子群的绘制者
	boolean flag=true;//线程工作的标志位
	float halfSize;//粒子半径
	
	public float[] points;//粒子对应的所有顶点数据数组
	
    public ParticleSystem(float positionx,float positionz,ParticleForDraw fpfd,int count)
    {//构造器
    	this.positionX=positionx;//初始化此粒子系统的绘制位置x坐标
    	this.positionZ=positionz;//初始化此粒子系统的绘制位置y坐标
    	this.startColor=START_COLOR[CURR_INDEX];//初始化粒子起始颜色
    	this.endColor=END_COLOR[CURR_INDEX];//初始化粒子终止颜色
    	this.srcBlend=SRC_BLEND[CURR_INDEX];//初始化源混合因子 
    	this.dstBlend=DST_BLEND[CURR_INDEX];//初始化目标混合因子
    	this.blendFunc=BLEND_FUNC[CURR_INDEX];//初始化混合方式
    	this.maxLifeSpan=MAX_LIFE_SPAN[CURR_INDEX];//初始化每个粒子的最大生命期
    	this.lifeSpanStep=LIFE_SPAN_STEP[CURR_INDEX];//初始化每个粒子的生命步进
    	this.groupCount=GROUP_COUNT[CURR_INDEX];//初始化每批喷发的粒子数
    	this.sleepSpan=THREAD_SLEEP[CURR_INDEX];//初始化线程的休眠时间
    	this.sx=0;//初始化此粒子系统的中心点x坐标
    	this.sy=0;//初始化此粒子系统的中心点y坐标
    	this.xRange=X_RANGE[CURR_INDEX];//初始粒子距离中心点x方向的最大距离
    	this.yRange=Y_RANGE[CURR_INDEX];//初始粒子距离中心点y方向的最大距离
    	this.vx=0;//初始化粒子的x方向运动速度
    	this.vy=VY[CURR_INDEX];//初始化粒子的y方向运动速度
    	this.halfSize=RADIS[CURR_INDEX];//初始化此粒子系统的粒子半径
    	
    	this.fpfd=fpfd;//初始化粒子群的绘制者
    	
    	this.points=initPoints(count);//初始化粒子所对应的所有顶点数据数组
    	fpfd.initVertexData(points);//调用初始化顶点坐标与纹理坐标数据的方法
    	new Thread()
    	{//创建粒子的更新线程
    		public void run()//重写run方法
    		{
    			while(flag)
    			{
    				update();//调用update方法更新粒子状态
    				try 
    				{
						Thread.sleep(sleepSpan);//休眠一定的时间
					} catch (InterruptedException e) 
					{
						e.printStackTrace();//打印异常信息
					}
    			}
    		}
    	}.start();//启动线程
    }
    
	public float[] initPoints(int zcount)//初始化粒子所对应的所有顶点数据的方法
	{
		float[] points=new float[zcount*4*6];//临时存放顶点数据的数组-每个粒子对应6个顶点，每个顶点包含4个值
		for(int i=0;i<zcount;i++)
		{//循环遍历所有粒子
			//在中心附近产生产生粒子的位置------**/
			float px=(float) (sx+xRange*(Math.random()*2-1.0f));//计算粒子位置的x坐标
	        float py=(float) (sy+yRange*(Math.random()*2-1.0f));//计算粒子位置的y坐标
	        float vx=(sx-px)/150;//计算粒子的x方向运动速度
	        
	        points[i*4*6]=px-halfSize/2;//粒子对应的第一个点的x坐标
	        points[i*4*6+1]=py+halfSize/2;//粒子对应的第一个点的y坐标
	        points[i*4*6+2]=vx;//粒子对应的第一个点的x方向运动速度
	        points[i*4*6+3]=10.0f;//粒子对应的第一个点的当前生命期--10代表粒子处于未激活状态
			
	        points[i*4*6+4]=px-halfSize/2;
	        points[i*4*6+5]=py-halfSize/2;
	        points[i*4*6+6]=vx;
	        points[i*4*6+7]=10.0f;
			
	        points[i*4*6+8]=px+halfSize/2;
	        points[i*4*6+9]=py+halfSize/2;
	        points[i*4*6+10]=vx;
	        points[i*4*6+11]=10.0f;
			
	        points[i*4*6+12]=px+halfSize/2;
	        points[i*4*6+13]=py+halfSize/2;
	        points[i*4*6+14]=vx;
	        points[i*4*6+15]=10.0f;
			
	        points[i*4*6+16]=px-halfSize/2;
	        points[i*4*6+17]=py-halfSize/2;
	        points[i*4*6+18]=vx;
	        points[i*4*6+19]=10.0f;
			
	        points[i*4*6+20]=px+halfSize/2;
	        points[i*4*6+21]=py-halfSize/2;
	        points[i*4*6+22]=vx;
	        points[i*4*6+23]=10.0f;
		}
		for(int j=0;j<groupCount;j++)
	    {//循环遍历第一批的粒子
			points[4*j*6+3]=lifeSpanStep;//设置粒子生命期，不为10时，表示粒子处于活跃状态
			points[4*j*6+7]=lifeSpanStep;//设置粒子生命期，不为10时，表示粒子处于活跃状态
			points[4*j*6+11]=lifeSpanStep;//设置粒子生命期，不为10时，表示粒子处于活跃状态
			points[4*j*6+15]=lifeSpanStep;//设置粒子生命期，不为10时，表示粒子处于活跃状态
			points[4*j*6+19]=lifeSpanStep;//设置粒子生命期，不为10时，表示粒子处于活跃状态
			points[4*j*6+23]=lifeSpanStep;//设置粒子生命期，不为10时，表示粒子处于活跃状态
			
	    }
		return points;//返回所有粒子顶点属性数据数组
	}
    
//  int countt=0;//计算帧速率的时间间隔次数--计算器
//	long timeStart=System.nanoTime();//开始时间	
	
    public void drawSelf(int texId)
    {//绘制此粒子系统中所有粒子的方法
//    	if(countt==19)//每十次一计算帧速率
//    	{
//    		long timeEnd=System.nanoTime();//结束时间
//    		
//    		//计算帧速率
//    		float ps=(float)(1000000000.0/((timeEnd-timeStart)/20));
//    		System.out.println("ps="+ps);
//    		countt=0;//计算器置0
//    		timeStart=timeEnd;//起始时间置为结束时间
//    	}
//    	countt=(countt+1)%20;//更新计数器的值
    	
        GLES30.glDisable(GLES30.GL_DEPTH_TEST);//关闭深度检测
        GLES30.glEnable(GLES30.GL_BLEND); //开启混合 
        GLES30.glBlendEquation(blendFunc);//设置混合方式
        GLES30.glBlendFunc(srcBlend,dstBlend); //设置混合因子
        
    	MatrixState.translate(positionX, 1, positionZ);//执行平移变换
		MatrixState.rotate(yAngle, 0, 1, 0);//执行旋转变换
		
		MatrixState.pushMatrix();//保护现场
    	fpfd.drawSelf(texId,startColor,endColor,maxLifeSpan);//绘制粒子群   	
		MatrixState.popMatrix();//恢复现场
    	
        GLES30.glEnable(GLES30.GL_DEPTH_TEST);//开启深度检测
        GLES30.glDisable(GLES30.GL_BLEND);//关闭混合  
    }
    
	int count=1;//激活粒子的位置计算器
	public void update()//更新粒子状态的方法
	{	
		if(count>=(points.length/groupCount/4/6))//计算器超过激活粒子位置时
		{
			count=0;//重新计数
		}
		
		//查看生命期以及计算下一位置的相应数据
		for(int i=0;i<points.length/4/6;i++)
		{//循环遍历所有粒子
			if(points[i*4*6+3]!=10.0f)//当前为活跃粒子时
			{
				points[i*4*6+3]+=lifeSpanStep;//计算当前生命期
				points[i*4*6+7]+=lifeSpanStep;//计算当前生命期
				points[i*4*6+11]+=lifeSpanStep;//计算当前生命期
				points[i*4*6+15]+=lifeSpanStep;//计算当前生命期
				points[i*4*6+19]+=lifeSpanStep;//计算当前生命期
				points[i*4*6+23]+=lifeSpanStep;//计算当前生命期
	    		if(points[i*4*6+3]>this.maxLifeSpan)//当前生命期大于最大生命期时---重新设置该粒子参数
	    		{
	    			float px=(float) (sx+xRange*(Math.random()*2-1.0f));//计算粒子位置x坐标
	                float py=(float) (sy+yRange*(Math.random()*2-1.0f));//计算粒子位置y坐标
	                float vx=(sx-px)/150;//计算粒子x方向的速度
	                
	                points[i*4*6]=px-halfSize/2;//粒子对应的第一个顶点的x坐标
	    	        points[i*4*6+1]=py+halfSize/2;//粒子对应的第一个顶点的y坐标
	    	        points[i*4*6+2]=vx;//粒子对应的第一个顶点的x方向运动速度
	    	        points[i*4*6+3]=10.0f;//粒子对应的第一个顶点的当前生命期--10代表粒子处于未激活状态
	    			
	    	        points[i*4*6+4]=px-halfSize/2;
	    	        points[i*4*6+5]=py-halfSize/2;
	    	        points[i*4*6+6]=vx;
	    	        points[i*4*6+7]=10.0f;
	    			
	    	        points[i*4*6+8]=px+halfSize/2;
	    	        points[i*4*6+9]=py+halfSize/2;
	    	        points[i*4*6+10]=vx;
	    	        points[i*4*6+11]=10.0f;
	    			
	    	        points[i*4*6+12]=px+halfSize/2;
	    	        points[i*4*6+13]=py+halfSize/2;
	    	        points[i*4*6+14]=vx;
	    	        points[i*4*6+15]=10.0f;
	    			
	    	        points[i*4*6+16]=px-halfSize/2;
	    	        points[i*4*6+17]=py-halfSize/2;
	    	        points[i*4*6+18]=vx;
	    	        points[i*4*6+19]=10.0f;
	    			
	    	        points[i*4*6+20]=px+halfSize/2;
	    	        points[i*4*6+21]=py-halfSize/2;
	    	        points[i*4*6+22]=vx;
	    	        points[i*4*6+23]=10.0f;
	    		}else//生命期小于最大生命期时----计算粒子的下一位置坐标
	    		{
	    			 points[i*4*6]+=points[i*4*6+2];//计算粒子对应的第一个顶点的x坐标
	                 points[i*4*6+1]+=vy;//计算粒子对应的第一个顶点的y坐标
	                 
	                 points[i*4*6+4]+=points[i*4*6+6];
	                 points[i*4*6+5]+=vy;
	                 
	                 points[i*4*6+8]+=points[i*4*6+10];
	                 points[i*4*6+9]+=vy;
	                 
	                 points[i*4*6+12]+=points[i*4*6+14];
	                 points[i*4*6+13]+=vy;
	                 
	                 points[i*4*6+16]+=points[i*4*6+18];
	                 points[i*4*6+17]+=vy;
	                 
	                 points[i*4*6+20]+=points[i*4*6+22];
	                 points[i*4*6+21]+=vy;
	    		}
			}
		}
		
		for(int i=0;i<groupCount;i++)
		{//循环发射一批激活计数器所指定位置的粒子
			if(points[groupCount*count*4*6+4*i*6+3]==10.0f)//如果粒子处于未激活状态时
			{
				points[groupCount*count*4*6+4*i*6+3]=lifeSpanStep;//激活粒子--设置粒子当前的生命周期
				points[groupCount*count*4*6+4*i*6+7]=lifeSpanStep;//激活粒子--设置粒子当前的生命周期
				points[groupCount*count*4*6+4*i*6+11]=lifeSpanStep;//激活粒子--设置粒子当前的生命周期
				points[groupCount*count*4*6+4*i*6+15]=lifeSpanStep;//激活粒子--设置粒子当前的生命周期
				points[groupCount*count*4*6+4*i*6+19]=lifeSpanStep;//激活粒子--设置粒子当前的生命周期
				points[groupCount*count*4*6+4*i*6+23]=lifeSpanStep;//激活粒子--设置粒子当前的生命周期
			}
		}
		synchronized(lock)
		{//加锁--防止在更新顶点坐标数据时，将顶点坐标数据送入渲染管线
			fpfd.updatVertexData(points);//更新顶点坐标数据缓冲的方法
		}
		count++;//下次激活粒子的位置
	}
    
	public void calculateBillboardDirection()
	{
		//根据摄像机位置计算火焰朝向
		float xspan=positionX-MySurfaceView.cx;
		float zspan=positionZ-MySurfaceView.cz;
		
		if(zspan<=0)
		{
			yAngle=(float)Math.toDegrees(Math.atan(xspan/zspan));	
		}
		else
		{
			yAngle=180+(float)Math.toDegrees(Math.atan(xspan/zspan));
		}
	}
	@Override
	public int compareTo(ParticleSystem another) {
		//重写的比较两个火焰离摄像机距离的方法
		float xs=positionX-cx;
		float zs=positionZ-cz;
		
		float xo=another.positionX-cx;
		float zo=another.positionZ-cz;
		
		float disA=(float)(xs*xs+zs*zs);
		float disB=(float)(xo*xo+zo*zo);
		return ((disA-disB)==0)?0:((disA-disB)>0)?-1:1;  
	}

}
