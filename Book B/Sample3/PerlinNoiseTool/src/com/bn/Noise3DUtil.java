package com.bn;
import static com.bn.Constant.*;
import java.awt.image.*;

public class Noise3DUtil 
{
	
	public static int GLOBAL_SIZE=64;
	
	//求乘方运算的方法
	public static int pow(int a,int b)
	{
		int result=1;
		for(int i=0;i<b;i++)
		{
			result=result*a;
		}
		return result;
	}
	
	//生成噪声控制点的方法
	public static double noise(int x,int y,int z,double amp)
	{
		int n = x + y * 57 + z * 3249;
		n = (n<<13) ^ n;
		double result=(( 1.0 - ( (n * (n * n * 15731 + 789221) + 1376312589) & 0x7fffffff) / 1073741824.0)+1)/2.0; 
		return result*amp;
	}

	//生成某一级倍频的数据
	public static int[][][] calYJBP(int level)
	{
		//求出此级倍频的实际大小
		int size=pow(2,level);  
		//此级倍频的像素颜色数据
		int[][][] result=new int[size][size][size];
		
		//求出此级的振幅		
		double amp=255;
		
		//循环生成每一个位置的像素值
		for(int i=0;i<size;i++)
		{
			for(int j=0;j<size;j++)
			{
				for(int k=0;k<size;k++)
				{
					double val=noise(++X_CURR,++Y_CURR,++Y_CURR,amp);
					int gray=(int)val;					
	                result[i][j][k]=gray;		
				}
			}
		}		
		
		int[][][] resultHelp=new int[GLOBAL_SIZE][GLOBAL_SIZE][GLOBAL_SIZE];
		//求出缩放比
		int scale=GLOBAL_SIZE/size;
		for(int i=0;i<GLOBAL_SIZE;i++)
		{
			for(int j=0;j<GLOBAL_SIZE;j++)
			{
				for(int k=0;k<GLOBAL_SIZE;k++)
				{
					resultHelp[i][j][k]=result[i/scale][j/scale][k/scale];
				}
			}
		}
		
		int[][][] resultFinal=new int[GLOBAL_SIZE][GLOBAL_SIZE][GLOBAL_SIZE];
		for(int i=0;i<GLOBAL_SIZE;i++)
		{
			for(int j=0;j<GLOBAL_SIZE;j++)
			{
				for(int k=0;k<GLOBAL_SIZE;k++)
				{
					float count=0;
					int temp=0;
					int half=scale/2;
					for(int a=-half;a<=half;a++)
					{
						for(int b=-half;b<=half;b++)
						{
							for(int c=-half;c<=half;c++)
							{
								int indexX=i+a;
								int indexY=j+b;
								int indexZ=k+c;
								if(indexX>=0&&indexX<GLOBAL_SIZE&&indexY>=0&&indexY<GLOBAL_SIZE&&indexZ>=0&&indexZ<GLOBAL_SIZE)
								{
									temp=temp+resultHelp[indexX][indexY][indexZ];
									count++;
								}
							}
						}
					}
					
					resultFinal[i][j][k]=(int)(temp/count);
				}
			}
		}
				
		return resultFinal;
	}
	
	//RGBA各自一个倍频
	public static int[][][][] calSYBP_BPFL()
	{
		final int PLS4=4;
		
		int[][][][] resultFinalL=new int[GLOBAL_SIZE][GLOBAL_SIZE][GLOBAL_SIZE][PLS4];
		
		for(int l=1;l<=PLS4;l++)
    	{
			int[][][] resulttemp=calYJBP(l);
			for(int i=0;i<GLOBAL_SIZE;i++)
			{
				for(int j=0;j<GLOBAL_SIZE;j++)
				{
					for(int k=0;k<GLOBAL_SIZE;k++)
					{ 
						resultFinalL[i][j][k][l-1]=resulttemp[i][j][k];						
					}
				}
			}
    	}
		
		//创建512x512的结果图像
		D3Each=new BufferedImage[GLOBAL_SIZE];
		for(int d=0;d<GLOBAL_SIZE;d++)
		{
			D3Each[d]=new BufferedImage(GLOBAL_SIZE,GLOBAL_SIZE,BufferedImage.TYPE_INT_ARGB);
			for(int i=0;i<GLOBAL_SIZE;i++)
			{
				for(int j=0;j<GLOBAL_SIZE;j++)
				{
					int red=resultFinalL[d][i][j][0];	
					int green=resultFinalL[d][i][j][1];
					int blue=resultFinalL[d][i][j][2];
					int alpha=resultFinalL[d][i][j][3];
	                
	                int cResult=0x00000000;
	                cResult+=alpha<<24;
	                cResult+=red<<16;
	                cResult+=green<<8;
	                cResult+=blue;       
	                D3Each[d].setRGB(i,j,cResult);
				}
			}			
		}
		
		return resultFinalL;
	}
	
	
	//合成统一灰度
	public static int[][][] calSYBP()
	{
		int[][][] resultFinal=new int[GLOBAL_SIZE][GLOBAL_SIZE][GLOBAL_SIZE];
		
		//resultFinal=calYJBP(5);
		
		for(int l=1;l<=PLS;l++)
    	{
			int[][][] resulttemp=calYJBP(l);
			for(int i=0;i<GLOBAL_SIZE;i++)
			{
				for(int j=0;j<GLOBAL_SIZE;j++)
				{
					for(int k=0;k<GLOBAL_SIZE;k++)
					{ 
						resultFinal[i][j][k]=resultFinal[i][j][k]*2+resulttemp[i][j][k];						
					}
				}
			}
    	}
		
		int max=0;
		for(int i=0;i<GLOBAL_SIZE;i++)
		{
			for(int j=0;j<GLOBAL_SIZE;j++)
			{
				for(int k=0;k<GLOBAL_SIZE;k++)
				{ 
					if(max<resultFinal[i][j][k])
					{
						max=resultFinal[i][j][k];
					}
				}
			}
		}
		
		for(int i=0;i<GLOBAL_SIZE;i++)
		{
			for(int j=0;j<GLOBAL_SIZE;j++)
			{
				for(int k=0;k<GLOBAL_SIZE;k++)
				{ 
					resultFinal[i][j][k]=resultFinal[i][j][k]*256/max;
				}
			}
		}
		
		//创建512x512的结果图像
		D3Each=new BufferedImage[GLOBAL_SIZE];
		
		for(int d=0;d<GLOBAL_SIZE;d++)
		{
			D3Each[d]=new BufferedImage(GLOBAL_SIZE,GLOBAL_SIZE,BufferedImage.TYPE_INT_ARGB);
			for(int i=0;i<GLOBAL_SIZE;i++)
			{
				for(int j=0;j<GLOBAL_SIZE;j++)
				{
					int gray=resultFinal[d][i][j];	
					int cResult=0x00000000;
	                cResult+=255<<24;
	                cResult+=gray<<16;
	                cResult+=gray<<8;
	                cResult+=gray;                
	                D3Each[d].setRGB(i,j,cResult);
				}
			}			
		}
		return resultFinal;
	}
}
