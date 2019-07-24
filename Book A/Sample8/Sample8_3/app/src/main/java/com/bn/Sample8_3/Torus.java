package com.bn.Sample8_3;

import static com.bn.Sample8_3.ShaderUtil.createProgram;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;

import android.opengl.GLES30;

/*
 * 圆环
 */
public class Torus 
{	
	int mProgram;//自定义渲染管线着色器程序id
    int muMVPMatrixHandle;//总变换矩阵引用
    int maPositionHandle; //顶点位置属性引用
    int maTexCoorHandle; //顶点纹理坐标属性引用
    
    String mVertexShader;//顶点着色器代码脚本  	 
    String mFragmentShader;//片元着色器代码脚本
	
	FloatBuffer   mVertexBuffer;//顶点坐标数据缓冲
	FloatBuffer   mTexCoorBuffer;//顶点纹理坐标数据缓冲

    int vCount=0;   
    float xAngle=0;//绕x轴旋转的角度
    float yAngle=0;//绕y轴旋转的角度
    float zAngle=0;//绕z轴旋转的角度
    
    public Torus(MySurfaceView mv,float rBig, float rSmall,int nCol ,int nRow)
    {
    	//调用初始化顶点数据的initVertexData方法
    	initVertexData(rBig,rSmall,nCol,nRow);
    	//调用初始化着色器的intShader方法
    	initShader(mv);
    }
    
    //自定义的初始化顶点数据的方法
    public void initVertexData(
			float rBig, float rSmall,//圆环外径、内径
			int nCol ,int nRow) {//小圆周和大圆周切分的份数
	
		float angdegColSpan=360.0f/nCol;//小圆周每份的角度跨度
		float angdegRowSpan=360.0f/nRow;//大圆周每份的角度跨度
		float A=(rBig-rSmall)/2;//用于旋转的小圆半径
		float D=rSmall+A;//旋转轨迹形成的大圆周半径
		vCount=3*nCol*nRow*2;//顶点个数
		
		ArrayList<Float> alVertix=new ArrayList<Float>();//原始顶点列表（未卷绕）
		ArrayList<Integer> alFaceIndex=new ArrayList<Integer>();//用于组织三角形面的顶点编号列表		
		
		for(float angdegCol=0;Math.ceil(angdegCol)<360+angdegColSpan;
		angdegCol+=angdegColSpan)	{//对小圆按照等角度间距循环
			double a=Math.toRadians(angdegCol);//当前小圆弧度
			for(float angdegRow=0;Math.ceil(angdegRow)<360+angdegRowSpan;angdegRow+=angdegRowSpan)
			{//对大圆按照等角度间距循环
				double u=Math.toRadians(angdegRow);//当前大圆弧度
				float y=(float) (A*Math.cos(a));//按照公式计算当前顶点
				float x=(float) ((D+A*Math.sin(a))*Math.sin(u));	//的X、Y、Z坐标
				float z=(float) ((D+A*Math.sin(a))*Math.cos(u));
				//将计算出来的X、Y、Z坐标放入原始顶点列表
        		alVertix.add(x); alVertix.add(y); alVertix.add(z);
			}
		}				
		
		for(int i=0;i<nCol;i++){//按照卷绕成三角形的需要
			for(int j=0;j<nRow;j++){//生成顶点编号列表
				int index=i*(nRow+1)+j;//当前四边形第一顶点编号
			
				alFaceIndex.add(index+1);//第一个三角形三个顶点的编号入列表
				alFaceIndex.add(index+nRow+1);
				alFaceIndex.add(index+nRow+2);
				
				alFaceIndex.add(index+1);//第二个三角形三个顶点的编号入列表
				alFaceIndex.add(index);
				alFaceIndex.add(index+nRow+1);
			}
		}
		float[] vertices=new float[vCount*3];//存放按照卷绕顺序顶点坐标值的数组
		
		cullVertex(alVertix, alFaceIndex, vertices);//生成卷绕后的顶点坐标数组值
		
		ArrayList<Float> alST=new ArrayList<Float>();//原纹理坐标列表（未卷绕）
		for(float angdegCol=0;Math.ceil(angdegCol)<360+angdegColSpan;angdegCol+=angdegColSpan)
		{//对小圆按照等角度间距循环
			float t=angdegCol/360;//当前角度对应的t坐标
			for(float angdegRow=0;Math.ceil(angdegRow)<360+angdegRowSpan;angdegRow+=angdegRowSpan)//重复了一列纹理坐标，以索引的计算
			{//对大圆按照等角度间距循环
				float s=angdegRow/360;//当前角度对应的s坐标
				
				alST.add(s); alST.add(t);//存入原始纹理坐标列表
			}
		}
		
		float[] textures=cullTexCoor(alST, alFaceIndex);//生成卷绕后纹理坐标数组值
		
		//顶点坐标数据初始化
		ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length*4);//创建顶点坐标数据缓冲
        vbb.order(ByteOrder.nativeOrder());//设置字节顺序为本地操作系统顺序
        mVertexBuffer = vbb.asFloatBuffer();//转换为float型缓冲
        mVertexBuffer.put(vertices);//向缓冲区中放入顶点坐标数据
        mVertexBuffer.position(0);//设置缓冲区起始位置

        //st坐标数据初始化		
        ByteBuffer tbb = ByteBuffer.allocateDirect(textures.length*4);//创建顶点纹理数据缓冲
        tbb.order(ByteOrder.nativeOrder());//设置字节顺序为本地操作系统顺序
        mTexCoorBuffer = tbb.asFloatBuffer();//转换为float型缓冲
        mTexCoorBuffer.put(textures);//向缓冲区中放入顶点纹理数据
        mTexCoorBuffer.position(0);//设置缓冲区起始位置
	}
    
	public static void cullVertex(//根据顶点编号生成卷绕后顶点坐标数组的方法
			ArrayList<Float> alv,//原始顶点列表
			ArrayList<Integer> alFaceIndex,//用于组织三角形面的顶点编号列表
			float[] vertices//存放卷绕后顶点坐标值的数组
		){
		int vCount=0;//顶点计数器
		for(int i:alFaceIndex){//对顶点编号列表进行循环
			vertices[vCount++]=alv.get(3*i);//将当前编号顶点的X坐标值存入最终数组
			vertices[vCount++]=alv.get(3*i+1);//将当前编号顶点的Y坐标值存入最终数组
			vertices[vCount++]=alv.get(3*i+2);//将当前编号顶点的Z坐标值存入最终数组
		}
	}
	
	public static float[] cullTexCoor(//根据顶点编号生成卷绕后顶点纹理坐标数组的方法
			ArrayList<Float> alST,//原始纹理坐标列表
			ArrayList<Integer> alTexIndex//用于组织三角形面的顶点编号列表
			)
	{
		float[] textures=new float[alTexIndex.size()*2];//结果纹理坐标数组
		
		int stCount=0;//纹理坐标计数器
		for(int i:alTexIndex){//对顶点编号列表进行循环
			textures[stCount++]=alST.get(2*i);//将当前编号顶点的S坐标值存入最终数组
			textures[stCount++]=alST.get(2*i+1);//将当前编号顶点的T坐标值存入最终数组
		}
		return textures;//返回结果纹理坐标数组
	}

    //自定义初始化着色器initShader方法
    public void initShader(MySurfaceView mv)
    {
    	//加载顶点着色器的脚本内容
        mVertexShader=ShaderUtil.loadFromAssetsFile("vertex_tex.sh", mv.getResources());
        //加载片元着色器的脚本内容
        mFragmentShader=ShaderUtil.loadFromAssetsFile("frag_tex.sh", mv.getResources());  
        //基于顶点着色器与片元着色器创建程序
        mProgram = createProgram(mVertexShader, mFragmentShader);
        //获取程序中顶点位置属性引用id  
        maPositionHandle = GLES30.glGetAttribLocation(mProgram, "aPosition");
        //获取程序中顶点纹理坐标属性引用id  
        maTexCoorHandle= GLES30.glGetAttribLocation(mProgram, "aTexCoor");
        //获取程序中总变换矩阵引用id
        muMVPMatrixHandle = GLES30.glGetUniformLocation(mProgram, "uMVPMatrix"); 
    }
    
    public void drawSelf(int texId)
    {
    	 MatrixState.rotate(xAngle, 1, 0, 0);
    	 MatrixState.rotate(yAngle, 0, 1, 0);
    	 MatrixState.rotate(zAngle, 0, 0, 1);    	 
    	
    	 //制定使用某套shader程序
    	 GLES30.glUseProgram(mProgram);        
         
         //将最终变换矩阵传入shader程序
         GLES30.glUniformMatrix4fv(muMVPMatrixHandle, 1, false, MatrixState.getFinalMatrix(), 0);
         
         //传送顶点位置数据
         GLES30.glVertexAttribPointer  
         (
         		maPositionHandle,   
         		3, 
         		GLES30.GL_FLOAT, 
         		false,
                3*4,   
                mVertexBuffer
         );       
         //传送顶点纹理坐标数据
         GLES30.glVertexAttribPointer  
         (
        		maTexCoorHandle, 
         		2, 
         		GLES30.GL_FLOAT, 
         		false,
                2*4,   
                mTexCoorBuffer
         ); 
         
         //启用顶点位置数据
         GLES30.glEnableVertexAttribArray(maPositionHandle);
         //启用顶点纹理数据
         GLES30.glEnableVertexAttribArray(maTexCoorHandle);  
         
         //绑定纹理
         GLES30.glActiveTexture(GLES30.GL_TEXTURE0);
         GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, texId);
         
         //绘制纹理矩形
         GLES30.glDrawArrays(GLES30.GL_TRIANGLES, 0, vCount); 
         
    }
}
