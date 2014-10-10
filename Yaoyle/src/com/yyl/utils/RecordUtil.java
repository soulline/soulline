package com.yyl.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.util.Date;

import android.text.TextUtils;

public class RecordUtil {

	/**
	 * 记录客户端异常log
	 * 
	 * @param data
	 */
	public static void recordCrashLog(final String data) {
		if (!YylConfig.IS_DEBUG) {
			return;
		}
		
		if (TextUtils.isEmpty(data)) {
			return;
		}
		StringBuffer sb = new StringBuffer();
		sb.append(System.getProperty("line.separator"));
		sb.append(System.getProperty("line.separator"));
		for (int i = 0; i < 10; i++) {
			sb.append(">>");
		}
		sb.append(System.getProperty("line.separator"));
		sb.append(DateFormat.getInstance().format(new Date()));
		sb.append(System.getProperty("line.separator"));
		for (int i = 0; i < 10; i++) {
			sb.append("--");
		}
		sb.append(System.getProperty("line.separator"));
		sb.append(data);
		sb.append(System.getProperty("line.separator"));
		sb.append(System.getProperty("line.separator"));
		for (int i = 0; i < 10; i++) {
			sb.append("<<");
		}

		sb.append(System.getProperty("line.separator"));
		sb.append(System.getProperty("line.separator"));
		FileOutputStream fos = null;
		OutputStreamWriter writer = null;
		try {
			File dir_ = new File(YylConfig.LOG_DIR_DIR);
			if (!dir_.exists() || !dir_.isDirectory()) {
				dir_.mkdirs();
			}

			File file = new File(dir_, YylConfig.CRASH_NAME);
			if (!file.exists()) {
				file.createNewFile();
			}
			fos = new FileOutputStream(file, true);
			writer = new OutputStreamWriter(fos, "utf-8");
			writer.write(sb.toString());
			writer.flush();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (fos != null) {
					fos.close();
				}
				if (writer != null) {
					writer.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static void recordDataLog(String query, String url, String response) {
		if (!YylConfig.IS_DEBUG) {
			return;
		}
		StringBuffer sb = new StringBuffer();
		sb.append(System.getProperty("line.separator"));
		sb.append(System.getProperty("line.separator"));
		for (int i = 0; i < 10; i++) {
			sb.append(">>");
		}
		sb.append(System.getProperty("line.separator"));
		sb.append(DateFormat.getInstance().format(new Date()));
		sb.append(System.getProperty("line.separator"));
		for (int i = 0; i < 10; i++) {
			sb.append("--");
		}
		sb.append(System.getProperty("line.separator"));
		sb.append("query = " + query);
		sb.append(System.getProperty("line.separator"));
		sb.append("url = " + url);
		sb.append(System.getProperty("line.separator"));
		sb.append("response = " + response);
		sb.append(System.getProperty("line.separator"));
		for (int i = 0; i < 10; i++) {
			sb.append("<<");
		}

		sb.append(System.getProperty("line.separator"));
		sb.append(System.getProperty("line.separator"));
		FileOutputStream fos = null;
		OutputStreamWriter writer = null;
		try {
			File dir_ = new File(YylConfig.LOG_DIR_DIR);
			if (!dir_.exists() || !dir_.isDirectory()) {
				dir_.mkdirs();
			}

			File file = new File(dir_, YylConfig.DEBUG_DATA_NAME);
			if (!file.exists()) {
				file.createNewFile();
			}
			fos = new FileOutputStream(file, true);
			writer = new OutputStreamWriter(fos, "utf-8");
			writer.write(sb.toString());
			writer.flush();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (fos != null) {
					fos.close();
				}
				if (writer != null) {
					writer.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
