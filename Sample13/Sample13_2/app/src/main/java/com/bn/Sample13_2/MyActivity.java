package com.bn.Sample13_2;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.TextView;
public class MyActivity extends Activity {
	SensorManager mySensorManager;	//SensorManager对象引用	
	Sensor myMagnetic; //Sensor对象引用
	TextView tvX;	//显示磁场强度在x轴上分量的TextView	
	TextView tvY;	//显示磁场强度在y轴上分量的TextView
	TextView tvZ;	//显示磁场强度在z轴上分量的TextView
	TextView info;	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);//切换到主界面
        
        tvX = (TextView)findViewById(R.id.tvX);	//用于显示x轴方向磁场强度
        tvY = (TextView)findViewById(R.id.tvY);	//用于显示y轴方向磁场强度
        tvZ = (TextView)findViewById(R.id.tvZ); //用于显示z轴方向磁场强度
        info= (TextView)findViewById(R.id.info);//用于显示手机中磁场传感器的相关信息
        //获得SensorManager对象
        mySensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);	
        //传感器的类型为磁场传感器
        myMagnetic=mySensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        
        StringBuffer strb=new StringBuffer();//创建一个StringBuffer 
        strb.append("\n名称: ");
        strb.append(myMagnetic.getName());//获取传感器名称
        strb.append("\n耗电量(mA): ");
        strb.append(myMagnetic.getPower());//获取此传感器的耗电量，以毫安(mA)为单位
        strb.append("\n类型编号 : ");
        strb.append(myMagnetic.getType());//获取传感器类型编号
        strb.append("\n制造商: ");
        strb.append(myMagnetic.getVendor());//获取传感器的制造商
        strb.append("\n版本: ");
        strb.append(myMagnetic.getVersion());//获取传感器版本
        strb.append("\n最大测量范围: ");
        strb.append(myMagnetic.getMaximumRange());//获取传感器的最大测量范围(量程)
        
        info.setText(strb.toString());	//将信息放到显示用的TextView中
    }
	private SensorEventListener mySensorListener = 
		new SensorEventListener(){//开发实现了SensorEventListener接口的传感器监听器
		@Override
		public void onAccuracyChanged(Sensor sensor, int accuracy){}
		@Override
		public void onSensorChanged(SensorEvent event){
			float []values=event.values;//获取x、y、z三个轴方向上的磁场强度分量值
			tvX.setText("x轴方向上的磁场强度为： "+values[0]);		
			tvY.setText("y轴方向上的磁场强度为： "+values[1]);		
			tvZ.setText("z轴方向上的磁场强度为： "+values[2]);		
		}
	};
    @Override
	protected void onResume(){ //重写onResume方法
		super.onResume();
		mySensorManager.registerListener(//注册监听器
				mySensorListener, 		//监听器引用
				myMagnetic, 			//被监听的传感器引用
				SensorManager.SENSOR_DELAY_NORMAL	//传感器采样的频率
		);
	}	
	@Override
	protected void onPause(){//重写onPause方法	
		super.onPause();
		mySensorManager.unregisterListener(mySensorListener);//注销监听器
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