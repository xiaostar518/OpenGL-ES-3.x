package com.bn.Sample4_8;//声明包

import java.io.IOException;
import java.io.InputStream;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES30;
import android.opengl.GLSurfaceView;
import android.opengl.GLUtils;
import android.view.MotionEvent;
import static com.bn.Sample4_8.Constant.*;

public class MySurfaceView extends GLSurfaceView
{	
	private SceneRenderer mRenderer;//场景渲染器    
	public Flare flare;//光晕对象
	
	public float lpx;//太阳位置x坐标
	public float lpy;//太阳位置y坐标
	float preX;//记录触控点x坐标
    float preY;//记录触控点y坐标
	public MySurfaceView(Context context){
		super(context);
        this.setEGLContextClientVersion(3); //设置使用OPENGL ES3.0
        mRenderer = new SceneRenderer();	//创建场景渲染器
        setRenderer(mRenderer);				//设置渲染器		        
        setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);//设置渲染模式为主动渲染   
	}
    
    @Override 
    public boolean onTouchEvent(MotionEvent e) 
    {//触摸事件回调方法         
    	float x=e.getX();//获取触控点x坐标
    	float y=e.getY();//获取触控点y坐标
    	
    	int action=e.getAction()&MotionEvent.ACTION_MASK;		
		switch(action)
		{
			case MotionEvent.ACTION_DOWN: //主点down
				preX=x;//记录当前触控点x坐标
				preY=y;//记录当前触控点y坐标
			break;	
			case MotionEvent.ACTION_MOVE://移动
				float dx=x-preX;//计算x轴移动位移
				float dy=y-preY;//计算y轴移动位移				
				CameraUtil.changeDirection(dx*0.1f);//改变摄像机方位角
				CameraUtil.changeYj(dy*0.1f);//改变摄像机仰角
				preX=x;//记录当前触控点x坐标
				preY=y;//记录当前触控点y坐标
			break;
		}
		return true;//返回true
    }
    
	private class SceneRenderer implements GLSurfaceView.Renderer 
    {
		int[] textureIds=new int[3];
		DrawFlare df;
		TextureRect texRect;//纹理矩形
		int[] textureIdA=new int[6];//天空盒六面的纹理
		@Override
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
          
			textureIds[0]=initTexture(R.drawable.flare1);
			textureIds[1]=initTexture(R.drawable.flare2);
			textureIds[2]=initTexture(R.drawable.flare3);
            
            textureIdA[0]=initTexture(R.raw.skycubemap_back);
            textureIdA[1]=initTexture(R.raw.skycubemap_left);
            textureIdA[2]=initTexture(R.raw.skycubemap_right);
            textureIdA[3]=initTexture(R.raw.skycubemap_down);
            textureIdA[4]=initTexture(R.raw.skycubemap_up);
            textureIdA[5]=initTexture(R.raw.skycubemap_front); 
            
            flare=new Flare(textureIds);
			df=new DrawFlare(MySurfaceView.this);
			//创建纹理矩形对对象 
            texRect=new TextureRect(MySurfaceView.this);  
		}

		@Override
		public void onSurfaceChanged(GL10 gl, int width, int height){
			//设置视窗大小及位置 
        	GLES30.glViewport(0, 0, width, height); 
        	//计算GLSurfaceView的宽高比
        	RATIO = (float) width / height;
        	CameraUtil.init3DCamera();        
        	DIS_MAX=(int)Math.sqrt(RATIO*RATIO+1);        	
		}

		@Override
		public void onDrawFrame(GL10 gl)
		{
			//清除深度缓冲与颜色缓冲
            GLES30.glClear(GLES30.GL_DEPTH_BUFFER_BIT | GLES30.GL_COLOR_BUFFER_BIT);
            
            //3D绘制部分
            MatrixState.setProjectFrustum(-RATIO, RATIO, -1.0f, 1.0f, 2, 1000);//设置透视投影
            CameraUtil.flush3DCamera();//更新3D摄像机位置           
            
            //天空盒六面调整值
            final float tzz=0.4f;
            //绘制天空盒后面
            MatrixState.pushMatrix();
            MatrixState.translate(0, 0, -UNIT_SIZE+tzz);
            texRect.drawSelf(textureIdA[0]);
            MatrixState.popMatrix();              
            //绘制天空盒前面
            MatrixState.pushMatrix();
            MatrixState.translate(0, 0, UNIT_SIZE-tzz);
            MatrixState.rotate(180, 0, 1, 0);
            texRect.drawSelf(textureIdA[5]);
            MatrixState.popMatrix(); 
            //绘制左墙
            MatrixState.pushMatrix();
            MatrixState.translate(-UNIT_SIZE+tzz, 0, 0);
            MatrixState.rotate(90, 0, 1, 0);
            texRect.drawSelf(textureIdA[1]);
            MatrixState.popMatrix(); 
            //绘制右墙
            MatrixState.pushMatrix();
            MatrixState.translate(UNIT_SIZE-tzz, 0, 0);
            MatrixState.rotate(-90, 0, 1, 0);
            texRect.drawSelf(textureIdA[2]);
            MatrixState.popMatrix();
            //绘制下墙
            MatrixState.pushMatrix();
            MatrixState.translate(0, -UNIT_SIZE+tzz, 0);
            MatrixState.rotate(-90, 1, 0, 0);
            texRect.drawSelf(textureIdA[3]);
            MatrixState.popMatrix(); 
            //绘制上墙
            MatrixState.pushMatrix();
            MatrixState.translate(0, UNIT_SIZE-tzz, 0);
            MatrixState.rotate(90, 1, 0, 0);
            texRect.drawSelf(textureIdA[4]);
            MatrixState.popMatrix();
            
            //获取光源在屏幕上的坐标
        	float[] ls=CameraUtil.calLightScreen(RATIO);//计算在当前摄像机观察情况下光源点的屏幕坐标
			lpx=ls[0];//获取太阳位置x坐标
			lpy=ls[1];//获取太阳位置y坐标
			         
			if(lpx>RATIO||lpy>1)
			{//太阳出屏幕
				return;//返回
			}
			flare.update(lpx, lpy);//更新光晕绘制位置
			
			//平行投影绘制部分
            MatrixState.setProjectOrtho(-RATIO, RATIO, -1.0f, 1.0f, 2, 1000);//设置平行投影
            MatrixState.setCamera(0,0,0, 0,0,-1, 0,1,0); //设置摄像机
			//绘制光晕
            MatrixState.pushMatrix();//保护现场
            
            GLES30.glEnable(GLES30.GL_BLEND);//打开混合
            GLES30.glBlendFunc(GLES30.GL_SRC_COLOR, GLES30.GL_ONE);//设置混合因子
            for(SingleFlare ss:flare.sFl)
            {//循环遍历光晕元素列表-进行绘制
            	MatrixState.pushMatrix();//保护现场
            	MatrixState.translate(ss.px, ss.py, -100+ss.distance);//平移到指定位置
            	MatrixState.scale(ss.bSize, ss.bSize, ss.bSize);//按比例缩放
            	df.drawSelf(ss.texture,ss.color);//绘制光晕元素
            	MatrixState.popMatrix();//恢复现场
            }
            GLES30.glDisable(GLES30.GL_BLEND);//关闭混合
            
            MatrixState.popMatrix(); //恢复现场
     
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
        
        //实际加载纹理
        GLUtils.texImage2D
        (
        		GLES30.GL_TEXTURE_2D,   //纹理类型，在OpenGL ES中必须为GL10.GL_TEXTURE_2D
        		0, 					  //纹理的层次，0表示基本图像层，可以理解为直接贴图
        		bitmapTmp, 			  //纹理图像
        		0					  //纹理边框尺寸
        );
        bitmapTmp.recycle(); 		  //纹理加载成功后释放图片
        return textureId;
	}
}
