package com.bn.Sample5_3_V3;
import static com.bn.Sample5_3_V3.Constant.SCREEN_HEIGHT;
import static com.bn.Sample5_3_V3.Constant.SCREEN_WIDTH;
import static com.bn.Sample5_3_V3.Constant.SHADOW_TEX_HEIGHT;
import static com.bn.Sample5_3_V3.Constant.SHADOW_TEX_WIDTH;
import android.opengl.GLES30;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import android.annotation.SuppressLint;
import android.content.Context;
 
class MySurfaceView extends GLSurfaceView
{
	private final float TOUCH_SCALE_FACTOR = 180.0f/320;//角度缩放比例
    private SceneRenderer mRenderer;//场景渲染器
    
    private float mPreviousY;//上次的触控位置Y坐标
    private float mPreviousX;//上次的触控位置X坐标
    
    //摄像机位置相关
    float cx=0;
    float cy=20;
    float cz=50;
    float cAngle=0;
    final float cR=50;
    //灯光位置
    float lx=0;
    final float ly=10;
    float lz=85;
    float lAngle=0;
    final float lR=85;
    
    final float cDis=15;
    
    //光源总变换矩阵
    float[] mMVPMatrixGY;
	
	public MySurfaceView(Context context) {
        super(context);
        this.setEGLContextClientVersion(3); //设置使用OPENGL ES3.0
        mRenderer = new SceneRenderer();	//创建场景渲染器
        setRenderer(mRenderer);				//设置渲染器		        
        setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);//设置渲染模式为主动渲染   
        
        new Thread()
        {
        	public void run()
        	{
        		while(true)
        		{
        			lAngle += 0.5;//设置沿x轴旋转角度                    
                    lx=(float) Math.sin(Math.toRadians(lAngle))*lR;
                    lz=(float) Math.cos(Math.toRadians(lAngle))*lR;
                    try {
   					Thread.sleep(80);
	   				} catch (InterruptedException e) {
	   					e.printStackTrace();
	   				}
        		}
        	}
        }.start();
    }
	
	//触摸事件回调方法
    @SuppressLint("ClickableViewAccessibility")
	@Override 
    public boolean onTouchEvent(MotionEvent e) 
    {
        float y = e.getY();
        float x = e.getX();
        switch (e.getAction()) {
        case MotionEvent.ACTION_MOVE:
            float dy = y - mPreviousY;//计算触控笔Y位移
            float dx = x - mPreviousX;//计算触控笔X位移
            cAngle += dx * TOUCH_SCALE_FACTOR;//设置沿x轴旋转角度
            cx=(float) Math.sin(Math.toRadians(cAngle))*cR;
            cz=(float) Math.cos(Math.toRadians(cAngle))*cR;
            cy+= dy/10.0f;//设置沿z轴移动
            requestRender();//重绘画面
        }
        mPreviousY = y;//记录触控笔位置
        mPreviousX = x;//记录触控笔位置
        return true;
    }

	private class SceneRenderer implements GLSurfaceView.Renderer 
    {
    	//从指定的obj文件中加载对象
		LoadedObjectVertexNormal lovo_pm;//平面
		LoadedObjectVertexNormal lovo_ch;//茶壶
		LoadedObjectVertexNormal lovo_cft;//长方体
		LoadedObjectVertexNormal lovo_qt;//球体
		LoadedObjectVertexNormal lovo_yh;//圆环

		int frameBufferId;
		int shadowId;// 动态产生的阴影纹理id
		int renderDepthBufferId;// 动态产生的阴影纹理id

		//初始化帧缓冲和渲染缓冲
		public void initFRBuffers()
		{
			int[] tia=new int[1];
			GLES30.glGenFramebuffers(1, tia, 0);
			frameBufferId=tia[0];
			
			GLES30.glGenRenderbuffers(1, tia, 0);
			renderDepthBufferId=tia[0];
			GLES30.glBindRenderbuffer(GLES30.GL_RENDERBUFFER, renderDepthBufferId);
        	GLES30.glRenderbufferStorage(GLES30.GL_RENDERBUFFER,
        			GLES30.GL_DEPTH_COMPONENT16, SHADOW_TEX_WIDTH, SHADOW_TEX_HEIGHT);
			
			int[] tempIds = new int[1];
    		GLES30.glGenTextures
    		(
    				1,          //产生的纹理id的数量
    				tempIds,   	//纹理id的数组
    				0           //偏移量
    		);
    		
    		shadowId=tempIds[0];
    		//初始化颜色附件纹理
        	GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, shadowId);        	
        	GLES30.glTexParameterf(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_MIN_FILTER,GLES30.GL_LINEAR);
    		GLES30.glTexParameterf(GLES30.GL_TEXTURE_2D,GLES30.GL_TEXTURE_MAG_FILTER,GLES30.GL_LINEAR);
    		GLES30.glTexParameterf(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_WRAP_S,GLES30.GL_CLAMP_TO_EDGE);
    		GLES30.glTexParameterf(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_WRAP_T,GLES30.GL_CLAMP_TO_EDGE); 
    		
    		GLES30.glTexImage2D
        	(
        		GLES30.GL_TEXTURE_2D,  
        		0, 
        		GLES30.GL_R16F,
        		SHADOW_TEX_WIDTH, 
        		SHADOW_TEX_HEIGHT, 
        		0, 
        		GLES30.GL_RED, 
        		GLES30.GL_FLOAT, 
        		null
        	);
        	
        	GLES30.glBindFramebuffer(GLES30.GL_FRAMEBUFFER, frameBufferId);
            GLES30.glFramebufferTexture2D
            (
            	GLES30.GL_FRAMEBUFFER, 
            	GLES30.GL_COLOR_ATTACHMENT0,
            	GLES30.GL_TEXTURE_2D, 
            	shadowId, 
            	0
            );       
        	GLES30.glFramebufferRenderbuffer
        	(
        		GLES30.GL_FRAMEBUFFER,
        		GLES30.GL_DEPTH_ATTACHMENT,
        		GLES30.GL_RENDERBUFFER,
        		renderDepthBufferId
        	);
		}
		
        //通过绘制产生阴影纹理
        public void generateShadowImage()
        {
        	GLES30.glViewport(0, 0, SHADOW_TEX_WIDTH, SHADOW_TEX_HEIGHT);
        	GLES30.glBindFramebuffer(GLES30.GL_FRAMEBUFFER, frameBufferId);
        	//清除深度缓冲与颜色缓冲
        	GLES30.glClear( GLES30.GL_DEPTH_BUFFER_BIT | GLES30.GL_COLOR_BUFFER_BIT);
        	
        	//调用此方法产生摄像机9参数位置矩阵
        	MatrixState.setCamera(lx,ly,lz,0f,0f,0f,0f,1,0);
        	MatrixState.setProjectFrustum(-1, 1, -1.0f, 1.0f, 1.5f, 400);
        	mMVPMatrixGY=MatrixState.getViewProjMatrix();
        	
        	//绘制最下面的平面
        	lovo_pm.drawSelfForShadow();
        	
        	//绘制球体
        	MatrixState.pushMatrix();
        	MatrixState.translate(-cDis, 0, 0);
        	//若加载的物体部位空则绘制物体
        	lovo_qt.drawSelfForShadow();
        	MatrixState.popMatrix();
        	
        	//绘制圆环
        	MatrixState.pushMatrix();            
        	MatrixState.translate(cDis, 0, 0);
        	MatrixState.rotate(30, 0, 1, 0);
        	//若加载的物体部位空则绘制物体
        	lovo_yh.drawSelfForShadow();
        	MatrixState.popMatrix();  
        	
        	//绘制长方体
        	MatrixState.pushMatrix(); 
        	MatrixState.translate(0, 0, -cDis);
        	//若加载的物体部位空则绘制物体
        	lovo_cft.drawSelfForShadow();
        	MatrixState.popMatrix();
        	
        	//绘制茶壶
        	MatrixState.pushMatrix(); 
        	MatrixState.translate(0, 0, cDis);
        	//若加载的物体部位空则绘制物体
        	lovo_ch.drawSelfForShadow();
        	MatrixState.popMatrix();     
        }
        
        public void drawScene(GL10 gl)
        {
        	//设置视窗大小及位置 
        	GLES30.glViewport(0, 0, (int)SCREEN_WIDTH, (int)SCREEN_HEIGHT);
        	GLES30.glBindFramebuffer(GLES30.GL_FRAMEBUFFER, 0);
        	
        	//清除深度缓冲与颜色缓冲
        	GLES30.glClear( GLES30.GL_DEPTH_BUFFER_BIT | GLES30.GL_COLOR_BUFFER_BIT);
        	//调用此方法产生摄像机9参数位置矩阵
        	MatrixState.setCamera(cx,cy,cz,0f,0f,0f,0f,1f,0f);
        	MatrixState.setProjectFrustum(-ratio, ratio, -1.0f, 1.0f, 2, 1000);  
        	
        	//绘制最下面的平面
        	lovo_pm.drawSelf(shadowId,mMVPMatrixGY);
        	
        	//绘制球体
        	MatrixState.pushMatrix();
        	MatrixState.translate(-cDis, 0, 0);
        	//若加载的物体部位空则绘制物体
        	lovo_qt.drawSelf(shadowId,mMVPMatrixGY);
        	MatrixState.popMatrix();
        	
        	//绘制圆环
        	MatrixState.pushMatrix(); 
        	MatrixState.translate(cDis, 0, 0);
        	MatrixState.rotate(30, 0, 1, 0);
        	//若加载的物体部位空则绘制物体
        	lovo_yh.drawSelf(shadowId,mMVPMatrixGY);
        	MatrixState.popMatrix();
        	
        	//绘制长方体
        	MatrixState.pushMatrix(); 
        	MatrixState.translate(0, 0, -cDis);
        	//若加载的物体部位空则绘制物体
        	lovo_cft.drawSelf(shadowId,mMVPMatrixGY);
        	MatrixState.popMatrix();
        	
        	//绘制茶壶
        	MatrixState.pushMatrix(); 
        	MatrixState.translate(0, 0, cDis);
        	//若加载的物体部位空则绘制物体
        	lovo_ch.drawSelf(shadowId,mMVPMatrixGY);
        	MatrixState.popMatrix();
        }
        long start=System.nanoTime();
        int count=0;
        public void onDrawFrame(GL10 gl)
        {
        	//计算FPS=========================================================
        	count++;
        	if(count==150)
        	{
        		count=0;
        		long end=System.nanoTime();
        		System.out.println("FPS:"+(1000000000.0*150/(end-start)));
        		start=end;
        	}
        	//计算FPS=========================================================
        	
        	MatrixState.setLightLocation(lx, ly, lz);     
        	//通过绘制产生阴影纹理
        	generateShadowImage();
        	drawScene(gl);
        }
        
        float ratio;
        public void onSurfaceChanged(GL10 gl, int width, int height) 
        {
        	//设置视窗大小及位置 
        	GLES30.glViewport(0, 0, width, height); 
        	//计算GLSurfaceView的宽高比
        	ratio = (float) width / height;  
        	Constant.SCREEN_HEIGHT=height;
        	Constant.SCREEN_WIDTH=width;
        	
            //初始化帧缓冲
            initFRBuffers();
        }
        public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        	//设置屏幕背景色RGBA
            GLES30.glClearColor(0f,0f,0f,1.0f);
            //打开深度检测
            GLES30.glEnable(GLES30.GL_DEPTH_TEST);
            //打开背面剪裁
            GLES30.glEnable(GLES30.GL_CULL_FACE);
            //初始化变换矩阵
            MatrixState.setInitStack();
            //初始化光源位置
            MatrixState.setLightLocation(lx, ly, lz);
            //加载要绘制的物体
            lovo_ch=LoadUtil.loadFromFileVertexOnly("ch.obj", MySurfaceView.this.getResources(),MySurfaceView.this);
            lovo_pm=LoadUtil.loadFromFileVertexOnly("pm.obj", MySurfaceView.this.getResources(),MySurfaceView.this);
            lovo_cft=LoadUtil.loadFromFileVertexOnly("cft.obj", MySurfaceView.this.getResources(),MySurfaceView.this);
            lovo_qt=LoadUtil.loadFromFileVertexOnly("qt.obj", MySurfaceView.this.getResources(),MySurfaceView.this);
            lovo_yh=LoadUtil.loadFromFileVertexOnly("yh.obj", MySurfaceView.this.getResources(),MySurfaceView.this);       
        }
    }
}
