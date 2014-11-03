package com.cdd.util;

import java.io.File;

import android.os.Environment;

public class CddConfig {

	public static final boolean IS_DEBUG = true;
	
	public static final String BASE_URL = "http://123.57.45.145:8080/api/";
	
	public static final String DOMAIN = "123.57.45.145:8080";
	
	public static final int LOGIN_PATH_KEY = 10178;
	
	public static final int CODE_ERROR_LOGIN = 910;
	
	public static final String APP_DIR = Environment
			.getExternalStorageDirectory().toString();
	public static final String PIC_DIR_NAME = "cddpic";
	public static final String IMAGE_CACHE_FILE = APP_DIR + File.separator
			+ PIC_DIR_NAME + File.separator;
}
