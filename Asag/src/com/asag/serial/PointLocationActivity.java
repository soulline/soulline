package com.asag.serial;

import java.util.ArrayList;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.asag.serial.base.BaseActivity;
import com.asag.serial.data.AsagProvider;
import com.asag.serial.mode.PointInfo;
import com.asag.serial.utils.DataUtils;

public class PointLocationActivity extends BaseActivity implements
		OnClickListener {

	private EditText x_0_input, y_0_input, z_0_input;

	private EditText x_1_input, y_1_input, z_1_input;

	private EditText x_2_input, y_2_input, z_2_input;

	private EditText x_3_input, y_3_input, z_3_input;

	private EditText x_4_input, y_4_input, z_4_input;

	private EditText x_5_input, y_5_input, z_5_input;

	private EditText x_6_input, y_6_input, z_6_input;

	private EditText x_7_input, y_7_input, z_7_input;

	private EditText x_8_input, y_8_input, z_8_input;

	private EditText x_9_input, y_9_input, z_9_input;

	private EditText x_10_input, y_10_input, z_10_input;

	private EditText x_11_input, y_11_input, z_11_input;

	private EditText x_12_input, y_12_input, z_12_input;

	private EditText x_13_input, y_13_input, z_13_input;

	private EditText x_14_input, y_14_input, z_14_input;

	private EditText x_15_input, y_15_input, z_15_input;

	private TextView number_title, x_title, y_title, z_title;

	private TextView number_0_title, number_1_title, number_2_title,
			number_3_title, number_4_title, number_5_title, number_6_title,
			number_7_title, number_8_title, number_9_title, number_10_title,
			number_11_title, number_12_title, number_13_title, number_14_title,
			number_15_title;
	
	private Button btn_ok, btn_cancel;
	
	private TextView top_title_tx;

	private ProgressDialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.point_location_set_activity);
		initView();
		initTextSize();
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				loadData();
			}
		}).start();
	}

	private void initTextSize() {
		int size = DataUtils.getPreferences(DataUtils.KEY_TEXT_SIZE, 5);
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
		x_0_input.setTextSize(x_0_input.getTextSize() * size);
		y_0_input.setTextSize(y_0_input.getTextSize() * size);
		z_0_input.setTextSize(z_0_input.getTextSize() * size);
		
		x_1_input.setTextSize(x_1_input.getTextSize() * size);
		y_1_input.setTextSize(y_1_input.getTextSize() * size);
		z_1_input.setTextSize(z_1_input.getTextSize() * size);
		
		x_2_input.setTextSize(x_2_input.getTextSize() * size);
		y_2_input.setTextSize(y_2_input.getTextSize() * size);
		z_2_input.setTextSize(z_2_input.getTextSize() * size);
		
		x_3_input.setTextSize(x_3_input.getTextSize() * size);
		y_3_input.setTextSize(y_3_input.getTextSize() * size);
		z_3_input.setTextSize(z_3_input.getTextSize() * size);
		
		x_4_input.setTextSize(x_4_input.getTextSize() * size);
		y_4_input.setTextSize(y_4_input.getTextSize() * size);
		z_4_input.setTextSize(z_4_input.getTextSize() * size);
		
		x_5_input.setTextSize(x_5_input.getTextSize() * size);
		y_5_input.setTextSize(y_5_input.getTextSize() * size);
		z_5_input.setTextSize(z_5_input.getTextSize() * size);
		
		x_6_input.setTextSize(x_6_input.getTextSize() * size);
		y_6_input.setTextSize(y_6_input.getTextSize() * size);
		z_6_input.setTextSize(z_6_input.getTextSize() * size);
		
		x_7_input.setTextSize(x_7_input.getTextSize() * size);
		y_7_input.setTextSize(y_7_input.getTextSize() * size);
		z_7_input.setTextSize(z_7_input.getTextSize() * size);
		
		x_8_input.setTextSize(x_8_input.getTextSize() * size);
		y_8_input.setTextSize(y_8_input.getTextSize() * size);
		z_8_input.setTextSize(z_8_input.getTextSize() * size);
		
		x_9_input.setTextSize(x_9_input.getTextSize() * size);
		y_9_input.setTextSize(y_9_input.getTextSize() * size);
		z_9_input.setTextSize(z_9_input.getTextSize() * size);
		
		x_10_input.setTextSize(x_10_input.getTextSize() * size);
		y_10_input.setTextSize(y_10_input.getTextSize() * size);
		z_10_input.setTextSize(z_10_input.getTextSize() * size);
		
		x_11_input.setTextSize(x_11_input.getTextSize() * size);
		y_11_input.setTextSize(y_11_input.getTextSize() * size);
		z_11_input.setTextSize(z_11_input.getTextSize() * size);
		
		x_12_input.setTextSize(x_12_input.getTextSize() * size);
		y_12_input.setTextSize(y_12_input.getTextSize() * size);
		z_12_input.setTextSize(z_12_input.getTextSize() * size);
		
		x_13_input.setTextSize(x_13_input.getTextSize() * size);
		y_13_input.setTextSize(y_13_input.getTextSize() * size);
		z_13_input.setTextSize(z_13_input.getTextSize() * size);
		
		x_14_input.setTextSize(x_14_input.getTextSize() * size);
		y_14_input.setTextSize(y_14_input.getTextSize() * size);
		z_14_input.setTextSize(z_14_input.getTextSize() * size);
		
		x_15_input.setTextSize(x_15_input.getTextSize() * size);
		y_15_input.setTextSize(y_15_input.getTextSize() * size);
		z_15_input.setTextSize(z_15_input.getTextSize() * size);
		
		number_title.setTextSize(number_title.getTextSize() * size);
		x_title.setTextSize(x_title.getTextSize() * size);
		y_title.setTextSize(y_title.getTextSize() * size);
		z_title.setTextSize(z_title.getTextSize() * size);

		number_0_title.setTextSize(number_0_title.getTextSize() * size);
		number_1_title.setTextSize(number_1_title.getTextSize() * size);
		number_2_title.setTextSize(number_2_title.getTextSize() * size);
		number_3_title.setTextSize(number_3_title.getTextSize() * size);
		number_4_title.setTextSize(number_4_title.getTextSize() * size);
		number_5_title.setTextSize(number_5_title.getTextSize() * size);
		number_6_title.setTextSize(number_6_title.getTextSize() * size);
		number_7_title.setTextSize(number_7_title.getTextSize() * size);
		number_8_title.setTextSize(number_8_title.getTextSize() * size);
		number_9_title.setTextSize(number_9_title.getTextSize() * size);
		number_10_title.setTextSize(number_10_title.getTextSize() * size);
		number_11_title.setTextSize(number_11_title.getTextSize() * size);
		number_12_title.setTextSize(number_12_title.getTextSize() * size);
		number_13_title.setTextSize(number_13_title.getTextSize() * size);
		number_14_title.setTextSize(number_14_title.getTextSize() * size);
		number_15_title.setTextSize(number_15_title.getTextSize() * size);
		
		top_title_tx.setTextSize(top_title_tx.getTextSize() * size);
		btn_ok.setTextSize(btn_ok.getTextSize() * size);
		btn_cancel.setTextSize(btn_cancel.getTextSize() * size);
	}

	private void initView() {
		top_title_tx = (TextView) findViewById(R.id.top_title_tx);
		
		x_0_input = (EditText) findViewById(R.id.x_0_input);
		y_0_input = (EditText) findViewById(R.id.y_0_input);
		z_0_input = (EditText) findViewById(R.id.z_0_input);

		x_1_input = (EditText) findViewById(R.id.x_1_input);
		y_1_input = (EditText) findViewById(R.id.y_1_input);
		z_1_input = (EditText) findViewById(R.id.z_1_input);

		x_2_input = (EditText) findViewById(R.id.x_2_input);
		y_2_input = (EditText) findViewById(R.id.y_2_input);
		z_2_input = (EditText) findViewById(R.id.z_2_input);

		x_3_input = (EditText) findViewById(R.id.x_3_input);
		y_3_input = (EditText) findViewById(R.id.y_3_input);
		z_3_input = (EditText) findViewById(R.id.z_3_input);

		x_4_input = (EditText) findViewById(R.id.x_4_input);
		y_4_input = (EditText) findViewById(R.id.y_4_input);
		z_4_input = (EditText) findViewById(R.id.z_4_input);

		x_5_input = (EditText) findViewById(R.id.x_5_input);
		y_5_input = (EditText) findViewById(R.id.y_5_input);
		z_5_input = (EditText) findViewById(R.id.z_5_input);

		x_6_input = (EditText) findViewById(R.id.x_6_input);
		y_6_input = (EditText) findViewById(R.id.y_6_input);
		z_6_input = (EditText) findViewById(R.id.z_6_input);

		x_7_input = (EditText) findViewById(R.id.x_7_input);
		y_7_input = (EditText) findViewById(R.id.y_7_input);
		z_7_input = (EditText) findViewById(R.id.z_7_input);

		x_8_input = (EditText) findViewById(R.id.x_8_input);
		y_8_input = (EditText) findViewById(R.id.y_8_input);
		z_8_input = (EditText) findViewById(R.id.z_8_input);

		x_9_input = (EditText) findViewById(R.id.x_9_input);
		y_9_input = (EditText) findViewById(R.id.y_9_input);
		z_9_input = (EditText) findViewById(R.id.z_9_input);

		x_10_input = (EditText) findViewById(R.id.x_10_input);
		y_10_input = (EditText) findViewById(R.id.y_10_input);
		z_10_input = (EditText) findViewById(R.id.z_10_input);

		x_11_input = (EditText) findViewById(R.id.x_11_input);
		y_11_input = (EditText) findViewById(R.id.y_11_input);
		z_11_input = (EditText) findViewById(R.id.z_11_input);

		x_12_input = (EditText) findViewById(R.id.x_12_input);
		y_12_input = (EditText) findViewById(R.id.y_12_input);
		z_12_input = (EditText) findViewById(R.id.z_12_input);

		x_13_input = (EditText) findViewById(R.id.x_13_input);
		y_13_input = (EditText) findViewById(R.id.y_13_input);
		z_13_input = (EditText) findViewById(R.id.z_13_input);

		x_14_input = (EditText) findViewById(R.id.x_14_input);
		y_14_input = (EditText) findViewById(R.id.y_14_input);
		z_14_input = (EditText) findViewById(R.id.z_14_input);

		x_15_input = (EditText) findViewById(R.id.x_15_input);
		y_15_input = (EditText) findViewById(R.id.y_15_input);
		z_15_input = (EditText) findViewById(R.id.z_15_input);

		number_title = (TextView) findViewById(R.id.number_title);
		x_title = (TextView) findViewById(R.id.x_title);
		y_title = (TextView) findViewById(R.id.y_title);
		z_title = (TextView) findViewById(R.id.z_title);
		number_0_title = (TextView) findViewById(R.id.number_0_title);
		number_1_title = (TextView) findViewById(R.id.number_1_title);
		number_2_title = (TextView) findViewById(R.id.number_2_title);
		number_3_title = (TextView) findViewById(R.id.number_3_title);
		number_4_title = (TextView) findViewById(R.id.number_4_title);
		number_5_title = (TextView) findViewById(R.id.number_5_title);
		number_6_title = (TextView) findViewById(R.id.number_6_title);
		number_7_title = (TextView) findViewById(R.id.number_7_title);
		number_8_title = (TextView) findViewById(R.id.number_8_title);
		number_9_title = (TextView) findViewById(R.id.number_9_title);
		number_10_title = (TextView) findViewById(R.id.number_10_title);
		number_11_title = (TextView) findViewById(R.id.number_11_title);
		number_12_title = (TextView) findViewById(R.id.number_12_title);
		number_13_title = (TextView) findViewById(R.id.number_13_title);
		number_14_title = (TextView) findViewById(R.id.number_14_title);
		number_15_title = (TextView) findViewById(R.id.number_15_title);

		btn_ok = (Button) findViewById(R.id.btn_ok);
		btn_cancel = (Button) findViewById(R.id.btn_cancel);
		findViewById(R.id.btn_ok).setOnClickListener(this);
		findViewById(R.id.btn_cancel).setOnClickListener(this);
	}
	
	private void loadData() {
		final ArrayList<PointInfo> list = queryData();
		
		if (list.size() > 0) {
			handler.post(new Runnable() {
				
				@Override
				public void run() {
					for (PointInfo info : list) {
						if (info.way.equals("0")) {
							if (!TextUtils.isEmpty(info.xpoint)) {
								x_0_input.setText(info.xpoint);
							}
							if (!TextUtils.isEmpty(info.ypoint)) {
								y_0_input.setText(info.ypoint);
							}
							if (!TextUtils.isEmpty(info.zpoint)) {
								z_0_input.setText(info.zpoint);
							}
						} else if (info.way.equals("1")) {
							if (!TextUtils.isEmpty(info.xpoint)) {
								x_1_input.setText(info.xpoint);
							}
							if (!TextUtils.isEmpty(info.ypoint)) {
								y_1_input.setText(info.ypoint);
							}
							if (!TextUtils.isEmpty(info.zpoint)) {
								z_1_input.setText(info.zpoint);
							}
						} else if (info.way.equals("2")) {
							if (!TextUtils.isEmpty(info.xpoint)) {
								x_2_input.setText(info.xpoint);
							}
							if (!TextUtils.isEmpty(info.ypoint)) {
								y_2_input.setText(info.ypoint);
							}
							if (!TextUtils.isEmpty(info.zpoint)) {
								z_2_input.setText(info.zpoint);
							}
						} else if (info.way.equals("3")) {
							if (!TextUtils.isEmpty(info.xpoint)) {
								x_3_input.setText(info.xpoint);
							}
							if (!TextUtils.isEmpty(info.ypoint)) {
								y_3_input.setText(info.ypoint);
							}
							if (!TextUtils.isEmpty(info.zpoint)) {
								z_3_input.setText(info.zpoint);
							}
						} else if (info.way.equals("4")) {
							if (!TextUtils.isEmpty(info.xpoint)) {
								x_4_input.setText(info.xpoint);
							}
							if (!TextUtils.isEmpty(info.ypoint)) {
								y_4_input.setText(info.ypoint);
							}
							if (!TextUtils.isEmpty(info.zpoint)) {
								z_4_input.setText(info.zpoint);
							}
						} else if (info.way.equals("5")) {
							if (!TextUtils.isEmpty(info.xpoint)) {
								x_5_input.setText(info.xpoint);
							}
							if (!TextUtils.isEmpty(info.ypoint)) {
								y_5_input.setText(info.ypoint);
							}
							if (!TextUtils.isEmpty(info.zpoint)) {
								z_5_input.setText(info.zpoint);
							}
						} else if (info.way.equals("6")) {
							if (!TextUtils.isEmpty(info.xpoint)) {
								x_6_input.setText(info.xpoint);
							}
							if (!TextUtils.isEmpty(info.ypoint)) {
								y_6_input.setText(info.ypoint);
							}
							if (!TextUtils.isEmpty(info.zpoint)) {
								z_6_input.setText(info.zpoint);
							}
						} else if (info.way.equals("7")) {
							if (!TextUtils.isEmpty(info.xpoint)) {
								x_7_input.setText(info.xpoint);
							}
							if (!TextUtils.isEmpty(info.ypoint)) {
								y_7_input.setText(info.ypoint);
							}
							if (!TextUtils.isEmpty(info.zpoint)) {
								z_7_input.setText(info.zpoint);
							}
						} else if (info.way.equals("8")) {
							if (!TextUtils.isEmpty(info.xpoint)) {
								x_8_input.setText(info.xpoint);
							}
							if (!TextUtils.isEmpty(info.ypoint)) {
								y_8_input.setText(info.ypoint);
							}
							if (!TextUtils.isEmpty(info.zpoint)) {
								z_8_input.setText(info.zpoint);
							}
						} else if (info.way.equals("9")) {
							if (!TextUtils.isEmpty(info.xpoint)) {
								x_9_input.setText(info.xpoint);
							}
							if (!TextUtils.isEmpty(info.ypoint)) {
								y_9_input.setText(info.ypoint);
							}
							if (!TextUtils.isEmpty(info.zpoint)) {
								z_9_input.setText(info.zpoint);
							}
						} else if (info.way.equals("10")) {
							if (!TextUtils.isEmpty(info.xpoint)) {
								x_10_input.setText(info.xpoint);
							}
							if (!TextUtils.isEmpty(info.ypoint)) {
								y_10_input.setText(info.ypoint);
							}
							if (!TextUtils.isEmpty(info.zpoint)) {
								z_10_input.setText(info.zpoint);
							}
						} else if (info.way.equals("11")) {
							if (!TextUtils.isEmpty(info.xpoint)) {
								x_11_input.setText(info.xpoint);
							}
							if (!TextUtils.isEmpty(info.ypoint)) {
								y_11_input.setText(info.ypoint);
							}
							if (!TextUtils.isEmpty(info.zpoint)) {
								z_11_input.setText(info.zpoint);
							}
						} else if (info.way.equals("12")) {
							if (!TextUtils.isEmpty(info.xpoint)) {
								x_12_input.setText(info.xpoint);
							}
							if (!TextUtils.isEmpty(info.ypoint)) {
								y_12_input.setText(info.ypoint);
							}
							if (!TextUtils.isEmpty(info.zpoint)) {
								z_12_input.setText(info.zpoint);
							}
						} else if (info.way.equals("13")) {
							if (!TextUtils.isEmpty(info.xpoint)) {
								x_13_input.setText(info.xpoint);
							}
							if (!TextUtils.isEmpty(info.ypoint)) {
								y_13_input.setText(info.ypoint);
							}
							if (!TextUtils.isEmpty(info.zpoint)) {
								z_13_input.setText(info.zpoint);
							}
						} else if (info.way.equals("14")) {
							if (!TextUtils.isEmpty(info.xpoint)) {
								x_14_input.setText(info.xpoint);
							}
							if (!TextUtils.isEmpty(info.ypoint)) {
								y_14_input.setText(info.ypoint);
							}
							if (!TextUtils.isEmpty(info.zpoint)) {
								z_14_input.setText(info.zpoint);
							}
						} else if (info.way.equals("15")) {
							if (!TextUtils.isEmpty(info.xpoint)) {
								x_15_input.setText(info.xpoint);
							}
							if (!TextUtils.isEmpty(info.ypoint)) {
								y_15_input.setText(info.ypoint);
							}
							if (!TextUtils.isEmpty(info.zpoint)) {
								z_15_input.setText(info.zpoint);
							}
						}
					}
				}
			});
			
		}
	}
	
	private ArrayList<PointInfo> queryData() {
		ArrayList<PointInfo> list = new ArrayList<PointInfo>();
		Cursor cursor = getContentResolver().query(AsagProvider.PointColumns.CONTENT_URI, new String[] { AsagProvider.PointColumns.NUMBER, AsagProvider.PointColumns.XPOINT, AsagProvider.PointColumns.YPOINT
				,AsagProvider.PointColumns.ZPOINT}, null, null, null);
		if (cursor != null) {
			while(cursor.moveToNext()) {
				PointInfo point = new PointInfo();
				point.way = cursor.getString(cursor.getColumnIndexOrThrow(AsagProvider.PointColumns.NUMBER));
				point.xpoint = cursor.getString(cursor.getColumnIndexOrThrow(AsagProvider.PointColumns.XPOINT));
				point.ypoint = cursor.getString(cursor.getColumnIndexOrThrow(AsagProvider.PointColumns.YPOINT));
				point.zpoint = cursor.getString(cursor.getColumnIndexOrThrow(AsagProvider.PointColumns.ZPOINT));
				list.add(point);
			}
			cursor.close();
		}
		return list;
	}

	private void savaData() {
		PointInfo info0 = new PointInfo();
		info0.way = "0";
		info0.xpoint = x_0_input.getText().toString().trim();
		info0.ypoint = y_0_input.getText().toString().trim();
		info0.zpoint = z_0_input.getText().toString().trim();
		saveValues(info0);
		PointInfo info1 = new PointInfo();
		info1.way = "1";
		info1.xpoint = x_1_input.getText().toString().trim();
		info1.ypoint = y_1_input.getText().toString().trim();
		info1.zpoint = z_1_input.getText().toString().trim();
		saveValues(info1);

		PointInfo info2 = new PointInfo();
		info2.way = "2";
		info2.xpoint = x_2_input.getText().toString().trim();
		info2.ypoint = y_2_input.getText().toString().trim();
		info2.zpoint = z_2_input.getText().toString().trim();
		saveValues(info2);

		PointInfo info3 = new PointInfo();
		info3.way = "3";
		info3.xpoint = x_3_input.getText().toString().trim();
		info3.ypoint = y_3_input.getText().toString().trim();
		info3.zpoint = z_3_input.getText().toString().trim();
		saveValues(info3);

		PointInfo info4 = new PointInfo();
		info4.way = "4";
		info4.xpoint = x_4_input.getText().toString().trim();
		info4.ypoint = y_4_input.getText().toString().trim();
		info4.zpoint = z_4_input.getText().toString().trim();
		saveValues(info4);

		PointInfo info5 = new PointInfo();
		info5.way = "5";
		info5.xpoint = x_5_input.getText().toString().trim();
		info5.ypoint = y_5_input.getText().toString().trim();
		info5.zpoint = z_5_input.getText().toString().trim();
		saveValues(info5);

		PointInfo info6 = new PointInfo();
		info6.way = "6";
		info6.xpoint = x_6_input.getText().toString().trim();
		info6.ypoint = y_6_input.getText().toString().trim();
		info6.zpoint = z_6_input.getText().toString().trim();
		saveValues(info6);

		PointInfo info7 = new PointInfo();
		info7.way = "7";
		info7.xpoint = x_7_input.getText().toString().trim();
		info7.ypoint = y_7_input.getText().toString().trim();
		info7.zpoint = z_7_input.getText().toString().trim();
		saveValues(info7);

		PointInfo info8 = new PointInfo();
		info8.way = "8";
		info8.xpoint = x_8_input.getText().toString().trim();
		info8.ypoint = y_8_input.getText().toString().trim();
		info8.zpoint = z_8_input.getText().toString().trim();
		saveValues(info8);

		PointInfo info9 = new PointInfo();
		info9.way = "9";
		info9.xpoint = x_9_input.getText().toString().trim();
		info9.ypoint = y_9_input.getText().toString().trim();
		info9.zpoint = z_9_input.getText().toString().trim();
		saveValues(info9);

		PointInfo info10 = new PointInfo();
		info10.way = "10";
		info10.xpoint = x_10_input.getText().toString().trim();
		info10.ypoint = y_10_input.getText().toString().trim();
		info10.zpoint = z_10_input.getText().toString().trim();
		saveValues(info10);

		PointInfo info11 = new PointInfo();
		info11.way = "11";
		info11.xpoint = x_11_input.getText().toString().trim();
		info11.ypoint = y_11_input.getText().toString().trim();
		info11.zpoint = z_11_input.getText().toString().trim();
		saveValues(info11);

		PointInfo info12 = new PointInfo();
		info12.way = "12";
		info12.xpoint = x_12_input.getText().toString().trim();
		info12.ypoint = y_12_input.getText().toString().trim();
		info12.zpoint = z_12_input.getText().toString().trim();
		saveValues(info12);

		PointInfo info13 = new PointInfo();
		info13.way = "13";
		info13.xpoint = x_13_input.getText().toString().trim();
		info13.ypoint = y_13_input.getText().toString().trim();
		info13.zpoint = z_13_input.getText().toString().trim();
		saveValues(info13);

		PointInfo info14 = new PointInfo();
		info14.way = "14";
		info14.xpoint = x_14_input.getText().toString().trim();
		info14.ypoint = y_14_input.getText().toString().trim();
		info14.zpoint = z_14_input.getText().toString().trim();
		saveValues(info14);

		PointInfo info15 = new PointInfo();
		info15.way = "15";
		info15.xpoint = x_15_input.getText().toString().trim();
		info15.ypoint = y_15_input.getText().toString().trim();
		info15.zpoint = z_15_input.getText().toString().trim();
		saveValues(info15);
		showToast("保存成功");
		showLoading(false);
		handler.post(new Runnable() {

			@Override
			public void run() {
				finish();
			}
		});
	}

	private void showLoading(final boolean isShow) {
		handler.post(new Runnable() {

			@Override
			public void run() {
				if (isShow) {
					dialog = new ProgressDialog(context);
					dialog.setTitle("正在加载中，请稍候...");
					dialog.show();
				} else if (dialog != null) {
					dialog.dismiss();
				}
			}
		});
	}

	private void saveValues(PointInfo info) {
		ContentValues values = new ContentValues();
		values.put(AsagProvider.PointColumns.NUMBER, info.way);
		values.put(AsagProvider.PointColumns.XPOINT, info.xpoint);
		values.put(AsagProvider.PointColumns.YPOINT, info.ypoint);
		values.put(AsagProvider.PointColumns.ZPOINT, info.zpoint);
		Cursor cursor = getContentResolver().query(
				AsagProvider.PointColumns.CONTENT_URI,
				new String[] { AsagProvider.PointColumns._ID,
						AsagProvider.PointColumns.NUMBER,
						AsagProvider.PointColumns.XPOINT,
						AsagProvider.PointColumns.YPOINT,
						AsagProvider.PointColumns.ZPOINT }, AsagProvider.PointColumns.NUMBER + "='" + 
								info.way + "'", null,
								null);
		Log.d("zhao", "save Point value cursor : " + cursor);
		if (cursor != null) {
			Log.d("zhao", "save Point value count : " + cursor.getCount());
		}
		if (cursor != null && cursor.getCount() > 0) {
			getContentResolver().update(AsagProvider.PointColumns.CONTENT_URI,
					values, AsagProvider.PointColumns.NUMBER + "=" + info.way,
					null);
		} else {
			getContentResolver().insert(AsagProvider.PointColumns.CONTENT_URI,
					values);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_ok:
			showLoading(true);
			new Thread(new Runnable() {

				@Override
				public void run() {
					savaData();
				}
			}).start();
			break;

		case R.id.btn_cancel:
			finish();
			break;
		default:
			break;
		}

	}

}
