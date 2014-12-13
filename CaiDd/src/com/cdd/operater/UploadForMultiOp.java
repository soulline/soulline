package com.cdd.operater;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import android.util.Log;

import com.cdd.app.CddApp;
import com.cdd.util.CddConfig;
import com.cdd.util.CryptAES;

public class UploadForMultiOp {

	public interface OnUploadListener {
		public void onError(String error);

		public void onSuccess(String result);
	}

	public CddApp app = CddApp.getInstance();

	public String action = "";

	public UploadForMultiOp() {
		initAction();
	}

	public String initBaseInfo() {
		StringBuffer sb = new StringBuffer();
		sb.append("?");
		StringBuffer bbi = new StringBuffer();
		bbi.append("uid=" + app.getUID());
		sb.append(bbi.toString());
		return sb.toString();
	}

	public void initAction() {
		action = "forum/photoUpload.do";
	}

	public void uploadFile(ArrayList<File> imgPaths, OnUploadListener listener) {
		String actionUrl = app.getDomain() + action + initBaseInfo();
		if (CddConfig.IS_DEBUG) {
			Log.i("CDD", actionUrl);
		}
		try {
			String BOUNDARY = "------------------------7dc3482080a10"; // 定义数据分隔线
			URL url = new URL(actionUrl);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();

			// 发送POST请求必须设置如下两行
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setUseCaches(false);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("user-agent",
					"Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; Win64; x64; Trident/5.0)");
			conn.setRequestProperty("Charsert", "UTF-8");
			conn.setRequestProperty("Content-Type",
					"multipart/form-data; boundary=" + BOUNDARY);

			OutputStream out = new DataOutputStream(conn.getOutputStream());
			byte[] end_data = ("\r\n--" + BOUNDARY + "--\r\n").getBytes();// 定义最后数据分隔线
			int leng = imgPaths.size();
			for (int i = 0; i < leng; i++) {
				File file = imgPaths.get(i);
				StringBuilder sb = new StringBuilder();
				sb.append("--");
				sb.append(BOUNDARY);
				sb.append("\r\n");
				sb.append("Content-Disposition: form-data;name=\"files\";filename=\""
						+ file.getName() + "\"\r\n");
				sb.append("Content-Type:image/png\r\n\r\n");

				byte[] data = sb.toString().getBytes();
				out.write(data);
				DataInputStream in = new DataInputStream(new FileInputStream(
						file));
				int bytes = 0;
				byte[] bufferOut = new byte[1024];
				while ((bytes = in.read(bufferOut)) != -1) {
					out.write(bufferOut, 0, bytes);
				}
				out.write("\r\n".getBytes()); // 多个文件时，二个文件之间加入这个
				in.close();
			}
			out.write(end_data);
			out.flush();
			out.close();
			/* 取得Response内容 */
			InputStream is = conn.getInputStream();
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			byte[] data = toByteArray(is, baos);
			String result = new String(data);
			String resultNew = CryptAES.getInstance().onDecrypt(result);
			if (CddConfig.IS_DEBUG) {
				Log.i("CDD", result);
				Log.i("CDD", resultNew);
			}
			listener.onSuccess(resultNew);
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
