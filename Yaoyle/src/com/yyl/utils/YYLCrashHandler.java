package com.yyl.utils;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.Thread.UncaughtExceptionHandler;


import android.content.Context;
import android.util.Log;


public class YYLCrashHandler implements UncaughtExceptionHandler{

	
	private Thread.UncaughtExceptionHandler mDefaultHandler;
	private static YYLCrashHandler INSTANCE;

	private YYLCrashHandler() {
	}

	public static YYLCrashHandler getInstance() {
		if (INSTANCE == null)
			INSTANCE = new YYLCrashHandler();
		return INSTANCE;
	}

	public void init(Context ctx) {
		mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
		Thread.setDefaultUncaughtExceptionHandler(this);
	}
	
	@Override
	public void uncaughtException(Thread thread, Throwable ex) {
		String msg = getCrashInfoToFile(ex);
		if (YylConfig.IS_DEBUG) {
			Log.i("PDA", msg);
		}
		RecordUtil.recordCrashLog(msg);
		if (mDefaultHandler != null) {
			mDefaultHandler.uncaughtException(thread, ex);
		}
	}

	public String getCrashInfoToFile(Throwable ex) {
		if (ex == null) {
			return "";
		}
		StringWriter info = null;
		PrintWriter printWriter = null;
		try {
			info = new StringWriter();
			printWriter = new PrintWriter(info);
			ex.printStackTrace(printWriter);
			Throwable cause = ex.getCause();
			while (cause != null) {
				cause.printStackTrace(printWriter);
				cause = cause.getCause();
			}
			return info.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		} finally {
			try {
				if (printWriter != null) {
					printWriter.close();
				}
				if (info != null) {
					info.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
}
