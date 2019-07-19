package com.bn.Sample13_8;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;

public class MyActivity extends Activity {
	//SensorManager对象引用
	SensorManager mySensorManager;		
	Sensor sensorAccelerometer;//加速度传感器的引用
	MySurfaceView mySurfaceView;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        //全屏
		requestWindowFeature(Window.FEATURE_NO_TITLE); 
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN ,  
		              WindowManager.LayoutParams.FLAG_FULLSCREEN);		
		//设置为屏模式
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		
		//获得SensorManager对象
        mySensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        sensorAccelerometer=mySensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);       
        
        mySurfaceView = new MySurfaceView(this);
        this.setContentView(mySurfaceView);       
        //获取焦点
        mySurfaceView.requestFocus();
        //设置为可触控
        mySurfaceView.setFocusableInTouchMode(true);
    }
    

  //重力传感器的监听器
  	private SensorEventListener mek=new SensorEventListener()
  	{
  		@Override
  		public void onAccuracyChanged(Sensor sensor, int accuracy) {}

  		@Override
  		public void onSensorChanged(SensorEvent event) 
  		{			
  			//获取重力加速度在屏幕上的XY分量
  			float gx=event.values[0];
  			float gy=event.values[1];

  			//求出屏幕上重力加速度向量的分量长度
  			double mLength=gx*gx+gy*gy;
  			mLength=Math.sqrt(mLength);
  			//若分量为0则返回
  			if(mLength==0)
  			{
  				return;
  			}
  			//若分量不为0则设置球滚动的步进
  			Constant.SPANX=(float)((gy/mLength)*0.08);
  			Constant.SPANZ=(float)((gx/mLength)*0.08);				
  		}		
  	};	

	@Override
	protected void onResume() {						//重写onResume方法
		mySensorManager.registerListener
		(mek, sensorAccelerometer, SensorManager.SENSOR_DELAY_UI);
		super.onResume();
	}
	@Override
	protected void onPause() {									//重写onPause方法
		mySensorManager.unregisterListener(mek);
		super.onPause();
	}
	@Override
	public boolean onKeyDown(int keyCode,KeyEvent e)
	{
		switch(keyCode)
	    	{
		case 4:
			System.exit(0);
			break;
	    	}
		return true;
	}
}