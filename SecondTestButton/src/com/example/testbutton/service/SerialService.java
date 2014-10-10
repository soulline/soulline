package com.example.testbutton.service;

import java.util.Timer;
import java.util.TimerTask;

import com.example.testbutton.app.SerialApp;
import com.example.testbutton.mode.CutDownEntry;
import com.example.testbutton.mode.RightDataEntry;
import com.example.testbutton.utils.CMDCode;
import com.example.testbutton.utils.SerialBroadCode;
import com.example.testbutton.utils.SerialPortManager;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

public class SerialService extends Service {

	private SerialPortManager serialManager;

	private LocalBroadcastManager lbm = null;

	private int checkMunite = 0;

	private int paikongMinute = 0;
	
	private Timer timer = null;

	private TimerTask task = null;

	private SerialApp app = SerialApp.getInstance();

	private int count = 0;

	@Override
	public void onCreate() {
		super.onCreate();
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	private void initSerialPort() {
		serialManager = new SerialPortManager(
				new SerialPortManager.SerialCallBack() {

					@Override
					public void onCallBack(final String message) {
						handler.post(new Runnable() {

							@Override
							public void run() {
								if (message.startsWith(CMDCode.PASSWAY_DATA
										.replaceAll(" ", ""))) {
									parsePassway(message);
								} else if (message
										.startsWith(CMDCode.RECEIVE_CO2
												.replaceAll(" ", ""))) {
									parseCO2(message);
								} else if (message
										.startsWith(CMDCode.RECEIVE_O2
												.replaceAll(" ", ""))) {
									parseO2(message);
								}
							}
						});

					}
				});
	}

	private void parseCO2(String data) {
		data = data.substring(6);
		data = data.replaceAll("FF", "");
		String co2Str = getAsiicForHex(data) + "";
		Intent intent = new Intent(SerialBroadCode.ACTION_CO2_RECEIVED);
		intent.putExtra("co2_data", co2Str);
		lbm.sendBroadcast(intent);
	}

	private void parseO2(String data) {
		data = data.substring(6);
		data = data.replaceAll("FF", "");
		int o2Int = getAsiicForHex(data);
		float o2i = ((float) o2Int) / 10L;
		String o2Str = o2i + "";
		Intent intent = new Intent(SerialBroadCode.ACTION_O2_RECEIVED);
		intent.putExtra("o2_data", o2Str);
		lbm.sendBroadcast(intent);
	}

	private int getAsiicForHex(String source) {
		String str = "";
		int code = 0;
		try {
			code = Integer.parseInt(source, 16);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return code;
	}

	private void startTimer() {
		count = 0;
		final int halfPaikong = paikongMinute / 2;
		final int halfCheck = checkMunite / 2;
		if (timer == null) {
			timer = new Timer();
		}
		if (task == null) {
			task = new TimerTask() {
				@Override
				public void run() {
					count++;
					if (count > 6) {
						count = 0;
						if (paikongMinute > 0) {
							paikongMinute--;
							if (paikongMinute == halfPaikong) {
								serialManager
										.sendMsg(CMDCode.PAIKONG_HALF_TIME);
							} else if (paikongMinute == 0) {
								serialManager.sendMsg(CMDCode.PAIKONG_END_TIME);
							}
							CutDownEntry entry = new CutDownEntry();
							entry.type = 2;
							entry.time = paikongMinute;
							sendCutDownUpdate(entry);
						} else if (checkMunite > 0) {
							checkMunite--;
							if (checkMunite == halfCheck) {
								serialManager.sendMsg(CMDCode.CHECK_TIME_HALF);
							} else if (checkMunite == 0) {
								serialManager.sendMsg(CMDCode.CHECK_TIME_END);
								handler.postDelayed(new Runnable() {
									
									@Override
									public void run() {
										serialManager.sendMsg(CMDCode.CHECK_TIME_END);
									}
								}, 2000);
							}
							CutDownEntry entry = new CutDownEntry();
							entry.type = 1;
							entry.time = checkMunite;
							sendCutDownUpdate(entry);
							if (paikongMinute == 0 && checkMunite == 0) {
								Message msg = new Message();
								msg.what = 1;
								handler.sendMessage(msg);
							}
						}
					}
					do {
						try {
							Log.i("Serial", "sleep(1000)...");
							Thread.sleep(1000);
						} catch (InterruptedException e) {
						}
					} while (app.isPause);
				}
			};
		}
		if (timer != null && task != null) {
			timer.scheduleAtFixedRate(task, 0, 1000);
		}

	}

	private void stopTimer() {

		if (timer != null) {
			timer.cancel();
			timer = null;
		}

		if (task != null) {
			task.cancel();
			task = null;
		}

	}

	private String getAsiicByInt(String source) {
		String str = "";
		int code = 0;
		try {
			code = Integer.parseInt(source, 16);
		} catch (Exception e) {
			e.printStackTrace();
		}
		char c = (char) code;
		str = c + "";
		return str;
	}

	private void parsePassway(String data) {
		data = data.substring(6);
		RightDataEntry dataEntry = new RightDataEntry();
		dataEntry.number = getAsiicForHex(data.substring(0, 2)) + "";
		data = data.substring(2);
		String[] array = data.split("FF");
		dataEntry.co2 = subDataHex(array[0]);
		dataEntry.wendu = subDataHex(array[1]);
		dataEntry.shidu = subDataHex(array[2]);
		if (!dataEntry.number.equals("15")) {
			checkMunite = app.oldCheckTime;
			paikongMinute = app.oldPaikongTime;
			CutDownEntry cut1 = new CutDownEntry();
			cut1.type = 1;
			cut1.time = checkMunite;
			CutDownEntry cut2 = new CutDownEntry();
			cut2.type = 2;
			cut2.time = paikongMinute;
			sendCutDownUpdate(cut1);
			sendCutDownUpdate(cut2);
			stopTimer();
			startTimer();
		} else {
			stopTimer();
			app.oldCheckTime = 0;
			app.oldPaikongTime = 0;
		}
		Intent intent = new Intent(SerialBroadCode.ACTION_PASSWAY_DATA);
		intent.putExtra("right_data", dataEntry);
		lbm.sendBroadcast(intent);
	}

	private String subDataHex(String str) {
		StringBuilder sb = new StringBuilder();
		int n = 0;
		for (int i = 0; i < str.length(); i += 2) {
			n = i;
			String data = str.substring(n, n + 2);
			data = getAsiicByInt(data);
			sb.append(data);
		}
		return sb.toString();
	}

	private String getIntegerStr(String message) {
		String str = Integer.parseInt(message, 16) + "";
		return str;
	}

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 1:
				stopTimer();
				break;

			default:
				break;
			}
		}

	};

	private void initReceiverFilter() {
		IntentFilter filter = new IntentFilter(
				SerialBroadCode.ACTION_SEND_MESSAGE);
		filter.addAction(SerialBroadCode.ACTION_CHECK_MINUTE);
		filter.addAction(SerialBroadCode.ACTION_PAIKONG_MINUTE);
		lbm.registerReceiver(serialPortReceiver, filter);
	}

	private void continueCutDown() {
		doCutDown(2, paikongMinute);
		handler.postDelayed(new Runnable() {

			@Override
			public void run() {
				doCutDown(1, checkMunite);
			}
		}, paikongMinute * 60 * 1000L);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (serialManager != null) {
			serialManager.closeSerialPort();
		}
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		initSerialPort();
		lbm = LocalBroadcastManager.getInstance(this);
		initReceiverFilter();
		return START_STICKY;
	}

	public BroadcastReceiver serialPortReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals(SerialBroadCode.ACTION_SEND_MESSAGE)) {
				final String src = intent.getStringExtra("send_message");
				new Thread(new Runnable() {

					@Override
					public void run() {
						serialManager.sendMsg(src);
					}
				}).start();
			} else if (intent.getAction().equals(
					SerialBroadCode.ACTION_CHECK_MINUTE)) {
				String check = intent.getStringExtra("check_minute");
				if (!TextUtils.isEmpty(check)) {
					checkMunite = 0;
					int checkValue = Integer.valueOf(check);
					if (checkValue == 0)
						return;
					checkMunite = checkValue;
				}

				String paikong = intent.getStringExtra("paikong_minute");
				if (!TextUtils.isEmpty(paikong)) {
					paikongMinute = 0;
					int paikongValue = Integer.valueOf(paikong);
					if (paikongValue == 0)
						return;
					paikongMinute = paikongValue;
				}
				stopTimer();
				startTimer();
			}
		}
	};

	public void doSendCutMsg(int minute, final String endmsg,
			final String halfMsg) {
		handler.postDelayed(new Runnable() {

			@Override
			public void run() {
				serialManager.sendMsg(endmsg);
			}
		}, minute * 60 * 1000L);

		handler.postDelayed(new Runnable() {

			@Override
			public void run() {
				serialManager.sendMsg(halfMsg);
			}
		}, (minute * 60 * 1000L) / 2L);
	}

	public void sendCutDownUpdate(CutDownEntry entry) {
		Intent intent = new Intent(SerialBroadCode.ACTION_SEND_CUT_DOWN);
		intent.putExtra("cut_down", entry);
		lbm.sendBroadcast(intent);
	}

	public void doCutDown(int type, int minute) {
		String endMsg = "";
		String halfMsg = "";
		switch (type) {
		case 1:
			endMsg = CMDCode.CHECK_TIME_END;
			halfMsg = CMDCode.CHECK_TIME_HALF;
			break;
		case 2:
			endMsg = CMDCode.PAIKONG_END_TIME;
			halfMsg = CMDCode.PAIKONG_HALF_TIME;
			break;
		default:
			break;
		}
		if (TextUtils.isEmpty(endMsg) || TextUtils.isEmpty(halfMsg))
			return;
		doSendCutMsg(minute, endMsg, halfMsg);
		for (int i = 1; i < (minute + 1); i++) {
			CutDownEntry cutDown = new CutDownEntry();
			cutDown.type = type;
			cutDown.time = (minute - i);
		}
	}
}
