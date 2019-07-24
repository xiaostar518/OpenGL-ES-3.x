package com.bn.Sample7_8;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import android.opengl.GLES30;

public class Stairs
{	
	int mProgram;//自定义渲染管线着色器程序id  
    int muMVPMatrixHandle;//总变换矩阵引用
    int muMMatrixHandle;//位置、旋转变换矩阵
    int maPositionHandle; //顶点位置属性引用  
    int maNormalHandle; //顶点法向量属性引用  
    int maLightLocationHandle;//光源位置属性引用  
    int maCameraHandle; //摄像机位置属性引用 
    String mVertexShader;//顶点着色器代码脚本    	 
    String mFragmentShader;//片元着色器代码脚本    
	
	FloatBuffer   mVertexBuffer;//顶点坐标数据缓冲  
	FloatBuffer   mNormalBuffer;//顶点法向量数据缓冲
    int vCount=0;  
    float xSize=0.2f;
	float y1Size=0.1f;
	float y2Size=0.2f;
	float y3Size=0.3f;
	float y4Size=0.4f;
	float z1Size=0.4f;
	float z2Size=0.3f;
	float z3Size=0.2f;
	float z4Size=0.1f;
    public Stairs(MySurfaceView mv)
    {    	
    	//初始化顶点坐标与着色数据
    	initVertexData();
    	//初始化着色器
    	initShader(mv);
    }
    
    //初始化顶点坐标与着色数据的方法
    public void initVertexData()
    {
    	//顶点坐标数据的初始化================begin============================
    	float[] vertices=new float[]{
    			//第四个（最下面）立方体的下面
    			xSize,0,0,
    			xSize,0,z1Size,
    			-xSize,0,z1Size,
    			-xSize,0,z1Size,
    			-xSize,0,0,
    			xSize,0,0,
    			//第四个（最下面）立方体的上面
    			xSize,y1Size,0,
    			-xSize,y1Size,0,
    			-xSize,y1Size,z1Size,
    			-xSize,y1Size,z1Size,
    			xSize,y1Size,z1Size,
    			xSize,y1Size,0,
    			//第四个（最下面）立方体的前面
    			xSize,y1Size,z1Size,
    			-xSize,y1Size,z1Size,
    			-xSize,0,z1Size,
    			-xSize,0,z1Size,
    			xSize,0,z1Size,
    			xSize,y1Size,z1Size,
    			//第四个（最下面）立方体的后面
    			xSize,y1Size,0,
    			xSize,0,0,
    			-xSize,0,0,
    			-xSize,0,0,
    			-xSize,y1Size,0,
    			xSize,y1Size,0,
    			//第四个（最下面）立方体的左面
    			-xSize,y1Size,z1Size,
    			-xSize,y1Size,0,
    			-xSize,0,0,
    			-xSize,0,0,
    			-xSize,0,z1Size,
    			-xSize,y1Size,z1Size,
    			//第四个（最下面）立方体的右面
    			xSize,y1Size,z1Size,
    			xSize,0,z1Size,
    			xSize,0,0,
    			xSize,0,0,
    			xSize,y1Size,0,
    			xSize,y1Size,z1Size,
    			
    			//第三个立方体的上面
    			xSize,y2Size,0,
    			-xSize,y2Size,0,
    			-xSize,y2Size,z2Size,
    			-xSize,y2Size,z2Size,
    			xSize,y2Size,z2Size,
    			xSize,y2Size,0,
    			//第三个立方体的前面
    			xSize,y2Size,z2Size,
    			-xSize,y2Size,z2Size,
    			-xSize,y1Size,z2Size,
    			-xSize,y1Size,z2Size,
    			xSize,y1Size,z2Size,
    			xSize,y2Size,z2Size,
    			//第三个立方体的后面
    			xSize,y2Size,0,
    			xSize,y1Size,0,
    			-xSize,y1Size,0,
    			-xSize,y1Size,0,
    			-xSize,y2Size,0,
    			xSize,y2Size,0,
    			//第三个立方体的左面
    			-xSize,y2Size,z2Size,
    			-xSize,y2Size,0,
    			-xSize,y1Size,0,
    			-xSize,y1Size,0,
    			-xSize,y1Size,z2Size,
    			-xSize,y2Size,z2Size,
    			//第三个立方体的右面
    			xSize,y2Size,z2Size,
    			xSize,y1Size,z2Size,
    			xSize,y1Size,0,
    			xSize,y1Size,0,
    			xSize,y2Size,0,
    			xSize,y2Size,z2Size,
    			
    			//第二个立方体的上面
    			xSize,y3Size,0,
    			-xSize,y3Size,0,
    			-xSize,y3Size,z3Size,
    			-xSize,y3Size,z3Size,
    			xSize,y3Size,z3Size,
    			xSize,y3Size,0,
    			//第二个立方体的前面
    			xSize,y3Size,z3Size,
    			-xSize,y3Size,z3Size,
    			-xSize,y2Size,z3Size,
    			-xSize,y2Size,z3Size,
    			xSize,y2Size,z3Size,
    			xSize,y3Size,z3Size,
    			//第二个立方体的后面
    			xSize,y3Size,0,
    			xSize,y2Size,0,
    			-xSize,y2Size,0,
    			-xSize,y2Size,0,
    			-xSize,y3Size,0,
    			xSize,y3Size,0,
    			//第二个立方体的左面
    			-xSize,y3Size,z3Size,
    			-xSize,y3Size,0,
    			-xSize,y2Size,0,
    			-xSize,y2Size,0,
    			-xSize,y2Size,z3Size,
    			-xSize,y3Size,z3Size,
    			//第二个立方体的右面
    			xSize,y3Size,z3Size,
    			xSize,y2Size,z3Size,
    			xSize,y2Size,0,
    			xSize,y2Size,0,
    			xSize,y3Size,0,
    			xSize,y3Size,z3Size,
    			
    			//第一个立方体的上面
    			xSize,y4Size,0,
    			-xSize,y4Size,0,
    			-xSize,y4Size,z4Size,
    			-xSize,y4Size,z4Size,
    			xSize,y4Size,z4Size,
    			xSize,y4Size,0,
    			//第一个立方体的前面
    			xSize,y4Size,z4Size,
    			-xSize,y4Size,z4Size,
    			-xSize,y3Size,z4Size,
    			-xSize,y3Size,z4Size,
    			xSize,y3Size,z4Size,
    			xSize,y4Size,z4Size,
    			//第一个立方体的后面
    			xSize,y4Size,0,
    			xSize,y3Size,0,
    			-xSize,y3Size,0,
    			-xSize,y3Size,0,
    			-xSize,y4Size,0,
    			xSize,y4Size,0,
    			//第一个立方体的左面
    			-xSize,y4Size,z4Size,
    			-xSize,y4Size,0,
    			-xSize,y3Size,0,
    			-xSize,y3Size,0,
    			-xSize,y3Size,z4Size,
    			-xSize,y4Size,z4Size,
    			//第一个立方体的右面
    			xSize,y4Size,z4Size,
    			xSize,y3Size,z4Size,
    			xSize,y3Size,0,
    			xSize,y3Size,0,
    			xSize,y4Size,0,
    			xSize,y4Size,z4Size,
    	};
    	vCount=vertices.length/3;
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
        
        float[] normals=
    		{
    		//第四个（最下面）立方体的下面
    		0,-1,0, 0,-1,0, 0,-1,0,
    		0,-1,0, 0,-1,0, 0,-1,0,
    		//第四个（最下面）立方体的上面
    		0,1,0, 0,1,0, 0,1,0,
    		0,1,0, 0,1,0, 0,1,0,
    		//第四个（最下面）立方体的前面
    		0,0,1, 0,0,1, 0,0,1,
    		0,0,1, 0,0,1, 0,0,1,
    		//第四个（最下面）立方体的后面
    		0,0,-1, 0,0,-1, 0,0,-1,
    		0,0,-1, 0,0,-1, 0,0,-1,
    		//第四个（最下面）立方体的左面
    		-1,0,0, -1,0,0, -1,0,0,
    		-1,0,0, -1,0,0, -1,0,0,
    		//第四个（最下面）立方体的右面
    		1,0,0, 1,0,0, 1,0,0,
    		1,0,0, 1,0,0, 1,0,0,

    		//第三个立方体的上面
    		0,1,0, 0,1,0, 0,1,0,
    		0,1,0, 0,1,0, 0,1,0,
    		//第三个立方体的前面
    		0,0,1, 0,0,1, 0,0,1,
    		0,0,1, 0,0,1, 0,0,1,
    		//第三个立方体的后面
    		0,0,-1, 0,0,-1, 0,0,-1,
    		0,0,-1, 0,0,-1, 0,0,-1,
    		//第三个立方体的左面
    		-1,0,0, -1,0,0, -1,0,0,
    		-1,0,0, -1,0,0, -1,0,0,
    		//第三个立方体的右面
    		1,0,0, 1,0,0, 1,0,0,
    		1,0,0, 1,0,0, 1,0,0,

    		//第二个立方体的上面
    		0,1,0, 0,1,0, 0,1,0,
    		0,1,0, 0,1,0, 0,1,0,
    		//第二个立方体的前面
    		0,0,1, 0,0,1, 0,0,1,
    		0,0,1, 0,0,1, 0,0,1,
    		//第二个立方体的后面
    		0,0,-1, 0,0,-1, 0,0,-1,
    		0,0,-1, 0,0,-1, 0,0,-1,
    		//第二个立方体的左面
    		-1,0,0, -1,0,0, -1,0,0,
    		-1,0,0, -1,0,0, -1,0,0,
    		//第二个立方体的右面
    		1,0,0, 1,0,0, 1,0,0,
    		1,0,0, 1,0,0, 1,0,0,
    		
    		//第一个立方体的上面
    		0,1,0, 0,1,0, 0,1,0,
    		0,1,0, 0,1,0, 0,1,0,
    		//第一个立方体的前面
    		0,0,1, 0,0,1, 0,0,1,
    		0,0,1, 0,0,1, 0,0,1,
    		//第一个立方体的后面
    		0,0,-1, 0,0,-1, 0,0,-1,
    		0,0,-1, 0,0,-1, 0,0,-1,
    		//第一个立方体的左面
    		-1,0,0, -1,0,0, -1,0,0,
    		-1,0,0, -1,0,0, -1,0,0,
    		//第一个立方体的右面
    		1,0,0, 1,0,0, 1,0,0,
    		1,0,0, 1,0,0, 1,0,0,
    		};
        //顶点法向量数据的初始化================begin============================  
        ByteBuffer cbb = ByteBuffer.allocateDirect(normals.length*4);
        cbb.order(ByteOrder.nativeOrder());//设置字节顺序
        mNormalBuffer = cbb.asFloatBuffer();//转换为Float型缓冲
        mNormalBuffer.put(normals);//向缓冲区中放入顶点法向量数据
        mNormalBuffer.position(0);//设置缓冲区起始位置
        //特别提示：由于不同平台字节顺序不同数据单元不是字节的一定要经过ByteBuffer
        //转换，关键是要通过ByteOrder设置nativeOrder()，否则有可能会出问题
        //顶点法向量数据的初始化================end============================
    }

    //初始化着色器
    public void initShader(MySurfaceView mv)
    {
    	//加载顶点着色器的脚本内容
        mVertexShader=ShaderUtil.loadFromAssetsFile("vertex.sh", mv.getResources());
        //加载片元着色器的脚本内容
        mFragmentShader=ShaderUtil.loadFromAssetsFile("frag.sh", mv.getResources());  
        //基于顶点着色器与片元着色器创建程序
        mProgram = ShaderUtil.createProgram(mVertexShader, mFragmentShader);
        //获取程序中顶点位置属性引用  
        maPositionHandle = GLES30.glGetAttribLocation(mProgram, "aPosition");
        //获取程序中顶点颜色属性引用  
        maNormalHandle= GLES30.glGetAttribLocation(mProgram, "aNormal");
        //获取程序中总变换矩阵引用
        muMVPMatrixHandle = GLES30.glGetUniformLocation(mProgram, "uMVPMatrix");  
        //获取位置、旋转变换矩阵引用
        muMMatrixHandle = GLES30.glGetUniformLocation(mProgram, "uMMatrix"); 
        //获取程序中光源位置引用
        maLightLocationHandle=GLES30.glGetUniformLocation(mProgram, "uLightLocation");
        //获取程序中摄像机位置引用
        maCameraHandle=GLES30.glGetUniformLocation(mProgram, "uCamera"); 
    }
    
    public void drawSelf(int texId)
    {        
    	 //指定使用某套着色器程序
    	 GLES30.glUseProgram(mProgram);
         //将最终变换矩阵传入渲染管线
         GLES30.glUniformMatrix4fv(muMVPMatrixHandle, 1, false, MatrixState.getFinalMatrix(), 0); 
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
         

         GLES30.glEnableVertexAttribArray(maPositionHandle);  //启用顶点位置数据
         GLES30.glEnableVertexAttribArray(maNormalHandle);  //启用顶点法向量数据
         //绑定纹理
         GLES30.glActiveTexture(GLES30.GL_TEXTURE0);//设置使用的纹理编号
         GLES30.glBindTexture(GLES30.GL_TEXTURE_3D, texId);//绑定纹理
         //绘制楼梯
         GLES30.glDrawArrays(GLES30.GL_TRIANGLES, 0, vCount); 
    }
}
