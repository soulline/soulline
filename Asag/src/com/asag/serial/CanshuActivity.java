package com.asag.serial;

import java.util.ArrayList;
import java.util.List;

import com.asag.serial.base.BaseActivity;
import com.asag.serial.fragment.BaseFragmentListener;
import com.asag.serial.fragment.InputSureFragment;
import com.asag.serial.mode.InputEntry;

import android.content.Intent;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

public class CanshuActivity extends BaseActivity implements OnClickListener {

	private Spinner sp1, sp2;
	private List<String> arr1;
	private List<String> arr2;

	private Button bt1, bt2;
	
	private TextView minuteCheck, minutePaikong;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.parasetting);
		InitUI();
		InitData();

		setClickListener();
	}

	public void InitUI() {
		sp1 = (Spinner) findViewById(R.id.liangzhong);
		sp2 = (Spinner) findViewById(R.id.chandi);

		bt1 = (Button) findViewById(R.id.confirm);
		Log.d("Test", "CanshuActivity.init bt1");
		bt2 = (Button) findViewById(R.id.cancel);
		minuteCheck = (TextView) findViewById(R.id.minute_check);
		minutePaikong = (TextView) findViewById(R.id.minute_paikong);
	}

	public void InitData() {

		arr1 = new ArrayList<String>();
		arr1.add("大麦");
		arr1.add("小麦");
		arr1.add("玉米");
		arr1.add("大豆");
		arr1.add("菜籽");
		arr1.add("饲料");

		arr2 = new ArrayList<String>();
		arr2.add("测试项目1");
		arr2.add("测试项目2");
		arr2.add("测试项目3");

		MyBaseAdapter adapter1 = new MyBaseAdapter(CanshuActivity.this, arr1);
		sp1.setAdapter(adapter1);
		MyBaseAdapter adapter2 = new MyBaseAdapter(CanshuActivity.this, arr2);
		sp2.setAdapter(adapter2);

	}

	private void setClickListener() {
		sp1.setOnItemSelectedListener(new SpinnerListener1());
		sp2.setOnItemSelectedListener(new SpinnerListener2());
		Log.d("Test", "para setClickclickbt1");
		bt1.setOnClickListener(this);
		Log.d("Test", "para setClickclickbt2");
		bt2.setOnClickListener(this);
		minutePaikong.setOnClickListener(this);
		minuteCheck.setOnClickListener(this);

		// bt1.setOnClickListener(new View.OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// // TODO Auto-generated method stub
		// Log.d("Test","para clickss");
		// startMainActivity();
		// }});

	}

	//

	//
	// public OnClickListener paraitemsOnClick = new OnClickListener(){
	//
	// public void onClick(View v) {
	//
	// switch (v.getId()) {
	// case R.id.queding:
	// Log.d("Test","para clickss");
	// startMainActivity();
	// break;
	// case R.id.quxiao:
	// Log.d("Test","para clickss");
	// startMainActivity();
	// break;
	//
	// default:
	// break;
	// }
	// }
	//
	//
	//
	// };

	public void startMainActivity() {
		Intent intent = new Intent(CanshuActivity.this, MainActivity.class);
		startActivity(intent);
		overridePendingTransition(android.R.anim.fade_in,
				android.R.anim.fade_out);
		// finish();
	}

	//
	// @Override
	// public void finish() {
	// // TODO Auto-generated method stub
	// super.finish();
	// }

	class SpinnerListener1 implements OnItemSelectedListener {

		@Override
		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO Auto-generated method stub
			// 这个函数当选择下拉菜单选项时调用，第一个参数是下拉菜单控件，第二个参数是当前被选中的Item,
			// 第三个参数是选中的位置，第四个参数是当前选中的Id。

			String selected1 = arg0.getItemAtPosition(arg2).toString();
			// text1.setText(selected1);

		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			// TODO Auto-generated method stub

		}
	}

	class SpinnerListener2 implements OnItemSelectedListener {

		@Override
		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO Auto-generated method stub
			// 这个函数当选择下拉菜单选项时调用，第一个参数是下拉菜单控件，第二个参数是当前被选中的Item,
			// 第三个参数是选中的位置，第四个参数是当前选中的Id。

			String selected2 = arg0.getItemAtPosition(arg2).toString();
			// text2.setText(selected2);
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			// TODO Auto-generated method stub

		}
	}


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.confirm:
			startMainActivity();
			Log.d("Test", "para clickss");
			break;
		case R.id.cancel:
			Log.d("Test", "para clickssssssssssss");
			startMainActivity();
			break;

		default:
			break;
		}

	}

}