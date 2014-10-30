package com.cdd.util;

import com.cdd.app.CddApp;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * 用SharedPreferences存储
 * 
 * @author ztf
 * 
 */

public class DataUtils {
	

	public static final String KEY_UUID = "key_uuid";
	public static final String KEY_TIMESTAMP = "key_timestamp";
	public static final String KEY_GAME_ID = "key_game_id";
	public static final String KEY_SOUND_ONOFF = "key_sound_onoff";
	
	public static final String KEY_COIN_VALUE = "key_coin_value";
	
	public static final String KEY_IS_REBUID = "key_is_rebuild";
	
	public static final String KEY_ISLOGIN = "key_islogin";
	
	
	public static SharedPreferences sharedPreferences = CddApp.getInstance()
			.getSharedPreferences(CddApp.getInstance().getPackageName(), 0);

	public static void removePreferences(String key) {
		if (key == null) {
			return;
		}

		Editor editor = DataUtils.sharedPreferences.edit();
		editor.remove(key);
		editor.commit();
	}

	public static synchronized void putPreferences(String key, String value) {
		if (key == null || value == null) {
			return;
		}
		Editor editor = DataUtils.sharedPreferences.edit();
		editor.putString(key, value);
		editor.commit();
	}

	public static void putPreferences(String key, boolean value) {
		Editor editor = DataUtils.sharedPreferences.edit();
		editor.putBoolean(key, value);
		editor.commit();
	}

	public static void putPreferences(String key, int value) {
		Editor editor = DataUtils.sharedPreferences.edit();
		editor.putInt(key, value);
		editor.commit();
	}

	public static void putPreferences(String key, long value) {
		Editor editor = DataUtils.sharedPreferences.edit();
		editor.putLong(key, value);
		editor.commit();
	}

	public static void putPreferences(String key, float value) {
		Editor editor = DataUtils.sharedPreferences.edit();
		editor.putFloat(key, value);
		editor.commit();
	}

	public static boolean getPreferences(String key, boolean defaultValue) {
		try {
			return DataUtils.sharedPreferences.getBoolean(key, defaultValue);
		} catch (Exception e) {
			return defaultValue;
		}
	}

	public static int getPreferences(String key, int defValue) {
		try {
			return DataUtils.sharedPreferences.getInt(key, defValue);
		} catch (Exception e) {
			return defValue;
		}
	}

	public static String getPreferences(String key, String defValue) {
		try {
			return DataUtils.sharedPreferences.getString(key, defValue);
		} catch (Exception e) {
			return defValue;
		}
	}

	public static long getPreferences(String key, long defValue) {
		try {
			return DataUtils.sharedPreferences.getLong(key, defValue);
		} catch (Exception e) {
			return defValue;
		}
	}

	public static float getPreferences(String key, float defValue) {
		try {
			return DataUtils.sharedPreferences.getFloat(key, defValue);
		} catch (Exception e) {
			return defValue;
		}
	}

}
