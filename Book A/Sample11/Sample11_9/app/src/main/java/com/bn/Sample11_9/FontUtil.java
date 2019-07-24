package com.bn.Sample11_9;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

public class FontUtil 
{
	static int cIndex=0;//文本内容索引
	static final float textSize=40;//字体的大小
	static int R=255;//画笔红色通道的值
	static int G=255;//画笔绿色通道的值
	static int B=255;//画笔蓝色通道的值
	public static Bitmap generateWLT(String[] str,int width,int height)
	{//生成文本纹理图的方法
		Paint paint=new Paint();//创建画笔对象
		paint.setARGB(255, R, G, B);//设置画笔颜色
		paint.setTextSize(textSize);//设置字体大小
		paint.setTypeface(null);
		paint.setFlags(Paint.ANTI_ALIAS_FLAG);//打开抗锯齿，使字体边缘光滑
		Bitmap bmTemp=Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
		Canvas canvasTemp = new Canvas(bmTemp);//根据指定的位图创建画布
		for(int i=0;i<str.length;i++)//绘制当前纹理图对应的每行文字
		{
			canvasTemp.drawText(str[i], 0, textSize*i+(i-1)*5, paint);
		}
		return bmTemp;//返回绘制的作为纹理图的位图
	}
	static String[] content=
	{
		"赵客缦胡缨，吴钩霜雪明。",
		"银鞍照白马，飒沓如流星。",
		"十步杀一人，千里不留行。",
		"事了拂衣去，深藏身与名。",
		"闲过信陵饮，脱剑膝前横。",
		"将炙啖朱亥，持觞劝侯嬴。",
		"三杯吐然诺，五岳倒为轻。",
		"眼花耳热后，意气素霓生。",
		"救赵挥金槌，邯郸先震惊。",
		"千秋二壮士，煊赫大梁城。",
		"纵死侠骨香，不惭世上英。",
		"谁能书閤下，白首太玄经。",
	};
	
	public static String[] getContent(int length,String[] content)
	{//获取指定行数字符串数组的方法
		String[] result=new String[length+1];//创建字符串数组
		for(int i=0;i<=length;i++)
		{
			result[i]=content[i];//将当前需要的内容填入数组
		}
		return result;
	}
	
	public static void updateRGB()//随机产生画笔颜色值的方法
	{
		R=(int)(255*Math.random());//随机产生画笔红色通道值
		G=(int)(255*Math.random());//随机产生画笔绿色通道值
		B=(int)(255*Math.random());//随机产生画笔蓝色通道值
	}
}