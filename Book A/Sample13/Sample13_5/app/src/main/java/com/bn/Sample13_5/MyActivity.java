package com.bn.Sample13_5;
import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.TextView;

public class MyActivity extends Activity {
	SensorManager mySensorManager; // SensorManager对象引用
	Sensor myTemperature; // 传感器类型
	TextView temperature; // TextView对象引用
	TextView info;

	@SuppressWarnings("deprecation")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);//切换到主界面
		temperature = (TextView) findViewById(R.id.temperature); //用于显示温度的TextView
		info = (TextView) findViewById(R.id.info);//用于显示温度传感器属性信息的TextView
		// 获得SensorManager对象
		mySensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		//获取传感器
		myTemperature = mySensorManager
				.getDefaultSensor(Sensor.TYPE_TEMPERATURE);
		// 创建一个StringBuffer
		StringBuffer strb = new StringBuffer();
		strb.append("\n名称: ");
		strb.append(myTemperature.getName());
		strb.append("\n耗电量(mA): ");
		strb.append(myTemperature.getPower());
		strb.append("\n类型编号 : ");
		strb.append(myTemperature.getType());
		strb.append("\n制造商: ");
		strb.append(myTemperature.getVendor());
		strb.append("\n版本: ");
		strb.append(myTemperature.getVersion());
		strb.append("\n最大测量范围: ");
		strb.append(myTemperature.getMaximumRange());
		info.setText(strb.toString()); // 将信息字符串赋予名为info的TextView
	}

	@Override
	protected void onResume() { // 重写onResume方法
		super.onResume();
		mySensorManager.registerListener(mySensorListener, // 添加监听
				myTemperature, // 传感器类型
				SensorManager.SENSOR_DELAY_NORMAL // 传感器事件传递的频度
				);
	}

	@Override
	protected void onPause() {// 重写onPause方法
		super.onPause();
		mySensorManager.unregisterListener(mySensorListener);// 取消注册监听器
	}
	//实现了SensorEventListener接口的传感器监听器
	private SensorEventListener mySensorListener = new SensorEventListener() {
		@Override
		public void onAccuracyChanged(Sensor sensor, int accuracy) {//重写onAccuracyChanged方法
		}

		@Override
		public void onSensorChanged(SensorEvent event) {//重写onSensorChanged方法
			float[] values = event.values;//获取温度值所属的values数组
			temperature.setText("温度为：" + values[0]);//显示温度
		}
	};

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent e) {
		switch (keyCode) {
		case 4:
			System.exit(0);
			break;
		}
		return true;
	}

}