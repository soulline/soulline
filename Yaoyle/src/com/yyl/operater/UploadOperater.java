package com.yyl.operater;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


import android.util.Log;

import com.yyl.application.YylApp;
import com.yyl.utils.CryptAES;
import com.yyl.utils.YylConfig;

public class UploadOperater {
	
	public interface OnUploadListener {
		public void onError(String error);
		
		public void onSuccess(String result);
	}
	
	public YylApp app = YylApp.getInstance();

	public UploadOperater() {
		
	}

	public String initBaseInfo() {
		StringBuffer sb = new StringBuffer();
		sb.append("?");
		StringBuffer bbi = new StringBuffer();
		bbi.append("&uid=" + app.getUID());
		sb.append(bbi.toString());
		return sb.toString();
	}

	public void uploadFile(String action, String imgPath, OnUploadListener listener) {
		String actionUrl = app.getDomain() + action + initBaseInfo();
		String end = "\r\n";
		String twoHyphens = "--";
		String boundary = "*****";
		try {
			URL url = new URL(actionUrl);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			/* 允许Input、Output，不使用Cache */
			con.setDoInput(true);
			con.setDoOutput(true);
			con.setUseCaches(false);
			/* 设置传送的method=POST */
			con.setRequestMethod("POST");
			/* setRequestProperty */
			con.setRequestProperty("Connection", "Keep-Alive");
			con.setRequestProperty("Charset", "UTF-8");
			con.setRequestProperty("Content-Type",
					"multipart/form-data;boundary=" + boundary);
			/* 设置DataOutputStream */
			DataOutputStream ds = new DataOutputStream(con.getOutputStream());
			ds.writeBytes(twoHyphens + boundary + end);
			ds.writeBytes("Content-Disposition: form-data; "
					+ "name=\"file\";filename=\"" + "upload.jpg" + "\"" + end);
			ds.writeBytes(end);
			/* 取得文件的FileInputStream */
			FileInputStream fStream = new FileInputStream(imgPath);
			/* 设置每次写入1024bytes */
			int bufferSize = 1024;
			byte[] buffer = new byte[bufferSize];
			int length = -1;
			/* 从文件读取数据至缓冲区 */
			while ((length = fStream.read(buffer)) != -1) {
				/* 将资料写入DataOutputStream中 */
				ds.write(buffer, 0, length);
			}
			ds.writeBytes(end);
			ds.writeBytes(twoHyphens + boundary + twoHyphens + end);
			/* close streams */
			fStream.close();
			ds.flush();
			/* 取得Response内容 */
			InputStream is = con.getInputStream();
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			byte[] data = toByteArray(is, baos);
			String result = new String(data);
			String resultNew = CryptAES.getInstance().onDecrypt(result);
			listener.onSuccess(resultNew);
			/* 关闭DataOutputStream */
			ds.close();
			if (YylConfig.IS_DEBUG) {
				Log.i("YYL", resultNew);
			}
		} catch (Exception e) {
			listener.onError("上传失败");
		}
	}
	
	private byte[] toByteArray(InputStream is, ByteArrayOutputStream baos)
			throws IOException {
		if (is == null) {
			return null;
		}
		byte[] buffer = new byte[4096];
		int n = 0;
		while (-1 != (n = is.read(buffer))) {
			baos.write(buffer, 0, n);
		}
		return baos.toByteArray();
	}
}
