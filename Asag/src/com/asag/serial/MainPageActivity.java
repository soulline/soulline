package com.asag.serial;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

import android.app.KeyguardManager;
import android.app.KeyguardManager.KeyguardLock;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.SystemClock;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;
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
import com.asag.serial.data.AsagProvider;
import com.asag.serial.fragment.BaseFragmentListener;
import com.asag.serial.fragment.DatePickFragment;
import com.asag.serial.fragment.InputSureFragment;
import com.asag.serial.fragment.PointCheckFragment;
import com.asag.serial.fragment.PointSetFragment;
import com.asag.serial.mode.AlarmInfo;
import com.asag.serial.mode.CheckDetailItem;
import com.asag.serial.mode.CutDownEntry;
import com.asag.serial.mode.PointInfo;
import com.asag.serial.mode.PointItemRecord;
import com.asag.serial.mode.PointRecord;
import com.asag.serial.mode.RightDataEntry;
import com.asag.serial.mode.TimeSetEntry;
import com.asag.serial.service.SerialService;
import com.asag.serial.utils.ButtonUtils;
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
			settingMenu, shituMenu, searchMenu, helpMenu, cancelAlarmMenu,
			checkFunctionTx;

	private TextView titleResult, resultCo2, resultRh, resultTc, co2Danwei,
			ph3Danwei, o2Danwei, rhDanwei, tDanwei, chuliangState, chuliangStateValue,
			shuifenState, shuifenStateValue;

	private TextView paikongCheckDanwei, checkCheckDanwei;

	private DigitalNewClock digitalClock;

	private ArrayList<String> checkWayList = new ArrayList<String>();

	private int checkState = 0;
	
	private CheckDetailItem checkDetail = new CheckDetailItem();
	
	private boolean isSaving = false;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_activity);
		initService();
		initView();
		registerReceiver();
		initTextSize();
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				initRecord();
			}
		}).start();
	}

	private void initRecord() {
		ArrayList<CheckDetailItem> checkList = queryData();
		for (CheckDetailItem detailItem : checkList) {
			fillPointRecord(detailItem);
		}
		if (checkList.size() > 0) {
			CheckDetailItem detail = checkList.get(0);
			for (PointItemRecord record : detail.pointList) {
				RightDataEntry dataEntry = new RightDataEntry();
				dataEntry.co2 = record.co2;
				dataEntry.number = record.wayNum;
				dataEntry.o2 = record.o2Value;
				dataEntry.ph3data = record.ph3Value;
				dataEntry.shidu = record.rhValue;
				dataEntry.wendu = record.tValue;
				final RightDataEntry entry = dataEntry;
				handler.post(new Runnable() {
					
					@Override
					public void run() {
						if (entry.number.equals("0")) {
							if (!TextUtils.isEmpty(entry.co2)
									&& isNumber(entry.co2)) {
								long value = Long.valueOf(entry.co2);
								setCo2Value(value);
							}
							if (!TextUtils.isEmpty(entry.o2)) {
								float value = Float.valueOf(entry.o2);
								setO2Value(value);
							}
							if (!TextUtils.isEmpty(entry.ph3data)
									&& isNumber(entry.ph3data)) {
								long value = Long.valueOf(entry.ph3data);
								setPH3Value(value);
							}
							if (!TextUtils.isEmpty(entry.shidu)) {
								float value = Float.valueOf(entry.shidu);
								setRHValue(value);
							}
							if (!TextUtils.isEmpty(entry.wendu)) {
								float value = Float.valueOf(entry.wendu);
								setTValue(value);
							}
							
						} else {
							addData(entry);
						}
					}
				});
				
			}
		}
	}
	
	private void initTextSize() {
		int size = DataUtils.getPreferences(DataUtils.KEY_TEXT_SIZE, 5);
		if (adapter != null && adapter.getCount() > 0) {
			adapter.setItemTextSize(size);
			adapter.notifyDataSetChanged();
		}
		switch (size) {
		case 1:
			reloadNewTextSize(1.4f);
			break;
		case 2:
			reloadNewTextSize(1.6f);
			break;
		case 3:
			reloadNewTextSize(1.8f);
			break;
		case 4:
			reloadNewTextSize(2.0f);
			break;
		case 5:
			reloadNewTextSize(2.2f);
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

	public static String hexToBinary(String s) {
		return Long.toBinaryString(Long.parseLong(s, 16));
	}

	private void reloadNewTextSize(float size) {
		topTitleTx.setTextSize(15.69f * size);
		fileMenu.setTextSize(12.55f * size);
		functionMenu.setTextSize(12.55f * size);
		cedingMenu.setTextSize(12.55f * size);
		settingMenu.setTextSize(12.55f * size);
		shituMenu.setTextSize(12.55f * size);
		searchMenu.setTextSize(12.55f * size);
		helpMenu.setTextSize(12.55f * size);
		cancelAlarmMenu.setTextSize(12.55f * size);
		checkFunctionTx.setTextSize(12.55f * size);
		// checkWayValue.setTextSize(checkWayValue.getTextSize() * size);
		paikongCheckTime.setTextSize(11.0f * size);
		checkCheckTime.setTextSize(11.0f * size);
		co2Tx.setTextSize(11.0f * size);
		paikongTimeState.setTextSize(11.0f * size);
		checkTimeState.setTextSize(11.0f * size);
		checkCheckTime.setTextSize(11.0f * size);
		o2Tx.setTextSize(11.0f * size);

		ph3tx.setTextSize(11.0f * size);
		rhtx.setTextSize(11.0f * size);
		ttx.setTextSize(11.0f * size);
		co2State.setTextSize(11.0f * size);
		o2State.setTextSize(11.0f * size);
		ph3State.setTextSize(11.0f * size);
		rhState.setTextSize(11.0f * size);
		tState.setTextSize(11.0f * size);
		stopMenu.setTextSize(11.0f * size);

		titleResult.setTextSize(11.0f * size);
		resultCo2.setTextSize(11.0f * size);
		resultRh.setTextSize(11.0f * size);
		resultTc.setTextSize(11.0f * size);
		digitalClock.setTextSize(11.0f * size);
		co2Danwei.setTextSize(11.0f * size);
		o2Danwei.setTextSize(11.0f * size);
		ph3Danwei.setTextSize(11.0f * size);
		rhDanwei.setTextSize(11.0f * size);
		tDanwei.setTextSize(11.0f * size);
		paikongCheckDanwei.setTextSize(11.0f * size);
		checkCheckDanwei.setTextSize(11.0f * size);
		chuliangState.setTextSize(11.0f * size);
		chuliangStateValue.setTextSize(11.0f * size);
		shuifenState.setTextSize(11.0f * size);
		shuifenStateValue.setTextSize(11.0f * size);
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

		paikongCheckDanwei = (TextView) findViewById(R.id.paikong_check_danwei);
		checkCheckDanwei = (TextView) findViewById(R.id.check_check_danwei);
		
		chuliangState = (TextView) findViewById(R.id.chuliang_state);
		chuliangStateValue = (TextView) findViewById(R.id.chuliang_state_value);
		shuifenState = (TextView) findViewById(R.id.shuifen_state);
		shuifenStateValue = (TextView) findViewById(R.id.shuifen_state_value);

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
		for (int i = 0; i < rightList.size(); i++) {
			RightDataEntry entry = rightList.get(i);
			if (entry.number.equals(data.number)) {
				index = i;
				break;
			}
		}
		if (index != -1 && rightList != null) {
			rightList.remove(index);
		}
		int size = DataUtils.getPreferences(DataUtils.KEY_TEXT_SIZE, 5);
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
		if (!ServiceUtil
				.isServiceRunning("com.example.testbutton.service.SerialService")) {
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
			drawable = getResources().getDrawable(R.drawable.redpoint);
		} else {
			drawable = getResources().getDrawable(R.drawable.big_greenpoint);
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
		if (value < 15) {
			showPointView(o2State, true);
		} else {
			showPointView(o2State, false);
		}
		o2Tx.setText(value + "");
	}

	public void setPH3Value(double value) {
		if ((value < 0.15d && (value > 0 || value == 0))) {
			showPointView(ph3State, false);
		} else {
			showPointView(ph3State, true);
		}
		ph3tx.setText(value + "");
	}

	public void setRHValue(float value) {
		if (value > 30.0f) {
			showPointView(rhState, true);
		} else if ((value < 30.0f && (value > 0 || value == 0)) || value == 30.0f) {
			showPointView(rhState, false);
		}
		rhtx.setText(value + "");
	}

	public void setTValue(float value) {
		if ((value < 80.0f && (value > 0.0f || value == 0.0f)) || value == 80.0f) {
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
	
	private int getCo2Status(String co2value) {
		if (!isNumber(co2value)) {
			return 1;
		}
		long co2v = Long.valueOf(co2value);
		if (co2v < 800) {
			return 1;
		} else if (co2v == 800 || (co2v > 800 && co2v < 2000)) {
			return 2;
		} else if (co2v == 2000 || (co2v > 2000 && co2v < 5000) || co2v == 5000) {
			return 3;
		} else if (co2v > 5000) {
			return 4;
		}
		return 1;
	}
	
	private float getMmi(String tvalue) {
		if (checkDetail == null) return 0.0f;
		for (PointItemRecord record : checkDetail.pointList) {
			if (record.wayNum.equals("0")) {
				float a = Float.valueOf(tvalue);
				float b = Float.valueOf(record.tValue);
				return Math.abs(a - b);
			}
		}
		return 0.0f;
	}
	
	private CheckDetailItem checkState(CheckDetailItem check) {
		StringBuilder sb1 = new StringBuilder();
		StringBuilder sb2 = new StringBuilder();
		boolean safe1 = true;
		boolean safe2 = true;
		boolean safe3 = true;
		sb1.append("有潜在风险：");
		sb2.append("有潜在风险：");
		for (PointItemRecord record : check.pointList) {
			if (record.status > 2) {
				safe1 = false;
				safe2 = false;
				sb1.append(record.wayNum).append("、");
			} else if (record.status == 2) {
				safe1 = false;
				safe2 = true;
			}
			float wendu = Float.valueOf(record.tValue);
			if (wendu >= 8.0f) {
				safe3 = false;
				sb2.append(record.wayNum).append("、");
			} else {
				safe3 = true;
			}
		}
		if (safe1 && safe2) {
			check.chuliangState = "安全";
		} else if (!safe1 && safe2) {
			check.chuliangState = "基本安全";
		} else {
			check.chuliangState = sb1.toString().substring(0, sb1.toString().length());
		}
		if (!safe3) {
			check.shuifenState = sb2.toString().substring(0, sb2.toString().length());
		} else {
			check.shuifenState = "安全";
		}
		return check;
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
							double value = Double.valueOf(dataEntry.ph3data);
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
					PointItemRecord record = new PointItemRecord();
					record.checkDate = checkDetail.checkDate;
					record.checkType = checkDetail.checkType;
					record.co2 = dataEntry.co2;
					record.rhValue = dataEntry.shidu;
					record.tValue = dataEntry.wendu;
					record.wayNum = dataEntry.number;
					record.ssi = dataEntry.co2;
					record.status = getCo2Status(record.ssi);
					record.mmi = getMmi(dataEntry.wendu) + "";
					record.o2Value = dataEntry.o2;
					record.ph3Value = dataEntry.ph3data;
					Log.d("zhaoz", "receive checkDate : " + checkDetail.checkDate + "  checkType : " + checkDetail.checkType);
					if (checkDetail != null) {
						checkDetail.pointList.add(record);
					}
					checkDetail = checkState(checkDetail);
					chuliangStateValue.setText(checkDetail.chuliangState);
					shuifenStateValue.setText(checkDetail.shuifenState);
					if (checkState == 0 && dataEntry.number.equals("15")) {
						showToast("检测结束");
						checkWayValue.setText("0");
						wayCount = 0;
						checkWayList.clear();
//						setAlarmCheck(app.alarmInfo);
						saveCheckInNewTask(checkDetail);
					} else {
						int number = Integer.valueOf(dataEntry.number);
						if (checkState == 1) {
							int next = getNextWay(number + "");
							Log.d("zhao", "next : " + next + " -- size : " + checkWayList.size());
							if (next != -1) {
								if (next < checkWayList.size()) {
									String checkWay = checkWayList.get(next + 1);
									checkWayValue.setText(checkWay + "");
									float check = ((float) checkMinuteValue) / 10.0f;
									float paikong = ((float) paikongMinuteValue) / 10.0f;
									updateCheckMinute(check + "");
									updatePaikongMinute(paikong + "");
								} else {
									showToast("检测结束");
									checkWayValue.setText("0");
									wayCount = 0;
									checkWayList.clear();
									saveCheckInNewTask(checkDetail);
								}
							} else {
								showToast("检测结束");
								checkWayValue.setText("0");
								wayCount = 0;
								checkWayList.clear();
								saveCheckInNewTask(checkDetail);
							}

						} else if (checkState == 0) {
							number += 1;
							wayCount = number;
							checkWayValue.setText(number + "");
							float check = ((float) checkMinuteValue) / 10.0f;
							float paikong = ((float) paikongMinuteValue) / 10.0f;
							updateCheckMinute(check + "");
							updatePaikongMinute(paikong + "");
						} else if (checkState == 2) {
							showToast("检测结束");
							checkWayValue.setText("0");
							wayCount = 0;
							checkWayList.clear();
							saveCheckInNewTask(checkDetail);
						}
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
				if (checkState == 0 && app.isCheckIng) {
					showToast("粮安监测正在进行中..请等待下一次监测");
					return;
				}
				lbm.sendBroadcast(new Intent(SerialBroadCode.ACTION_FINISH_CHECKING));
				if (checkDetail != null && app.isCheckIng) {
					sendMessageS(CMDCode.DATA_INTERRUP_STOP);
					CheckDetailItem checkNew = checkDetail;
					saveCheckInNewTask(checkNew);
				}
				checkWayList.clear();
				checkFunctionTx.setText("粮安监测");
				if (app.isPause) {
					Log.d("zhao", "interrup  pause : " + app.isPause);
					app.isPause = false;
				}
				app.isCheckIng = false;
				Log.d("zhao", "alarm_receiver_stating====");
				checkState = 0;
				app.lastWay = "15";
				clearRightData();
				alarmInfo = (AlarmInfo) intent
						.getSerializableExtra("alarm_info");
//				setCheckinfo(alarmInfo, 0);
				if (alarmInfo != null) {
					Log.d("zhao", "alarm stating check time : " + alarmInfo.checkN + "  == paikong time : " + alarmInfo.paikongN);
				}
				handler.postDelayed(new Runnable() {

					@Override
					public void run() {
						sendMessageS(CMDCode.CD_LIANGAN_CHECK_1);
						if (checkDetail != null) {
							checkDetail.pointList.clear();
						}
						long today = System.currentTimeMillis();
						SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
						checkDetail.checkDate = format.format(today);
						checkDetail.checkType = 1 + "";
						if (alarmInfo != null) {
							setCheckinfo(alarmInfo, 0);
							startCutDown(0);
						}
					}
				}, 2000);
			} else if (intent.getAction().equals(
					SerialBroadCode.ACTION_START_CHECKING)) {
				showCheckAnim(true);
			} else if (intent.getAction().equals(
					SerialBroadCode.ACTION_STOP_CHECKING)) {
				showCheckAnim(false);
			}

		}
	};
	
	private void saveCheckInNewTask(final CheckDetailItem check) {
		Log.d("zhao", "saveCheckInNewTask date : " + check.checkDate + "  -- type: " + check.checkType + 
				"    -- point size : " + check.pointList.size());
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				if (!isSaving) {
					isSaving = true;
					saveCheckDetail(check);
				}
			}
		}).start();
	}
	
	private synchronized void saveCheckDetail(CheckDetailItem check) {
		check.saveTime = System.currentTimeMillis() + "";
		Log.d("zhao", "save time : " + check.saveTime);
		ContentValues values = new ContentValues();
		values.put(AsagProvider.CheckDetail.CANGHAO, check.canghao);
		values.put(AsagProvider.CheckDetail.LIANGZHONG, check.liangzhong);
		values.put(AsagProvider.CheckDetail.SHULIANG, check.shuliang);
		values.put(AsagProvider.CheckDetail.SHUIFEN, check.shuifen);
		values.put(AsagProvider.CheckDetail.CHANDI, check.chandi);
		values.put(AsagProvider.CheckDetail.RUKUDATE, check.rukuDate);
		values.put(AsagProvider.CheckDetail.CHECKDATE, check.checkDate);
		values.put(AsagProvider.CheckDetail.CHECKTYPE, check.checkType);
		values.put(AsagProvider.CheckDetail.SAVE_TIME, check.saveTime);
		values.put(AsagProvider.CheckDetail.CHULIANGSTATE, check.chuliangState);
		values.put(AsagProvider.CheckDetail.SHUIFENSTATE, check.shuifenState);
		if (check.checkType.equals("1")) {
			Cursor cursor = getContentResolver().query(
					AsagProvider.CheckDetail.CONTENT_URI,
					new String[] { AsagProvider.CheckDetail._ID, AsagProvider.CheckDetail.CANGHAO,
							AsagProvider.CheckDetail.CHANDI,
							AsagProvider.CheckDetail.CHECKDATE,
							AsagProvider.CheckDetail.CHECKTYPE,
							AsagProvider.CheckDetail.LIANGZHONG,
							AsagProvider.CheckDetail.RUKUDATE,
							AsagProvider.CheckDetail.SHUIFEN,
							AsagProvider.CheckDetail.SHULIANG,
							AsagProvider.CheckDetail.SAVE_TIME }, AsagProvider.CheckDetail.CHECKDATE + "='" + 
									check.checkDate + "' AND "+
									AsagProvider.CheckDetail.CHECKTYPE + "='" + check.checkType + "'", null,
									null); 
			Log.d("zhao", "save qeury cursor --- " + cursor);
			if (cursor != null) {
				Log.d("zhao", "save qeury cursor count --- " + cursor.getCount());
			}
			if (cursor != null && cursor.getCount() > 0) {
				getContentResolver().update(AsagProvider.CheckDetail.CONTENT_URI,
						values, AsagProvider.CheckDetail.CHECKDATE + "='" + check.checkDate + "' AND " + AsagProvider.CheckDetail.CHECKTYPE + "='" + check.checkType + "'",
						null);
			} else {
				getContentResolver().insert(AsagProvider.CheckDetail.CONTENT_URI,
						values);
			}
			cursor.close();
			getContentResolver().delete(AsagProvider.PointRecord.CONTENT_URI,
					AsagProvider.PointRecord.CHECKDATE + "='" + check.checkDate + 
					"' AND " + AsagProvider.PointRecord.CHECKTYPE + "='" + check.checkType + "'", null);
		} else {
			getContentResolver().insert(AsagProvider.CheckDetail.CONTENT_URI,
					values);
		}
		Log.d("zhao", "save point --- " + check.pointList.size());
		for (PointItemRecord record : check.pointList) {
			record.saveTime = check.saveTime;
			saveCheckItemRecord(record);
		}
		isSaving = false;
	}
	
	private void saveCheckItemRecord(PointItemRecord record) {
		Log.d("zhao", "checkdate : " + record.checkDate + " checkType : " + record.checkType);
		ContentValues values = new ContentValues();
		values.put(AsagProvider.PointRecord.COTWO, record.co2);
		values.put(AsagProvider.PointRecord.MMI, record.mmi);
		values.put(AsagProvider.PointRecord.RHVALUE, record.rhValue);
		values.put(AsagProvider.PointRecord.SSI, record.ssi);
		values.put(AsagProvider.PointRecord.STATUS, record.status);
		values.put(AsagProvider.PointRecord.TVALUE, record.tValue);
		values.put(AsagProvider.PointRecord.WAYNUMBER, record.wayNum);
		values.put(AsagProvider.PointRecord.CHECKDATE, record.checkDate);
		values.put(AsagProvider.PointRecord.CHECKTYPE, record.checkType);
		values.put(AsagProvider.PointRecord.OTWO, record.o2Value);
		values.put(AsagProvider.PointRecord.PHVALUE, record.ph3Value);
		values.put(AsagProvider.PointRecord.SAVE_TIME, record.saveTime);
		getContentResolver().insert(AsagProvider.PointRecord.CONTENT_URI, values);
	}

	public void startCutDown(int checkCode) {
		app.isPause = false;
		if (checkCode == 0) {
			String title = wayCount + "";
			checkWayValue.setText(title);
		} else if ((checkCode == 1 || checkCode == 2)
				&& checkWayList.size() > 0) {
			checkWayValue.setText(checkWayList.get(0));
		}
		Intent check = new Intent(SerialBroadCode.ACTION_CHECK_MINUTE);
		check.putExtra("check_minute", checkMinuteValue + "");
		check.putExtra("paikong_minute", paikongMinuteValue + "");
		lbm.sendBroadcast(check);
	}

	private void setCheckinfo(AlarmInfo alarm, int wayNow) {
		checkMinuteValue = alarm.checkN * 10;
		paikongMinuteValue = alarm.paikongN * 10;
		wayCount = wayNow;
		app.oldCheckTime = checkMinuteValue;
		app.oldPaikongTime = paikongMinuteValue;
		// fragment.clearRightData();
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
			float mbei = (float) (System.currentTimeMillis() - alarm.firstTimeN) / (alarm.minuteN * 60 * 1000L);
			long mod = (System.currentTimeMillis() - alarm.firstTimeN) % (alarm.minuteN * 60 * 1000L);
			long nminute = alarm.minuteN * 60 * 1000L - mod;
			int nbei = 0;
			if (nminute > 60 * 1000L) {
				nbei = (int) Math.ceil(mbei);
			} else {
				nbei = (int) Math.ceil(mbei) + 1;
			}
			alarm.firstTimeN = alarm.firstTimeN + alarm.minuteN * 60
					* 1000L * nbei;
			alarmInfo = alarm;
		}
		if (app.isAreadyAlarm) {
			JcAlarm.cancelSendAlarm();
			app.isAreadyAlarm = false;
			handler.postDelayed(new Runnable() {

				@Override
				public void run() {
					JcAlarm.setSendAlarm(alarm);
					app.isAreadyAlarm = true;
				}
			}, 500);
		} else {
			JcAlarm.setSendAlarm(alarm);
			app.isAreadyAlarm = true;
		}
	}

	public void sendMessageS(String message) {
		Intent intent = new Intent(SerialBroadCode.ACTION_SEND_MESSAGE);
		intent.putExtra("send_message", message);
		lbm.sendBroadcast(intent);
	}

	private void startCanshuActivity(int newType) {
		Intent params = new Intent(context, ParamsSetActivity.class);
		if (newType != -1) {
			params.putExtra("set_type", newType);
		}
		checkDetail = new CheckDetailItem();
		startActivityForResult(params, SerialRequestCode.REQUEST_SET_DATA);
		overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
	}

	private void fillCheckWayList(String result) {
		checkWayList.clear();
		StringBuilder sb = new StringBuilder();
		StringBuilder sbNew = new StringBuilder();
		String binaryStr = hexToBinary(result);
		for (int i = 0; i < (16 - binaryStr.length()); i++) {
			sbNew.append("0");
		}
		binaryStr = sbNew.toString() + binaryStr;
		char[] array = binaryStr.toCharArray();
		for (int i = (array.length - 1); i > -1; i--) {
			int wayI = array.length - 1 - i;
			String way = wayI + "";
			String wayN = array[i] + "";
			if (wayN.equals("1")) {
				checkWayList.add(way);
				sb.append(way).append(",");
			}
		}
		Log.d("zhao", "aready_check : " + sb.toString());
	}

	private int getNextWay(String nowNumber) {
		if (checkWayList.size() == 0) {
			showToast("未选择检测通道号");
			return -1;
		}
		if (nowNumber.equals(checkWayList.get(checkWayList.size() - 1))) {
			return -1;
		}
		int position = -1;
		for (int i = 0; i < checkWayList.size(); i++) {
			if (nowNumber.equals(checkWayList.get(i))) {
				position = i;
				break;
			}
		}
		return position;
	}

	private void restartAlarmSet() {
		if (app.isAreadyAlarm && app.alarmInfo != null
				&& app.alarmInfo.minuteN > 0) {
			app.alarmInfo = alarmInfo;
			app.alarmInfo.checkN = DataUtils.getPreferences("check_time", 0);
			app.alarmInfo.paikongN = DataUtils.getPreferences("paikong_time", 0);
//			setCheckinfo(alarmInfo, 0);
			Log.d("zhao", "start set alarm : " + alarmInfo.minuteN);
			setAlarmCheck(app.alarmInfo);
		}
	}
	
	private ArrayList<CheckDetailItem> queryData() {
		long today = System.currentTimeMillis();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String date = format.format(today);
		ArrayList<CheckDetailItem> list = new ArrayList<CheckDetailItem>();
		Cursor cursor = getContentResolver().query(
				AsagProvider.CheckDetail.CONTENT_URI,
				new String[] { AsagProvider.CheckDetail._ID, AsagProvider.CheckDetail.CANGHAO,
						AsagProvider.CheckDetail.CHANDI,
						AsagProvider.CheckDetail.CHECKDATE,
						AsagProvider.CheckDetail.CHECKTYPE,
						AsagProvider.CheckDetail.LIANGZHONG,
						AsagProvider.CheckDetail.RUKUDATE,
						AsagProvider.CheckDetail.SHUIFEN,
						AsagProvider.CheckDetail.SHULIANG,
						AsagProvider.CheckDetail.CHULIANGSTATE,
						AsagProvider.CheckDetail.SHUIFENSTATE,
						AsagProvider.CheckDetail.SAVE_TIME },AsagProvider.CheckDetail.CHECKDATE + "='"
							    + date + "' AND " +
				AsagProvider.CheckDetail.CHECKTYPE + "='" + "1" + "'", null,
				null);
		Log.d("zhao", "cursor count : " + cursor.getCount());
		if (cursor != null) {
			while (cursor.moveToNext()) {
				CheckDetailItem point = new CheckDetailItem();
				point.id = cursor.getInt(cursor
						.getColumnIndexOrThrow(AsagProvider.CheckDetail._ID));
				point.canghao = cursor
						.getString(cursor
								.getColumnIndexOrThrow(AsagProvider.CheckDetail.CANGHAO));
				point.liangzhong = cursor
						.getString(cursor
								.getColumnIndexOrThrow(AsagProvider.CheckDetail.LIANGZHONG));
				point.shuliang = cursor
						.getString(cursor
								.getColumnIndexOrThrow(AsagProvider.CheckDetail.SHULIANG));
				point.shuifen = cursor
						.getString(cursor
								.getColumnIndexOrThrow(AsagProvider.CheckDetail.SHUIFEN));
				point.chandi = cursor
						.getString(cursor
								.getColumnIndexOrThrow(AsagProvider.CheckDetail.CHANDI));
				point.rukuDate = cursor
						.getString(cursor
								.getColumnIndexOrThrow(AsagProvider.CheckDetail.RUKUDATE));
				point.checkDate = cursor
						.getString(cursor
								.getColumnIndexOrThrow(AsagProvider.CheckDetail.CHECKDATE));
				point.checkType = cursor
						.getString(cursor
								.getColumnIndexOrThrow(AsagProvider.CheckDetail.CHECKTYPE));
				point.saveTime = cursor.getString(cursor.getColumnIndexOrThrow(AsagProvider.CheckDetail.SAVE_TIME));
				list.add(point);
				Log.d("zhao", "query PointRecord cursor id : " + point.id + " checkDate : " + point.checkDate + "" +
						"  -- checkType : " + point.checkType);
				
			}
			cursor.close();
		}
		return list;
	}
	
	private synchronized void fillPointRecord(CheckDetailItem point) {
		Cursor cursor1 = getContentResolver().query(
				AsagProvider.PointRecord.CONTENT_URI,
				new String[] { AsagProvider.PointRecord._ID, AsagProvider.PointRecord.WAYNUMBER,
						AsagProvider.PointRecord.COTWO,
						AsagProvider.PointRecord.CHECKDATE,
						AsagProvider.PointRecord.CHECKTYPE,
						AsagProvider.PointRecord.MMI,
						AsagProvider.PointRecord.RHVALUE,
						AsagProvider.PointRecord.SSI,
						AsagProvider.PointRecord.TVALUE,
						AsagProvider.PointRecord.OTWO,
						AsagProvider.PointRecord.PHVALUE,
						AsagProvider.PointRecord.STATUS,
						AsagProvider.PointRecord.SAVE_TIME },
				AsagProvider.PointRecord.SAVE_TIME + "='"
					    + point.saveTime.trim() + "' AND "
						+ AsagProvider.PointRecord.CHECKTYPE + "="
						+ point.checkType.trim(), null, null);
		Log.d("zhao", " fillPointRecord  checkDate : " + point + " checkTYPE : " + point.checkType);
		if (cursor1 != null) {
			Log.d("zhao", "query fillPointRecord cursor1 count : " + cursor1.getCount());
		}
		if (cursor1 != null) {
			while (cursor1.moveToNext()) {
				PointItemRecord record = new PointItemRecord();
				record.id = cursor1
						.getInt(cursor1
								.getColumnIndexOrThrow(AsagProvider.PointRecord._ID));
				record.wayNum = cursor1
						.getString(cursor1
								.getColumnIndexOrThrow(AsagProvider.PointRecord.WAYNUMBER));
				record.co2 = cursor1
						.getString(cursor1
								.getColumnIndexOrThrow(AsagProvider.PointRecord.COTWO));
				record.mmi = cursor1
						.getString(cursor1
								.getColumnIndexOrThrow(AsagProvider.PointRecord.MMI));
				record.rhValue = cursor1
						.getString(cursor1
								.getColumnIndexOrThrow(AsagProvider.PointRecord.RHVALUE));
				record.ssi = cursor1
						.getString(cursor1
								.getColumnIndexOrThrow(AsagProvider.PointRecord.SSI));
				record.status = cursor1
						.getInt(cursor1
								.getColumnIndexOrThrow(AsagProvider.PointRecord.STATUS));
				record.checkDate = cursor1
						.getString(cursor1
								.getColumnIndexOrThrow(AsagProvider.PointRecord.CHECKDATE));
				record.checkType = cursor1
						.getString(cursor1
								.getColumnIndexOrThrow(AsagProvider.PointRecord.CHECKTYPE));
				record.tValue = cursor1
						.getString(cursor1
								.getColumnIndexOrThrow(AsagProvider.PointRecord.TVALUE));
				record.o2Value = cursor1
						.getString(cursor1
								.getColumnIndexOrThrow(AsagProvider.PointRecord.OTWO));
				record.ph3Value = cursor1
						.getString(cursor1
								.getColumnIndexOrThrow(AsagProvider.PointRecord.PHVALUE));
				record.saveTime = cursor1.getString(cursor1.getColumnIndexOrThrow(AsagProvider.PointRecord.SAVE_TIME));
				point.pointList.add(record);
				Log.d("zhao", "query PointRecord cursor1 id : " + record.id);
			}
			cursor1.close();
		}
	}
	
	private void showFunctionMenu(final int type) {
		FunctionPopMenu functionMenu = new FunctionPopMenu(context,
				new OnFunctionClickListener() {

					@Override
					public void onClick(int resourceId) {
						if (type == 0) {
							int newType = -1;
							if (resourceId == R.id.liangan_jiance_menu) {
								if (app.isCheckIng) {
									showToast("检测正在进行中，无法开启新检测");
								} else {
									newType = 1;
									checkFunctionTx.setText("粮安监测");
									sendMessageS(CMDCode.FF_LIANGAN_CHECK_1);
									startCanshuActivity(newType);
								}
							} else if (resourceId == R.id.point_check_menu) {
								newType = 2;
								if (app.isCheckIng) {
									showToast("检测正在进行中，无法开启新检测");
								} else {
									checkFunctionTx.setText("粮安检测");
									sendMessageS(CMDCode.FF_LIANGAN_CHECK_2);
									// startCanshuActivity(newType);
									displayFragment(true, "point_set", null,
											new BaseFragmentListener() {

												@Override
												public void onCallBack(
														Object object) {
													if (object instanceof TimeSetEntry) {
														long today = System.currentTimeMillis();
														SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
														checkDetail.checkDate = format.format(today);
														checkDetail.checkType = 2 + "";
														TimeSetEntry entry = (TimeSetEntry) object;
														checkMinuteValue = entry.checkTime * 10;
														paikongMinuteValue = entry.paikongTime * 10;
														wayCount = 0;
														app.oldCheckTime = checkMinuteValue;
														app.oldPaikongTime = paikongMinuteValue;
														// fragment.clearRightData();
														if (checkMinuteValue > 0) {
															float checkF = ((float) checkMinuteValue) / 10.0f;
															updateCheckMinute(checkF
																	+ "");
														}
														if (paikongMinuteValue > 0) {
															float paikongF = ((float) paikongMinuteValue) / 10.0f;
															updatePaikongMinute(paikongF
																	+ "");
														}
													}

												}
											});
								}
							} else if (resourceId == R.id.cangan_jiance_menu) {
								newType = 3;
								if (app.isCheckIng) {
									showToast("检测正在进行中，无法开启新检测");
								} else {
									checkFunctionTx.setText("仓安监测");
									sendMessageS(CMDCode.FF_CANGAN_CHECK);
//								startCanshuActivity(newType);
									displayFragment(true, "point_set", null,
											new BaseFragmentListener() {
										
										@Override
										public void onCallBack(
												Object object) {
											if (object instanceof TimeSetEntry) {
												long today = System.currentTimeMillis();
												SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
												checkDetail.checkDate = format.format(today);
												checkDetail.checkType = 3 + "";
												TimeSetEntry entry = (TimeSetEntry) object;
												checkMinuteValue = entry.checkTime * 10;
												paikongMinuteValue = entry.paikongTime * 10;
												wayCount = 0;
												app.oldCheckTime = checkMinuteValue;
												app.oldPaikongTime = paikongMinuteValue;
												// fragment.clearRightData();
												if (checkMinuteValue > 0) {
													float checkF = ((float) checkMinuteValue) / 10.0f;
													updateCheckMinute(checkF
															+ "");
												}
												if (paikongMinuteValue > 0) {
													float paikongF = ((float) paikongMinuteValue) / 10.0f;
													updatePaikongMinute(paikongF
															+ "");
												}
											}
											
										}
									});
								}
							}

						} else if (type == 1) {
							if (resourceId == R.id.liangan_jiance_menu) {
								if (app.isPause) {
									showToast("检测正在进行中，无法开启新检测");
								} else {
									checkFunctionTx.setText("粮安监测");
									clearRightData();
									checkState = 0;
									showToast("定时器开启");
									if (checkDetail != null) {
										checkDetail.pointList.clear();
									}
									if (alarmInfo == null) {
										alarmInfo = new AlarmInfo();
									}
									DataUtils.putPreferences("first_alarm_time", alarmInfo.firstTimeN);
									DataUtils.putPreferences("interval_time", alarmInfo.minuteN);
									if (alarmInfo != null
											&& alarmInfo.firstTimeN > 0L
											&& alarmInfo.minuteN > 0) {
										app.alarmInfo = alarmInfo;
										app.alarmInfo.checkN = DataUtils.getPreferences("check_time", 0);
										app.alarmInfo.paikongN = DataUtils.getPreferences("paikong_time", 0);
										setCheckinfo(alarmInfo, 0);
										Log.d("zhao", "start set alarm : " + alarmInfo.minuteN);
										setAlarmCheck(alarmInfo);
									}
								}
							} else if (resourceId == R.id.point_check_menu) {
								if (app.isPause) {
									showToast("检测正在进行中，无法开启新检测");
									if (app.isAreadyAlarm) {
//										restartAlarmSet();
									}
								} else {
									checkFunctionTx.setText("粮安检测");
									clearRightData();
									if (checkDetail != null) {
										checkDetail.pointList.clear();
									}
									checkState = 1;
									sendMessageS(CMDCode.CD_POINT_CHECK);
									displayFragment(true, "point_select", null,
											new BaseFragmentListener() {
										
										@Override
										public void onCallBack(Object object) {
											if (object != null
													&& object instanceof String) {
												String result = (String) object;
												Log.d("zhao", "result : " + result);
												fillCheckWayList(result);
												if (checkWayList.size() == 0) {
													showToast("请选择检测通道");
													return;
												}
												app.lastWay = checkWayList.get(checkWayList
														.size() - 1);
												if (alarmInfo == null) {
													alarmInfo = new AlarmInfo();
												}
												long today = System.currentTimeMillis();
												SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
												checkDetail.checkDate = format.format(today);
												checkDetail.checkType = 2 + "";
												app.alarmInfo = alarmInfo;
												app.alarmInfo.checkN = DataUtils.getPreferences("check_time", 0);
												app.alarmInfo.paikongN = DataUtils.getPreferences("paikong_time", 0);
												String wayN = checkWayList.get(0);
												int way = 0;
												if (isNumber(wayN)) {
													way = Integer.valueOf(wayN);
												}
												setCheckinfo(alarmInfo, way);
												result = result.substring(
														0, 2)
														+ " "
														+ result.substring(
																2,
																result.length());
												String message = CMDCode.CD_LIANGAN_CHECK_2
														+ result + "FF FF";
												showToast("开始测定");
												sendMessageS(message);
												startCutDown(1);
											}
										}
									});
								}
							} else if (resourceId == R.id.cangan_jiance_menu) {
								if (app.isPause) {
									showToast("检测正在进行中，无法开启新检测");
									/*if (app.isAreadyAlarm) {
										restartAlarmSet();
									}*/
								} else {
									checkFunctionTx.setText("仓安监测");
									clearRightData();
									app.lastWay = "0";
									checkState = 2;
									if (checkDetail != null) {
										checkDetail.pointList.clear();
									}
									if (alarmInfo == null) {
										alarmInfo = new AlarmInfo();
									}
									long today = System.currentTimeMillis();
									SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
									checkDetail.checkDate = format.format(today);
									checkDetail.checkType = 3 + "";
									app.alarmInfo = alarmInfo;
									app.alarmInfo.checkN = DataUtils.getPreferences("check_time", 0);
									app.alarmInfo.paikongN = DataUtils.getPreferences("paikong_time", 0);
									setCheckinfo(alarmInfo, 0);
									showToast("开始测定");
									sendMessageS(CMDCode.CD_CANGAN_CHECK);
									startCutDown(2);
								}
							}
						} else if (type == 2) {
							Intent record = new Intent(context, PointRecordActivity.class);
							if (resourceId == R.id.liangan_jiance_menu) {
								record.putExtra("record_title", "粮安监测结果");
								record.putExtra("record_type", 1);
								startActivity(record);
							} else if (resourceId == R.id.point_check_menu) {
								record.putExtra("record_title", "粮安检测结果");
								record.putExtra("record_type", 2);
								startActivity(record);
							} else if (resourceId == R.id.cangan_jiance_menu) {
								Intent canganRec = new Intent(context, CanganRecordActivity.class);
								canganRec.putExtra("record_title", "仓安检测结果");
								canganRec.putExtra("record_type", 3);
								startActivity(canganRec);
							}
							
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

	public void displayFragment(boolean isOpen, String tag, Bundle bundle,
			BaseFragmentListener listener) {
		if (isOpen) {
			((BaseActivity) context).showFragment(tag, -1,
					createFragment(tag, bundle, listener));
		} else {
			((BaseActivity) context).closeFragment(tag);
		}
	}

	public DialogFragment createFragment(final String tag, Bundle b,
			BaseFragmentListener listener) {
		if (tag.equals("point_select")) {
			PointCheckFragment pointF = new PointCheckFragment();
			pointF.addBaseFragmentListener(listener);
			return pointF;
		} else if (tag.equals("point_set")) {
			PointSetFragment pointSetF = new PointSetFragment();
			pointSetF.addBaseFragmentListener(listener);
			return pointSetF;
		} else if (tag.equals("time_set")) {
			DatePickFragment datePickF = new DatePickFragment(context, b);
			datePickF.setIsTime(true);
			datePickF.addFragmentListener(listener);
			return datePickF;
		}
		return null;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK
				&& requestCode == SerialRequestCode.REQUEST_SET_DATA) {
			if (alarmInfo == null) {
				alarmInfo = new AlarmInfo();
			}
			alarmInfo.checkN = data.getIntExtra("check_value", 0);
			alarmInfo.paikongN = data.getIntExtra("paikong_value", 0);
			alarmInfo.firstTimeN = data.getLongExtra("first_alarm_time", 0L);
			alarmInfo.minuteN = data.getIntExtra("interval_time", 0);
			setCheckinfo(alarmInfo, 0);
			checkDetail = (CheckDetailItem) data.getSerializableExtra("check_detail");
			Log.d("zhao", "activityresult checkDetail : " + checkDetail);
			if (checkDetail != null) {
				Log.d("zhaoz", "activityresult checkDate : " + checkDetail.checkDate + "  checkType : " + checkDetail.checkType);
			}
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		lbm.unregisterReceiver(dataReceiver);
		Intent service = new Intent(context, SerialService.class);
		stopService(service);
	}

	private void showSettingMenu() {
		SettingPopMenu settingMenu = new SettingPopMenu(context,
				new OnSettingClickListener() {

					@Override
					public void onClick(int resourceId) {
						switch (resourceId) {
							
						case R.id.check_params_menu:
							Intent checkParam = new Intent(context, CheckParamsSetActivity.class);
							startActivity(checkParam);
							break;
							
						case R.id.point_set_menu:
							Intent intent = new Intent(context, PointLocationActivity.class);
							startActivity(intent);
							break;
							
						case R.id.date_set_menu:
							Bundle b = new Bundle();
							b.putSerializable("choose_time", System.currentTimeMillis());
							displayFragment(true, "time_set", b, new BaseFragmentListener() {
								
								@Override
								public void onCallBack(Object object) {
									if (object instanceof Date) {
										Date date = (Date) object;
										if (date != null) {
											SystemClock.setCurrentTimeMillis(date.getTime());
										}
									}
								}
							});
							break;

						default:
							break;
						}

					}
				});
		settingMenu.showPopupWindow(findViewById(R.id.setting_menu));
	}

	private void showTextSizeMenu() {
		TextSizeMenu settingMenu = new TextSizeMenu(context,
				new OnTextSizeClickListener() {

					@Override
					public void onClick(int resourceId) {
						final ProgressDialog dialog = new ProgressDialog(
								context);
						dialog.setTitle("正在设置字体，请稍后...");
						dialog.show();
						handler.postDelayed(new Runnable() {

							@Override
							public void run() {
								dialog.dismiss();
							}
						}, 1000);
						switch (resourceId) {
						case R.id.s_small_size:
							DataUtils
									.putPreferences(DataUtils.KEY_TEXT_SIZE, 1);
							reloadNewTextSize(1.0f);
							break;

						case R.id.small_size:
							DataUtils
									.putPreferences(DataUtils.KEY_TEXT_SIZE, 2);
							reloadNewTextSize(1.2f);
							break;

						case R.id.normal_size:
							DataUtils
									.putPreferences(DataUtils.KEY_TEXT_SIZE, 3);
							reloadNewTextSize(1.4f);
							break;

						case R.id.big_size:
							DataUtils
									.putPreferences(DataUtils.KEY_TEXT_SIZE, 4);
							reloadNewTextSize(1.6f);
							break;

						case R.id.b_big_size:
							DataUtils
									.putPreferences(DataUtils.KEY_TEXT_SIZE, 5);
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
						switch (resourceId) {
						case R.id.point_distribute_menu:
							Intent gotoV = new Intent(context, PointFangActivity.class);
							startActivity(gotoV);
							break;

						default:
							break;
						}

					}
				});
		shituMenu.showPopupWindow(findViewById(R.id.shitu_menu));
	}
	
	@Override
	public void onClick(View v) {
		if (ButtonUtils.isFastClick()) {
			return;
		}
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
			} else if (app.isPause && !app.isCheckIng) {
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
			app.isAreadyAlarm = false;
			JcAlarm.cancelSendAlarm();
			showToast("已取消定时器");
			break;

		default:
			break;
		}

	}

}
