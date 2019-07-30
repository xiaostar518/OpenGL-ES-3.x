package com.bn.Sample3_14;
import static com.bn.Sample3_14.ParticleDataConstant.lock;
import static com.bn.Sample3_14.ShaderUtil.createProgram;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import android.opengl.GLES30;
//纹理三角形
public class ParticleForDraw 
{	
	int mProgram;//自定义渲染管线程序id
    int muMVPMatrixHandle;//总变换矩阵引用id
    int muLifeSpan;//衰减因子引用id
    int muBj;//半径引用id  
    int muStartColor;//起始颜色引用id
    int muEndColor;//终止颜色引用id
    int maPositionHandle; //顶点位置属性引用id  
    int maTexCoorHandle; //顶点纹理坐标属性引用id  
    String mVertexShader;//顶点着色器    	 
    String mFragmentShader;//片元着色器
	
	FloatBuffer mVertexBuffer;//顶点坐标数据缓冲
	FloatBuffer mTexCoorBuffer;//顶点纹理坐标数据缓冲
    int vCount=0;   //顶点个数
    float halfSize;//粒子半径
    
    public ParticleForDraw(MySurfaceView mv,float halfSize)
    {//构造器	
    	this.halfSize=halfSize;//初始化粒子半径
    	//初始化着色器        
    	initShader(mv);
    }
    //更新顶点坐标数据缓冲的方法
    public void updatVertexData(float[] points)
    {
    	mVertexBuffer.clear();//清空顶点坐标数据缓冲
        mVertexBuffer.put(points);//向缓冲区中放入顶点坐标数据
        mVertexBuffer.position(0);//设置缓冲区起始位置
    }
    
    //初始化顶点坐标与纹理坐标数据的方法
    public void initVertexData(float[] points)
    {
    	//顶点坐标数据的初始化================begin============================
    	vCount=points.length/4;//个数
        float vertices[]=points;
       
        //创建顶点坐标数据缓冲
        //vertices.length*4是因为一个整数四个字节
        ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length*4);
        vbb.order(ByteOrder.nativeOrder());//设置字节顺序
        mVertexBuffer = vbb.asFloatBuffer();//转换为Float型缓冲
        mVertexBuffer.put(vertices);//向缓冲区中放入顶点坐标数据
        mVertexBuffer.position(0);//设置缓冲区起始位置
        //特别提示：由于不同平台字节顺序不同数据单元不是字节的一定要经过ByteBuffer
        //转换，关键是要通过ByteOrder设置nativeOrder()，否则有可能会出问题
        //顶点坐标数据的初始化================end============================
        //顶点纹理坐标数据的初始化================begin============================
       
    	 float texCoor[]=new float[vCount*2];//顶点颜色值数组，每个顶点4个色彩值RGBA
         for(int i=0;i<vCount/6;i++)
         {
         	texCoor[12*i]=0;
         	texCoor[12*i+1]=0;
         	texCoor[12*i+2]=0;
         	texCoor[12*i+3]=1;
         	texCoor[12*i+4]=1;
         	texCoor[12*i+5]=0;
         	
         	texCoor[12*i+6]=1;
         	texCoor[12*i+7]=0;
         	texCoor[12*i+8]=0;
         	texCoor[12*i+9]=1;
         	texCoor[12*i+10]=1;
         	texCoor[12*i+11]=1;
         }
         
         //创建顶点纹理坐标数据缓冲
         ByteBuffer cbb = ByteBuffer.allocateDirect(texCoor.length*4);
         cbb.order(ByteOrder.nativeOrder());//设置字节顺序
         mTexCoorBuffer = cbb.asFloatBuffer();//转换为Float型缓冲
         mTexCoorBuffer.clear();
         mTexCoorBuffer.put(texCoor);//向缓冲区中放入顶点着色数据
         mTexCoorBuffer.position(0);//设置缓冲区起始位置
         //特别提示：由于不同平台字节顺序不同数据单元不是字节的一定要经过ByteBuffer
         //转换，关键是要通过ByteOrder设置nativeOrder()，否则有可能会出问题
         //顶点纹理坐标数据的初始化================end============================  
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
        //获取程序中顶点纹理坐标属性引用id  
        maTexCoorHandle= GLES30.glGetAttribLocation(mProgram, "aTexCoor");
        //获取程序中总变换矩阵引用id
        muMVPMatrixHandle = GLES30.glGetUniformLocation(mProgram, "uMVPMatrix");  
        //
        muLifeSpan=GLES30.glGetUniformLocation(mProgram, "maxLifeSpan");
        //获取程序中半径引用id
        muBj=GLES30.glGetUniformLocation(mProgram, "bj");
        //获取起始颜色引用id
        muStartColor=GLES30.glGetUniformLocation(mProgram, "startColor");
        //获取终止颜色引用id
        muEndColor=GLES30.glGetUniformLocation(mProgram, "endColor");
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
         GLES30.glUniform1f(muBj, halfSize*60);
         //将起始颜色送入渲染管线
         GLES30.glUniform4fv(muStartColor, 1, startColor, 0);
         //将终止颜色送入渲染管线
         GLES30.glUniform4fv(muEndColor, 1, endColor, 0);
        
         //将顶点纹理坐标数据送入渲染管线
         GLES30.glVertexAttribPointer  
         (
        		maTexCoorHandle, 
         		2, 
         		GLES30.GL_FLOAT, 
         		false,
                2*4,   
                mTexCoorBuffer
         );   
         //允许顶点位置数据数组
         GLES30.glEnableVertexAttribArray(maPositionHandle);  
         GLES30.glEnableVertexAttribArray(maTexCoorHandle);  
         
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
        	//绘制纹理矩形
             GLES30.glDrawArrays(GLES30.GL_TRIANGLES, 0, vCount);
     	 } 
    }
}
