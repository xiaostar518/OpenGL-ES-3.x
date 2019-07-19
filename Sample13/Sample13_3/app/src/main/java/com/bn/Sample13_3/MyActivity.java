package com.bn.Sample13_3;
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
	Sensor myGyroscope; 	// Sensor对象引用
	TextView tvX;	//用于显示x轴角速度的TextView
	TextView tvY;	//用于显示y轴角速度的TextView
	TextView tvZ;	//用于显示z轴角速度的TextView
	TextView info;	//显示传感器属性信息的TextView
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        tvX = (TextView)findViewById(R.id.tvX);	//用于显示x轴角速度
        tvY = (TextView)findViewById(R.id.tvY);	//用于显示y轴角速度	
        tvZ = (TextView)findViewById(R.id.tvZ); //用于显示z轴角速度
        info= (TextView)findViewById(R.id.info);//用于显示陀螺仪传感器的属相信息
        //获得SensorManager对象
        mySensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);	
        //传感器的类型
        myGyroscope=mySensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        

        StringBuffer strb=new StringBuffer();//创建一个StringBuffer
        strb.append("\n名称: ");
        strb.append(myGyroscope.getName());	//获取传感器名称
        strb.append("\n耗电量(mA): ");
        strb.append(myGyroscope.getPower());//获取此传感器的耗电量，以毫安(mA)为单位
        strb.append("\n类型编号 : ");
        strb.append(myGyroscope.getType());//获取传感器类型编号
        strb.append("\n制造商: ");
        strb.append(myGyroscope.getVendor());//获取传感器的制造商
        strb.append("\n版本: ");
        strb.append(myGyroscope.getVersion());//获取传感器版本
        strb.append("\n最大测量范围: ");
        strb.append(myGyroscope.getMaximumRange());	//获取传感器的最大测量范围(量程)
        
        info.setText(strb.toString());	//将信息放到显示用的TextView中
    }
    @Override
	protected void onResume(){ //重写onResume方法
		super.onResume();
		mySensorManager.registerListener(//注册监听器
				mySensorListener, 		//监听器引用
				myGyroscope, 			//被监听的传感器引用
				SensorManager.SENSOR_DELAY_NORMAL	//传感器采样的频率
		);
	}	
	@Override
	protected void onPause(){//重写onPause方法	
		super.onPause();
		mySensorManager.unregisterListener(mySensorListener);//注销监听器
	}
	private SensorEventListener mySensorListener = 
		new SensorEventListener(){//开发实现了SensorEventListener接口的传感器监听器
		@Override
		public void onAccuracyChanged(Sensor sensor, int accuracy){}
		@Override
		public void onSensorChanged(SensorEvent event){
			float []values=event.values;//获取三个轴方向上的角速度值
			tvX.setText("x轴的角速度为："+values[0]);		
			tvY.setText("y轴的角速度为："+values[1]);		
			tvZ.setText("z轴的角速度为："+values[2]);		
		}
	};
	
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