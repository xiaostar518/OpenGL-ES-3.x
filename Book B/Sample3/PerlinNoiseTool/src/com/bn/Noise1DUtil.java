package com.bn;
import static com.bn.Constant.*;
import java.util.*;

public class Noise1DUtil 
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
	public static double noise(int x,double amp)
	{
		x = (x<<13) ^ x;
		double result=(( 1.0 - ( (x * (x * x * 15731 + 789221) + 1376312589) & 0x7fffffff) / 1073741824.0)+1)/2.0; 
		return result*amp;
	}
	
	 //余弦插值函数
	public static double cosIn(double a,double b,double x)
	{
		double ft = x * Math.PI;
		double f = (1 - Math.cos(ft)) * 0.5;
		return a*(1-f) + b*f;
	}
	
	 //求出某一级倍频的数据
    public static double[][] calYJBP(int level)
    {
    	double[][] result=null;
    	 
    	//计算出此一级倍频的总段数
    	int tc=COUNT*pow(BP,level-1);
    	//此一级倍频的控制点值
    	double[] cp=new double[tc+1];
    	//此一级倍频X步进
    	double xSpan=X_SPAN/pow(BP,level-1);
    	//此一级倍频的振幅
    	double amp=1.0/pow(BP,level-1);
    	//求出此一级倍频的所有控制点		
    	for(int i=0;i<=tc;i++)
    	{
    		cp[i]=noise(++X_CURR,amp);
    	}
    	
    	//此一级倍频切分数
    	int qfs=pow(BP,8-level);
    	//此一级倍频切分后的X步进
    	double xSpanQf=xSpan/qfs;    	
    	//存放切分插值后点的坐标列表
    	List<double[]> list=new ArrayList<double[]>();
    	
    	for(int i=0;i<tc;i++)
    	{
    		//取出这一段的两个控制点的值
    		double a=cp[i];
    		double b=cp[i+1];
    		//将起始点添加进列表
    		list.add(new double[]{i*xSpan,a});
    		//循环插值求出后面的中间点
    		for(int j=1;j<qfs;j++)
    		{
    			//插值用X
    			double xIn=(1.0/qfs)*j;
    			//求出此点的值加入列表
    			list.add(new double[]{i*xSpan+xSpanQf*j,cosIn(a,b,xIn)});
    		}
    	}
    	//将最后一个点加入列表
    	list.add(new double[]{COUNT,cp[tc]});
    	
    	result=new double[list.size()][];
    	for(int i=0;i<list.size();i++)
    	{
    		result[i]=list.get(i);
    	}    	    	
    	return result;
    }

    //求出所有倍频的情况以及总情况
    public static double[][][] calSYBP()
    {
    	double[][][] result=new double[PLS+1][][];
    	
    	for(int i=1;i<=PLS;i++)
    	{
    		result[i-1]=calYJBP(i);
    	}
    	
    	result[PLS]=new double[result[0].length][2];
    	
    	for(int i=0;i<result[0].length;i++)
    	{
    		result[PLS][i][0]=result[0][i][0];
    		double temp=0;
    		for(int j=0;j<PLS;j++)
    		{
    			temp+=result[j][i][1];
    		}
    		result[PLS][i][1]=temp;
    	}
    	return result;
    }
}
