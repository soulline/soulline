package com.yyl.socket;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.URLEncoder;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.StrictMode;
import android.util.Log;

import com.yyl.R;
import com.yyl.application.YylApp;
import com.yyl.utils.CryptAES;
import com.yyl.utils.GameAction;
import com.yyl.utils.YylConfig;

public class SocketManager {
	private Socket socket = null;
	private BufferedReader in = null;
	private PrintWriter out = null;
	private SocketRequestListener listener = null;
	private boolean isOpen = false;
	
	private boolean isEncrypt = true;
	
	public SocketManager() {
		setStrictMode();
	}

	public void onRequestListener(SocketRequestListener listener) {
		this.listener = listener;
		startSocket();
	}

	public boolean isSocketOpen() {
		if (socket == null || !isOpen) {
			return false;
		}
		return true;
	}

	public void startSocket() {
		try {
			socket = new Socket(YylConfig.SOCKET_HOST, YylConfig.SOCKET_PORT);
			in = new BufferedReader(new InputStreamReader(
					socket.getInputStream()));
			out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(
					socket.getOutputStream())), true);
			isOpen = true;
			readThread.start();
			try {
				timer.schedule(task, 1000, 60 * 1000);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (IOException ex) {
			ex.printStackTrace();
			if (listener != null) {
				listener.onError(YylApp.getInstance().getString(R.string.connect_init_exception));
			}
		}
	}

	public void setStrictMode() {
		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
					.permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}
	}

	Timer timer = new Timer();
	
	TimerTask task = new TimerTask() {
		
		@Override
		public void run() {
			sendCMD(getPulseJSON());
		}
	};
	
	private Thread readThread = new Thread(new Runnable() {
		
		@Override
		public void run() {
			try {
				while (isOpen) {
					if (socket.isConnected()) {
						String content = "";
						if (!socket.isInputShutdown()) {
							if ((content = in.readLine()) != null) {
								String contentN = isEncrypt ? CryptAES.getInstance().onDecrypt(content) : content;
								if (YylConfig.IS_DEBUG) {
									Log.i("YYL", contentN);
								}
								if (listener != null) {
									JSONObject result = new JSONObject(contentN);
									listener.onSuccess(result);
									if (YylConfig.IS_DEBUG && result.optInt("actionCode") == GameAction.SOCKET_TEST_PULSE) {
										Log.i("YYL", result.toString());
									}
								}
							}
						} else if (listener != null) {
							isOpen = false;
							listener.onError(YylApp.getInstance().getString(R.string.connect_failed));
						}
					} else if (listener != null) {
						isOpen = false;
						listener.onError(YylApp.getInstance().getString(R.string.connect_failed));
					}
				}
				Thread.sleep(1000);
			} catch (Exception e) {
				e.printStackTrace();
				if (listener != null && isOpen) {
					isOpen = false;
					listener.onError(YylApp.getInstance().getString(R.string.connect_exception));
				}
			}
		}
	});

	public void onDestory() {
		isOpen = false;
		if (YylConfig.IS_DEBUG) {
			Log.i("YYL", "socket ondestory");
		}
		if (readThread.isAlive()) {
			try {
				readThread.interrupt();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (socket != null) {
			try {
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (out != null) {
			try {
				out.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (in != null) {
			try {
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		timer.cancel();
	}
	
	public JSONObject getPulseJSON() {
		JSONObject pulseObj = new JSONObject();
		try {
			pulseObj.put("actionCode", GameAction.SOCKET_TEST_PULSE);
			pulseObj.put("msg", "yyl_test");
			pulseObj.put("uid", YylApp.getInstance().getUID());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		if (YylConfig.IS_DEBUG) {
			Log.i("YYL", pulseObj.toString());
		}
		return pulseObj;
	}

	public void sendCMD(JSONObject obj) {
		if (YylConfig.IS_DEBUG) {
			Log.i("YYL", obj.toString());
		}
		if (!JSONObject.NULL.equals(obj) && socket.isConnected()
				&& !socket.isOutputShutdown()) {
			String request = isEncrypt ? CryptAES.getInstance().onEncrypt(obj.toString()) : obj.toString();
			try {
				request = URLEncoder.encode(request, "UTF-8");
			} catch (UnsupportedEncodingException e1) {
				e1.printStackTrace();
			}
			try {
				out.println(request);
				out.flush();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
