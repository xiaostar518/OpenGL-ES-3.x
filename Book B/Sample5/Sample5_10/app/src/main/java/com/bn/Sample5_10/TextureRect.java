package com.bn.Sample5_10;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Arrays;

import static com.bn.Sample5_10.Constant.*;
import android.opengl.GLES30;

//有波浪效果的纹理矩形
public class TextureRect 
{	
	int mProgram;//自定义渲染管线着色器程序id  
    int muMVPMatrixHandle;//总变换矩阵引用    
    int muMMatrixHandle;//位置、旋转变换矩阵
    int maPositionHandle; //顶点位置属性引用 
    int maTexCoorHandle; //顶点纹理坐标属性引用 
    int muMVPMatrixMirrorHandle;//镜像摄像机的观察与投影组合矩阵引用
    
    int maNormalHandle; //顶点法向量属性引用  
    int maLightLocationHandle;//光源属性引用
    int maCameraHandle; //摄像机位置属性引用 
    
    int uDYTexHandle;//倒影纹理属性引用
    int uWaterTexHandle;//水自身纹理属性引用
    int uNormalTexHandle;//法线纹理属性引用
    
	FloatBuffer   mVertexBuffer;//顶点坐标数据缓冲
	FloatBuffer   mTexCoorBuffer;//顶点纹理坐标数据缓冲
	FloatBuffer   mNormalBuffer;//顶点法向量数据缓冲
    IntBuffer mIndicesBuffer;
    
    public  float mytime;//计时器	
	float[] zero1;//1号波振源
	float[] zero2;//2号波振源
	float[] zero3;//3号波振源
	
    int[] indices;//索引数组
    float vertices[]; //顶点坐标
    float normals[];//法向量
    float texCoor[];//纹理
    
    float[] verticesForCal;//用于计算的顶点位置坐标数组
    float[] normalsForCal;//用于计算的顶点法向量数组
    
   	int iCount;
    int vCount=0;   //顶点数量
    
    public TextureRect(MySurfaceView mv)
    {    	
    	//初始化顶点数据的方法
    	initVertexData();
    	//初始化着色器的方法        
    	initShader(mv);
    }
    //初始化顶点数据的方法
    public void initVertexData()
    {	
        	int cols=64;//列数
        	int rows=64;//行数	       
	       	float UNIT_SIZE=WIDTH_SPAN/(cols-1);//每格的单位长度
	        ArrayList<Integer> alVertixIndice=new ArrayList<Integer>();//存放顶点坐标的ArrayList
	        ArrayList<Float> alVertixV=new ArrayList<Float>();//存放顶点位置坐标的ArrayList
	        ArrayList<Float> alVertixN=new ArrayList<Float>();//存放顶点法向量的ArrayList
	        ArrayList<Float> alVertixT=new ArrayList<Float>();//存放顶点纹理坐标的ArrayList
           
        
	        for(int j = 0; j <rows;j++){//行
	       		for(int i = 0; i< cols; i++){//列
	       		//计算当前格子左上侧点坐标 
	           		float zsx=-WIDTH_SPAN/2+i*UNIT_SIZE;
	           		float zsz=-WIDTH_SPAN/2+j*UNIT_SIZE;
	           		float zsy=0;
	           		
	           		alVertixV.add(zsx);alVertixV.add(zsy);alVertixV.add(zsz);
	           		alVertixN.add(0.0f);alVertixN.add(1.0f);alVertixN.add(0.0f);
	           		float s=zsx/WIDTH_SPAN+0.5f;
	           		float t=zsz/WIDTH_SPAN+0.5f;
	           		alVertixT.add(s);alVertixT.add(t);	           		
	       		}
	        }
	    
	       for(int i = 0; i <(rows-1); i++){//行
	       		for(int j = 0; j < (cols-1); j++){//列
	       			
	       			int x = i * rows + j;
	       			alVertixIndice.add(x);
	       			alVertixIndice.add(x + cols);
	       			alVertixIndice.add(x + 1);
	       			
	       			alVertixIndice.add(x + 1);
	       			alVertixIndice.add(x + cols);
	       			alVertixIndice.add(x + cols + 1);
	       		}
	       	}
	    	vCount=alVertixV.size()/3;//每个格子两个三角形，每个三角形3个顶点
	    	
	        vertices=new float[vCount*3];//每个顶点xyz三个坐标
	        texCoor=new float[vCount*2];
	        normals=new float[vCount*3];
	        verticesForCal=new float[vCount*3];
	        normalsForCal=new float[vCount*3];
	        iCount=alVertixIndice.size();
	        indices=new int[alVertixIndice.size()];
	        for(int i=0;i<alVertixIndice.size();i++)
	        {
        	   indices[i]=alVertixIndice.get(i);
	        }
           
	        for(int i=0;i<alVertixV.size();i++)
	        {
        	   vertices[i]=alVertixV.get(i);
	        }
           
	        for(int i=0;i<alVertixN.size();i++)
	        {
        	   normals[i]=alVertixN.get(i);
	        }
        
	        for(int i=0;i<alVertixT.size();i++)
	        {
        	   texCoor[i]= alVertixT.get(i);
	        }     
           
           
	       //初始化4个波的振源位置
	       zero1=new float[]{wave1PositionX,wave1PositionY,wave1PositionZ};
	       zero2=new float[]{wave2PositionX,wave2PositionY,wave2PositionZ};
	       zero3=new float[]{wave3PositionX,wave3PositionY,wave3PositionZ};
	       
	        //顶点顶点坐标数据的初始化================start============================
	       //创建顶点坐标数据缓冲
	       //vertices.length*4是因为一个整数四个字节
	       ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length*4);
	       vbb.order(ByteOrder.nativeOrder());//设置字节顺序
	       mVertexBuffer = vbb.asFloatBuffer();//转换为Float型缓冲
	       mVertexBuffer.put(vertices);//向缓冲区中放入顶点坐标数据
	       mVertexBuffer.position(0);//设置缓冲区起始位置
	        //顶点顶点坐标数据的初始化================end============================
	       
	       //顶点纹理坐标数据的初始化================begin============================   
	       //创建顶点纹理坐标数据缓冲
	       ByteBuffer cbb = ByteBuffer.allocateDirect(texCoor.length*4);
	       cbb.order(ByteOrder.nativeOrder());//设置字节顺序
	       mTexCoorBuffer = cbb.asFloatBuffer();//转换为Float型缓冲
	       mTexCoorBuffer.put(texCoor);//向缓冲区中放入顶点着色数据
	       mTexCoorBuffer.position(0);//设置缓冲区起始位置
	        //顶点顶点纹理坐标数据的初始化================end============================
	       
	       //顶点法向量数据的初始化================begin============================  
	       	ByteBuffer nbb = ByteBuffer.allocateDirect(normals.length*4);
	        nbb.order(ByteOrder.nativeOrder());//设置字节顺序
	        mNormalBuffer = nbb.asFloatBuffer();//转换为Float型缓冲
	        mNormalBuffer.put(normals);//向缓冲区中放入顶点法向量数据
	        mNormalBuffer.position(0);//设置缓冲区起始位置
	        //特别提示：由于不同平台字节顺序不同数据单元不是字节的一定要经过ByteBuffer
	        //转换，关键是要通过ByteOrder设置nativeOrder()，否则有可能会出问题
	        //顶点法向量数据的初始化================end============================
	        
	        
	        //顶点索引数据的初始化================begin============================  
	        ByteBuffer  ibb = ByteBuffer.allocateDirect(indices.length*4);
	        ibb.order(ByteOrder.nativeOrder());//设置字节顺序
	        mIndicesBuffer = ibb.asIntBuffer();//转换为Float型缓冲
	        mIndicesBuffer.put(indices);//向缓冲区中放入顶点法向量数据
	        mIndicesBuffer.position(0);//设置缓冲区起始位置
	      //顶点索引数据的初始化================end============================
    }
    
   
    //初始化着色器的方法
    public void initShader(MySurfaceView mv)
    {
    	//加载顶点着色器的脚本内容
        String mVertexShader=ShaderUtil.loadFromAssetsFile("water_vertex.sh", mv.getResources());
        //加载片元着色器的脚本内容
        String mFragmentShader=ShaderUtil.loadFromAssetsFile("water_frag.sh", mv.getResources());  
        //基于顶点着色器与片元着色器创建程序
        mProgram= ShaderUtil.createProgram(mVertexShader, mFragmentShader);
        //获取程序中顶点位置属性引用  
        maPositionHandle = GLES30.glGetAttribLocation(mProgram, "aPosition");
        //获取程序中总变换矩阵引用
        muMVPMatrixHandle = GLES30.glGetUniformLocation(mProgram, "uMVPMatrix");  
        //获取位置、旋转变换矩阵引用
        muMMatrixHandle = GLES30.glGetUniformLocation(mProgram, "uMMatrix"); 
        //获取程序中顶点纹理坐标属性引用  
        maTexCoorHandle= GLES30.glGetAttribLocation(mProgram, "aTexCoor"); 
        //获取镜像摄像机的观察与投影组合矩阵引用
        muMVPMatrixMirrorHandle=GLES30.glGetUniformLocation(mProgram, "uMVPMatrixMirror");        
        //获取倒影纹理引用
        uDYTexHandle=GLES30.glGetUniformLocation(mProgram, "sTextureDY");  
        //获取水面自身的纹理引用
        uWaterTexHandle=GLES30.glGetUniformLocation(mProgram, "sTextureWater");
        //获取法向量纹理引用
        uNormalTexHandle=GLES30.glGetUniformLocation(mProgram, "sTextureNormal");          
        //获取程序中顶点法向量属性引用  
        maNormalHandle= GLES30.glGetAttribLocation(mProgram, "aNormal");
        //获取程序中光源位置引用
        maLightLocationHandle=GLES30.glGetUniformLocation(mProgram, "uLightLocation"); 
        //获取程序中摄像机位置引用
        maCameraHandle=GLES30.glGetUniformLocation(mProgram, "uCamera"); 
    }
    
    
    public void drawSelf(int texId,int waterId,int textureIdNormal,float[] mMVPMatrixMirror)
    {
		synchronized(lock){
    		updateData();
    	}
    	//指定使用某套shader程序
    	GLES30.glUseProgram(mProgram); 
    	//将最终变换矩阵传入渲染管线
    	GLES30.glUniformMatrix4fv(muMVPMatrixHandle, 1, false, MatrixState.getFinalMatrix(), 0); 
    	//将镜像摄像机的观察与投影组合矩阵传入渲染管线
        GLES30.glUniformMatrix4fv(muMVPMatrixMirrorHandle, 1, false, mMVPMatrixMirror, 0);
    	//将位置、旋转变换矩阵传入渲染管线
    	GLES30.glUniformMatrix4fv(muMMatrixHandle, 1, false, MatrixState.getMMatrix(), 0);   
		//将光源位置传入渲染管线
    	GLES30.glUniform3fv(maLightLocationHandle, 1, MatrixState.lightPositionFB); 
        //将摄像机位置传入渲染管线
    	GLES30.glUniform3fv(maCameraHandle, 1, MatrixState.cameraFB);
    	//将顶点位置数据传入渲染管线
    	GLES30.glVertexAttribPointer
    	(
    			maPositionHandle,
    			3,
    			GLES30.GL_FLOAT,
    			false,
    			3*4,
    			mVertexBuffer
    			);
    	//将顶点纹理坐标数据传入渲染管线
    	GLES30.glVertexAttribPointer
    	(
    			maTexCoorHandle, 
    			2, 
    			GLES30.GL_FLOAT, 
    			false,
    			2*4,   
    			mTexCoorBuffer
    			);   
    	 //将顶点法向量数据传入渲染管线
        GLES30.glVertexAttribPointer  
        (
       		maNormalHandle, 
        		3,   
        		GLES30.GL_FLOAT, 
        		false,
               3*4,
               mNormalBuffer
        );   
    	
    	GLES30.glEnableVertexAttribArray(maPositionHandle);  //启用顶点位置数据数组
    	GLES30.glEnableVertexAttribArray(maTexCoorHandle);  //启用顶点纹理坐标数据数组
        GLES30.glEnableVertexAttribArray(maNormalHandle);  //启用顶点法向量数据数组
    	//绑定纹理
        GLES30.glActiveTexture(GLES30.GL_TEXTURE0);//激活0号纹理
    	GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, texId);//水面倒影纹理
    	GLES30.glActiveTexture(GLES30.GL_TEXTURE1);//激活1号纹理
    	GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, waterId); //水面自身纹理
    	GLES30.glActiveTexture(GLES30.GL_TEXTURE2);//激活2号纹理
    	GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, textureIdNormal);//法向量纹理
    	GLES30.glUniform1i(uDYTexHandle, 0);//使用0号纹理
    	GLES30.glUniform1i(uWaterTexHandle, 1); //使用1号纹理
    	GLES30.glUniform1i(uNormalTexHandle, 2);  //使用2号纹理  
    	//以三角形方式执行绘制
        GLES30.glDrawElements(GLES30.GL_TRIANGLES, iCount, GLES30.GL_UNSIGNED_INT, mIndicesBuffer);
    	
    }
    //更新顶点数据和法向量数据的缓冲数据
    public void updateData()
    {
    	//顶点坐标数据的初始化================begin============================  
    	//创建顶点坐标数据缓冲
        //vertices.length*4是因为一个整数四个字节
        ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length*4);
        vbb.order(ByteOrder.nativeOrder());//设置字节顺序
        mVertexBuffer = vbb.asFloatBuffer();//转换为Float型缓冲
        mVertexBuffer.put(vertices);//向缓冲区中放入顶点坐标数据
        mVertexBuffer.position(0);//设置缓冲区起始位置
    	//顶点坐标数据的初始化================end============================  
        
        //顶点法向量数据的初始化================begin============================  
        ByteBuffer nbb = ByteBuffer.allocateDirect(normals.length*4);
        nbb.order(ByteOrder.nativeOrder());//设置字节顺序
        mNormalBuffer = nbb.asFloatBuffer();//转换为Float型缓冲
        mNormalBuffer.put(normals);//向缓冲区中放入顶点法向量数据
        mNormalBuffer.position(0);//设置缓冲区起始位置
        //特别提示：由于不同平台字节顺序不同数据单元不是字节的一定要经过ByteBuffer
        //转换，关键是要通过ByteOrder设置nativeOrder()，否则有可能会出问题
        //顶点法向量数据的初始化================end============================
    }
    //计算顶点坐标、法向量
    public void calVerticesNormalAndTangent()
    {
    	//计算顶点坐标
    	for(int i=0;i<vCount;i++)
    	{
    		verticesForCal[i*3]=vertices[i*3];
    		verticesForCal[i*3+1]=findHeight(vertices[i*3],vertices[i*3+2]);
    		verticesForCal[i*3+2]=vertices[i*3+2];
    	}
    	//计算法向量
    	normalsForCal=CalNormal.calNormal(verticesForCal, indices);
    	
        synchronized (lock) {
        	vertices=Arrays.copyOf(verticesForCal,verticesForCal.length);   
        	normals=Arrays.copyOf(normalsForCal,normalsForCal.length);    	 
		}
    }    
  
    //计算3个波对顶点的影响之后的高度值
    public float findHeight(float x,float z)
    {
    	float result=0;
    	//获取点到中心的距离
    	float distance1=(float) Math.sqrt((x-zero1[0])*(x-zero1[0])+(z-zero1[2])*(z-zero1[2]));
    	//顶点距离2号波起始位置的距离
    	float distance2=(float) Math.sqrt((x-zero2[0])*(x-zero2[0])+(z-zero2[2])*(z-zero2[2]));
    	//顶点距离3号波起始位置的距离
    	float distance3=(float) Math.sqrt((x-zero3[0])*(x-zero3[0])+(z-zero3[2])*(z-zero3[2]));
    	
    	result= (float) (Math.sin((distance1) * waveFrequency1 * Math.PI + mytime) *waveAmplitude1);		//设置顶点高度
    	result=(float) (result+Math.sin((distance2) * waveFrequency2 * Math.PI + mytime)*waveAmplitude2);//设置顶点高度
    	result=(float) (result+Math.sin((distance3) * waveFrequency3 * Math.PI + mytime) *waveAmplitude3);//设置顶点高度
    	return result;
    }
}
