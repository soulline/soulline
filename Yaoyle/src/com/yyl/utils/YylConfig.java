package com.yyl.utils;

import java.io.File;

import android.os.Environment;

public class YylConfig {

	public static boolean IS_DEBUG = false;

	// public static final String BASE_URL = "http://115.29.144.246/api/";

//	public static final String BASE_URL = "http://182.92.170.180/api/";

	 public static final String BASE_URL =
	 "http://www.yedianshaiwang.com/api/";

	 public static final String DOMAIN = "www.yedianshaiwang.com";

	// public static final String DOMAIN = "115.29.144.246";

//	public static final String DOMAIN = "182.92.170.180";

	// public static final String SOCKET_HOST = "115.29.144.246";

//	public static final String SOCKET_HOST = "182.92.170.180";

	 public static final String SOCKET_HOST = "www.yedianshaiwang.com";
	public static final int SOCKET_PORT = 8088;

	// uid过期
	public static final int CODE_ERROR_UID = -930;
	// 登录过期
	public static final int CODE_ERROR_LOGIN = 910;
	// 请求过快
	public static final int CODE_ERROR_REQUEST_QUICKY = -920;

	public static final int PATH_KEY = 10151;

	public static final String BASE_CID = "600100";

	public static final String APP_DIR_NAME = "yesw";

	public static final String APP_DIR = Environment
			.getExternalStorageDirectory().toString();
	public static final String PIC_DIR_NAME = "pic";
	public static final String IMAGE_CACHE_FILE = APP_DIR + File.separator
			+ PIC_DIR_NAME + File.separator;

	public static final String LOG_DIR_NAME = "log";
	public static final String LOG_DIR_DIR = APP_DIR + File.separator
			+ LOG_DIR_NAME;

	// crash
	public static final String CRASH_NAME = "crash.txt";
	// debug
	public static final String DEBUG_DATA_NAME = "debug_data.txt";

}
