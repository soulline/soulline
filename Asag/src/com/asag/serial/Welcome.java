package com.asag.serial;

import java.text.SimpleDateFormat;
import java.util.regex.Pattern;

import com.asag.serial.base.BaseActivity;
import com.asag.serial.service.SerialService;
import com.asag.serial.utils.CMDCode;
import com.asag.serial.utils.DataUtils;
import com.asag.serial.utils.SerialBroadCode;
import com.asag.serial.utils.ServiceUtil;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;

public class Welcome extends BaseActivity {

	private ImageView welcomeLoading;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setFullScreen();
		setContentView(R.layout.start);
		initView();
		initService();
		handler.postDelayed(new Runnable() {

			@Override
			public void run() {
				sendIfhasData();
			}
		}, 1000);

	}

	public void initService() {
		if (!ServiceUtil
				.isServiceRunning("com.example.testbutton.service.SerialService")) {
			Intent service = new Intent(context, SerialService.class);
			startService(service);
		}
	}

	private void sleep() {
		try {
			Log.i("Serial", "start --- sleep(2000)...");
			Thread.sleep(2000);
		} catch (InterruptedException e) {
		}
	}

	private void sendIfhasData() {
		sendMessageS(CMDCode.DATA_TRANSFER_START);
		handler.postDelayed(new Runnable() {

			@Override
			public void run() {
				String canghao = DataUtils.getPreferences("canghao_data", "");
				if (!TextUtils.isEmpty(canghao)) {
					sendMessageS(getHexString(canghao, 1));
				}
			}
		}, 2000);
		handler.postDelayed(new Runnable() {

			@Override
			public void run() {
				String liangzhongcode = DataUtils.getPreferences(
						"liangzhong_data", "");
				if (!TextUtils.isEmpty(liangzhongcode)) {
					sendFoodCode(liangzhongcode);
				}
			}
		}, 4000);
		handler.postDelayed(new Runnable() {

			@Override
			public void run() {
				String count = DataUtils.getPreferences("check_count_data", "");
				if (!TextUtils.isEmpty(count)) {
					sendMessageS(getHexString(count, 3));
				}
			}
		}, 6000);
		handler.postDelayed(new Runnable() {

			@Override
			public void run() {
				String shuifen = DataUtils.getPreferences("shuifen_data", "");
				if (!TextUtils.isEmpty(shuifen)) {
					sendMessageS(getHexString(shuifen, 2));
				}
			}
		}, 8000);

		handler.postDelayed(new Runnable() {

			@Override
			public void run() {
				String timeMsg = DataUtils.getPreferences("ruku_date", "");
				if (!TextUtils.isEmpty(timeMsg)) {
					sendMessageS(getHexString(timeMsg, 6));
				}
			}
		}, 10000);

		handler.postDelayed(new Runnable() {

			@Override
			public void run() {
				String chandiCode = DataUtils.getPreferences("chandi_data", "");
				if (!TextUtils.isEmpty(chandiCode)) {
					sendChandiCode(chandiCode);
				}
				sendMessageS(CMDCode.DATA_TRANSFER_FINISH);
			}
		}, 12000);

		
		
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
		sb.append(" FF FF");
		return sb.toString();
	}

	public static boolean isLetter(String str) {
		Pattern pattern = Pattern.compile("[a-zA-Z]+");
		return pattern.matcher(str).matches();
	}

	public static boolean isNumeric(String str) {
		Pattern pattern = Pattern.compile("[0-9]*");
		return pattern.matcher(str).matches();
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

	private void sendMessageS(String message) {
		Intent intent = new Intent(SerialBroadCode.ACTION_SEND_MESSAGE);
		intent.putExtra("send_message", message);
		LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
	}

	private void sendChandiCode(String chandiCode) {
		if (TextUtils.isEmpty(chandiCode))
			return;
		String chandiStrMsg = CMDCode.DATA_CHANDI + " " + chandiCode + "FF FF";
		sendMessageS(chandiStrMsg);
	}

	private void sendFoodCode(String foodCode) {
		if (TextUtils.isEmpty(foodCode))
			return;
		String foodStr = CMDCode.DATA_LIANGZHONG + " " + foodCode + "FF FF";
		sendMessageS(foodStr);
	}

	private void initView() {
		welcomeLoading = (ImageView) findViewById(R.id.welcome_loading);
		welcomeLoading.setImageResource(R.drawable.start_loading);
		final AnimationDrawable animDrawable = (AnimationDrawable) welcomeLoading
				.getDrawable();
		animDrawable.start();
		handler.postDelayed(new Runnable() {

			@Override
			public void run() {
				Intent intent = new Intent(context, MainPageActivity.class);
				startActivity(intent);
				finish();
			}
		}, 29000);
	}

}
