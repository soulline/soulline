package com.asag.serial;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

import com.asag.serial.base.BaseActivity;
import com.asag.serial.data.AsagProvider;
import com.asag.serial.mode.PointInfo;
import com.asag.serial.utils.DataUtils;

public class PointLocationActivity extends BaseActivity implements OnClickListener{

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
	
	private ProgressDialog dialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.point_location_set_activity);
		initView();
	}
	
	private void initView() {
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
		findViewById(R.id.btn_ok).setOnClickListener(this);
		findViewById(R.id.btn_cancel).setOnClickListener(this);
	}

	private void savaData() {
		PointInfo info0 = new PointInfo();
		info0.way = "0";
		info0.xpoint = x_0_input.getText().toString().trim();
		info0.ypoint = y_0_input.getText().toString().trim();
		info0.zpoint = z_0_input.getText().toString().trim();
		savaValues(info0);
		PointInfo info1 = new PointInfo();
		info1.way = "1";
		info1.xpoint = x_1_input.getText().toString().trim();
		info1.ypoint = y_1_input.getText().toString().trim();
		info1.zpoint = z_1_input.getText().toString().trim();
		savaValues(info1);
		
		PointInfo info2 = new PointInfo();
		info2.way = "2";
		info2.xpoint = x_2_input.getText().toString().trim();
		info2.ypoint = y_2_input.getText().toString().trim();
		info2.zpoint = z_2_input.getText().toString().trim();
		savaValues(info2);
		
		PointInfo info3 = new PointInfo();
		info3.way = "3";
		info3.xpoint = x_3_input.getText().toString().trim();
		info3.ypoint = y_3_input.getText().toString().trim();
		info3.zpoint = z_3_input.getText().toString().trim();
		savaValues(info3);
		
		PointInfo info4 = new PointInfo();
		info4.way = "4";
		info4.xpoint = x_4_input.getText().toString().trim();
		info4.ypoint = y_4_input.getText().toString().trim();
		info4.zpoint = z_4_input.getText().toString().trim();
		savaValues(info4);
		
		PointInfo info5 = new PointInfo();
		info5.way = "5";
		info5.xpoint = x_5_input.getText().toString().trim();
		info5.ypoint = y_5_input.getText().toString().trim();
		info5.zpoint = z_5_input.getText().toString().trim();
		savaValues(info5);
		
		PointInfo info6 = new PointInfo();
		info6.way = "6";
		info6.xpoint = x_6_input.getText().toString().trim();
		info6.ypoint = y_6_input.getText().toString().trim();
		info6.zpoint = z_6_input.getText().toString().trim();
		savaValues(info6);
		
		PointInfo info7 = new PointInfo();
		info7.way = "7";
		info7.xpoint = x_7_input.getText().toString().trim();
		info7.ypoint = y_7_input.getText().toString().trim();
		info7.zpoint = z_7_input.getText().toString().trim();
		savaValues(info7);
		
		PointInfo info8 = new PointInfo();
		info8.way = "8";
		info8.xpoint = x_8_input.getText().toString().trim();
		info8.ypoint = y_8_input.getText().toString().trim();
		info8.zpoint = z_8_input.getText().toString().trim();
		savaValues(info8);
		
		PointInfo info9 = new PointInfo();
		info9.way = "9";
		info9.xpoint = x_9_input.getText().toString().trim();
		info9.ypoint = y_9_input.getText().toString().trim();
		info9.zpoint = z_9_input.getText().toString().trim();
		savaValues(info9);
		
		PointInfo info10 = new PointInfo();
		info10.way = "10";
		info10.xpoint = x_10_input.getText().toString().trim();
		info10.ypoint = y_10_input.getText().toString().trim();
		info10.zpoint = z_10_input.getText().toString().trim();
		savaValues(info10);
		
		PointInfo info11 = new PointInfo();
		info11.way = "11";
		info11.xpoint = x_11_input.getText().toString().trim();
		info11.ypoint = y_11_input.getText().toString().trim();
		info11.zpoint = z_11_input.getText().toString().trim();
		savaValues(info11);
		
		PointInfo info12 = new PointInfo();
		info12.way = "12";
		info12.xpoint = x_12_input.getText().toString().trim();
		info12.ypoint = y_12_input.getText().toString().trim();
		info12.zpoint = z_12_input.getText().toString().trim();
		savaValues(info12);
		
		PointInfo info13 = new PointInfo();
		info13.way = "13";
		info13.xpoint = x_13_input.getText().toString().trim();
		info13.ypoint = y_13_input.getText().toString().trim();
		info13.zpoint = z_13_input.getText().toString().trim();
		savaValues(info13);
		
		PointInfo info14 = new PointInfo();
		info14.way = "14";
		info14.xpoint = x_14_input.getText().toString().trim();
		info14.ypoint = y_14_input.getText().toString().trim();
		info14.zpoint = z_14_input.getText().toString().trim();
		savaValues(info14);
		
		PointInfo info15 = new PointInfo();
		info15.way = "15";
		info15.xpoint = x_15_input.getText().toString().trim();
		info15.ypoint = y_15_input.getText().toString().trim();
		info15.zpoint = z_15_input.getText().toString().trim();
		savaValues(info15);
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
					dialog.setTitle("正在加载中，请稍后...");
					dialog.show();
				} else if (dialog != null) {
					dialog.dismiss();
				}
			}
		});
	}
	
	private void savaValues(PointInfo info) {
		ContentValues values = new ContentValues();  
        values.put(AsagProvider.PointColumns.NUMBER, info.way);  
        values.put(AsagProvider.PointColumns.XPOINT, info.xpoint);  
        values.put(AsagProvider.PointColumns.YPOINT, info.ypoint);
        values.put(AsagProvider.PointColumns.ZPOINT, info.zpoint);
        boolean iscache = DataUtils.getPreferences(DataUtils.KEY_IS_CACHE, false);
        if (!iscache) {
        	getContentResolver().insert(AsagProvider.PointColumns.CONTENT_URI, values);
        	DataUtils.getPreferences(DataUtils.KEY_IS_CACHE, true);
        } else {
        	getContentResolver().update(AsagProvider.PointColumns.CONTENT_URI, values, AsagProvider.PointColumns.NUMBER
        			+ "=" + info.way, null);
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
