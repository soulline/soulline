package com.yyl.net;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import org.apache.http.HttpHost;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.params.HttpParams;

import com.yyl.utils.StringUtil;



import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;
import android.util.Log;

/**
 * 
 * 包含网络处理方法的工具类
 * 
 */
public class NetworkUtil {

	private static final String TAG = "YYL";

	public static boolean hasInternet(Context context) {
		ConnectivityManager manager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = manager.getActiveNetworkInfo();
		if (info == null || !info.isConnected()) {
			return false;
		}
		// if (info.isRoaming()) {
		// //漫游
		// return true;
		// }
		return true;
	}

	public static boolean isWiFiActive(Context inContext) {
		Context context = inContext.getApplicationContext();
		ConnectivityManager connectivity = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity != null) {
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null) {
				for (int i = 0; i < info.length; i++) {
					if (info[i].getTypeName().equals("WIFI")
							&& info[i].isConnected()) {
						return true;
					}
				}
			}
		}
		return false;
	}

	public static boolean hasMoreThanOneConnection(Context context) {
		ConnectivityManager manager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (manager == null) {
			return false;
		} else {
			NetworkInfo[] info = manager.getAllNetworkInfo();
			int counter = 0;
			for (int i = 0; i < info.length; i++) {
				if (info[i].isConnected()) {
					counter++;
				}
			}
			if (counter > 1) {
				return true;
			}
		}

		return false;
	}

	/**
	 * Returns whether the network is available
	 */
	public static boolean isNetworkAvailable(Context context) {
		ConnectivityManager connectivity = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity == null) {
			Log.w(TAG, "couldn't get connectivity manager");
		} else {
			NetworkInfo[] infos = connectivity.getAllNetworkInfo();
			if (infos != null) {
				for (int i = 0, count = infos.length; i < count; i++) {
					if (infos[i].getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 * Returns whether the network is roaming
	 */
	public static boolean isNetworkRoaming(Context context) {
		ConnectivityManager connectivity = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity == null) {
			Log.w(TAG, "couldn't get connectivity manager");
		} else {
			NetworkInfo info = connectivity.getActiveNetworkInfo();
			if (info != null
					&& info.getType() == ConnectivityManager.TYPE_MOBILE) {
				TelephonyManager telManager = (TelephonyManager) context
						.getSystemService(Context.TELEPHONY_SERVICE);
				if (telManager != null && telManager.isNetworkRoaming()) {
					Log.v(TAG, "network is roaming");
					return true;
				} else {
					Log.v(TAG, "network is not roaming");
				}
			} else {
				Log.v(TAG, "not using mobile network");
			}
		}
		return false;
	}

	public static boolean isCMWAP(Context context) {
		ConnectivityManager manager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = manager.getActiveNetworkInfo();
		if (info != null && StringUtil.equals(info.getExtraInfo(), "cmwap")) {
			return true;
		}
		return false;
	}

	public static String[] getNetWorkInfo(Context context) {
		ConnectivityManager manager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = manager.getActiveNetworkInfo();
		if (info != null) {
			String[] networkInfo = new String[3];
			networkInfo[0] = (info.getTypeName() == null ? "" : info
					.getTypeName());
			networkInfo[1] = (info.getExtraInfo() == null ? "" : info
					.getExtraInfo());
			networkInfo[2] = (info.getSubtypeName() == null ? "" : info
					.getSubtypeName());

			return networkInfo;
		}

		return null;
	}

	public static String getLocalIpAddress() {
		try {
			for (Enumeration<NetworkInterface> en = NetworkInterface
					.getNetworkInterfaces(); en.hasMoreElements();) {
				NetworkInterface intf = en.nextElement();
				for (Enumeration<InetAddress> enumIpAddr = intf
						.getInetAddresses(); enumIpAddr.hasMoreElements();) {
					InetAddress inetAddress = enumIpAddr.nextElement();
					if (!inetAddress.isLoopbackAddress()) {
						return inetAddress.getHostAddress().toString();
					}
				}
			}
		} catch (SocketException ex) {
			Log.e("getLocalIpAddress", ex.toString());
		}
		return null;
	}

	public static void resetHttpClientWap(Context context, HttpClient httpClient) {
		/*
		 * 移动 cmwap 10.0.0.172 80 联通2G uniwap 10.0.0.172 80 联通3G 3gwap
		 * 10.0.0.172 80 电信 ctwap:cdma 10.0.0.200 80
		 */
		HttpParams params = httpClient.getParams();
		if (isWiFiActive(context)) {
			params.removeParameter(ConnRoutePNames.DEFAULT_PROXY);
		} else {
			Object proxy = params.getParameter(ConnRoutePNames.DEFAULT_PROXY);
			if (proxy == null) {
				ConnectivityManager manager = (ConnectivityManager) context
						.getSystemService(Context.CONNECTIVITY_SERVICE);
				NetworkInfo info = manager.getActiveNetworkInfo();
				String extraInfo = null;
				if (info != null) {
					extraInfo = StringUtil.strip(info.getExtraInfo());
				}
				if (!StringUtil.isEmptyOrWhitespace(extraInfo)) {
					extraInfo = extraInfo.toLowerCase();
					if (extraInfo.equals("cmwap") || extraInfo.equals("uniwap")
							|| extraInfo.equals("3gwap")) {
						params.setParameter(ConnRoutePNames.DEFAULT_PROXY,
								new HttpHost("10.0.0.172", 80, "http"));
					} else if (extraInfo.equals("ctwap:cdma")) {
						params.setParameter(ConnRoutePNames.DEFAULT_PROXY,
								new HttpHost("10.0.0.200", 80, "http"));
					}
				}
			}
		}
	}

}
