package com.yyl.application;

import java.lang.ref.WeakReference;

import com.yyl.mode.AccountInfo;
import com.yyl.operater.BaseOperater;
import com.yyl.utils.BaseActivityCloseListener;
import com.yyl.utils.DataUtils;
import com.yyl.utils.YylConfig;


import android.app.Application;
import android.util.SparseArray;

public class YylApp extends Application {

	private static WeakReference<YylApp> instance;
	
	public BaseOperater operate;
	
	public SparseArray<SparseArray<BaseActivityCloseListener>> closeMap;
	
	public boolean isRebuild = false;

	@Override
	public void onCreate() {
		super.onCreate();
		initInstance();
	}

	private void initInstance() {
		closeMap = new SparseArray<SparseArray<BaseActivityCloseListener>>();
		instance = new WeakReference<YylApp>(this);
	}
	
	public static YylApp getInstance() {
		return instance.get();
	}
	
	public void setLoginState(boolean isLogin) {
		DataUtils.putPreferences("isLogin", isLogin);
	}
	
	public boolean getLoginState() {
		return DataUtils.getPreferences("isLogin", false);
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

	public void setAccount(AccountInfo account) {
		DataUtils.putPreferences("mobile", account.mobile);
		DataUtils.putPreferences("name", account.name);
		DataUtils.putPreferences("sex", account.sex);
		DataUtils.putPreferences("photoUrl", account.photoUrl);
		DataUtils.putPreferences("accountId", account.accountId);
		setLoginState(true);
	}
	
	public void clearAccountInfo() {
		DataUtils.putPreferences("mobile", "");
		DataUtils.putPreferences("name", "");
		DataUtils.putPreferences("sex", "");
		DataUtils.putPreferences("photoUrl", "");
		DataUtils.putPreferences("accountId", "");
		setLoginState(false);
	}

	public AccountInfo getAccount() {
		AccountInfo account = new AccountInfo();
		account.mobile = DataUtils.getPreferences("mobile", "");
		account.name = DataUtils.getPreferences("name", "");
		account.sex = DataUtils.getPreferences("sex", "");
		account.accountId = DataUtils.getPreferences("accountId", "");
		account.photoUrl = DataUtils.getPreferences("photoUrl", "");
		return account;
	}
	
	public String getDomain() {
		String d = DataUtils.getPreferences("domain", YylConfig.BASE_URL);
		return d;
	}

	public String getUID() {
		return DataUtils.getPreferences(DataUtils.KEY_UUID, "");
	}
	
	public String getGameId() {
		return DataUtils.getPreferences(DataUtils.KEY_GAME_ID, "");
	}
}
