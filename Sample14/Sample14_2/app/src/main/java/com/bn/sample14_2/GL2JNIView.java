package com.bn.sample14_2;

import java.io.IOException;
import java.io.InputStream;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLES30;
import android.opengl.GLUtils;

import static com.bn.sample14_2.Sample14_2Activity.*;

class GL2JNIView extends GLSurfaceView {
    Renderer renderer;
    
	static float direction=0;//视线方向
    static float cx=0;//摄像机x坐标 
    static float cz=20;//摄像机z坐标
    
    static float tx=0;//观察目标点x坐标
    static float tz=0;//观察目标点z坐标
    static final float DEGREE_SPAN=(float)(3.0/180.0f*Math.PI);//摄像机每次转动的角度
    //线程循环的标志位  
    boolean flag=true;
    float x;
    float y;
    float Offset=20;
    
    @SuppressLint("ClickableViewAccessibility") @Override
	public boolean onTouchEvent(MotionEvent event) {
    	x=event.getX();
		y=event.getY();
		switch(event.getAction())
		{
			case MotionEvent.ACTION_DOWN:
				flag=true;
				new Thread()
				{
					@Override
					public void run()
					{
						while(flag)
						{
							if(x>0&&x<WIDTH/2&&y>0&&y<HEIGHT/2)
							{//向前
								cx=cx-(float)Math.sin(direction)*1.0f;
								cz=cz-(float)Math.cos(direction)*1.0f;
							}
							else if(x>WIDTH/2&&x<WIDTH&&y>0&&y<HEIGHT/2)
							{//向后
								cx=cx+(float)Math.sin(direction)*1.0f;
								cz=cz+(float)Math.cos(direction)*1.0f;
							}
							else if(x>0&&x<WIDTH/2&&y>HEIGHT/2&&y<HEIGHT)
							{
								direction=direction+DEGREE_SPAN;
							}
							else if(x>WIDTH/2&&x<WIDTH&&y>HEIGHT/2&&y<HEIGHT)
							{
								direction=direction-DEGREE_SPAN;
							}
							try
							{
								Thread.sleep(100);
							}
							catch(Exception e)
							{
								e.printStackTrace();
							}
						}
					}
				}.start();
			break;
			case MotionEvent.ACTION_UP:
				flag=false;
			break;
		}
			//设置新的观察目标点XZ坐标
			tx=(float)(cx-Math.sin(direction)*Offset);//观察目标点x坐标 
	        tz=(float)(cz-Math.cos(direction)*Offset);//观察目标点z坐标
	        
	        //设置新的摄像机位置
	        GL2JNILib.setCamera(cx,5,cz,tx,1,tz,0,1,0);
			return true;
	}

	public GL2JNIView(Context context) {
        super(context);
		this.setEGLContextClientVersion(3);//使用OpenGL ES 3.0需设置该参数为3
		renderer=new Renderer();//创建Renderer类的对象
		this.setRenderer(renderer);//设置渲染器
		this.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
    }

    private class Renderer implements GLSurfaceView.Renderer {
        public void onDrawFrame(GL10 gl) {
            GL2JNILib.step();//调用本地方法刷新场景
        }

        public void onSurfaceChanged(GL10 gl, int width, int height) {
            GL2JNILib.init(GL2JNIView.this,width, height);//调用本地方法初始化
        }

        public void onSurfaceCreated(GL10 gl, EGLConfig config) 
        {

        }
    }
    
	//加载纹理的方法
	@SuppressLint("NewApi")
	public static int initTextureRepeat(GLSurfaceView gsv,String pname)
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
		InputStream is = null;
		try {
			is = gsv.getResources().getAssets().open(pname);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
        Bitmap bitmapTmp;
        try {
        	bitmapTmp = BitmapFactory.decodeStream(is);
        } 
        finally {
            try {
                is.close();
            } 
            catch(IOException e) {
                e.printStackTrace();
            }
        }
        //实际加载纹理
        GLUtils.texImage2D
        (
        		GLES30.GL_TEXTURE_2D,   //纹理类型
        		0, 					  	//纹理的层次，0表示基本图像层，可以理解为直接贴图
        		bitmapTmp, 			  	//纹理图像
        		0					  	//纹理边框尺寸
        );
        bitmapTmp.recycle(); 		  	//纹理加载成功后释放图片 
        return textureId;
	}
}
