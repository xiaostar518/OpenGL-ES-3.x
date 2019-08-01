package com.bn.Sample5_8;
import android.opengl.GLSurfaceView;
import android.opengl.GLES30;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import android.annotation.SuppressLint;
import android.content.Context;
import static com.bn.Sample5_8.Constant.*;
@SuppressLint("NewApi") class MySurfaceView extends GLSurfaceView 
{
    private SceneRenderer mRenderer;//场景渲染器   
	public MySurfaceView(Context context) {
        super(context);
        this.setEGLContextClientVersion(2); //设置使用OPENGL ES2.0
        mRenderer = new SceneRenderer();	//创建场景渲染器
        setRenderer(mRenderer);				//设置渲染器	
        
        //设置渲染模式为=========只渲染一帧=========
        //The renderer only renders when the surface is created, or when requestRender() is called
        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }
	private class SceneRenderer implements GLSurfaceView.Renderer 
    {
	    Camera cam;
	    Scene scn;
	    Light light;
		ColorRect rect;		
		
        public void onDrawFrame(GL10 gl) 
        { 
        	//清除颜色缓冲
        	GLES30.glClear(GLES30.GL_DEPTH_BUFFER_BIT | GLES30.GL_COLOR_BUFFER_BIT);
            /*
        	 * 此处设置正交投影和摄像机的位置，
        	 * 只是为了能将3D世界中的正方形直接绘制在视口上
        	 */
        	//调用此方法计算产生正交投影矩阵
        	MatrixState.setProjectOrtho(-W, W, -H, H, 1, 2);
        	//设置绘制基本块时的camera位置
			MatrixState.setCamera(0, 0, 1, 0, 0, 0, 0, 1, 0);
			
			//设置光线跟踪算法中的真实摄像机位置
            cam.setMyCamera(CAM_X, CAM_Y, CAM_Z, 0f, 0f, 0f, 0f, 1.0f, 0.0f);
            //对场景中物体进行变换
            scn.transform();
            //开始光线跟踪以渲染物体
            cam.raytrace(scn, rect);
        }
        @Override
        public void onSurfaceChanged(GL10 gl, int width, int height) {
            //设置视窗大小及位置 
        	GLES30.glViewport(0, 0, (int)nCols, (int)nRows); 
        	/*
        	 * 此处设置正交投影和摄像机的位置，
        	 * 只是为了能将3D世界中的正方形直接绘制在视口上
        	 */
            //调用此方法计算产生正交投影矩阵
        	MatrixState.setProjectOrtho(-W, W, -H, H, 1, 2);
            //设置camera位置
			MatrixState.setCamera(0, 0, 1, 0, 0, 0, 0, 1, 0);
        }
        @Override
        public void onSurfaceCreated(GL10 gl, EGLConfig config) {
            //设置屏幕背景色RGBA
            GLES30.glClearColor(0,0,0,1);
            //打开背面剪裁   
            GLES30.glEnable(GLES30.GL_CULL_FACE);
            //打开深度检测
            GLES30.glEnable(GLES30.GL_DEPTH_TEST); 
            //初始化变换矩阵
            MatrixState.setInitStack(); 
            rect = new ColorRect(MySurfaceView.this);//创建用于绘制基本块的对象

    	    light = new Light(new Point3(LIGHT_X,LIGHT_Y,LIGHT_Z));//创建光源对象
            cam=new Camera(light);//创建摄像机对象
    	    scn=new Scene(cam, light);//创建场景对象
        }
    }
}
