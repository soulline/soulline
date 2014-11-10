package com.cdd.activity.minepage;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.cdd.R;
import com.cdd.base.BaseActivity;

public class SettingActivity extends BaseActivity implements OnClickListener{

	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.setting_activity);
		initTitle("设置");
		initView();
	}
	
	private void initView() {
		findViewById(R.id.version_check_layout).setOnClickListener(this);
		findViewById(R.id.mianze_layout).setOnClickListener(this);
		findViewById(R.id.about_layout).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.version_check_layout:
			
			break;

		default:
			break;
		}
		
	}

}
