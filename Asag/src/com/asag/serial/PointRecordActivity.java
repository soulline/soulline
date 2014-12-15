package com.asag.serial;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;

import com.asag.serial.base.BaseActivity;

public class PointRecordActivity extends BaseActivity {

	private TextView top_title_tx;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.point_record_activity);
		initView();
		initContent();
	}
	
	private void initView() {
		top_title_tx = (TextView) findViewById(R.id.top_title_tx);
	}
	
	private void initContent() {
		String title = getIntent().getStringExtra("record_title");
		if (!TextUtils.isEmpty(title)) {
			top_title_tx.setText(title);
		}
	}

}
