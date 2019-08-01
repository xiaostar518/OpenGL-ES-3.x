package com.bn.Sample4_7;

import java.util.HashMap;
import java.util.HashSet;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;

public class Constant
{
	public static float[][] yArray;
	public static float [][][] normols;
	public static final float LAND_HIGH_ADJUST=2f;//陆地的高度调整值
	public static final float LAND_HIGHEST=60f;//陆地最大高差 
	//单位长度
	public static final float UNIT_SIZE=3.0f;
	
	//从灰度图片中加载陆地上每个顶点的高度
	public static float[][] loadLandforms(Resources resources,int index)
	{
		Bitmap bt=BitmapFactory.decodeResource(resources, index);
		int colsPlusOne=bt.getWidth();
		int rowsPlusOne=bt.getHeight();
		float[][] result=new float[rowsPlusOne][colsPlusOne];
		for(int i=0;i<rowsPlusOne;i++)
		{
			for(int j=0;j<colsPlusOne;j++)
			{
				int color=bt.getPixel(j,i);
				int r=Color.red(color);
				int g=Color.green(color); 
				int b=Color.blue(color);
				int h=(r+g+b)/3;
				result[i][j]=h*LAND_HIGHEST/255-LAND_HIGH_ADJUST;  
			}
		}
		return result;
	}

	public static float[][][]  caleNormal(float yArray[][])//入口参数为从灰度图加载的高度数组
	{
		//首先计算每个顶点的坐标
		float [][][] vertices = new float [yArray.length][yArray[0].length][3];//存放山地中顶点位置的三维数组
		float [][][] normols = new float [yArray.length][yArray[0].length][3];//存放山地中顶点法向量的三维数组
		//对高度数组遍历，计算顶点位置坐标
		for(int i=0;i<yArray.length;i++)
		{
			for(int j=0;j<yArray[0].length;j++)
			{
				float zsx=-UNIT_SIZE*yArray.length/2+i*UNIT_SIZE;
	    		float zsz=-UNIT_SIZE*yArray[0].length/2+j*UNIT_SIZE;
				vertices[i][j][0]=zsx; //顶点的x坐标  	
				vertices[i][j][1]=yArray[i][j];//顶点的y坐标
				vertices[i][j][2]=zsz;//顶点的z坐标
			}
		}
		//用于存放顶点法向量的HashMap，其键为顶点的索引，值为对应顶点的法向量集合
		HashMap<Integer,HashSet<Normal>> hmn=new HashMap<Integer,HashSet<Normal>>();
		int rows = yArray.length-1; //地形网格的行数
		int cols = yArray[0].length-1;//地形网格的列数
		
		for(int i=0;i<rows;i++)//对地形网格进行遍历
        {
        	for(int j=0;j<cols;j++) 
        	{  
	      		//计算4个顶点的索引
        		//0  1
        		//2  3
        		int []index = new int[4];//创建用于存放当前网格四个顶点索引的数组
        		index[0]=i*(cols+1)+j;//网格中0号点的索引
        		index[1]=index[0]+1;//网格中1号点的索引
        		index[2]=index[0]+cols+1;//网格中2号点的索引
        		index[3]=index[1]+cols+1;//网格中3号点的索引
        		//计算当前地形网格左上三角形面的法向量
	      		float vxa=vertices[i+1][j][0]-vertices[i][j][0];
	      		float vya=vertices[i+1][j][1]-vertices[i][j][1];
	      		float vza=vertices[i+1][j][2]-vertices[i][j][2];
	      		float vxb=vertices[i][j+1][0]-vertices[i][j][0];
	      		float vyb=vertices[i][j+1][1]-vertices[i][j][1];
	      		float vzb=vertices[i][j+1][2]-vertices[i][j][2];
	      		float[] vNormal1=Normal.vectorNormal(Normal.getCrossProduct
		      			(
		      					vxa,vya,vza,vxb,vyb,vzb
		      			));
	      		for(int k=0;k<3;k++){//将计算出的法向量加入各个顶点对应的法向量集合中
	      			HashSet<Normal> hsn=hmn.get(index[k]);
	      			if(hsn==null)
	      			{//若集合不存在则创建
	      				hsn=new HashSet<Normal>();
	      			}
	      			hsn.add(new Normal(vNormal1[0],vNormal1[1],vNormal1[2]));
	      			//将集合放进HsahMap中
	      			hmn.put(index[k], hsn);
        		} 
	      		
	      		//计算当前地形网格右下三角形面的法向量
	      		vxa=vertices[i+1][j][0]-vertices[i+1][j+1][0];
	      		vya=vertices[i+1][j][1]-vertices[i+1][j+1][1];
	      		vza=vertices[i+1][j][2]-vertices[i+1][j+1][2];
	      		vxb=vertices[i][j+1][0]-vertices[i+1][j+1][0];
	      		vyb=vertices[i][j+1][1]-vertices[i+1][j+1][1];
	      		vzb=vertices[i][j+1][2]-vertices[i+1][j+1][2];
	      		float[] vNormal2=Normal.vectorNormal(Normal.getCrossProduct
		      			(
		      					vxb,vyb,vzb,vxa,vya,vza
		      			));
	      		for(int k=1;k<4;k++){//将计算出的法向量加入各个顶点对应的法向量集合中
	      			HashSet<Normal> hsn=hmn.get(index[k]);
	      			if(hsn==null)
	      			{//若集合不存在则创建
	      				hsn=new HashSet<Normal>();
	      			}
	      			hsn.add(new Normal(vNormal2[0],vNormal2[1],vNormal2[2]));
	      			//将集合放进HsahMap中
	      			hmn.put(index[k], hsn);
        		}
        	}
        }
		
		for(int i=0;i<yArray.length;i++)//遍历顶点数组，计算每个顶点的平均法向量
		{
			for(int j=0;j<yArray[0].length;j++)
			{
				int index=i*(cols+1)+j;
				
		    	HashSet<Normal> hsn=hmn.get(index);
		    	//求出平均法向量
		    	float[] tn=Normal.getAverage(hsn);
		    	//将计算出的平均法向量存放到法向量数组中
		    	normols[i][j] = tn;//返回法向量数组
			}
		}

		return normols;
	}
}