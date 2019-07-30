package com.bn;

import java.awt.image.BufferedImage;

public class Constant 
{
	public static float BASE_AMPLITUDE=80;  //1D绘图用基础振幅
	public static float X_ARRANGE=800;	  //1D绘图用基础宽度
	
	//是否增强对比度标志
	public static boolean ZQDBD_FLAG=true;
	
	public static int BP=2;//倍频
	public static int PLS=5;//频率数量
	public static int COUNT=16;//一级倍频段数
	public static double X_SPAN=1.0;//一级倍频X步进
	public static int X_CURR=0;//求噪声的起始X
	public static int Y_CURR=0;//求噪声的起始Y
	public static int Z_CURR=0;//求噪声的起始Z
	
	public static BufferedImage[] D3Each;
}
