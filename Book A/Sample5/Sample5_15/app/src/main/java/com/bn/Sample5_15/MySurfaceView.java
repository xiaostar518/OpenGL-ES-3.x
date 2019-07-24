package com.bn.Sample5_15;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import android.annotation.SuppressLint;
import android.content.Context;
import android.opengl.GLES30;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;

class MySurfaceView extends GLSurfaceView 
{
	private final float TOUCH_SCALE_FACTOR = 180.0f/320;//角度缩放比例
    private SceneRenderer mRenderer;//场景渲染器
    private float mPreviousX;//上次的触控位置X坐标
    float yAngle=90;//总场景绕y轴旋转的角度
    private float mPreviousY;//上次的触控位置X坐标
    float xAngle=20;//总场景绕y轴旋转的角度
    float polygonOffsetFactor =-1.0f;
    float polygonOffsetUnits  =-2.0f;
    
    
	public MySurfaceView(Context context) {
        super(context);
        this.setEGLContextClientVersion(3); //设置使用OPENGL ES3.0
        mRenderer = new SceneRenderer();	//创建场景渲染器
        setRenderer(mRenderer);				//设置渲染器		        
        setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);//设置渲染模式为主动渲染   
    }
	//触摸事件回调方法
    @SuppressLint("ClickableViewAccessibility")
	@Override 
    public boolean onTouchEvent(MotionEvent e) {
        float x = e.getX();
        float y = e.getY();
        switch (e.getAction()) {
        case MotionEvent.ACTION_MOVE:
            float dx = x - mPreviousX;//计算触控笔X位移
            yAngle += dx * TOUCH_SCALE_FACTOR;//设置三角形对绕y轴旋转角度
            float dy = y - mPreviousY;//计算触控笔X位移
            xAngle += dy * TOUCH_SCALE_FACTOR;//设置三角形对绕y轴旋转角度
        }
        mPreviousX=x;
        mPreviousY=y;
        return true;
    }

	private class SceneRenderer implements GLSurfaceView.Renderer 
    {   
		ColorRect color1;//平面对象1引用
		ColorRect color2;//平面对象2引用
    	
        @SuppressLint({ "InlinedApi", "NewApi" })
		public void onDrawFrame(GL10 gl) 
        { 
        	//清除深度缓冲与颜色缓冲
            GLES30.glClear( GLES30.GL_DEPTH_BUFFER_BIT | GLES30.GL_COLOR_BUFFER_BIT);
            //保护现场
            MatrixState.pushMatrix();
            //绕Y轴旋转
            MatrixState.rotate(yAngle, 0, 1, 0);//绕y轴旋转yAngle度
            MatrixState.rotate(xAngle, 1, 0, 0);//绕X轴旋转xAngle度
            //绘制左侧立方体
            MatrixState.pushMatrix();
            MatrixState.translate(-250f, 0, -0.1f);
            color1.drawSelf();
            MatrixState.popMatrix();
            
            GLES30.glEnable (GLES30.GL_POLYGON_OFFSET_FILL );//启用多边形偏移
            GLES30.glPolygonOffset ( polygonOffsetFactor, polygonOffsetUnits );
            
            //绘制右侧立方体
            MatrixState.pushMatrix();
            MatrixState.translate(250f,0, 0f);
            color2.drawSelf();
            MatrixState.popMatrix();
            
            GLES30.glDisable(GLES30.GL_POLYGON_OFFSET_FILL );//禁用多边形偏移
            
            //恢复现场
            MatrixState.popMatrix();
        }  

        @SuppressLint("NewApi")
		public void onSurfaceChanged(GL10 gl, int width, int height) 
        {
            //设置视口的大小及位置 
        	GLES30.glViewport(0, 0, width, height); 
        	//计算视口的宽高比
            float ratio = (float) width / height;
             
	        //调用此方法计算产生透视投影矩阵
	        MatrixState.setProjectFrustum(-ratio*75f, ratio*75f, -75f, 75f, 300f, 10000f);
	        //调用此方法产生摄像机矩阵
	        MatrixState.setCamera(5000f,0.5f,0f,0f,0f,0f,0f,1.0f,0.0f);
	        
            //初始化变换矩阵
            MatrixState.setInitStack();
        }

        @SuppressLint("NewApi")
		public void onSurfaceCreated(GL10 gl, EGLConfig config) {
            //设置屏幕背景色RGBA
            GLES30.glClearColor(0f,0f,0f, 1.0f);  
            //创建立方体对象
            color1=new ColorRect(MySurfaceView.this,new float[]{0,1,1,0});//蓝色
            color2=new ColorRect(MySurfaceView.this,new float[]{1,1,0,0});//黄色
            //打开深度检测
            GLES30.glEnable(GLES30.GL_DEPTH_TEST);
            //打开背面剪裁   
            GLES30.glEnable(GLES30.GL_CULL_FACE);  
        }
    }
}
