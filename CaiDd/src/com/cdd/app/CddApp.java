package com.cdd.app;

import java.lang.ref.WeakReference;

import com.cdd.util.DataUtils;
import com.cdd.util.CddConfig;


import android.app.Application;
import android.util.SparseArray;

public class CddApp extends Application {

	private static WeakReference<CddApp> instance;
	
	public SparseArray<SparseArray<BaseActivityCloseListener>> closeMap;
	
	public boolean isRebuild = false;
	
	@Override
	public void onCreate() {
		super.onCreate();
		initInstance();
	}
	
	private void initInstance() {
		closeMap = new SparseArray<SparseArray<BaseActivityCloseListener>>();
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
	
	public synchronized void putClosePath(int key,
			BaseActivityCloseListener listener) {
		if (closeMap.indexOfKey(key) < 0) {
			SparseArray<BaseActivityCloseListener> sa = new SparseArray<BaseActivityCloseListener>();
			sa.put(sa.size(), listener);
			closeMap.put(key, sa);
		} else {
			SparseArray<BaseActivityCloseListener> sa = closeMap.get(key);
			if (sa.indexOfValue(listener) < 0) {
				sa.put(sa.size(), listener);
			}
		}
	}

	public synchronized void popClosePath(boolean finish, int key) {
		if (closeMap.indexOfKey(key) >= 0) {
			SparseArray<BaseActivityCloseListener> sa = closeMap.get(key);
			if (finish) {
				int s = sa.size();
				for (int i = 0; i < s; i++) {
					BaseActivityCloseListener bl = sa.get(sa.keyAt(i));
					bl.onFinish();
				}
			}
			sa.clear();
			closeMap.remove(key);
		}
	}
}
