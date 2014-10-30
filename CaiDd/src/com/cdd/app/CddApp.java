package com.cdd.app;

import java.lang.ref.WeakReference;

import com.cdd.mode.AccountInfo;
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
	
	public AccountInfo getAccount() {
		AccountInfo account = new AccountInfo();
		account.availableScore = DataUtils.getPreferences("availableScore", "");
		account.description = DataUtils.getPreferences("description", "");
		account.deviceFlag = DataUtils.getPreferences("deviceFlag", "");
		account.isAdmin = DataUtils.getPreferences("isAdmin", "");
		account.levelId = DataUtils.getPreferences("levelId", "");
		account.loginId = DataUtils.getPreferences("loginId", "");
		account.name = DataUtils.getPreferences("name", "");
		account.scoreCeiling = DataUtils.getPreferences("scoreCeiling", "");
		account.sex = DataUtils.getPreferences("sex", "");
		account.signTime = DataUtils.getPreferences("signTime", "");
		account.status = DataUtils.getPreferences("status", "");
		return account;
	}
	
	public void setAccount(AccountInfo account) {
		DataUtils.putPreferences("availableScore", account.availableScore);
		DataUtils.putPreferences("description", account.description);
		DataUtils.putPreferences("deviceFlag", account.deviceFlag);
		DataUtils.putPreferences("isAdmin", account.isAdmin);
		DataUtils.putPreferences("levelId", account.levelId);
		DataUtils.putPreferences("loginId", account.loginId);
		DataUtils.putPreferences("name", account.name);
		DataUtils.putPreferences("scoreCeiling", account.scoreCeiling);
		DataUtils.putPreferences("sex", account.sex);
		DataUtils.putPreferences("signTime", account.signTime);
		DataUtils.putPreferences("status", account.status);
		setLoginState(true);
	}
	
	public void clearLogin() {
		DataUtils.putPreferences("availableScore", "");
		DataUtils.putPreferences("description", "");
		DataUtils.putPreferences("deviceFlag", "");
		DataUtils.putPreferences("isAdmin", "");
		DataUtils.putPreferences("levelId", "");
		DataUtils.putPreferences("loginId", "");
		DataUtils.putPreferences("name", "");
		DataUtils.putPreferences("scoreCeiling", "");
		DataUtils.putPreferences("sex", "");
		DataUtils.putPreferences("signTime", "");
		DataUtils.putPreferences("status", "");
		setLoginState(false);
	}
	
	public void setLoginState(boolean isLogin) {
		DataUtils.putPreferences(DataUtils.KEY_ISLOGIN, isLogin);
	}
	
	public boolean isLogin() {
		return DataUtils.getPreferences(DataUtils.KEY_ISLOGIN, false);
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
