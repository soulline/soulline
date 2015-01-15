package com.asag.serial.service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import com.asag.serial.app.SerialApp;
import com.asag.serial.mode.CutDownEntry;
import com.asag.serial.mode.RightDataEntry;
import com.asag.serial.utils.CMDCode;
import com.asag.serial.utils.DataUtils;
import com.asag.serial.utils.SerialBroadCode;
import com.asag.serial.utils.SerialPortManager;

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

	private HashMap<String, String> tMap = new HashMap<String, String>();

	private HashMap<String, String> rMap = new HashMap<String, String>();
	
	private long sendTimeMills = 0L;

	@Override
	public void onCreate() {
		super.onCreate();
		initMap();
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	private void initMap() {
		tMap.put("0", "t_0_input");
		tMap.put("1", "t_1_input");
		tMap.put("2", "t_2_input");
		tMap.put("3", "t_3_input");
		tMap.put("4", "t_4_input");
		tMap.put("5", "t_5_input");
		tMap.put("6", "t_6_input");
		tMap.put("7", "t_7_input");
		tMap.put("8", "t_8_input");
		tMap.put("9", "t_9_input");
		tMap.put("10", "t_10_input");
		tMap.put("11", "t_11_input");
		tMap.put("12", "t_12_input");
		tMap.put("13", "t_13_input");
		tMap.put("14", "t_14_input");
		tMap.put("15", "t_15_input");

		rMap.put("0", "r_0_input");
		rMap.put("1", "r_1_input");
		rMap.put("2", "r_2_input");
		rMap.put("3", "r_3_input");
		rMap.put("4", "r_4_input");
		rMap.put("5", "r_5_input");
		rMap.put("6", "r_6_input");
		rMap.put("7", "r_7_input");
		rMap.put("8", "r_8_input");
		rMap.put("9", "r_9_input");
		rMap.put("10", "r_10_input");
		rMap.put("11", "r_11_input");
		rMap.put("12", "r_12_input");
		rMap.put("13", "r_13_input");
		rMap.put("14", "r_14_input");
		rMap.put("15", "r_15_input");
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
									if (!checkZeroData(message)) {
										parsePassway(message);
									}
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

	private boolean checkZeroData(String data) {
		if (data.length() < 8) {
			return false;
		}
		String number = getAsiicForHex(data.substring(6, 8)) + "";
		if (number.equals("0")) {
			data = data.substring(6);
			RightDataEntry dataEntry = new RightDataEntry();
			dataEntry.number = getAsiicForHex(data.substring(0, 2)) + "";
			data = data.substring(2);
			String[] array = data.split("FF");
			if (array.length == 5) {
				dataEntry.co2 = subDataHex(array[0]).trim();
				dataEntry.ph3data = subDataHex(array[1]).trim();
				dataEntry.o2 = subDataHex(array[2]).trim();
				dataEntry.shidu = subDataHex(array[3]).trim();
				dataEntry.wendu = subDataHex(array[4]).trim();
				String co2P = DataUtils.getPreferences("co2_input", "0");
				if (isNumber(co2P) && isNumber(dataEntry.co2)) {
					long co2N = Long.valueOf(dataEntry.co2)
							+ Long.valueOf(co2P);
					dataEntry.co2 = co2N + "";
				}
				String ph3P = DataUtils.getPreferences("ph3_input", "0");
				if (isNumber(ph3P) && isNumber(dataEntry.ph3data)) {
					Log.d("zhao", "原始PH3 : " + dataEntry.ph3data
							+ "   校正参数ph3：" + ph3P);
					long ph3N = Long.valueOf(dataEntry.ph3data)
							+ Long.valueOf(ph3P);
					dataEntry.ph3data = ph3N + "";
				}
				String o2P = DataUtils.getPreferences("o2_input", "0");
				float o2N = Float.valueOf(dataEntry.o2) + Float.valueOf(o2P);
				dataEntry.o2 = o2N + "";
				String wenduP = DataUtils.getPreferences("t_0_input", "0");
				float wenduN = Float.valueOf(dataEntry.wendu)
						+ Float.valueOf(wenduP);
				BigDecimal b = new BigDecimal(wenduN);
				float fwendu = b.setScale(1, BigDecimal.ROUND_HALF_UP)
						.floatValue();
				dataEntry.wendu = fwendu + "";
				String shiduP = DataUtils.getPreferences("r_0_input", "0");
				float shiduN = Float.valueOf(dataEntry.shidu)
						+ Float.valueOf(shiduP);
				dataEntry.shidu = shiduN + "";
				Log.d("zhao", "dataentry.number : " + dataEntry.number + "   -- lastway: " + app.lastWay);
				if (!dataEntry.number.equals(app.lastWay)) {
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
					Log.d("zhao", " 0 continue ---- ");
				} else {
					app.isCheckIng = false;
					stopTimer();
					app.oldCheckTime = 0;
					app.oldPaikongTime = 0;
					Log.d("zhao", " 0 stop ---- ");
				}
				Intent intent = new Intent(SerialBroadCode.ACTION_PASSWAY_DATA);
				intent.putExtra("right_data", dataEntry);
				lbm.sendBroadcast(intent);
				return true;
			}
		}
		return false;
	}

	private void parseCO2(String data) {
		data = data.substring(6);
		data = data.replaceAll("FF", "");
		String co2Str = getAsiicForHex(data) + "";
		String co2P = DataUtils.getPreferences("co2_input", "0");
		float co2N = Float.valueOf(co2Str) + Float.valueOf(co2P);
		co2Str = co2N + "";
		Intent intent = new Intent(SerialBroadCode.ACTION_CO2_RECEIVED);
		intent.putExtra("co2_data", co2Str);
		lbm.sendBroadcast(intent);
	}

	private void parseO2(String data) {
		data = data.substring(6);
		data = data.replaceAll("FF", "");
		int o2Int = getAsiicForHex(data);
		float o2i = ((float) o2Int) / 10f;
		String o2Str = o2i + "";
		String o2P = DataUtils.getPreferences("o2_input", "0");
		float o2N = Float.valueOf(o2Str) + Float.valueOf(o2P);
		o2Str = o2N + "";
		Intent intent = new Intent(SerialBroadCode.ACTION_O2_RECEIVED);
		intent.putExtra("o2_data", o2Str);
		lbm.sendBroadcast(intent);
	}

	private boolean isNumber(String str) {
		boolean result = str.matches("[0-9]+");
		return result;
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

	private long getValueForHex(String source) {
		String str = "";
		long code = 0;
		try {
			code = Long.parseLong(source, 16);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return code;
	}

	private void startTimer() {
		app.isCheckIng = true;
		lbm.sendBroadcast(new Intent(SerialBroadCode.ACTION_START_CHECKING));
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
								/*handler.postDelayed(new Runnable() {

									@Override
									public void run() {
										serialManager
												.sendMsg(CMDCode.CHECK_TIME_END);
									}
								}, 2000);*/
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
		lbm.sendBroadcast(new Intent(SerialBroadCode.ACTION_STOP_CHECKING));
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
		if (data.length() < 8) {
			return;
		}
		data = data.substring(6);
		RightDataEntry dataEntry = new RightDataEntry();
		dataEntry.number = getAsiicForHex(data.substring(0, 2)) + "";
		data = data.substring(2);
		String[] array = data.split("FF");
		dataEntry.co2 = subDataHex(array[0]).trim();
		dataEntry.shidu = subDataHex(array[1]).trim();
		dataEntry.wendu = subDataHex(array[2]).trim();
		String co2P = DataUtils.getPreferences("co2_input", "0");
		if (isNumber(co2P) && isNumber(dataEntry.co2)) {
			Long co2N = Long.valueOf(dataEntry.co2) + Long.valueOf(co2P);
			dataEntry.co2 = co2N + "";
		}
		String wenduP = DataUtils.getPreferences(tMap.get(dataEntry.number),
				"0");
		float wenduN = Float.valueOf(dataEntry.wendu) + Float.valueOf(wenduP);
		BigDecimal b = new BigDecimal(wenduN);
		float fwendu = b.setScale(1, BigDecimal.ROUND_HALF_UP)
				.floatValue();
		dataEntry.wendu = fwendu + "";
		String shiduP = DataUtils.getPreferences(rMap.get(dataEntry.number),
				"0");
		float shiduN = Float.valueOf(dataEntry.shidu) + Float.valueOf(shiduP);
		dataEntry.shidu = shiduN + "";
		Log.d("zhao", "parsePassway dataentry.number : " + dataEntry.number + "   -- lastway: " + app.lastWay);
		if (!dataEntry.number.equals(app.lastWay)) {
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
			app.isCheckIng = false;
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
		filter.addAction(SerialBroadCode.ACTION_FINISH_CHECKING);
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
		lbm.unregisterReceiver(serialPortReceiver);
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
				long nowTime = System.currentTimeMillis();
				long deep = nowTime - sendTimeMills;
				sendTimeMills = nowTime;
				if (deep > 0L && deep < 100L) {
					return;
				}
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
			} else if (intent.getAction().equals(SerialBroadCode.ACTION_FINISH_CHECKING)) {
				stopTimer();
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
