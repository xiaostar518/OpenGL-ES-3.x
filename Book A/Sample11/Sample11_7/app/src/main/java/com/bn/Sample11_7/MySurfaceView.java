package com.bn.Sample11_7;

import java.io.IOException;
import java.io.InputStream;
import android.opengl.GLES30;
import android.opengl.GLSurfaceView;
import android.opengl.GLUtils;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import static com.bn.Sample11_7.Constant.*;

class MySurfaceView extends GLSurfaceView 
{
    private SceneRenderer mRenderer;//场景渲染器    
    int textureFloor;//木地板纹理id
    int textureBallId;//篮球纹理id
	
	public MySurfaceView(Context context) {
        super(context);
        this.setEGLContextClientVersion(3); //设置使用OPENGL ES3.0
        mRenderer = new SceneRenderer();	//创建场景渲染器
        setRenderer(mRenderer);				//设置渲染器		        
        setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);//设置渲染模式为主动渲染   
    }

	private class SceneRenderer implements GLSurfaceView.Renderer 
    {   
    	TextureRect texRect;//表示地板的纹理矩形
    	BallTextureByVertex btbv;//用于绘制的纹理球
    	BallForControl bfd;//完成物理计算球控制类
    	
        public void onDrawFrame(GL10 gl) 
        { 
        	//清除深度缓冲与颜色缓冲
            GLES30.glClear( GLES30.GL_DEPTH_BUFFER_BIT | GLES30.GL_COLOR_BUFFER_BIT);
             
            MatrixState.pushMatrix();
            MatrixState.translate(0, -2, 0);
            
            MatrixState.pushMatrix();	
    		MatrixState.translate(0, FLOOR_Y, 0);
            //绘制木地板
            texRect.drawSelf(textureFloor);
            MatrixState.popMatrix();
            
            bfd.drawSelfMirror(textureBallId);//绘制镜像体
            bfd.drawSelf(textureBallId);//绘制实际物体 
            MatrixState.popMatrix();
        }  

        public void onSurfaceChanged(GL10 gl, int width, int height) 
        {
            //设置视窗大小及位置 
        	GLES30.glViewport(0, 0, width, height); 
        	//计算GLSurfaceView的宽高比
            float ratio = (float) width / height;
            //调用此方法计算产生透视投影矩阵
            MatrixState.setProjectFrustum(-ratio, ratio, -1, 1, 3, 100);
            //调用此方法产生摄像机9参数位置矩阵
            MatrixState.setCamera(0.0f,7.0f,7.0f,0,0f,0,0,1,0);
        }

        public void onSurfaceCreated(GL10 gl, EGLConfig config) {
            //设置屏幕背景色RGBA
            GLES30.glClearColor(0.0f,0.0f,0.0f,1.0f);  
            //创建纹理矩形对对象 
            texRect=new TextureRect(MySurfaceView.this,4,2.568f);  
            //创建用于绘制篮球的纹理球对象
            btbv=new BallTextureByVertex(MySurfaceView.this,BALL_SCALE);
            //创建用于完成篮球自由下落与反弹物理计算的BallForControl类对象
            bfd=new BallForControl(btbv,3f);
            //关闭深度检测
            //开启深度检测之后，只有距离摄像机最近处才会被绘制
            GLES30.glDisable(GLES30.GL_DEPTH_TEST);
            //初始化纹理
            textureFloor=initTexture(R.drawable.mdb);
            textureBallId=initTexture(R.drawable.basketball);
            //打开背面剪裁   
            GLES30.glEnable(GLES30.GL_CULL_FACE);
            //初始化变换矩阵
            MatrixState.setInitStack();
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
		GLES30.glTexParameterf(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_WRAP_S,GLES30.GL_CLAMP_TO_EDGE);
		GLES30.glTexParameterf(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_WRAP_T,GLES30.GL_CLAMP_TO_EDGE);
        
        //通过输入流加载图片
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
        
        //实际加载纹理
        GLUtils.texImage2D
        (
        		GLES30.GL_TEXTURE_2D,   //纹理类型
        		0, 					  //纹理的层次，0表示基本图像层，可以理解为直接贴图
        		bitmapTmp, 			  //纹理图像
        		0					  //纹理边框尺寸
        );
        bitmapTmp.recycle(); 		  //纹理加载成功后释放图片
        return textureId;
	}
}
