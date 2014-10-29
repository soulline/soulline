package com.example.testbutton;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.regex.Pattern;

import com.example.alarm.JcAlarm;
import com.example.testbutton.app.SerialApp;
import com.example.testbutton.base.BaseActivity;
import com.example.testbutton.fragment.BaseFragmentListener;
import com.example.testbutton.fragment.DatePickFragment;
import com.example.testbutton.fragment.InputSureFragment;
import com.example.testbutton.mode.InputEntry;
import com.example.testbutton.utils.CMDCode;
import com.example.testbutton.utils.SerialBroadCode;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;

public class DetailsSetActivity extends BaseActivity implements OnClickListener {
	private Button btn_confirm;
	private Button btn_cancel;
	private Spinner sp_food, chandi;

	private TextView minuteCheck, minutePaikong;

	private TextView inputCanghao, inputShuifen, inputShuliang;
	
	private TextView startTime, intervalTime;

	private Button setRKTime;

	private long chooseTime = 0L;
	
	private long firstAlarmTime = 0L;

	private HashMap<String, String> foodMap = new HashMap<String, String>();

	private HashMap<String, String> chandiMap = new HashMap<String, String>();

	private LocalBroadcastManager lbm = LocalBroadcastManager
			.getInstance(SerialApp.getInstance());

	// private View mMenuView;

	/*
	 * public DetailsPopupWindow(Context context) { // super(context);
	 * this.context = context;
	 * 
	 * sp_food.setOnClickListener(new OnClickListener() {
	 * 
	 * @Override public void onClick(View v) {
	 * System.out.println(sp_food.getSelectedItem().toString()); } });
	 * 
	 * 
	 * 
	 * sp_food.setOnItemClickListener(new OnItemClickListener() {
	 * 
	 * @Override public void onItemClick(AdapterView<?> parent, View view, int
	 * position, long id) {
	 * //System.out.println(sp_food.getSelectedItem().toString()); // MyDialog
	 * myDialog = new MyDialog(myActivity); // myDialog.show(); } });
	 * 
	 * 
	 * this.setContentView(mMenuView); this.setWidth(600); this.setHeight(400);
	 * this.setFocusable(true);
	 * 
	 * // InitUI(context);
	 * 
	 * // MyDialog myDialog = new MyDialog(context); // myDialog.show();
	 * 
	 * Dialog alertDialog = new AlertDialog.Builder(context).
	 * setTitle("对话框的标题"). setMessage("对话框的内容").
	 * setIcon(R.drawable.ic_launcher). create(); alertDialog.show();
	 * 
	 * 
	 * // this.setAnimationStyle(R.style.AnimBottom); // ColorDrawable dw = new
	 * ColorDrawable(0xb0000000); // this.setBackgroundDrawable(dw); //
	 * mMenuView.setOnTouchListener(new OnTouchListener() { // // public boolean
	 * onTouch(View v, MotionEvent event) { // // int height =
	 * mMenuView.findViewById(R.id.file_pop).getTop(); // int y=(int)
	 * event.getY(); // if(event.getAction()==MotionEvent.ACTION_UP){ //
	 * if(y<height){ // dismiss(); // } // } // return true; // } // });
	 * 
	 * }
	 */

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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.parasetting);
		initFoodMap();
		initChandiMap();
		init();
	}

	private void init() {
		btn_confirm = (Button) findViewById(R.id.confirm);
		btn_confirm.setOnClickListener(this);

		btn_cancel = (Button) findViewById(R.id.cancel);
		btn_cancel.setOnClickListener(this);
		startTime = (TextView) findViewById(R.id.start_time);
		startTime.setOnClickListener(this);
		intervalTime = (TextView) findViewById(R.id.interval_time);
		intervalTime.setOnClickListener(this);
		sp_food = (Spinner) findViewById(R.id.liangzhong);
		sp_food.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				String food = sp_food.getSelectedItem().toString();
				String foodCode = foodMap.get(food);
				if (TextUtils.isEmpty(foodCode))
					return;
				String foodStr = CMDCode.DATA_LIANGZHONG + " "
						+ foodMap.get(food) + "FF FF";
				sendMessageS(foodStr);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});
		chandi = (Spinner) findViewById(R.id.chandi);
		chandi.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				String chandiStr = chandi.getSelectedItem().toString();
				String chandiCode = chandiMap.get(chandiStr);
				if (TextUtils.isEmpty(chandiCode))
					return;
				String chandiStrMsg = CMDCode.DATA_CHANDI + " " + chandiCode
						+ "FF FF";
				sendMessageS(chandiStrMsg);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}

		});
		minuteCheck = (TextView) findViewById(R.id.minute_check);
		minutePaikong = (TextView) findViewById(R.id.minute_paikong);
		minuteCheck.setOnClickListener(this);
		minutePaikong.setOnClickListener(this);

		inputCanghao = (TextView) findViewById(R.id.input_canghao);
		inputShuifen = (TextView) findViewById(R.id.input_shuifen);
		inputShuliang = (TextView) findViewById(R.id.input_shuliang);
		inputCanghao.setOnClickListener(this);
		inputShuifen.setOnClickListener(this);
		inputShuliang.setOnClickListener(this);

		setRKTime = (Button) findViewById(R.id.set_rk_time);
		setRKTime.setOnClickListener(this);
	}

	/*
	 * public void InitUI(Activity context){ sp1
	 * =(Spinner)context.findViewById(R.id.liangzhong); sp2
	 * =(Spinner)context.findViewById(R.id.chandi); bt1=
	 * (Button)findViewById(R.id.queding); Log.d("Test",
	 * "CanshuActivity.init bt1"); bt2= (Button)findViewById(R.id.quxiao); }
	 */
	private void dataCollect() {

	}

	public void displayFragment(boolean isOpen, boolean isTime, String tag, Bundle bundle,
			BaseFragmentListener listener) {
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

	public DialogFragment createFragment(boolean isTime, final String tag, Bundle b,
			BaseFragmentListener listener) {
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

	private String getHexFromInt(int value) {
		String str = Integer.toHexString(value).toUpperCase();
		if (str.length() < 2) {
			str = "0" + str;
		}
		return str;
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

	private void showInputFragment(int type) {
		Bundle b = new Bundle();
		b.putInt("type", type);
		displayFragment(true, false, "input_dialog", b, new BaseFragmentListener() {

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

	private void setValueText(InputEntry inputEntry) {
		switch (inputEntry.type) {
		case 1:
			sendMessageS(getHexString(inputEntry.value, 1));
			inputCanghao.setText(inputEntry.value);
			break;
		case 2:
			sendMessageS(getHexString(inputEntry.value, 2));
			inputShuifen.setText(inputEntry.value);
			break;
		case 3:
			sendMessageS(getHexString(inputEntry.value, 3));
			inputShuliang.setText(inputEntry.value);
			break;
		case 4:
			minuteCheck.setText(inputEntry.value);
			break;
		case 5:
			minutePaikong.setText(inputEntry.value);
			break;
			
		case 6:
			intervalTime.setText(inputEntry.value);
			break;

		default:
			break;
		}
	}

	private boolean checkInput() {
		if (TextUtils.isEmpty(inputCanghao.getText().toString().trim())) {
			showToast("请输入仓号");
			return false;
		}
		if (TextUtils.isEmpty(inputShuifen.getText().toString().trim())) {
			showToast("请输入水分");
			return false;
		}
		if (TextUtils.isEmpty(inputShuliang.getText().toString().trim())) {
			showToast("请输入数量");
			return false;
		}
		if (TextUtils.isEmpty(sp_food.getSelectedItem().toString().trim())) {
			showToast("请选择粮种");
			return false;
		}
		if (TextUtils.isEmpty(chandi.getSelectedItem().toString().trim())) {
			showToast("请选择产地");
			return false;
		}
		if (chooseTime == 0L) {
			showToast("请设置入库时间");
			return false;
		}
		if (TextUtils.isEmpty(minuteCheck.getText().toString().trim())) {
			showToast("请输入检测时间");
			return false;
		}
		if (TextUtils.isEmpty(minutePaikong.getText().toString().trim())) {
			showToast("请输入排空时间");
			return false;
		}
		return true;
	}

	private void showDatePick() {
		Bundle b = new Bundle();
		b.putLong("choose_time", chooseTime);
		displayFragment(true, false, "date_picker", b, new BaseFragmentListener() {

			@Override
			public void onCallBack(Object object) {
				if (object instanceof Date) {
					Date date = (Date) object;
					chooseTime = date.getTime();
					SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
					String dateStr = format.format(date.getTime());
					setRKTime.setText(dateStr);
					SimpleDateFormat format2 = new SimpleDateFormat("yyyyMMdd");
					String timeMsg = getHexString(format2.format(date), 6);
					sendMessageS(timeMsg);
				}

			}
		});
	}
	
	private void showTimePicker() {
		displayFragment(true, true, "time_picker", null, new BaseFragmentListener() {

			@Override
			public void onCallBack(Object object) {
				if (object instanceof Date) {
					Date date = (Date) object;
					firstAlarmTime = date.getTime();
					SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
					String dateStr = format.format(date.getTime());
					startTime.setText(dateStr);
				}

			}
		});
	}

	private String getHexToAsiicFor16(String source) {
		int code = 0;
		code = Integer.parseInt(source, 16);
		char result = (char) code;
		String str = result + "";
		return str;
	}

	private String getAsiicForInteger(String source) {
		int code = 0;
		code = Integer.parseInt(source);
		char result = (char) code;
		String str = result + "";
		return str;
	}

	private String getAsiicFromLetter(char letter) {
		int code = 0;
		code = (int) letter;
		String str = getHexFromInt(code);
		return str;
	}
	
	private boolean checkStartInput() {
		if (TextUtils.isEmpty(startTime.getText().toString().trim()) && 
				TextUtils.isEmpty(intervalTime.getText().toString().trim())) {
			return true;
		}
		if (!TextUtils.isEmpty(startTime.getText().toString().trim()) &&
				!TextUtils.isEmpty(intervalTime.getText().toString().trim())) {
			String interval = intervalTime.getText().toString().trim();
			if (!isNumeric(interval)) {
				showToast("请输入正确的间隔时间");
				return false;
			} else {
				return true;
			}
		}
		showToast("未输入起始时间或间隔时间");
		return false;
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.minute_check:
			showInputFragment(4);
			break;
		case R.id.minute_paikong:
			showInputFragment(5);
			break;

		case R.id.input_canghao:
			showInputFragment(1);
			break;

		case R.id.input_shuifen:
			showInputFragment(2);
			break;

		case R.id.input_shuliang:
			showInputFragment(3);
			break;

		case R.id.confirm:
			if (checkStartInput()) {
				Intent data = new Intent();
				int check = 0;
				int paikong = 0;
				if (!TextUtils.isEmpty(minuteCheck.getText().toString().trim())) {
					check = Integer
							.valueOf(minuteCheck.getText().toString().trim());
				}
				if (!TextUtils.isEmpty(minutePaikong.getText().toString()
						.trim())) {
					paikong = Integer.valueOf(minutePaikong.getText().toString()
							.trim());
				}
				String interval = intervalTime.getText().toString().trim();
				int interIn = Integer.valueOf(interval);
				data.putExtra("check_value", check);
				data.putExtra("paikong_value", paikong);
				data.putExtra("interval_time", interIn);
				data.putExtra("first_alarm_time", firstAlarmTime);
				sendMessageS(CMDCode.PREPARE_OK);
				setResult(RESULT_OK, data);
				finish();
			}
			break;
		case R.id.cancel:
			sendMessageS(CMDCode.PREPARE_CANCLE);
			finish();
			break;

		case R.id.set_rk_time:
			showDatePick();
			break;
			
		case R.id.start_time:
			showTimePicker();
			break;
			
		case R.id.interval_time:
			showInputFragment(6);
			break;

		default:
			break;
		}

	}

}