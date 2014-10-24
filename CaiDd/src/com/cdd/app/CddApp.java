package com.cdd.app;

import java.lang.ref.WeakReference;

import com.cdd.util.DataUtils;
import com.cdd.util.CddConfig;


import android.app.Application;

public class CddApp extends Application {

	private static WeakReference<CddApp> instance;
	
	public boolean isRebuild = false;
	
	@Override
	public void onCreate() {
		super.onCreate();
		initInstance();
	}
	
	private void initInstance() {
		instance = new WeakReference<CddApp>(this);
	}
	
	public static CddApp getInstance() {
		return instance.get();
	}
	
	public String getDomain() {
		String d = DataUtils.getPreferences("domain", CddConfig.BASE_URL);
		return d;
	}
	
	public String getUID() {
		return DataUtils.getPreferences(DataUtils.KEY_UUID, "");
	}
}
