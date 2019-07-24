package com.bn.Sample13_4;
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
	Sensor myLight; 	// Sensor对象引用
	TextView light;	//TextView对象引用	
	TextView info;	
    @Override
    public void onCreate(Bundle savedInstanceState) {//重写的onCreate方法
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main); //切换到主界面
        light = (TextView)findViewById(R.id.light);	//用于显示光强度的TextView
        info= (TextView)findViewById(R.id.info);//用于显示光传感器属性信息的TextView
        //获得SensorManager对象
        mySensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);	
        //传感器的类型为光传感器
        myLight=mySensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        
        StringBuffer strb=new StringBuffer();//创建一个StringBuffer
        strb.append("\n名称: ");
        strb.append(myLight.getName());//获取传感器名称
        strb.append("\n耗电量(mA) : ");
        strb.append(myLight.getPower());//获取此传感器的耗电量，以毫安(mA)为单位
        strb.append("\n类型编号  : ");
        strb.append(myLight.getType());//获取传感器类型编号
        strb.append("\n制造商: ");
        strb.append(myLight.getVendor());//获取传感器的制造商
        strb.append("\n版本: ");
        strb.append(myLight.getVersion());//获取传感器版本
        strb.append("\n最大测量范围: ");
        strb.append(myLight.getMaximumRange());//获取传感器的最大测量范围(量程)
        info.setText(strb.toString());	//将信息放到显示用的TextView中
    }
    @Override
	protected void onResume(){ //重写onResume方法
		super.onResume();
		mySensorManager.registerListener(//注册监听器
				mySensorListener, 		//监听器引用
				myLight, 	//被监听的传感器引用
				SensorManager.SENSOR_DELAY_NORMAL//传感器采样的频率
		);
	}	
	@Override
	protected void onPause(){//重写onPause方法	
		super.onPause();
		mySensorManager.unregisterListener(mySensorListener);//注销监听器
	}
	private SensorEventListener mySensorListener = 
		new SensorEventListener(){//实现了SensorEventListener接口的传感器监听器
		@Override//重写onAccuracyChanged方法
		public void onAccuracyChanged(Sensor sensor, int accuracy){}
		@Override	//重写onSensorChanged方法
		public void onSensorChanged(SensorEvent event){
			float []values=event.values;//获取光照强度值所属的values数组
			light.setText("光的强度为："+values[0]);	//显示光照强度		
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