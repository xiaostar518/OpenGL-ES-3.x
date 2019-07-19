package com.bn.orign.orientation.util;
import android.app.Activity;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Surface;

public class DefaultOrientationUtil 
{
	public static DefaultOrientation defaultOrientation;
	
	public static void calDefaultOrientation(Activity activity)
	{
		Display display;//创建Display对象
	    display = activity.getWindowManager().getDefaultDisplay();//获取Display对象
	    int rotation = display.getRotation();//读取当前姿态相对于原始姿态的旋转角度
	    int widthOrign=0;//初始姿态屏幕宽度存储变量
	    int heightOrign=0;//初始姿态屏幕高度存储变量
	    DisplayMetrics dm = new DisplayMetrics();//创建DisplayMetrics对象
	    display.getMetrics(dm);//将显示设备信息存入DisplayMetrics对象
	    switch (rotation) 
	    {
	    //当前姿态相对于原始姿态旋转了0度或180度
		    case Surface.ROTATION_0:
		    case Surface.ROTATION_180:
		    	widthOrign=dm.widthPixels;
		    	heightOrign=dm.heightPixels;
		    break;
		    //当前姿态相对于原始姿态旋转了90度或270度
		    case Surface.ROTATION_90:       
		    case Surface.ROTATION_270:
		    	widthOrign=dm.heightPixels;
		    	heightOrign=dm.widthPixels;
		    break;
	    }
	    
	    if(widthOrign>heightOrign)//若初始姿态下屏幕宽度大于高度则初始为横屏
	    {
	    	defaultOrientation=DefaultOrientation.LANDSCAPE;
	    }
	    else//若初始姿态下屏幕宽度不大于高度则初始为竖屏
	    {
	    	defaultOrientation=DefaultOrientation.PORTRAIT;
	    }
	}
}
