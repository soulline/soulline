package com.yyl.download;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import android.os.Environment;
import android.util.Log;

public class YylDownloadManger {

	public interface DownloadListener {
		public void onChange(int state, int persent);
	}

	private InputStream inputStream;
	private URLConnection connection;
	private OutputStream outputStream;

	private DownloadListener listener;

	public YylDownloadManger(DownloadListener listener) {
		this.listener = listener;
	}

	public void onDownload(String urlString) {
		try {
			URL url = new URL(urlString);
			connection = url.openConnection();
			if (connection.getReadTimeout() == 5) {
				Log.i("YYL", "当前网络有问题");
				return;
			}
			inputStream = connection.getInputStream();

		} catch (MalformedURLException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		String savePAth = Environment.getExternalStorageDirectory() + "/yyl";
		File file1 = new File(savePAth);
		if (!file1.exists()) {
			file1.mkdir();
		}
		String savePathString = Environment.getExternalStorageDirectory()
				+ "/yyl/" + "yaoyl.apk";
		File file = new File(savePathString);
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		/*
		 * 向SD卡中写入文件
		 */
		try {
			outputStream = new FileOutputStream(file, false);
			byte[] buffer = new byte[1024];
			long fileLength = connection.getContentLength();
			long downedFileLength = 0;
			if (listener != null) {
				listener.onChange(0, 0);
			}
			int readSize = 0;
			while ((readSize = inputStream.read(buffer)) != -1) {
				outputStream.write(buffer, 0, readSize);
				downedFileLength += readSize;
				Log.i("YYL", "download:" + downedFileLength + " total:"
						+ fileLength);
				int persent = (int) (downedFileLength * 100L / fileLength);
				if (listener != null) {
					listener.onChange(1, persent);
				}
			}
			if (listener != null) {
				listener.onChange(2, 100);
			}
		} catch (FileNotFoundException e) {
			if (listener != null) {
				listener.onChange(3, 0);
			}
			e.printStackTrace();
		} catch (IOException e) {
			if (listener != null) {
				listener.onChange(3, 0);
			}
			e.printStackTrace();
		}
		try {
			outputStream.close();
			inputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
