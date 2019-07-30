package com.bn.Sample3_15;//声明包
import static com.bn.Sample3_15.ParticleDataConstant.lock;
import static com.bn.Sample3_15.ShaderUtil.createProgram;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import android.opengl.GLES30;
//纹理三角形
public class ParticleForDraw 
{	
	int mProgram;//自定义渲染管线程序id
    int muMVPMatrixHandle;//总变换矩阵引用id
    int muLifeSpan;//
    int muBj;//单个粒子的半径引用id  
    int muStartColor;//起始颜色引用id
    int muEndColor;//终止颜色引用id
    int muCameraPosition;//摄像机位置
    int muMMatrix;//基本变换矩阵总矩阵
    int maPositionHandle; //顶点位置属性引用id  
    String mVertexShader;//顶点着色器    	 
    String mFragmentShader;//片元着色器
	
	FloatBuffer   mVertexBuffer;//顶点坐标数据缓冲
    int vCount=0;   
    float halfSize;
    
    public ParticleForDraw(MySurfaceView mv,float halfSize)
    {    	
    	this.halfSize=halfSize;
    	//初始化着色器        
    	initShader(mv);
    }
    
    //更新顶点坐标数据缓冲的方法
    public void updateVertexData(float[] points)
    {
    	mVertexBuffer.clear();//清空顶点坐标数据缓冲
        mVertexBuffer.put(points);//向缓冲区中放入顶点坐标数据
        mVertexBuffer.position(0);//设置缓冲区起始位置
    }
    
    //初始化顶点数据的方法
    public void initVertexData(float[] points)
    {
    	//顶点坐标数据的初始化================begin============================
        vCount=points.length/4;//顶点个数
        //创建顶点坐标数据缓冲
        //vertices.length*4是因为一个整数四个字节
        ByteBuffer vbb = ByteBuffer.allocateDirect(points.length*4);
        vbb.order(ByteOrder.nativeOrder());//设置字节顺序
        mVertexBuffer = vbb.asFloatBuffer();//转换为Float型缓冲
        mVertexBuffer.put(points);//向缓冲区中放入顶点坐标数据
        mVertexBuffer.position(0);//设置缓冲区起始位置
        //特别提示：由于不同平台字节顺序不同数据单元不是字节的一定要经过ByteBuffer
        //转换，关键是要通过ByteOrder设置nativeOrder()，否则有可能会出问题
        //顶点坐标数据的初始化================end============================
    }

    //初始化着色器
    public void initShader(MySurfaceView mv)
    {
    	//加载顶点着色器的脚本内容
        mVertexShader=ShaderUtil.loadFromAssetsFile("vertex.sh", mv.getResources());
        //加载片元着色器的脚本内容
        mFragmentShader=ShaderUtil.loadFromAssetsFile("frag.sh", mv.getResources());  
        //基于顶点着色器与片元着色器创建程序
        mProgram = createProgram(mVertexShader, mFragmentShader);
        //获取程序中顶点位置属性引用id  
        maPositionHandle = GLES30.glGetAttribLocation(mProgram, "aPosition");
        //获取程序中总变换矩阵引用id
        muMVPMatrixHandle = GLES30.glGetUniformLocation(mProgram, "uMVPMatrix");  
        //获取程序中衰减因子引用id
        muLifeSpan=GLES30.glGetUniformLocation(mProgram, "maxLifeSpan");
        //获取程序中半径引用id
        muBj=GLES30.glGetUniformLocation(mProgram, "bj");
        //获取起始颜色引用id
        muStartColor=GLES30.glGetUniformLocation(mProgram, "startColor");
        //获取终止颜色引用id
        muEndColor=GLES30.glGetUniformLocation(mProgram, "endColor");
        //获取摄像机位置引用id
        muCameraPosition=GLES30.glGetUniformLocation(mProgram, "cameraPosition");
        //获取基本变换矩阵总矩阵引用id
        muMMatrix=GLES30.glGetUniformLocation(mProgram, "uMMatrix");
    }
    
    public void drawSelf(int texId,float[] startColor,float[] endColor,float maxLifeSpan)
    {        
    	 //制定使用某套shader程序
    	 GLES30.glUseProgram(mProgram);  
         //将最终变换矩阵传入shader程序
         GLES30.glUniformMatrix4fv(muMVPMatrixHandle, 1, false, MatrixState.getFinalMatrix(), 0); 
         //
         GLES30.glUniform1f(muLifeSpan, maxLifeSpan);
         //将半径传入shader程序
         GLES30.glUniform1f(muBj, halfSize);
         //将起始颜色送入渲染管线
         GLES30.glUniform4fv(muStartColor, 1, startColor, 0);
         //将终止颜色送入渲染管线
         GLES30.glUniform4fv(muEndColor, 1, endColor, 0);
         //将摄像机位置传入渲染管线
         GLES30.glUniform3f(muCameraPosition,MatrixState.cx, MatrixState.cy, MatrixState.cz);
         //将基本变换矩阵总矩阵传入渲染管线
         GLES30.glUniformMatrix4fv(muMMatrix, 1, false, MatrixState.getMMatrix(), 0); 
         
         //允许顶点位置数据数组
         GLES30.glEnableVertexAttribArray(maPositionHandle);  
         
         //绑定纹理
         GLES30.glActiveTexture(GLES30.GL_TEXTURE0);
         GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, texId);
         
         synchronized(lock)
     	 {//加锁--防止在将顶点坐标数据送入渲染管线时，更新顶点坐标数据
        	//将顶点坐标数据送入渲染管线
             GLES30.glVertexAttribPointer  
             (
             		maPositionHandle,   
             		4, 
             		GLES30.GL_FLOAT, 
             		false,
                    4*4,   
                    mVertexBuffer
             );                
        	 //绘制点
        	 GLES30.glDrawArrays(GLES30.GL_POINTS, 0, vCount);
     	 }
    }
}
