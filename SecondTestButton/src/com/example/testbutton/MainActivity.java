package com.example.testbutton;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.LinkedList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.dwin.navy.serialportapi.SerialPortOpt;
import com.example.testbutton.app.SerialApp;
import com.example.testbutton.base.BaseActivity;
import com.example.testbutton.fragment.BaseFragmentListener;
import com.example.testbutton.fragment.InputSureFragment;
import com.example.testbutton.mode.BaseEntry;
import com.example.testbutton.mode.CutDownEntry;
import com.example.testbutton.mode.RightDataEntry;
import com.example.testbutton.service.SerialService;
import com.example.testbutton.utils.CMDCode;
import com.example.testbutton.utils.SerialBroadCode;
import com.example.testbutton.utils.SerialRequestCode;
import com.example.testbutton.utils.ServiceUtil;

public class MainActivity extends BaseActivity implements OnClickListener{

	public FilePopupWindow filePopupWindow;
	public FunctionPopupWindow functionPopupWindow;
	public CDPopupWindow cdPopupWindow;
	public DetailsSetActivity detailsPopupWindow;
	public ParaSettingPopupWindow parasettingpopupwindow;
	// public View mView;
	public Button bt_file, bt_function,bt_ceding, bt_stop, bt_setting, bt_view,
			bt_inquire, bt_help;

	public Button bt1, bt2;
	
	//add by zhanghebin 2014-8-22
	private TextView textView1,textView2,textView3;
	
	private LocalBroadcastManager lbm = null;
	
	private int checkMinuteValue = 0;
	private int paikongMinuteValue = 0;
	
	private int wayCount = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		initService();
		lbm = LocalBroadcastManager.getInstance(this);
		InitUI();
		registerReceiver();
		
		functionPopupWindow = new FunctionPopupWindow(
				MainActivity.this, functionitemsOnClick);
		
		cdPopupWindow = new CDPopupWindow(
				MainActivity.this, cedingitemsOnClick);

		// 把文字控件添加监听，点击弹出自定义窗口
		bt_file.setOnClickListener(new OnClickListener() {

			@SuppressLint("NewApi")
			public void onClick(View v) {
				// 实例化SelectPicPopupWindow
				filePopupWindow = new FilePopupWindow(MainActivity.this,
						fileitemsOnClick);
				// 显示窗口
				filePopupWindow.showAsDropDown(v);
			}
		});
		bt_function.setOnClickListener(new OnClickListener() {

			@SuppressLint("NewApi")
			public void onClick(View v) {
				// 实例化SelectPicPopupWindow
				// 显示窗口
				functionPopupWindow.showAsDropDown(v);
			}
		});
		bt_ceding.setOnClickListener(new OnClickListener() {

			@SuppressLint("NewApi")
			public void onClick(View v) {
				// 实例化SelectPicPopupWindow
				// modify by zhanghebin 2014-8-6
				// 显示窗口
				cdPopupWindow.showAsDropDown(v);
				
				
				
				
				
			}
		});
		bt_view.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//setContentView(R.layout.mainview);
			//	InitUI();
				//System.out.println("33333333333333333");
			}
		});
		

		
		
	}

	// public View onCreateView(LayoutInflater inflater, ViewGroup container,
	// Bundle savedInstanceState) {
	// // TODO Auto-generated method stub
	// mView = inflater.inflate(R.layout.main, container, false);
	// InitUI();
	//
	// return mView;
	// }
	
	private void registerReceiver() {
		IntentFilter filter = new IntentFilter(SerialBroadCode.ACTION_PASSWAY_DATA);
		filter.addAction(SerialBroadCode.ACTION_CO2_RECEIVED);
		filter.addAction(SerialBroadCode.ACTION_O2_RECEIVED);
		filter.addAction(SerialBroadCode.ACTION_SEND_CUT_DOWN);
		lbm.registerReceiver(dataReceiver, filter);
	}
	
	public void initService() {
		if (!ServiceUtil.isServiceRunning("com.example.testbutton.service.SerialService")) {
			Intent service = new Intent(context, SerialService.class);
			startService(service);
		}
	}
	
	public BroadcastReceiver dataReceiver = new BroadcastReceiver() {
		
		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals(SerialBroadCode.ACTION_PASSWAY_DATA)) {
				RightDataEntry dataEntry = (RightDataEntry) intent.getSerializableExtra("right_data");
				if (dataEntry != null) {
					PadFragment fragment = (PadFragment) getSupportFragmentManager().findFragmentById(R.id.padFragment);
					fragment.addData(dataEntry);
					if (dataEntry.number.equals("15")) {
						showToast("检测结束");
						textView1.setText("0");
						wayCount = 0;
					} else {
						int number = Integer.valueOf(dataEntry.number);
						number += 1;	
						wayCount = number;
						textView1.setText(number + "");
						float check = ((float) checkMinuteValue) / 10.0f;
						float paikong = ((float) paikongMinuteValue) / 10.0f;
						fragment.updateCheckMinute(check + "");
						fragment.updatePaikongMinute(paikong + "");
					}
				}
			} else if (intent.getAction().equals(SerialBroadCode.ACTION_CO2_RECEIVED)) {
				PadFragment fragment = (PadFragment) getSupportFragmentManager().findFragmentById(R.id.padFragment);
				String co2Str = intent.getStringExtra("co2_data");
				if (!TextUtils.isEmpty(co2Str)) {
					int value = Integer.valueOf(co2Str);
					fragment.setCo2Value(value);
				}
			} else if (intent.getAction().equals(SerialBroadCode.ACTION_O2_RECEIVED)) {
				PadFragment fragment = (PadFragment) getSupportFragmentManager().findFragmentById(R.id.padFragment);
				String o2Str = intent.getStringExtra("o2_data");
				if (!TextUtils.isEmpty(o2Str)) {
					float value = Float.valueOf(o2Str);
					fragment.setO2Value(value);
				}
			} else if (intent.getAction().equals(SerialBroadCode.ACTION_SEND_CUT_DOWN)) {
				CutDownEntry entry = (CutDownEntry) intent.getSerializableExtra("cut_down");
				if (entry != null) {
					PadFragment fragment = (PadFragment) getSupportFragmentManager().findFragmentById(R.id.padFragment);
					if (entry.type == 1) {
						checkMinuteValue = entry.time;
						float check = ((float) checkMinuteValue) / 10.0f;
						fragment.updateCheckMinute(check + "");
					} else if (entry.type == 2) {
						paikongMinuteValue = entry.time;
						float paikong = ((float) paikongMinuteValue) / 10.0f;
						fragment.updatePaikongMinute(paikong + "");
					}
				}
			}
			
		}
	};
	
	public void InitUI() {
		bt_file = (Button) findViewById(R.id.bt_file);
		bt_function = (Button) findViewById(R.id.bt_function);
		bt_ceding=(Button) findViewById(R.id.bt_ceding);
		bt_stop = (Button) findViewById(R.id.bt_stop);
		bt_stop.setOnClickListener(this);
		bt_setting = (Button) findViewById(R.id.bt_setting);
		bt_view = (Button) findViewById(R.id.bt_view);

		bt_inquire = (Button) findViewById(R.id.bt_inqure);
		bt_help = (Button) findViewById(R.id.bt_help);

		bt1 = (Button) findViewById(R.id.confirm);
		Log.d("Test", "MainActivity.init bt1");
		bt2 = (Button) findViewById(R.id.cancel);
		
		//add by zhanghebin 2014-8-22
		textView1 = (TextView) findViewById(R.id.textView1);
		textView2 = (TextView) findViewById(R.id.textView2);
		textView3 = (TextView) findViewById(R.id.textView3);
	}

	public void finishAll() {
		super.finish();
		System.gc();
		android.os.Process.killProcess(android.os.Process.myPid());
	}

	public OnClickListener fileitemsOnClick = new OnClickListener() {

		public void onClick(View v) {
			filePopupWindow.dismiss();
			switch (v.getId()) {
			case R.id.lujing:
				break;
			case R.id.lianjie:
				break;
			case R.id.tuichu:
				Log.d("Test", "click finish");
				finishAll();
				break;
			default:
				break;
			}
		}
	};
	
	private void sendMessageS(String message) {
		Intent intent = new Intent(SerialBroadCode.ACTION_SEND_MESSAGE);
		intent.putExtra("send_message", message);
		lbm.sendBroadcast(intent);
	}
	
	// add by zhanghebin 2014-8-6
	public OnClickListener cedingitemsOnClick = new OnClickListener() {

		public void onClick(View v) {
			cdPopupWindow.dismiss();
			switch (v.getId()) {
			case R.id.liangan1:
				startCutDown();
				showToast("开始测定");
				sendMessageS(CMDCode.CD_LIANGAN_CHECK_1);
				// parasettingpopupwindow = new
				// ParaSettingPopupWindow(MainActivity.this, paraitemsOnClick);
				// parasettingpopupwindow.showAsDropDown(v);
				break;
			case R.id.liangan2:
				startCutDown();
				showToast("开始测定");
				sendMessageS(CMDCode.CD_LIANGAN_CHECK_2);
				// parasettingpopupwindow = new
				// ParaSettingPopupWindow(MainActivity.this, paraitemsOnClick);
				// parasettingpopupwindow.showAsDropDown(v);
				break;
			case R.id.canganjiance:
				startCutDown();
				showToast("开始测定");
				sendMessageS(CMDCode.CD_CANGAN_CHECK);
				// parasettingpopupwindow = new
				// ParaSettingPopupWindow(MainActivity.this, paraitemsOnClick);
				// parasettingpopupwindow.showAsDropDown(v);
				break;
			default:
				break;
			}
		}

	};
	
	public OnClickListener functionitemsOnClick = new OnClickListener() {

		public void onClick(View v) {
			functionPopupWindow.dismiss();
			switch (v.getId()) {
			case R.id.liangan1:
				sendMessageS(CMDCode.FF_LIANGAN_CHECK_1);
				startCanshuActivity();
				// parasettingpopupwindow = new
				// ParaSettingPopupWindow(MainActivity.this, paraitemsOnClick);
				// parasettingpopupwindow.showAsDropDown(v);
				break;
			case R.id.liangan2:
				sendMessageS(CMDCode.FF_LIANGAN_CHECK_2);
				startCanshuActivity();
				// parasettingpopupwindow = new
				// ParaSettingPopupWindow(MainActivity.this, paraitemsOnClick);
				// parasettingpopupwindow.showAsDropDown(v);
				break;
			case R.id.canganjiance:
				sendMessageS(CMDCode.FF_CANGAN_CHECK);
				startCanshuActivity();
				// parasettingpopupwindow = new
				// ParaSettingPopupWindow(MainActivity.this, paraitemsOnClick);
				// parasettingpopupwindow.showAsDropDown(v);
				break;
			default:
				break;
			}
		}

	};
	
	public void startCutDown() {
		app.isPause = false;
		String title = wayCount + "";
		textView1.setText(title);
		Intent check = new Intent(SerialBroadCode.ACTION_CHECK_MINUTE);
		check.putExtra("check_minute", checkMinuteValue + "");
		check.putExtra("paikong_minute", paikongMinuteValue + "");
		lbm.sendBroadcast(check);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK  && requestCode == SerialRequestCode.REQUEST_SET_DATA) {
			int check = data.getIntExtra("check_value", 0);
			int paikong = data.getIntExtra("paikong_value", 0);
			checkMinuteValue = check * 10;
			paikongMinuteValue = paikong * 10;
			wayCount = 0;
			app.oldCheckTime = checkMinuteValue;
			app.oldPaikongTime = paikongMinuteValue;
			PadFragment fragment = (PadFragment) getSupportFragmentManager().findFragmentById(R.id.padFragment);
//			fragment.clearRightData();
			if (checkMinuteValue > 0) {
				float checkF = ((float) checkMinuteValue) / 10.0f;
				fragment.updateCheckMinute(checkF + "");
			}
			if (paikongMinuteValue > 0) {
				float paikongF = ((float) paikongMinuteValue) / 10.0f;
				fragment.updatePaikongMinute(paikongF + "");
			}
		}
	}

	public void startCanshuActivity() {
		// 实例化SelectPicPopupWindow
//		detailsPopupWindow = new DetailsPopupWindow(context);
		/*displayFragment(true, "detail_setting", null, new BaseFragmentListener() {
			
			@Override
			public void onCallBack(Object object) {
				
			}
		});*/
		// 显示窗口
//		detailsPopupWindow.showAsDropDown(bt_function);

		
		  Intent intent = new Intent(MainActivity.this, DetailsSetActivity.class);
		  startActivityForResult(intent, SerialRequestCode.REQUEST_SET_DATA);
		  overridePendingTransition(R.anim.bottom_in,
				  R.anim.bottom_out);
		 
		// finish();
	}

	// @Override
	// public void finish() {
	// // TODO Auto-generated method stub
	// super.finish();
	// }

	//  add by zhanghebin 2014-8-6
	@Override
	protected void onDestroy() {
		super.onDestroy();
		Intent service = new Intent(context, SerialService.class);
		stopService(service);
	}
	
	
	
	
	public void displayFragment(boolean isOpen, String tag, Bundle bundle,
			BaseFragmentListener listener) {
		if (isOpen) {
			((BaseActivity) context).showFragment(tag, -1,
					createFragment(tag, bundle, listener));
		} else {
			((BaseActivity)context).closeFragment(tag);
		}
	}

	public DialogFragment createFragment(final String tag, Bundle b,
			BaseFragmentListener listener) {
		if (tag.equals("input_dialog")) {
			InputSureFragment inputF = new InputSureFragment(context, b);
			inputF.addFragmentListener(listener);
			return inputF;
		}
		return null;
	}

	
	// add by zhanghebin 2014-8-22 将数据进行界面更新
	Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			if (msg.arg1==1) {
				textView1.setText(msg.obj+"");
			}else if (msg.arg1==2) {
				textView2.setText(msg.obj+"");
			}else if (msg.arg1==3) {
				textView3.setText(msg.obj+"");
			}
		};
	};
	
	@Override
	public void onClick(View v) {
			switch (v.getId()) {
			case R.id.bt_stop:
				if (!app.isPause) {
					sendMessageS(CMDCode.STOP_CMD);
					app.isPause = true;
					showToast("停止检测");
					bt_stop.setText("继续");
				} else {
					sendMessageS(CMDCode.STOP_CMD);
					app.isPause = false;
					showToast("继续检测");
					bt_stop.setText("停止");
				}
				break;

			default:
				
				break;
			}
			
	}
	
}
