package com.asag.serial;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.asag.serial.FilePopupMenu.OnFileClickListener;
import com.asag.serial.FunctionPopMenu.OnFunctionClickListener;
import com.asag.serial.SettingPopMenu.OnSettingClickListener;
import com.asag.serial.ShituPopMenu.OnShituClickListener;
import com.asag.serial.TextSizeMenu.OnTextSizeClickListener;
import com.asag.serial.alarm.JcAlarm;
import com.asag.serial.app.SerialApp;
import com.asag.serial.base.BaseActivity;
import com.asag.serial.mode.AlarmInfo;
import com.asag.serial.mode.CutDownEntry;
import com.asag.serial.mode.RightDataEntry;
import com.asag.serial.service.SerialService;
import com.asag.serial.utils.CMDCode;
import com.asag.serial.utils.DataUtils;
import com.asag.serial.utils.SerialBroadCode;
import com.asag.serial.utils.SerialRequestCode;
import com.asag.serial.utils.ServiceUtil;
import com.asag.serial.widget.DigitalNewClock;

public class MainPageActivity extends BaseActivity implements OnClickListener {

	private LocalBroadcastManager lbm = LocalBroadcastManager
			.getInstance(SerialApp.getInstance());
	
	private TextView checkWayValue;
	
	private int checkMinuteValue = 0;
	private int paikongMinuteValue = 0;
	
	private int wayCount = 0;
	
	private AlarmInfo alarmInfo = new AlarmInfo();
	
	private TextView paikongCheckTime, checkCheckTime;
	
	private TextView paikongTimeState, checkTimeState;
	
	private RightAdatper adapter;
	
	private ArrayList<RightDataEntry> rightList = new ArrayList<RightDataEntry>();
	
	private ListView listView;
	
	private TextView co2Tx, o2Tx, ph3tx, rhtx, ttx;
	
	private TextView co2State, o2State, ph3State, rhState, tState;
	
	private TextView stopMenu;
	
	private ImageView checkAnimationImg;
	
	private TextView topTitleTx, fileMenu, functionMenu, cedingMenu,
	settingMenu, shituMenu, searchMenu, helpMenu, cancelAlarmMenu, checkFunctionTx;
	
	private TextView titleResult, resultCo2, resultRh, resultTc, co2Danwei, ph3Danwei, o2Danwei, rhDanwei, tDanwei;
	
	private DigitalNewClock digitalClock;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_activity);
		initService();
		initView();
		registerReceiver();
		initTextSize();
	}
	
	private void initTextSize() {
		int size = DataUtils.getPreferences(DataUtils.KEY_TEXT_SIZE, 1);
		if (adapter != null) {
			adapter.setItemTextSize(size);
			adapter.notifyDataSetChanged();
		}
		switch (size) {
		case 1:
			reloadNewTextSize(1.0f);
			break;
		case 2:
			reloadNewTextSize(1.2f);
			break;
		case 3:
			reloadNewTextSize(1.4f);
			break;
		case 4:
			reloadNewTextSize(1.6f);
			break;
		case 5:
			reloadNewTextSize(1.8f);
			break;

		default:
			break;
		}
	}
	
	private void showCheckAnim(boolean isShow) {
		if (isShow) {
			checkAnimationImg.setImageResource(R.drawable.checking_anim);
			AnimationDrawable animDrawable = (AnimationDrawable) checkAnimationImg
					.getDrawable();
			animDrawable.start();
		} else {
			checkAnimationImg.setImageResource(R.drawable.check_anima_1);
		}
	}
	
	private void reloadNewTextSize(float size) {
		topTitleTx.setTextSize(topTitleTx.getTextSize() * size);
		fileMenu.setTextSize(fileMenu.getTextSize() * size);
		functionMenu.setTextSize(functionMenu.getTextSize() * size);
		cedingMenu.setTextSize(cedingMenu.getTextSize() * size);
		settingMenu.setTextSize(settingMenu.getTextSize() * size);
		shituMenu.setTextSize(shituMenu.getTextSize() * size);
		searchMenu.setTextSize(searchMenu.getTextSize() * size);
		helpMenu.setTextSize(helpMenu.getTextSize() * size);
		cancelAlarmMenu.setTextSize(cancelAlarmMenu.getTextSize() * size);
		checkFunctionTx.setTextSize(checkFunctionTx.getTextSize() * size);
//		checkWayValue.setTextSize(checkWayValue.getTextSize() * size);
		paikongCheckTime.setTextSize(paikongCheckTime.getTextSize() * size);
		checkCheckTime.setTextSize(checkCheckTime.getTextSize() * size);
		co2Tx.setTextSize(co2Tx.getTextSize() * size);
		paikongTimeState.setTextSize(checkWayValue.getTextSize() * size);
		checkTimeState.setTextSize(paikongCheckTime.getTextSize() * size);
		checkCheckTime.setTextSize(checkCheckTime.getTextSize() * size);
		o2Tx.setTextSize(o2Tx.getTextSize() * size);
		
		ph3tx.setTextSize(ph3tx.getTextSize() * size);
		rhtx.setTextSize(rhtx.getTextSize() * size);
		ttx.setTextSize(ttx.getTextSize() * size);
		co2State.setTextSize(co2State.getTextSize() * size);
		o2State.setTextSize(o2State.getTextSize() * size);
		ph3State.setTextSize(ph3State.getTextSize() * size);
		rhState.setTextSize(rhState.getTextSize() * size);
		tState.setTextSize(tState.getTextSize() * size);
		stopMenu.setTextSize(stopMenu.getTextSize() * size);
		
		titleResult.setTextSize(titleResult.getTextSize() * size);
		resultCo2.setTextSize(resultCo2.getTextSize() * size);
		resultRh.setTextSize(resultRh.getTextSize() * size);
		resultTc.setTextSize(resultTc.getTextSize() * size);
		digitalClock.setTextSize(digitalClock.getTextSize() * size);
		co2Danwei.setTextSize(co2Danwei.getTextSize() * size);
		ph3Danwei.setTextSize(ph3Danwei.getTextSize() * size);
		rhDanwei.setTextSize(rhDanwei.getTextSize() * size);
		tDanwei.setTextSize(tDanwei.getTextSize() * size);
	}

	private void initView() {
		topTitleTx = (TextView) findViewById(R.id.top_title_tx);
		fileMenu = (TextView) findViewById(R.id.file_menu);
		functionMenu = (TextView) findViewById(R.id.function_menu);
		cedingMenu = (TextView) findViewById(R.id.ceding_menu);
		settingMenu = (TextView) findViewById(R.id.setting_menu);
		shituMenu = (TextView) findViewById(R.id.shitu_menu);
		searchMenu = (TextView) findViewById(R.id.search_menu);
		helpMenu = (TextView) findViewById(R.id.help_menu);
		cancelAlarmMenu = (TextView) findViewById(R.id.cancel_alarm_menu);
		checkFunctionTx = (TextView) findViewById(R.id.check_function_tx);
		titleResult = (TextView) findViewById(R.id.title_result);
		
		resultCo2 = (TextView) findViewById(R.id.result_co2);
		resultRh = (TextView) findViewById(R.id.result_rh);
		resultTc = (TextView) findViewById(R.id.result_tc);
		digitalClock = (DigitalNewClock) findViewById(R.id.digital_clock);
		
		co2Danwei = (TextView) findViewById(R.id.co2_danwei);
		ph3Danwei = (TextView) findViewById(R.id.ph3_danwei);
		o2Danwei = (TextView) findViewById(R.id.o2_danwei);
		rhDanwei = (TextView) findViewById(R.id.rh_danwei);
		tDanwei = (TextView) findViewById(R.id.t_danwei);
		
		findViewById(R.id.file_menu).setOnClickListener(this);
		findViewById(R.id.function_menu).setOnClickListener(this);
		findViewById(R.id.ceding_menu).setOnClickListener(this);
		findViewById(R.id.stop_menu).setOnClickListener(this);
		findViewById(R.id.setting_menu).setOnClickListener(this);
		findViewById(R.id.shitu_menu).setOnClickListener(this);
		findViewById(R.id.search_menu).setOnClickListener(this);
		findViewById(R.id.help_menu).setOnClickListener(this);
		findViewById(R.id.cancel_alarm_menu).setOnClickListener(this);
		checkAnimationImg = (ImageView) findViewById(R.id.check_animation_img);
		checkWayValue = (TextView) findViewById(R.id.check_way_value);
		paikongCheckTime = (TextView) findViewById(R.id.paikong_check_time);
		checkCheckTime = (TextView) findViewById(R.id.check_check_time);
		paikongTimeState = (TextView) findViewById(R.id.paikong_time_state);
		checkTimeState = (TextView) findViewById(R.id.check_time_state);
		listView = (ListView) findViewById(R.id.listid);
		
		co2Tx = (TextView) findViewById(R.id.co2_value);
		o2Tx = (TextView) findViewById(R.id.o2_value);
		ph3tx = (TextView) findViewById(R.id.ph3_value);
		rhtx = (TextView) findViewById(R.id.rh_value);
		ttx = (TextView) findViewById(R.id.t_value);
		
		co2State = (TextView) findViewById(R.id.co2_value_state);
		o2State = (TextView) findViewById(R.id.o2_value_state);
		ph3State = (TextView) findViewById(R.id.ph3_value_state);
		rhState = (TextView) findViewById(R.id.rh_value_state);
		tState = (TextView) findViewById(R.id.t_value_state);
		
		stopMenu = (TextView) findViewById(R.id.stop_menu);
	}

	private void registerReceiver() {
		IntentFilter filter = new IntentFilter(
				SerialBroadCode.ACTION_PASSWAY_DATA);
		filter.addAction(SerialBroadCode.ACTION_CO2_RECEIVED);
		filter.addAction(SerialBroadCode.ACTION_O2_RECEIVED);
		filter.addAction(SerialBroadCode.ACTION_SEND_CUT_DOWN);
		filter.addAction(SerialBroadCode.ACTION_ALARM_CHECK_STARTING);
		filter.addAction(SerialBroadCode.ACTION_START_CHECKING);
		filter.addAction(SerialBroadCode.ACTION_STOP_CHECKING);
		lbm.registerReceiver(dataReceiver, filter);
	}

	private boolean isNumber(String str) {
		boolean result = str.matches("[0-9]+");
		return result;
	}
	
	public void addData(RightDataEntry data) {
		int index = -1;
		for (int i=0; i< rightList.size(); i++) {
			RightDataEntry entry = rightList.get(i);
			if (entry.number.equals(data.number)) {
				index = i;
				break;
			}
		}
		if (index != -1 && rightList != null) {
			rightList.remove(index);
		}
		int size = DataUtils.getPreferences(DataUtils.KEY_TEXT_SIZE, 1);
		if (adapter == null) {
			adapter = new RightAdatper(context);
			rightList.add(data);
			adapter.add(data);
			adapter.setItemTextSize(size);
			listView.setAdapter(adapter);
		} else {
			rightList.add(data);
			adapter.clear();
			Collections.sort(rightList, comparatorRight);
			adapter.addData(rightList);
			adapter.setItemTextSize(size);
			adapter.notifyDataSetChanged();
		}
	}
	
	public Comparator<RightDataEntry> comparatorRight = new Comparator<RightDataEntry>() {

		@Override
		public int compare(RightDataEntry rd1, RightDataEntry rd2) {
			int number1 = Integer.valueOf(rd1.number);
			int number2 = Integer.valueOf(rd2.number);
			if (number1 < number2) {
				return -1;
			} else if (number1 == number2) {
				return 0;
			}
			return 1;
		}
	};
	
	public void updatePaikongMinute(String value) {
		paikongCheckTime.setText(value);
		if (value.equals("0.0")) {
			showPointView(paikongTimeState, true);
		} else {
			showPointView(paikongTimeState, false);
		}
	}
	
	public void updateCheckMinute(String value) {
		checkCheckTime.setText(value);
		if (value.equals("0.0")) {
			showPointView(checkTimeState, true);
		} else {
			showPointView(checkTimeState, false);
		}
	}
	
	public void initService() {
		if (!ServiceUtil.isServiceRunning("com.example.testbutton.service.SerialService")) {
			Intent service = new Intent(context, SerialService.class);
			startService(service);
		}
	}
	
	public void finishAll() {
		super.finish();
		System.gc();
		android.os.Process.killProcess(android.os.Process.myPid());
	}
	
	private void showPointView(TextView title, boolean isRed) {
		Drawable drawable = null;
		if (isRed) {
			drawable = getResources().getDrawable(
					R.drawable.redpoint);
		} else {
			drawable = getResources().getDrawable(
					R.drawable.big_greenpoint);
		}
		if (drawable != null) {
			drawable.setBounds(0, 0, drawable.getMinimumWidth(),
					drawable.getMinimumHeight());
			title.setCompoundDrawables(drawable, null, null, null);
		}
	}

	public void setCo2Value(long value) {
		if ((value < 50000 && value > 0) || value == 50000) {
			showPointView(co2State, false);
		} else if (value > 50000) {
			showPointView(co2State, true);
		}
		co2Tx.setText(value + "");
	}
	
	public void setO2Value(float value) {
		if (value > 0) {
			showPointView(o2State, false);
		} else {
			showPointView(o2State, false);
		}
		o2Tx.setText(value + "");
	}
	
	public void setPH3Value(long value) {
		if ((value < 50000 && value > 0) || value == 50000) {
			showPointView(ph3State, false);
		} else if (value > 50000) {
			showPointView(ph3State, true);
		}
		ph3tx.setText(value + "");
	}
	
	public void setRHValue(float value) {
		if (value > 30.0f) {
			showPointView(rhState, true);
		} else if ((value < 30.0f && value > 0) || value == 30.0f) {
			showPointView(rhState, false);
		}
		rhtx.setText(value + "");
	}
	
	public void setTValue(float value) {
		if ((value < 80.0f && value > 0.0f) || value == 80.0f) {
			showPointView(tState, false);
		} else if (value > 80.0f) {
			showPointView(tState, true);
		}
		ttx.setText(value + "");
	}
	
	public void initAdapter(ArrayList<RightDataEntry> list) {
		if (adapter == null) {
			adapter = new RightAdatper(context);
			rightList = list;
			Collections.sort(rightList, comparatorRight);
			adapter.addData(rightList);
			listView.setAdapter(adapter);
		} else {
			adapter.clear();
			rightList.clear();
			rightList = list;
			Collections.sort(rightList, comparatorRight);
			adapter.addData(rightList);
			adapter.notifyDataSetChanged();
		}
	}
	
	public void clearRightData() {
		if (adapter != null) {
			adapter.clear();
			rightList.clear();
			adapter.notifyDataSetChanged();
		}
	}
	
	public BroadcastReceiver dataReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals(SerialBroadCode.ACTION_PASSWAY_DATA)) {
				RightDataEntry dataEntry = (RightDataEntry) intent
						.getSerializableExtra("right_data");
				if (dataEntry != null) {
					if (dataEntry.number.equals("0")) {
						if (!TextUtils.isEmpty(dataEntry.co2)
								&& isNumber(dataEntry.co2)) {
							long value = Long.valueOf(dataEntry.co2);
							setCo2Value(value);
						}
						if (!TextUtils.isEmpty(dataEntry.o2)) {
							float value = Float.valueOf(dataEntry.o2);
							setO2Value(value);
						}
						if (!TextUtils.isEmpty(dataEntry.ph3data)
								&& isNumber(dataEntry.ph3data)) {
							long value = Long.valueOf(dataEntry.ph3data);
							setPH3Value(value);
						}
						if (!TextUtils.isEmpty(dataEntry.shidu)) {
							float value = Float.valueOf(dataEntry.shidu);
							setRHValue(value);
						}
						if (!TextUtils.isEmpty(dataEntry.wendu)) {
							float value = Float.valueOf(dataEntry.wendu);
							setTValue(value);
						}
					} else {
						addData(dataEntry);
					}
					if (dataEntry.number.equals("15")) {
						showToast("检测结束");
						checkWayValue.setText("0");
						wayCount = 0;
					} else {
						int number = Integer.valueOf(dataEntry.number);
						number += 1;
						wayCount = number;
						checkWayValue.setText(number + "");
						float check = ((float) checkMinuteValue) / 10.0f;
						float paikong = ((float) paikongMinuteValue) / 10.0f;
						updateCheckMinute(check + "");
						updatePaikongMinute(paikong + "");
					}
				}
			} else if (intent.getAction().equals(
					SerialBroadCode.ACTION_CO2_RECEIVED)) {
				PadFragment fragment = (PadFragment) getSupportFragmentManager()
						.findFragmentById(R.id.padFragment);
				String co2Str = intent.getStringExtra("co2_data");
				if (!TextUtils.isEmpty(co2Str)) {
					int value = Integer.valueOf(co2Str);
					fragment.setCo2Value(value);
				}
			} else if (intent.getAction().equals(
					SerialBroadCode.ACTION_O2_RECEIVED)) {
				PadFragment fragment = (PadFragment) getSupportFragmentManager()
						.findFragmentById(R.id.padFragment);
				String o2Str = intent.getStringExtra("o2_data");
				if (!TextUtils.isEmpty(o2Str)) {
					float value = Float.valueOf(o2Str);
					fragment.setO2Value(value);
				}
			} else if (intent.getAction().equals(
					SerialBroadCode.ACTION_SEND_CUT_DOWN)) {
				CutDownEntry entry = (CutDownEntry) intent
						.getSerializableExtra("cut_down");
				if (entry != null) {
					if (entry.type == 1) {
						checkMinuteValue = entry.time;
						float check = ((float) checkMinuteValue) / 10.0f;
						updateCheckMinute(check + "");
					} else if (entry.type == 2) {
						paikongMinuteValue = entry.time;
						float paikong = ((float) paikongMinuteValue) / 10.0f;
						updatePaikongMinute(paikong + "");
					}
				}
			} else if (intent.getAction().equals(
					SerialBroadCode.ACTION_ALARM_CHECK_STARTING)) {
				alarmInfo = (AlarmInfo) intent
						.getSerializableExtra("alarm_info");
				handler.postDelayed(new Runnable() {

					@Override
					public void run() {
						sendMessageS(CMDCode.CD_LIANGAN_CHECK_1);
						if (alarmInfo != null) {
							setCheckinfo(alarmInfo);
							startCutDown();
						}
					}
				}, 500);
			} else if (intent.getAction().equals(SerialBroadCode.ACTION_START_CHECKING)) {
				showCheckAnim(true);
			} else if (intent.getAction().equals(SerialBroadCode.ACTION_STOP_CHECKING)) {
				showCheckAnim(false);
			}

		}
	};
	
	public void startCutDown() {
		app.isPause = false;
		String title = wayCount + "";
		checkWayValue.setText(title);
		Intent check = new Intent(SerialBroadCode.ACTION_CHECK_MINUTE);
		check.putExtra("check_minute", checkMinuteValue + "");
		check.putExtra("paikong_minute", paikongMinuteValue + "");
		lbm.sendBroadcast(check);
	}
	
	private void setCheckinfo(AlarmInfo alarm) {
		checkMinuteValue = alarm.checkN * 10;
		paikongMinuteValue = alarm.paikongN * 10;
		wayCount = 0;
		app.oldCheckTime = checkMinuteValue;
		app.oldPaikongTime = paikongMinuteValue;
//		fragment.clearRightData();
		if (checkMinuteValue > 0) {
			float checkF = ((float) checkMinuteValue) / 10.0f;
			updateCheckMinute(checkF + "");
		}
		if (paikongMinuteValue > 0) {
			float paikongF = ((float) paikongMinuteValue) / 10.0f;
			updatePaikongMinute(paikongF + "");
		}
	}

	private void showFileMenu() {
		FilePopupMenu fileMenu = new FilePopupMenu(context,
				new OnFileClickListener() {

					@Override
					public void onClick(int resourceId) {
						if (resourceId == R.id.exit_menu) {
							finishAll();
						}
					}
				});
		fileMenu.showPopupWindow(findViewById(R.id.file_menu));
	}

	private void setAlarmCheck(final AlarmInfo alarm) {
		if (alarm.firstTimeN < System.currentTimeMillis()) {
			alarm.firstTimeN = System.currentTimeMillis() + alarm.minuteN * 60 * 1000;
			alarmInfo = alarm;
		}
		if (app.isSetAlarm) {
			JcAlarm.cancelSendAlarm();
			handler.postDelayed(new Runnable() {
				
				@Override
				public void run() {
					JcAlarm.setSendAlarm(alarm);
				}
			}, 500);
		} else {
			JcAlarm.setSendAlarm(alarm);
		}
	}
	
	private void sendMessageS(String message) {
		Intent intent = new Intent(SerialBroadCode.ACTION_SEND_MESSAGE);
		intent.putExtra("send_message", message);
		lbm.sendBroadcast(intent);
	}

	private void startCanshuActivity(int newType) {
		Intent params = new Intent(context, ParamsSetActivity.class);
		if (newType != -1) {
			params.putExtra("set_type", newType);
		}
		startActivityForResult(params, SerialRequestCode.REQUEST_SET_DATA);
		overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
	}

	private void showFunctionMenu(final int type) {
		FunctionPopMenu functionMenu = new FunctionPopMenu(context,
				new OnFunctionClickListener() {

					@Override
					public void onClick(int resourceId) {
						if (type == 0) {
							int newType = -1;
							if (resourceId == R.id.liangan_jiance_menu) {
								newType = 1;
								sendMessageS(CMDCode.FF_LIANGAN_CHECK_1);
								startCanshuActivity(newType);
							} else if (resourceId == R.id.point_check_menu) {
								newType = 2;
								if (app.isCheckIng) {
									showToast("检测正在进行中，无法开启新检测");
								} else {
									sendMessageS(CMDCode.FF_LIANGAN_CHECK_2);
									startCanshuActivity(newType);
								}
							} else if (resourceId == R.id.cangan_jiance_menu) {
								newType = 3;
								sendMessageS(CMDCode.FF_CANGAN_CHECK);
								startCanshuActivity(newType);
							}

						} else if (type == 1) {
							if (resourceId == R.id.liangan_jiance_menu) {
								showToast("定时器开启");
								if (alarmInfo != null && alarmInfo.firstTimeN > 0L && alarmInfo.minuteN > 0) {
									setAlarmCheck(alarmInfo);
								}
							} else if (resourceId == R.id.point_check_menu) {
								showToast("开始测定");
								sendMessageS(CMDCode.CD_LIANGAN_CHECK_2);
							} else if (resourceId == R.id.cangan_jiance_menu) {
								showToast("开始测定");
								sendMessageS(CMDCode.CD_CANGAN_CHECK);
							}
						} else if (type == 2) {
							
						}
					}
				});
		View view = null;
		if (type == 0) {
			view = findViewById(R.id.function_menu);
		} else if (type == 1) {
			view = findViewById(R.id.ceding_menu);
		} else if (type == 2) {
			view = findViewById(R.id.search_menu);
		}
		if (view != null) {
			functionMenu.showPopupWindow(view);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK  && requestCode == SerialRequestCode.REQUEST_SET_DATA) {
			if (alarmInfo == null) {
				alarmInfo = new AlarmInfo();
			}
			alarmInfo.checkN = data.getIntExtra("check_value", 0);
			alarmInfo.paikongN = data.getIntExtra("paikong_value", 0);
			alarmInfo.firstTimeN = data.getLongExtra("first_alarm_time", 0L);
			alarmInfo.minuteN = data.getIntExtra("interval_time", 0);
			setCheckinfo(alarmInfo);
		}
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		Intent service = new Intent(context, SerialService.class);
		stopService(service);
	}
	
	private void showSettingMenu() {
		SettingPopMenu settingMenu = new SettingPopMenu(context,
				new OnSettingClickListener() {

					@Override
					public void onClick(int resourceId) {
						switch (resourceId) {
						case R.id.textSize_menu:
							showTextSizeMenu();
							break;

						default:
							break;
						}

					}
				});
		settingMenu.showPopupWindow(findViewById(R.id.setting_menu));
	}
	
	private void showTextSizeMenu() {
		TextSizeMenu settingMenu = new TextSizeMenu(context, new OnTextSizeClickListener() {
			
			@Override
			public void onClick(int resourceId) {
				switch (resourceId) {
				case R.id.s_small_size:
					DataUtils.putPreferences(DataUtils.KEY_TEXT_SIZE, 1);
					reloadNewTextSize(1.0f);
					break;
					
				case R.id.small_size:
					DataUtils.putPreferences(DataUtils.KEY_TEXT_SIZE, 2);
					reloadNewTextSize(1.2f);
					break;
					
				case R.id.normal_size:
					DataUtils.putPreferences(DataUtils.KEY_TEXT_SIZE, 3);
					reloadNewTextSize(1.4f);
					break;
					
				case R.id.big_size:
					DataUtils.putPreferences(DataUtils.KEY_TEXT_SIZE, 4);
					reloadNewTextSize(1.6f);
					break;
					
				case R.id.b_big_size:
					DataUtils.putPreferences(DataUtils.KEY_TEXT_SIZE, 5);
					reloadNewTextSize(1.8f);
					break;

				default:
					break;
				}
				
			}
		});
		settingMenu.showPopupWindow(findViewById(R.id.setting_menu));
	}

	private void showShituMenu() {
		ShituPopMenu shituMenu = new ShituPopMenu(context,
				new OnShituClickListener() {

					@Override
					public void onClick(int resourceId) {
						// TODO Auto-generated method stub

					}
				});
		shituMenu.showPopupWindow(findViewById(R.id.shitu_menu));
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.file_menu:
			showFileMenu();
			break;

		case R.id.function_menu:
			showFunctionMenu(0);
			break;

		case R.id.ceding_menu:
			showFunctionMenu(1);
			break;

		case R.id.stop_menu:
			if (!app.isPause && app.isCheckIng) {
				sendMessageS(CMDCode.STOP_CMD);
				app.isPause = true;
				app.isCheckIng = false;
				showToast("停止检测");
				stopMenu.setText("继续");
				showCheckAnim(false);
			} else if (app.isPause && !app.isCheckIng){
				sendMessageS(CMDCode.STOP_CMD);
				app.isPause = false;
				showToast("继续检测");
				app.isCheckIng = true;
				stopMenu.setText("停止");
				showCheckAnim(true);
			}
			break;

		case R.id.setting_menu:
			showSettingMenu();
			break;

		case R.id.shitu_menu:
			showShituMenu();
			break;

		case R.id.search_menu:
			showFunctionMenu(2);
			break;

		case R.id.help_menu:

			break;
			
		case R.id.cancel_alarm_menu:
			JcAlarm.cancelSendAlarm();
			showToast("已取消定时器");
			break;

		default:
			break;
		}

	}

}
