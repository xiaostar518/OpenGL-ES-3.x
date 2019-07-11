package com.bn.Sample9_2;//声明包
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import android.content.res.Resources;
import android.util.Log;

//用于从obj文件中加载3D模型的工具类
public class LoadUtil 
{
	//求两个向量叉积的方法
	public static float[] getCrossProduct(float x1,float y1,float z1,float x2,float y2,float z2)
	{		
		//求出两个矢量叉积矢量在XYZ轴的分量ABC
        float A=y1*z2-y2*z1;//求出两个向量的叉积向量，在X、Y、Z轴上的分量A、B、C
        float B=z1*x2-z2*x1;
        float C=x1*y2-x2*y1;
		
		return new float[]{A,B,C};//返回叉积结果向量
	}
	
	//向量规格化的方法
	public static float[] vectorNormal(float[] vector)
	{
		//求向量的模
		float module=(float)Math.sqrt(vector[0]*vector[0]+vector[1]*vector[1]+vector[2]*vector[2]);
		//返回规格化的向量
		return new float[]{vector[0]/module,vector[1]/module,vector[2]/module};
	}
	
	//从obj文件中加载仅携带顶点信息的物体
	//首先加载顶点信息，再根据顶点组成三角形面的情况自动计算出每个面的法向量
	//然后将这个面的法向量分配给这个面上的顶点
    public static LoadedObjectVertexNormal loadFromFile(String fname, Resources r,MySurfaceView mv)
    {//从obj文件中加载仅携带顶点信息的物体，并计算面法向量
    	
    	LoadedObjectVertexNormal lo=null; //加载后物体的引用   	
    	//原始顶点坐标列表--按顺序从obj文件中加载的
    	ArrayList<Float> alv=new ArrayList<Float>();
    	//结果顶点坐标列表 --根据组成面的情况组织好的
    	ArrayList<Float> alvResult=new ArrayList<Float>();	
    	//结果法向量列表--根据组成面的情况组织好的
    	ArrayList<Float> alnResult=new ArrayList<Float>();
    	
    	try
    	{
    		InputStream in=r.getAssets().open(fname);
    		InputStreamReader isr=new InputStreamReader(in);
    		BufferedReader br=new BufferedReader(isr);
    		String temps=null;
    		
    		//循环不断从文件中读取行，根据行类型的不同执行不同的处理逻辑
		    while((temps=br.readLine())!=null) 
		    {//读取一行文本
		    	String[] tempsa=temps.split("[ ]+");//将文本行用空格符切分
		      	if(tempsa[0].trim().equals("v"))
		      	{//顶点坐标行
		      		//将顶点的x、y、z坐标存入原始顶点坐标列表
		      		alv.add(Float.parseFloat(tempsa[1]));
		      		alv.add(Float.parseFloat(tempsa[2]));
		      		alv.add(Float.parseFloat(tempsa[3]));
		      	}
		      	else if(tempsa[0].trim().equals("f"))
		      	{//面数据行
		      		/*
		      		 *若为三角形面行则根据 组成面的顶点的索引从原始顶点坐标列表中
		      		 *提取相应的顶点坐标值添加到结果顶点坐标列表中，同时根据三个
		      		 *顶点的坐标计算出法向量并添加到结果法向量列表中
		      		*/
		      		
		      		//提取三角形第一个顶点的坐标
		      		int index=Integer.parseInt(tempsa[1].split("/")[0])-1;//得到顶点编号
		      		//将三角形第1个顶点的x、y、z坐标取出
		      		float x0=alv.get(3*index);
		      		float y0=alv.get(3*index+1);
		      		float z0=alv.get(3*index+2);
		      		alvResult.add(x0);
		      		alvResult.add(y0);
		      		alvResult.add(z0);  
		      		
		      	    //提取三角形第二个顶点的坐标
		      		index=Integer.parseInt(tempsa[2].split("/")[0])-1;
		      		float x1=alv.get(3*index);
		      		float y1=alv.get(3*index+1);
		      		float z1=alv.get(3*index+2);
		      		alvResult.add(x1);
		      		alvResult.add(y1);
		      		alvResult.add(z1);
		      		
		      		//提取三角形第三个顶点的坐标
		      		index=Integer.parseInt(tempsa[3].split("/")[0])-1;
		      		float x2=alv.get(3*index);
		      		float y2=alv.get(3*index+1);
		      		float z2=alv.get(3*index+2);
		      		alvResult.add(x2);
		      		alvResult.add(y2);
		      		alvResult.add(z2);	 
		      		
		      		//通过三角形面两个边向量0-1，0-2求叉积得到此面的法向量
		      		
		      		//求三角形中第一个点到第二个点的向量
		      		float vxa=x1-x0;
		      		float vya=y1-y0;
		      		float vza=z1-z0;
		      		//求三角形中第一个点到第三个点的向量
		      		float vxb=x2-x0;
		      		float vyb=y2-y0;
		      		float vzb=z2-z0;
		      		
		      		//通过求两个向量的叉积计算出此三角形面的法向量
		      		float[] vNormal=vectorNormal
		      		                ( 
	                                    getCrossProduct
						      			(
						      					vxa,vya,vza,vxb,vyb,vzb
						      			)
		      		                );
		      		//将计算出的法向量添加到结果法向量列表中
		      		for(int i=0;i<3;i++)
		      	    {
		      	    	alnResult.add(vNormal[0]);
		      	    	alnResult.add(vNormal[1]);
		      	    	alnResult.add(vNormal[2]);
		      	    }
		      	}		      		
		    } 
		    
		    //obj文件读取结束后生成顶点数组及生成法向量数组
		    //生成顶点数组
		    int size=alvResult.size();//获取结果顶点坐标列表的大小
		    float[] vXYZ=new float[size];
		    for(int i=0;i<size;i++)
		    {
		    	vXYZ[i]=alvResult.get(i);
		    }
		    
		    //生成法向量数组
		    size=alnResult.size();//获取结果法向量列表的大小
		    float[] nXYZ=new float[size];
		    for(int i=0;i<size;i++)
		    {		    	
		    	nXYZ[i]=alnResult.get(i);
		    }
		    
		    lo=new LoadedObjectVertexNormal(mv,vXYZ,nXYZ);//创建加载物体对象
    	}
    	catch(Exception e)
    	{
    		Log.d("load error", "load error");
    		e.printStackTrace();
    	}    	
    	return lo;//返回创建的物体对象的引用
    }
}
