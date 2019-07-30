package com.bn.Sample3_14;//声明包

import android.opengl.GLES30;//相关类的引用
     
public class ParticleDataConstant 
{       
	//资源访问锁
	public static Object lock=new Object();
	//墙体的长度缩放比
	public static float wallsLength=30;
	//当前索引  
    public static int CURR_INDEX=3;
    //火焰的初始总位置  
    public static float distancesFireXZ=6;
    //火盆的初始总位置  
    public static float distancesBrazierXZ=6;
    public static float[][] positionFireXZ={{distancesFireXZ,distancesFireXZ},{distancesFireXZ,-distancesFireXZ},{-distancesFireXZ,distancesFireXZ},{-distancesFireXZ,-distancesFireXZ}};
    public static float[][] positionBrazierXZ={{distancesBrazierXZ,distancesBrazierXZ},{distancesBrazierXZ,-distancesBrazierXZ},{-distancesBrazierXZ,distancesBrazierXZ},{-distancesBrazierXZ,-distancesBrazierXZ}};
    public static int walls[]=new int[6];
    
    public static final float[][] START_COLOR=
	{//粒子起始颜色
    	{0.7569f,0.2471f,0.1176f,1.0f},	//0-普通火焰
    	{0.7569f,0.2471f,0.1176f,1.0f},	//1-白亮火焰
    	{0.6f,0.6f,0.6f,1.0f},			//2-普通烟
    	{0.6f,0.6f,0.6f,1.0f},			//3-纯黑烟
	};
    
    public static final float[][] END_COLOR=
	{//粒子终止颜色
    	{0.0f,0.0f,0.0f,0.0f},	//0-普通火焰
    	{0.0f,0.0f,0.0f,0.0f},	//1-白亮火焰
    	{0.0f,0.0f,0.0f,0.0f},	//2-普通烟
    	{0.0f,0.0f,0.0f,0.0f},	//3-纯黑烟
	};
    
    public static final int[] SRC_BLEND=
	{//源混合因子
    	GLES30.GL_SRC_ALPHA,   				//0-普通火焰
    	GLES30.GL_ONE,   					//1-白亮火焰
    	GLES30.GL_SRC_ALPHA,				//2-普通烟
    	GLES30.GL_ONE,						//3-纯黑烟
	};
   
    public static final int[] DST_BLEND=
	{//目标混合因子
    	GLES30.GL_ONE,      				//0-普通火焰
    	GLES30.GL_ONE,      				//1-白亮火焰
    	GLES30.GL_ONE_MINUS_SRC_ALPHA,		//2-普通烟
    	GLES30.GL_ONE,						//3-纯黑烟
	};
    
    public static final int[] BLEND_FUNC=
	{//混合方式
    	GLES30.GL_FUNC_ADD,    				//0-普通火焰
    	GLES30.GL_FUNC_ADD,    				//1-白亮火焰
    	GLES30.GL_FUNC_ADD,    				//2-普通烟
    	GLES30.GL_FUNC_REVERSE_SUBTRACT,	//3-纯黑烟
	};
    
    public static final int[] COUNT=
	{//总粒子数
    	340,   					//0-普通火焰
    	340,   					//1-白亮火焰
    	99,						//2-普通烟
    	99,						//3-纯黑烟
	};
   
    public static final float[] RADIS=
    {//单个粒子半径
    	0.5f,		//0-普通火焰
    	0.5f,		//1-白亮火焰
    	0.8f,		//2-普通烟
    	0.8f,		//3-纯黑烟
    };
    
    public static final float[] MAX_LIFE_SPAN=
    {//粒子最大生命期
    	5.0f,		//0-普通火焰
    	5.0f,		//1-白亮火焰
    	6.0f,		//2-普通烟
    	6.0f,		//3-纯黑烟
    };
   
    public static final float[] LIFE_SPAN_STEP=
    {//粒子生命期步进
    	0.07f,		//0-普通火焰
    	0.07f,		//1-白亮火焰
    	0.07f,		//2-普通烟
    	0.07f,		//3-纯黑烟
    };
    
    public static final float[] X_RANGE=
	{//粒子发射的x左右范围
	    0.5f,		//0-普通火焰
	    0.5f,		//1-白亮火焰
	    0.5f,		//2-普通烟
	    0.5f,		//3-纯黑烟
	};
    
    
    public static final float[] Y_RANGE=
	{//粒子发射的y上下范围
	    0.3f,		//0-普通火焰
	    0.3f,		//1-白亮火焰
	    0.15f,		//2-普通烟
	    0.15f,		//3-纯黑烟
	};
    
    public static final int[] GROUP_COUNT=
	{//每批激活的粒子数量
    	4,			//0-普通火焰
    	4,			//1-白亮火焰
    	1,			//2-普通烟
    	1,			//3-纯黑烟
	};
    
    
    public static final float[] VY=
	{//粒子y方向升腾的速度
    	0.05f,		//0-普通火焰
    	0.05f,		//1-白亮火焰
    	0.04f,		//2-普通烟
    	0.04f,		//3-纯黑烟
	};
    
    public static final int[] THREAD_SLEEP=
    {//粒子更新物理线程休眠时间（ms）
    	60,		//0-普通火焰
    	60,		//1-白亮火焰
    	30,		//2-普通烟
    	30,		//3-纯黑烟
    };
}
