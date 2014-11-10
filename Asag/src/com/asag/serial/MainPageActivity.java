package com.asag.serial;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.asag.serial.base.BaseActivity;

public class MainPageActivity extends BaseActivity implements OnClickListener{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_activity);
		initView();
	}
	
	private void initView() {
		findViewById(R.id.file_menu).setOnClickListener(this);
		findViewById(R.id.function_menu).setOnClickListener(this);
		findViewById(R.id.ceding_menu).setOnClickListener(this);
		findViewById(R.id.stop_menu).setOnClickListener(this);
		findViewById(R.id.setting_menu).setOnClickListener(this);
		findViewById(R.id.shitu_menu).setOnClickListener(this);
		findViewById(R.id.search_menu).setOnClickListener(this);
		findViewById(R.id.help_menu).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.file_menu:
			
			break;
			
		case R.id.function_menu:
			
			break;
			
		case R.id.ceding_menu:
			
			break;

		case R.id.stop_menu:
			
			break;
			
		case R.id.setting_menu:
			
			break;
			
		case R.id.shitu_menu:
			
			break;
			
		case R.id.search_menu:
			
			break;
			
		case R.id.help_menu:
			
			break;

		default:
			break;
		}
		
	}

}
