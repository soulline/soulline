package com.cdd.base;

import android.os.Bundle;

import com.cdd.R;
import com.cdd.base.BaseActivity;

public class SlpashActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setFullScreen();
		setContentView(R.layout.slpash_activity);
	}

}
