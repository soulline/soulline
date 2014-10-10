package com.yyl.sensor;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Vibrator;

public class YylSensorManager {

	private Context context;

	private SensorManager sensorManager;
	private Vibrator vibrator;
	// 手机上一个位置时重力感应坐标
	private float lastX;
	private float lastY;
	private float lastZ;
	// 上次检测时间
	private long lastUpdateTime;
	
	private long lastShakeTime; //上一次摇骰子时间

	// 速度阈值，当摇晃速度达到这值后产生作用
	private static final int SPEED_SHRESHOLD = 300;
	// 两次检测的时间间隔
	private static final int UPTATE_INTERVAL_TIME = 70;
	
	//摇骰子响应时间间隔
	private static final int SHAKE_INTERVAL_TIME = 2000;

	private OnShakeListener listener;

	public YylSensorManager(Context context, OnShakeListener listener) {
		this.context = context;
		this.listener = listener;
	}

	public void initSensor() {
		sensorManager = (SensorManager) context
				.getSystemService(Context.SENSOR_SERVICE);
		vibrator = (Vibrator) context
				.getSystemService(Context.VIBRATOR_SERVICE);
		registerSensor();
	}

	public void registerSensor() {
		if (sensorManager != null) {
			sensorManager.registerListener(sensorEventListener,
					sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
					SensorManager.SENSOR_DELAY_NORMAL);
		}
	}

	private SensorEventListener sensorEventListener = new SensorEventListener() {

		@Override
		public void onSensorChanged(SensorEvent event) {
			// 现在检测时间
			long currentUpdateTime = System.currentTimeMillis();
			// 两次检测的时间间隔
			long timeInterval = currentUpdateTime - lastUpdateTime;
			// 判断是否达到了检测时间间隔
			if (timeInterval < UPTATE_INTERVAL_TIME)
				return;
			// 现在的时间变成last时间
			lastUpdateTime = currentUpdateTime;
			// 获得x,y,z坐标
			float x = event.values[0];
			float y = event.values[1];
			float z = event.values[2];
			// 获得x,y,z的变化值
			float deltaX = x - lastX;
			float deltaY = y - lastY;
			float deltaZ = z - lastZ;
			// 将现在的坐标变成last坐标
			lastX = x;
			lastY = y;
			lastZ = z;
			// sqrt 返回最近的双近似的平方根
			double speed = Math.sqrt(deltaX * deltaX + deltaY * deltaY + deltaZ
					* deltaZ)
					/ timeInterval * 10000;
			if (speed >= SPEED_SHRESHOLD) {
				if (SHAKE_INTERVAL_TIME > (currentUpdateTime - lastShakeTime)) {
					return;
				}
				lastShakeTime = currentUpdateTime;
				onShake();
				if (listener != null) {
					listener.onShake();
				}
			}
		}

		@Override
		public void onAccuracyChanged(Sensor sensor, int accuracy) {

		}
	};

	public void unRegistSensor() {
		if (sensorManager != null) {
			sensorManager.unregisterListener(sensorEventListener);
		}
	}

	private void onShake() {
		vibrator.vibrate(new long[] { 500, 200, 500, 200 }, -1);
	}
}
