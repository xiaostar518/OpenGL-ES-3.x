package com.bn;
import static com.bn.Constant.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.*;

public class Noise2DUtil 
{
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
	public static double noise(int x,int y,double amp)
	{
		int n = x + y * 57;
		n = (n<<13) ^ n;
		double result=(( 1.0 - ( (n * (n * n * 15731 + 789221) + 1376312589) & 0x7fffffff) / 1073741824.0)+1)/2.0; 
		return result*amp;
	}

	//生成某一级倍频的图
	public static BufferedImage calYJBP(int level)
	{
		//求出此级倍频的实际大小
		int size=COUNT*pow(2,level-1);  
		//创建此级倍频的实际图像
		BufferedImage  temp=new BufferedImage(size,size,BufferedImage.TYPE_INT_ARGB);
		//求出此级的振幅
		double amp=255.0/pow(2,level);		
		if(Constant.ZQDBD_FLAG)
		{
			amp=255;
		}
		
		//循环生成每一个像素值
		for(int i=0;i<size;i++)
		{
			for(int j=0;j<size;j++)
			{
				double val=noise(++X_CURR,++Y_CURR,amp);
				int gray=(int)val;
				int cResult=0x00000000;
                cResult+=255<<24;
                cResult+=gray<<16;
                cResult+=gray<<8;
                cResult+=gray;                
                temp.setRGB(j,i,cResult);
			}
		}
		
		//创建512x512的结果图像
		BufferedImage  result=new BufferedImage(512,512,BufferedImage.TYPE_INT_ARGB);
		//求出缩放比
		int scale=512/size;
		//获取画笔
		Graphics g=result.getGraphics();
		Graphics2D g2d=(Graphics2D)g;
		//设置插值
		g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);

		//创建缩放对象
		AffineTransform at=new AffineTransform();
		at.scale(scale, scale);
		g2d.drawImage(temp, at, null);
				
		return result;
	}
	
	
	public static BufferedImage[] calSYBP()
	{
		BufferedImage[] result=new BufferedImage[PLS+1];
		
		for(int i=1;i<=PLS;i++)
    	{
    		result[i-1]=calYJBP(i);
    	}
		
		//创建512x512的结果图像
		result[PLS]=new BufferedImage(512,512,BufferedImage.TYPE_INT_ARGB);
		
		for(int i=0;i<512;i++)
		{
			for(int j=0;j<512;j++)
			{
				int gray=0;
				for(int k=1;k<=PLS;k++)
				{
					int color=result[k-1].getRGB(i, j);
					//拆分出RGB三个色彩通道的值
					int r=(color >> 16) & 0xff;
					int g=(color >> 8) & 0xff;
					int b=(color) & 0xff;
					int grayTemp=0;
					if(Constant.ZQDBD_FLAG)
					{
						grayTemp=(int)((r+g+b)/3.0/pow(2,k));
					}
					else
					{
						grayTemp=(r+g+b)/3;
					}
					gray+=grayTemp;
				}
				int cResult=0x00000000;
                cResult+=255<<24;
                cResult+=gray<<16;
                cResult+=gray<<8;
                cResult+=gray;                
                result[PLS].setRGB(i,j,cResult);
			}
		}
		
		return result;
	}
}
