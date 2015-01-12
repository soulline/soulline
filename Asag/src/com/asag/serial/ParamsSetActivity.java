package com.asag.serial;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.regex.Pattern;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.asag.serial.ChandiPopMenu.OnChandiClickListener;
import com.asag.serial.LiangzhongPopMenu.OnLiangClickListener;
import com.asag.serial.app.SerialApp;
import com.asag.serial.base.BaseActivity;
import com.asag.serial.fragment.BaseFragmentListener;
import com.asag.serial.fragment.DatePickFragment;
import com.asag.serial.fragment.InputSureFragment;
import com.asag.serial.mode.CheckDetailItem;
import com.asag.serial.mode.InputEntry;
import com.asag.serial.mode.SpinnerItem;
import com.asag.serial.utils.ButtonUtils;
import com.asag.serial.utils.CMDCode;
import com.asag.serial.utils.DataUtils;
import com.asag.serial.utils.SerialBroadCode;

public class ParamsSetActivity extends BaseActivity implements OnClickListener {

	private ArrayList<SpinnerItem> foodList = new ArrayList<SpinnerItem>();

	private HashMap<String, String> foodMap = new HashMap<String, String>();

	private HashMap<String, String> chandiMap = new HashMap<String, String>();

	private ArrayList<SpinnerItem> chandiList = new ArrayList<SpinnerItem>();

	private TextView liangzhongSpinner;

	private TextView chandiSpinner;

	private TextView cangNumInput, countInput, waterInput, rkDateInput,
			canzhaoPointInput, startDateInput, paikongTimeInput,
			jianceTimeInput, startTimeInput, jiangeTimeInput;
	
	private TextView paramsSettingTitle, title1Ttx, canghaoTitle, liangzhongTitle, shuliangTitle, shuliangDun,
	                 shuifenTitle, persentDanwei, rukuTimeTitle, chandiTitle, title2Tx, canzhaodianTitle, startDateTitle,
	                 paikongTimeTitle, paikongTimeDanwei, jianceTimeTitle, jianceTimeDanwei, startTimeTitle, jiangeTimeTitle,
	                 jiangeTimeDanwei;
	
	private Button btnOk, btnCancel;

	private long chooseTime = 0L;

	private long firstAlarmTime = 0L;
	
	private CheckDetailItem checkDetail = new CheckDetailItem();

	private LocalBroadcastManager lbm = LocalBroadcastManager
			.getInstance(SerialApp.getInstance());

	private int setTpye = -1;
	
	private SpinnerItem liangzhongItem = new SpinnerItem();
	
	private SpinnerItem chandiItem = new SpinnerItem();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.parmas_setting_activity);
		initView();
		initContent();
		setTpye = getIntent().getIntExtra("set_type", -1);
		initTextSize();
	}
	
	private void initTextSize() {
		int size = DataUtils.getPreferences(DataUtils.KEY_TEXT_SIZE, 1);
		switch (size) {
		case 1:
			reloadTextSize(1.4f);
			break;
		case 2:
			reloadTextSize(1.6f);
			break;
		case 3:
			reloadTextSize(1.8f);
			break;
		case 4:
			reloadTextSize(2.0f);
			break;
		case 5:
			reloadTextSize(2.2f);
			break;

		default:
			break;
		}
	}
	
	private void reloadTextSize(float size) {
		liangzhongSpinner.setTextSize(liangzhongSpinner.getTextSize() * size);
		chandiSpinner.setTextSize(chandiSpinner.getTextSize() * size);
		cangNumInput.setTextSize(cangNumInput.getTextSize() * size);
		countInput.setTextSize(countInput.getTextSize() * size);
		waterInput.setTextSize(waterInput.getTextSize() * size);
		rkDateInput.setTextSize(rkDateInput.getTextSize() * size);
		canzhaoPointInput.setTextSize(canzhaoPointInput.getTextSize() * size);
		startDateInput.setTextSize(startDateInput.getTextSize() * size);
		paikongTimeInput.setTextSize(paikongTimeInput.getTextSize() * size);
		jianceTimeInput.setTextSize(jianceTimeInput.getTextSize() * size);
		startTimeInput.setTextSize(startTimeInput.getTextSize() * size);
		jiangeTimeInput.setTextSize(jiangeTimeInput.getTextSize() * size);
		paramsSettingTitle.setTextSize(paramsSettingTitle.getTextSize() * size);
		title1Ttx.setTextSize(title1Ttx.getTextSize() * size);
		canghaoTitle.setTextSize(canghaoTitle.getTextSize() * size);
		liangzhongTitle.setTextSize(liangzhongTitle.getTextSize() * size);
		shuliangTitle.setTextSize(shuliangTitle.getTextSize() * size);
		shuliangDun.setTextSize(shuliangDun.getTextSize() * size);
		shuifenTitle.setTextSize(shuifenTitle.getTextSize() * size);
		persentDanwei.setTextSize(persentDanwei.getTextSize() * size);
		rukuTimeTitle.setTextSize(rukuTimeTitle.getTextSize() * size);
		chandiTitle.setTextSize(chandiTitle.getTextSize() * size);
		title2Tx.setTextSize(title2Tx.getTextSize() * size);
		canzhaodianTitle.setTextSize(canzhaodianTitle.getTextSize() * size);
		startDateTitle.setTextSize(startDateTitle.getTextSize() * size);
		paikongTimeTitle.setTextSize(paikongTimeTitle.getTextSize() * size);
		paikongTimeDanwei.setTextSize(paikongTimeDanwei.getTextSize() * size);
		jianceTimeTitle.setTextSize(jianceTimeTitle.getTextSize() * size);
		jianceTimeDanwei.setTextSize(jianceTimeDanwei.getTextSize() * size);
		startTimeTitle.setTextSize(startTimeTitle.getTextSize() * size);
		jiangeTimeTitle.setTextSize(jiangeTimeTitle.getTextSize() * size);
		jiangeTimeDanwei.setTextSize(jiangeTimeDanwei.getTextSize() * size);
		btnOk.setTextSize(btnOk.getTextSize() * size);
		btnCancel.setTextSize(btnCancel.getTextSize() * size);
	}

	private void initView() {
		liangzhongSpinner = (TextView) findViewById(R.id.liangzhong_spinner);
		liangzhongSpinner.setOnClickListener(this);
		chandiSpinner = (TextView) findViewById(R.id.chandi_spinner);
		chandiSpinner.setOnClickListener(this);
		findViewById(R.id.btn_cancel).setOnClickListener(this);
		findViewById(R.id.btn_ok).setOnClickListener(this);
		
		paramsSettingTitle = (TextView) findViewById(R.id.params_setting_title);
		title1Ttx = (TextView) findViewById(R.id.title_1_tx);
		canghaoTitle = (TextView) findViewById(R.id.canghao_title);
		liangzhongTitle = (TextView) findViewById(R.id.liangzhong_title);
		shuliangTitle = (TextView) findViewById(R.id.shuliang_title);
		shuliangDun = (TextView) findViewById(R.id.shuliang_dun);
		shuifenTitle = (TextView) findViewById(R.id.shuifen_title);
		persentDanwei = (TextView) findViewById(R.id.persent_danwei);
		rukuTimeTitle = (TextView) findViewById(R.id.ruku_time_title);
		chandiTitle = (TextView) findViewById(R.id.chandi_title);
		title2Tx = (TextView) findViewById(R.id.title_2_tx);
		canzhaodianTitle = (TextView) findViewById(R.id.canzhaodian_title);
		startDateTitle = (TextView) findViewById(R.id.start_date_title);
		paikongTimeTitle = (TextView) findViewById(R.id.paikong_time_title);
		paikongTimeDanwei = (TextView) findViewById(R.id.paikong_time_danwei);
		jianceTimeTitle = (TextView) findViewById(R.id.jiance_time_title);
		jianceTimeDanwei = (TextView) findViewById(R.id.jiance_time_danwei);
		startTimeTitle = (TextView) findViewById(R.id.start_time_title);
		jiangeTimeTitle = (TextView) findViewById(R.id.jiange_time_title);
		jiangeTimeDanwei = (TextView) findViewById(R.id.jiange_time_danwei);
		btnOk = (Button) findViewById(R.id.btn_ok);
		btnCancel = (Button) findViewById(R.id.btn_cancel);

		cangNumInput = (TextView) findViewById(R.id.cang_num_input);
		countInput = (TextView) findViewById(R.id.count_input);
		waterInput = (TextView) findViewById(R.id.water_input);
		rkDateInput = (TextView) findViewById(R.id.rk_date_input);
		canzhaoPointInput = (TextView) findViewById(R.id.canzhao_point_input);
		startDateInput = (TextView) findViewById(R.id.start_date_input);
		paikongTimeInput = (TextView) findViewById(R.id.paikong_time_input);
		jianceTimeInput = (TextView) findViewById(R.id.jiance_time_input);
		startTimeInput = (TextView) findViewById(R.id.start_time_input);
		jiangeTimeInput = (TextView) findViewById(R.id.jiange_time_input);
		cangNumInput.setOnClickListener(this);
		countInput.setOnClickListener(this);
		waterInput.setOnClickListener(this);
		rkDateInput.setOnClickListener(this);
		canzhaoPointInput.setOnClickListener(this);
		startDateInput.setOnClickListener(this);
		paikongTimeInput.setOnClickListener(this);
		jianceTimeInput.setOnClickListener(this);
		startTimeInput.setOnClickListener(this);
		jiangeTimeInput.setOnClickListener(this);

	}

	private void initContent() {
		initFoodMap();
		initFoodList();
		initChandiMap();
		initChandiList();
		liangzhongSpinner.setText(foodList.get(0).name);
		chandiSpinner.setText(chandiList.get(0).name);
		sendChandiCode(chandiList.get(0).code);
		sendFoodCode(foodList.get(0).code);
	}

	private String getHexString(String str, int type) {
		char[] array = str.toCharArray();
		StringBuilder sb = new StringBuilder();
		switch (type) {
		case 1:
			sb.append(CMDCode.DATA_CANGHAO);
			break;
		case 2:
			sb.append(CMDCode.DATA_SHUIFEN);
			break;
		case 3:
			sb.append(CMDCode.DATA_SHULIANG);
			break;
		case 6:
			sb.append(CMDCode.DATA_RKSHIJIAN);
			break;

		default:
			break;
		}
		sb.append(" ");
		boolean isXiaoshu = false;
		for (int i = 0; i < array.length; i++) {
			char c = array[i];
			String cStr = c + "";
			if (cStr.equals(".")) {
				sb.append(getAsiicFromLetter(c));
				isXiaoshu = true;
			} else if (isNumeric(cStr)) {
				sb.append(getAsiicFromLetter(c));
			} else if (isLetter(cStr)) {
				sb.append(getAsiicFromLetter(c));
			}
			sb.append(" ");
		}
		if ((type == 2 || type == 3) && !isXiaoshu) {
			sb.append(getAsiicFromLetter('.')).append(" ")
					.append(getAsiicFromLetter('0'));
		}
		sb.append("FF FF");
		return sb.toString();
	}

	private String getHexFromInt(int value) {
		String str = Integer.toHexString(value).toUpperCase();
		if (str.length() < 2) {
			str = "0" + str;
		}
		return str;
	}

	private String getAsiicFromLetter(char letter) {
		int code = 0;
		code = (int) letter;
		String str = getHexFromInt(code);
		return str;
	}

	private boolean checkStartInput() {
		if (setTpye == 1) {
			if (TextUtils.isEmpty(startTimeInput.getText().toString().trim())) {
				showToast("未输入起始时间");
				return false;
			}
			if (TextUtils.isEmpty(jiangeTimeInput.getText().toString().trim())) {
				showToast("未输入间隔时间");
				return false;
			} else {
				String interval = jiangeTimeInput.getText().toString().trim();
				if (!isNumeric(interval)) {
					showToast("请输入正确的间隔时间");
					return false;
				}
			}
			if (TextUtils.isEmpty(jianceTimeInput.getText().toString().trim())) {
				showToast("未输入检测时间");
				return false;
			}
			if (TextUtils.isEmpty(paikongTimeInput.getText().toString().trim())) {
				showToast("未输入排空时间");
				return false;
			}
		} else if (setTpye == 2) {
			if (TextUtils.isEmpty(jianceTimeInput.getText().toString().trim())) {
				showToast("未输入检测时间");
				return false;
			}
			if (TextUtils.isEmpty(paikongTimeInput.getText().toString().trim())) {
				showToast("未输入排空时间");
				return false;
			}
		}
		return true;
	}

	private String getHexFromLetter(String letter) {
		byte[] bytes = letter.getBytes();
		return bytesToHexString(bytes, bytes.length);
	}

	public String bytesToHexString(byte[] src, int size) {
		String ret = "";
		if (src == null || size <= 0) {
			return null;
		}
		for (int i = 0; i < size; i++) {
			String hex = Integer.toHexString(src[i] & 0xFF);
			if (hex.length() < 2) {
				hex = "0" + hex;
			}
			hex += " ";
			ret += hex;
		}
		return ret.toUpperCase();
	}

	public static boolean isLetter(String str) {
		Pattern pattern = Pattern.compile("[a-zA-Z]+");
		return pattern.matcher(str).matches();
	}

	public static boolean isNumeric(String str) {
		Pattern pattern = Pattern.compile("[0-9]*");
		return pattern.matcher(str).matches();
	}

	private void initFoodList() {
		Iterator it = foodMap.keySet().iterator();
		while (it.hasNext()) {
			Object key = it.next();
			if (key instanceof String) {
				String name = (String) key;
				SpinnerItem item = new SpinnerItem();
				item.name = name;
				item.code = foodMap.get(name);
				foodList.add(item);
			}
		}
	}

	private void initChandiList() {
		Iterator it = chandiMap.keySet().iterator();
		while (it.hasNext()) {
			Object key = it.next();
			if (key instanceof String) {
				String name = (String) key;
				SpinnerItem item = new SpinnerItem();
				item.name = name;
				item.code = chandiMap.get(name);
				chandiList.add(item);
			}
		}

	}

	private void showDatePick() {
		Bundle b = new Bundle();
		b.putLong("choose_time", chooseTime);
		displayFragment(true, false, "date_picker", b,
				new BaseFragmentListener() {

					@Override
					public void onCallBack(Object object) {
						if (object instanceof Date) {
							Date date = (Date) object;
							chooseTime = date.getTime();
							SimpleDateFormat format = new SimpleDateFormat(
									"yyyy-MM-dd");
							String dateStr = format.format(date.getTime());
							rkDateInput.setText(dateStr);
							checkDetail.rukuDate = dateStr;
							SimpleDateFormat format2 = new SimpleDateFormat(
									"yyyyMMdd");
							 String timeMsg =
							 getHexString(format2.format(date),
							 6);
							 sendMessageS(timeMsg);
						}

					}
				});
	}

	private void showTimePicker() {
		displayFragment(true, true, "time_picker", null,
				new BaseFragmentListener() {

					@Override
					public void onCallBack(Object object) {
						if (object instanceof Date) {
							Date date = (Date) object;
							firstAlarmTime = date.getTime();
							SimpleDateFormat format = new SimpleDateFormat(
									"yyyy-MM-dd HH:mm");
							String dateStr = format.format(date.getTime());
							startTimeInput.setText(dateStr);
						}

					}
				});
	}

	private void initChandiMap() {
		chandiMap.put("黑龙江", "68 6C 6A");
		chandiMap.put("湖南", "68 75 6E");
		chandiMap.put("贵州", "00 67 7A");
		chandiMap.put("宁夏", "00 6E 78");
		chandiMap.put("河南", "68 65 6E");
		chandiMap.put("湖北", "68 75 62");
		chandiMap.put("陕西", "73 78 73");
		chandiMap.put("海南", "00 68 6E");
		chandiMap.put("山东", "00 73 6A");
		chandiMap.put("内蒙古", "6E 6D 67");
		chandiMap.put("山西", "73 78 6E");
		chandiMap.put("天津", "00 74 6A");

		chandiMap.put("四川", "00 73 63");
		chandiMap.put("江西", "00 6A 78");
		chandiMap.put("新疆", "00 78 6A");
		chandiMap.put("上海", "00 73 68");
		chandiMap.put("江苏", "00 6A 73");
		chandiMap.put("辽宁", "00 6C 6E");
		chandiMap.put("重庆", "00 63 71");
		chandiMap.put("北京", "00 62 6A");
		chandiMap.put("河北", "68 65 62");
		chandiMap.put("云南", "00 79 6E");
		chandiMap.put("甘肃", "00 67 73");
		chandiMap.put("西藏", "00 78 7A");

		chandiMap.put("吉林", "00 6A 6C");
		chandiMap.put("广西", "00 67 78");
		chandiMap.put("浙江", "00 7A 6A");
		chandiMap.put("青海", "00 71 68");
		chandiMap.put("安徽", "00 61 68");
		chandiMap.put("广东", "00 67 64");
		chandiMap.put("福建", "00 66 6A");
		chandiMap.put("台湾", "00 66 67");
	}

	private void initFoodMap() {
		foodMap.put("大麦", "00 64 6D");
		foodMap.put("小麦", "00 78 6D");
		foodMap.put("早稻", "7A 64 61");
		foodMap.put("中稻", "7A 64 62");
		foodMap.put("晚稻", "00 77 64");
		foodMap.put("粳稻", "00 6A 64");
		foodMap.put("玉米", "00 79 6D");
		foodMap.put("大豆", "00 64 64");
		foodMap.put("菜籽", "00 63 7A");
		foodMap.put("杂粮", "00 7A 6C");
		foodMap.put("饲料", "00 73 6C");
		foodMap.put("花生", "00 68 73");
	}

	private void showLiangzhongMenu() {
		LiangzhongPopMenu liangzhongMenu = new LiangzhongPopMenu(context,
				new OnLiangClickListener() {

					@Override
					public void onClick(SpinnerItem item) {
						liangzhongItem = item;
						liangzhongSpinner.setText(item.name);
						sendFoodCode(item.code);
						checkDetail.liangzhong = item.name;
					}
				}, foodList);
		liangzhongMenu.showPopupWindow(liangzhongSpinner);
	}

	private void showChandiMenu() {
		ChandiPopMenu chandiMenu = new ChandiPopMenu(context,
				new OnChandiClickListener() {

					@Override
					public void onClick(SpinnerItem item) {
						chandiItem = item;
						chandiSpinner.setText(item.name);
						sendChandiCode(item.code);
						checkDetail.chandi = item.name;
					}
				}, chandiList);
		chandiMenu.showPopupWindow(chandiSpinner);
	}

	public void displayFragment(boolean isOpen, boolean isTime, String tag,
			Bundle bundle, BaseFragmentListener listener) {
		if (isOpen) {
			((BaseActivity) context).showFragment(tag, -1,
					createFragment(isTime, tag, bundle, listener));
		} else {
			((BaseActivity) context).closeFragment(tag);
		}
	}

	private void sendMessageS(String message) {
		Intent intent = new Intent(SerialBroadCode.ACTION_SEND_MESSAGE);
		intent.putExtra("send_message", message);
		lbm.sendBroadcast(intent);
	}

	public DialogFragment createFragment(boolean isTime, final String tag,
			Bundle b, BaseFragmentListener listener) {
		if (tag.equals("input_dialog")) {
			InputSureFragment inputF = new InputSureFragment(context, b);
			inputF.addFragmentListener(listener);
			return inputF;
		} else if (tag.equals("date_picker")) {
			DatePickFragment dateFragment = new DatePickFragment(context, b);
			dateFragment.addFragmentListener(listener);
			return dateFragment;
		} else if (tag.equals("time_picker")) {
			DatePickFragment dateFragment = new DatePickFragment(context, b);
			dateFragment.addFragmentListener(listener);
			dateFragment.setIsTime(isTime);
			return dateFragment;
		}
		return null;
	}

	private void showInputFragment(int type) {
		Bundle b = new Bundle();
		b.putInt("type", type);
		displayFragment(true, false, "input_dialog", b,
				new BaseFragmentListener() {

					@Override
					public void onCallBack(Object object) {
						if (object instanceof InputEntry) {
							InputEntry inputEntry = (InputEntry) object;
							if (!TextUtils.isEmpty(inputEntry.value)) {
								setValueText(inputEntry);
							}
						}
					}
				});
	}

	private void sendChandiCode(String chandiCode) {
		if (TextUtils.isEmpty(chandiCode))
			return;
		String chandiStrMsg = CMDCode.DATA_CHANDI + " " + chandiCode
				+ "FF FF";
		sendMessageS(chandiStrMsg);
	}
	
	private void sendFoodCode(String foodCode) {
		if (TextUtils.isEmpty(foodCode))
			return;
		String foodStr = CMDCode.DATA_LIANGZHONG + " "
				+ foodCode + "FF FF";
		sendMessageS(foodStr);
	}
	
	private void setValueText(InputEntry inputEntry) {
		switch (inputEntry.type) {
		case 1:
			 sendMessageS(getHexString(inputEntry.value, 1));
			cangNumInput.setText(inputEntry.value);
			break;
		case 2:
			 sendMessageS(getHexString(inputEntry.value, 2));
			waterInput.setText(inputEntry.value);
			break;
		case 3:
			 sendMessageS(getHexString(inputEntry.value, 3));
			countInput.setText(inputEntry.value);
			break;
		case 4:
			jianceTimeInput.setText(inputEntry.value);
			break;
		case 5:
			paikongTimeInput.setText(inputEntry.value);
			break;

		case 6:
			jiangeTimeInput.setText(inputEntry.value);
			break;

		case 7:
			canzhaoPointInput.setText(inputEntry.value);
			break;
		default:
			break;
		}
	}

	@Override
	public void onClick(View v) {
		if (ButtonUtils.isFastClick()) {
			return;
		}
		switch (v.getId()) {
		case R.id.liangzhong_spinner:
			showLiangzhongMenu();
			break;

		case R.id.chandi_spinner:
			showChandiMenu();
			break;

		case R.id.btn_cancel:
			sendMessageS(CMDCode.PREPARE_CANCLE);
			finish();
			break;

		case R.id.btn_ok:
			if (checkStartInput()) {
				long today = System.currentTimeMillis();
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
				checkDetail.canghao = cangNumInput.getText().toString().trim();
				checkDetail.checkDate = format.format(today);
				checkDetail.checkType = setTpye + "";
				checkDetail.shuifen = waterInput.getText().toString().trim();
				checkDetail.shuliang = countInput.getText().toString().trim();
				Intent data = new Intent();
				int check = 0;
				int paikong = 0;
				if (!TextUtils.isEmpty(jianceTimeInput.getText().toString()
						.trim())) {
					check = Integer.valueOf(jianceTimeInput.getText()
							.toString().trim());
				}
				if (!TextUtils.isEmpty(paikongTimeInput.getText().toString()
						.trim())) {
					paikong = Integer.valueOf(paikongTimeInput.getText()
							.toString().trim());
				}
				String interval = jiangeTimeInput.getText().toString().trim();
				if (!TextUtils.isEmpty(interval)) {
					int interIn = Integer.valueOf(interval);
					data.putExtra("interval_time", interIn);
				}
				DataUtils.putPreferences("check_time", check);
				DataUtils.putPreferences("paikong_time", paikong);
				DataUtils.putPreferences("canghao_data", cangNumInput.getText().toString().trim());
				DataUtils.putPreferences("liangzhong_data", liangzhongItem.code);
				DataUtils.putPreferences("check_count_data", countInput.getText().toString().trim());
				DataUtils.putPreferences("shuifen_data", waterInput.getText().toString().trim());
				SimpleDateFormat format2 = new SimpleDateFormat(
						"yyyyMMdd");
				String timeMsg = format2.format(chooseTime);
				DataUtils.putPreferences("ruku_date", timeMsg);
				DataUtils.putPreferences("chandi_data", chandiItem.code);
				data.putExtra("check_value", check);
				data.putExtra("paikong_value", paikong);
				data.putExtra("first_alarm_time", firstAlarmTime);
				data.putExtra("check_detail", checkDetail);
				Log.d("zhao", "set_params : checkDate : " + checkDetail.checkDate + " -- checkType : " + checkDetail.checkType);
				sendMessageS(CMDCode.PREPARE_OK);
				setResult(RESULT_OK, data);
				finish();
			}
			break;

		case R.id.cang_num_input:
			showInputFragment(1);
			break;

		case R.id.count_input:
			showInputFragment(3);
			break;

		case R.id.water_input:
			showInputFragment(2);
			break;

		case R.id.rk_date_input:
			showDatePick();
			break;

		case R.id.canzhao_point_input:
			showInputFragment(7);
			break;

		case R.id.paikong_time_input:
			showInputFragment(5);
			break;

		case R.id.start_date_input:

			break;

		case R.id.jiance_time_input:
			showInputFragment(4);
			break;

		case R.id.start_time_input:
			showTimePicker();
			break;

		case R.id.jiange_time_input:
			showInputFragment(6);
			break;

		default:
			break;
		}

	}

}
