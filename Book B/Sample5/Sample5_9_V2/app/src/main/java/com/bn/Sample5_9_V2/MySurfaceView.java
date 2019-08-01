package com.bn.Sample5_9_V2;
import java.io.IOException;
import java.io.InputStream;

import static com.bn.Sample5_9_V2.Constant.*;
import android.opengl.GLSurfaceView;
import android.opengl.GLES30;
import android.opengl.GLUtils;
import android.view.MotionEvent;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import com.bn.Sample5_9_V2.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class MySurfaceView extends GLSurfaceView 
{
	private final float TOUCH_SCALE_FACTOR = 180.0f/320;//角度缩放比例
    private SceneRenderer mRenderer;//场景渲染器        
    int skyBoxTextureId;//系统分配的纹理id
    int teaPotTextureId;
    static final int GEN_TEX_WIDTH=1024;
    static final int GEN_TEX_HEIGHT=1024;
    private float mPreviousX;//上次的触控位置X坐标
    float xAngle=0;
	//镜像摄像机的观察与投影组合矩阵
    float[] mMVPMatrixMirror;
	
	public MySurfaceView(Context context) {
        super(context);
        this.setEGLContextClientVersion(3); //设置使用OPENGL ES3.0
        mRenderer = new SceneRenderer();	//创建场景渲染器
        setRenderer(mRenderer);				//设置渲染器		        
        setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);//设置渲染模式为主动渲染   
    }
	@SuppressLint("ClickableViewAccessibility")
	@Override 
  public boolean onTouchEvent(MotionEvent e) 
  {
      float x = e.getX();
      switch (e.getAction()) 
      {
      case MotionEvent.ACTION_MOVE:
			float dx = x - mPreviousX;//计算触控点X位移
			if(Math.abs(dx)<=0.02f){
				break;
			}
			xAngle += dx*TOUCH_SCALE_FACTOR;
			if(xAngle<ANGLE_MIN)
			{
				xAngle=ANGLE_MIN;
			}
			else if(xAngle>ANGLE_MAX)
			{
				xAngle=ANGLE_MAX;
			}		
			calculateMainAndMirrorCamera(xAngle);
		}
		mPreviousX = x;//记录触控点位置
      return true;
  }
	private class SceneRenderer implements GLSurfaceView.Renderer 
    {
		int mirrorId;// 动态产生的镜像纹理Id
		int frameBufferId;//帧缓冲id
		int renderDepthBufferId;//渲染深度缓冲id
    	//从指定的obj文件中加载对象
		LoadedObjectVertexNormalTexture lovo1;//房间绘制对象
		LoadedObjectVertexNormalTexture bed;//床绘制对象
		LoadedObjectVertexNormalTexture deng;//灯绘制对象
		LoadedObjectVertexNormalTexture book0;//书绘制对象
		LoadedObjectVertexNormalTexture chair;//椅子绘制对象
		LoadedObjectVertexNormalTexture book1;//书绘制对象
		LoadedObjectVertexNormalTexture border;//镜子边框绘制对象
		TextureRect mirror;//用作镜子的纹理矩形对象
		int bedId;
		int dengId;
		int bookId0;
		int bookId1;
		int chairId;
		int borderId;
		
    	
        public void onDrawFrame(GL10 gl) 
        {
        	generateTextImage();
        	drawMirrorNegativeTexture();
        }
        
        public void initFRBuffers()
        {
				int tia[]=new int[1];//用于存放产生的帧缓冲id的数组
				GLES30.glGenFramebuffers(tia.length, tia, 0);//产生一个帧缓冲id
				frameBufferId=tia[0];//将帧缓冲id记录到成员变量中
	    		//绑定帧缓冲id
				GLES30.glBindFramebuffer(GLES30.GL_FRAMEBUFFER, frameBufferId);

				GLES30.glGenRenderbuffers(tia.length, tia, 0);//产生一个渲染缓冲id
				renderDepthBufferId=tia[0];//将渲染缓冲id记录到成员变量中
				//绑定指定id的渲染缓冲
				GLES30.glBindRenderbuffer(GLES30.GL_RENDERBUFFER, renderDepthBufferId);
				//为渲染缓冲初始化存储
				GLES30.glRenderbufferStorage(GLES30.GL_RENDERBUFFER, 
						GLES30.GL_DEPTH_COMPONENT16,GEN_TEX_WIDTH, GEN_TEX_HEIGHT);
				
				int[] tempIds = new int[1];//用于存放产生纹理id的数组
				GLES30.glGenTextures//产生一个纹理id
	    		(
	    				tempIds.length,         //产生的纹理id的数量
	    				tempIds,   //纹理id的数组
	    				0           //偏移量
	    		);
				mirrorId=tempIds[0];//将纹理id记录到成员变量
				GLES30.glBindTexture(GLES30.GL_TEXTURE_2D,mirrorId);//绑定纹理id
				GLES30.glTexParameterf(GLES30.GL_TEXTURE_2D,//设置MIN采样方式
						GLES30.GL_TEXTURE_MIN_FILTER,GLES30.GL_LINEAR);
				GLES30.glTexParameterf(GLES30.GL_TEXTURE_2D,//设置MAG采样方式
						GLES30.GL_TEXTURE_MAG_FILTER,GLES30.GL_LINEAR);
				GLES30.glTexParameterf(GLES30.GL_TEXTURE_2D,//设置S轴拉伸方式
						GLES30.GL_TEXTURE_WRAP_S,GLES30.GL_CLAMP_TO_EDGE);
				GLES30.glTexParameterf(GLES30.GL_TEXTURE_2D,//设置T轴拉伸方式
						GLES30.GL_TEXTURE_WRAP_T,GLES30.GL_CLAMP_TO_EDGE); 
				GLES30.glTexImage2D//设置颜色附件纹理图的格式
	        	(
	        		GLES30.GL_TEXTURE_2D,
	        		0,						//层次
	        		GLES30.GL_RGBA, 		//内部格式
	        		GEN_TEX_WIDTH,			//宽度
	        		GEN_TEX_HEIGHT,			//高度
	        		0,						//边界宽度
	        		GLES30.GL_RGBA,			//格式 
	        		GLES30.GL_UNSIGNED_BYTE,//每个像素数据格式
	        		null
	        	);
				GLES30.glFramebufferTexture2D		//设置自定义帧缓冲的颜色缓冲附件
	            (
	            	GLES30.GL_FRAMEBUFFER,
	            	GLES30.GL_COLOR_ATTACHMENT0,	//颜色缓冲附件
	            	GLES30.GL_TEXTURE_2D,
	            	mirrorId, 						//纹理id
	            	0								//层次
	            );   		
				GLES30.glFramebufferRenderbuffer	//设置自定义帧缓冲的深度缓冲附件
	        	(
	        		GLES30.GL_FRAMEBUFFER,
	        		GLES30.GL_DEPTH_ATTACHMENT,		//深度缓冲附件
	        		GLES30.GL_RENDERBUFFER,			//渲染缓冲
	        		renderDepthBufferId				//渲染深度缓冲id
	        	);
        }
        
        //第一次绘制，绘制场景，不绘制镜面
        public void generateTextImage()
        {
        	//设置视口大小
			GLES30.glViewport(0,0,GEN_TEX_WIDTH,GEN_TEX_HEIGHT);	
			//绑定生成的帧缓冲id  
			GLES30.glBindFramebuffer(GLES30.GL_FRAMEBUFFER, frameBufferId);
			//设置镜像摄像机的9参数
			MatrixState.setCamera(mirrorCameraX,mirrorCameraY,mirrorCameraZ,  targetX,targetY,targetZ,  upX,upY,upZ);
        	//调用此方法计算产生透视投影矩阵
			MatrixState.setProjectFrustum(left, right, bottom, top, near, far);
			//获取镜像摄像机的观察与投影组合矩阵
			mMVPMatrixMirror=MatrixState.getViewProjMatrix();
			//清除深度缓冲和颜色缓冲
			GLES30.glClear( GLES30.GL_DEPTH_BUFFER_BIT | GLES30.GL_COLOR_BUFFER_BIT);
			drawThings();			//绘制场景
        }
        
        //第二次绘制，绘制场景，包括镜子
        public void drawMirrorNegativeTexture()
        {     	
        	//设置视口大小
        	GLES30.glViewport(0, 0,SCREEN_WIDTH,SCREEN_HEIGHT);
        	//绑定系统默认的帧缓冲id  
        	GLES30.glBindFramebuffer(GLES30.GL_FRAMEBUFFER, 0); 	
        	//清除深度缓冲与颜色缓冲
            GLES30.glClear( GLES30.GL_DEPTH_BUFFER_BIT | GLES30.GL_COLOR_BUFFER_BIT);
            //设置主摄像机的9参数
            MatrixState.setCamera(mainCameraX,mainCameraY,mainCameraZ,  targetX,targetY,targetZ,  upX,upY,upZ);
        	//调用此方法计算产生透视投影矩阵
        	MatrixState.setProjectFrustum(left, right, bottom, top, near, far);

        	drawThings();
            
            //绘制镜子边框
            MatrixState.pushMatrix();
            MatrixState.translate(2.2f,12,targetZ-2);     
            border.drawSelf(borderId);
            MatrixState.popMatrix();
            
            //绘制镜子
            MatrixState.pushMatrix();
            MatrixState.translate(2.0f,12,targetZ-1.7f);            
            mirror.drawSelf(mirrorId,mMVPMatrixMirror);
            MatrixState.popMatrix();
        }
        
        //绘制场景，不包括镜子
        public void drawThings()
        {
            //绘制房间
            MatrixState.pushMatrix();
            MatrixState.translate(0, -10, 7); 
            lovo1.drawSelf(skyBoxTextureId);
            MatrixState.popMatrix(); 
            
            MatrixState.pushMatrix();
            MatrixState.translate(40, 0,-12); 
            MatrixState.rotate(180, 0, 1, 0);
            deng.drawSelf(dengId);
            MatrixState.popMatrix(); 
            
            MatrixState.pushMatrix();
            MatrixState.translate(14, -7,-18); 
            book1.drawSelf(bookId1);
            MatrixState.popMatrix(); 
            
            MatrixState.pushMatrix();
            MatrixState.translate(22, 0,8);    
            MatrixState.rotate(180, 0, 1, 0);
            bed.drawSelf(bedId);
            MatrixState.popMatrix(); 
            
            MatrixState.pushMatrix();
            MatrixState.translate(-40, 30,10);
            book0.drawSelf(bookId0);
            MatrixState.popMatrix(); 
                        
            MatrixState.pushMatrix();
            MatrixState.translate(-40, 20,20); 
            book0.drawSelf(bookId0);
            MatrixState.popMatrix(); 
            
            MatrixState.pushMatrix();
            MatrixState.translate(-15, 0,-32); 
            chair.drawSelf(chairId);
            MatrixState.popMatrix(); 
                        
            MatrixState.pushMatrix();
            MatrixState.translate(40,0,34); 
            MatrixState.rotate(180, 0, 1, 0);
            deng.drawSelf(dengId);
            MatrixState.popMatrix(); 
        }

        public void onSurfaceChanged(GL10 gl, int width, int height) 
        {
        	SCREEN_WIDTH=width;
        	SCREEN_HEIGHT=height;
        	//设置视窗大小及位置 
        	GLES30.glViewport(0, 0, width, height); 
        	//计算GLSurfaceView的宽高比
        	ratio = (float) SCREEN_WIDTH / SCREEN_HEIGHT;
        	//初始化光源位置
        	MatrixState.setLightLocation(0, 10, 30);
        	initFRBuffers();        	       
        	initProject(1f); 	//设置投影参数
        }
        
        public void onSurfaceCreated(GL10 gl, EGLConfig config) 
        {
            //设置屏幕背景色RGBA
            GLES30.glClearColor(0.0f,0.0f,0.0f,1.0f);    
            //打开深度检测
            GLES30.glEnable(GLES30.GL_DEPTH_TEST);
            //打开背面剪裁   
            GLES30.glEnable(GLES30.GL_CULL_FACE); 
            //初始化变换矩阵
            MatrixState.setInitStack();
            //加载要绘制的物体
            lovo1=LoadUtil.loadFromFile("skybox.obj", MySurfaceView.this.getResources(),MySurfaceView.this);
            bed=LoadUtil.loadFromFile("chuang.obj", MySurfaceView.this.getResources(),MySurfaceView.this);
            deng=LoadUtil.loadFromFile("deng.obj", MySurfaceView.this.getResources(),MySurfaceView.this);
            book0=LoadUtil.loadFromFile("shu00.obj", MySurfaceView.this.getResources(),MySurfaceView.this);
            chair=LoadUtil.loadFromFile("yizi0.obj", MySurfaceView.this.getResources(),MySurfaceView.this);
            book1=LoadUtil.loadFromFile("shu1.obj", MySurfaceView.this.getResources(),MySurfaceView.this);
            border=LoadUtil.loadFromFile("mirror.obj", MySurfaceView.this.getResources(),MySurfaceView.this);
            mirror=new TextureRect(MySurfaceView.this);
            //加载纹理
            skyBoxTextureId=initTexture(R.drawable.skybox1);
            bedId=initTexture(R.drawable.chuang3);
            dengId=initTexture(R.drawable.deng2);
            bookId0=initTexture(R.drawable.shu0);
            bookId1=initTexture(R.drawable.shu1);
            chairId=initTexture(R.drawable.yizi);
            borderId=initTexture(R.drawable.mirror);
             //初始化主摄像机和镜像摄像机的参数
            calculateMainAndMirrorCamera(xAngle);
        }
    }
  	public int initTexture(int drawableId)//textureId
	{
		//生成纹理ID
		int[] textures = new int[1];
		GLES30.glGenTextures
		(
				1,          //产生的纹理id的数量
				textures,   //纹理id的数组
				0           //偏移量
		);
		int textureId=textures[0];    
		GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, textureId);
		GLES30.glTexParameterf(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_MIN_FILTER,GLES30.GL_NEAREST);
		GLES30.glTexParameterf(GLES30.GL_TEXTURE_2D,GLES30.GL_TEXTURE_MAG_FILTER,GLES30.GL_LINEAR);
		GLES30.glTexParameterf(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_WRAP_S,GLES30.GL_REPEAT);
		GLES30.glTexParameterf(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_WRAP_T,GLES30.GL_REPEAT);
        
        //通过输入流加载图片===============begin===================
        InputStream is = this.getResources().openRawResource(drawableId);
        Bitmap bitmapTmp;
        try 
        {
        	bitmapTmp = BitmapFactory.decodeStream(is);
        } 
        finally 
        {
            try 
            {
                is.close();
            } 
            catch(IOException e) 
            {
                e.printStackTrace();
            }
        }
        //通过输入流加载图片===============end===================== 
	   	GLUtils.texImage2D
	    (
	    		GLES30.GL_TEXTURE_2D, //纹理类型
	     		0, 
	     		GLUtils.getInternalFormat(bitmapTmp), 
	     		bitmapTmp, //纹理图像
	     		GLUtils.getType(bitmapTmp), 
	     		0 //纹理边框尺寸
	     );
	    bitmapTmp.recycle(); 		  //纹理加载成功后释放图片
        return textureId;
	}
}
