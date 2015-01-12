package com.asag.serial.app;

import java.lang.ref.WeakReference;

import com.asag.serial.mode.AlarmInfo;

import android.app.Application;

public class SerialApp extends Application {

	private static WeakReference<SerialApp> instance;
	
	public boolean isPause = false;
	
	public int oldCheckTime = 0;
	
	public int oldPaikongTime = 0;
	
	public String lastWay = "15";
	
	public boolean isCheckIng = false;
	
	public boolean isSetAlarm = false;
	
	public boolean isAreadyAlarm = false;
	
	public AlarmInfo alarmInfo = new AlarmInfo();
	
	@Override
	public void onCreate() {
		super.onCreate();
		initInstance();
	}
	
	public void initInstance() {
		instance = new WeakReference<SerialApp>(this);
	}
	
	public static SerialApp getInstance() {
		return instance.get();
	}
}
