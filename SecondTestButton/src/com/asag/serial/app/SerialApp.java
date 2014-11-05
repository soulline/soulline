package com.asag.serial.app;

import java.lang.ref.WeakReference;
import android.app.Application;

public class SerialApp extends Application {

	private static WeakReference<SerialApp> instance;
	
	public boolean isPause = false;
	
	public int oldCheckTime = 0;
	
	public int oldPaikongTime = 0;
	
	public boolean isCheckIng = false;
	
	public boolean isSetAlarm = false;
	
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
